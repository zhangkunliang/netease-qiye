package com.example.neteaseqiye.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.neteaseqiye.config.AppConfig;
import com.example.neteaseqiye.entity.Message;
import com.example.neteaseqiye.mapper.MessageMapper;
import com.example.neteaseqiye.model.EmployeeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class EmailService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${spring.mail.mailTo}")
    private String[] mailTo;
    @Value("${spring.mail.need.send.daily.report}")
    private String[] needSendEpy;

    /**
     * 获取企业邮箱token
     *
     * @return
     */
    public String acquireToken() {
        String url = "https://api.qiye.163.com/api/pub/token/acquireToken";
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("appId", appConfig.getAppId());
        tokenJson.put("authCode", appConfig.getAuthCode());
        tokenJson.put("orgOpenId", appConfig.getOrgOpenId());
        // 获取响应
        String response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(tokenJson.toString())
                .execute().body();
        return JSONUtil.parseObj(response).getJSONObject("data").get("accessToken").toString();
    }

    /**
     * 增加动态订阅
     *
     * @param accessToken
     * @param callbackUrl
     * @return
     */
    public boolean addDynamicSubscribe(String accessToken, String callbackUrl) {
        String url = "https://api.qiye.163.com/api/open/push/addDynamicSubscribe";
        JSONObject requestParam = new JSONObject();
        // 企业openId
        requestParam.put("orgOpenId", "15bd8407ae48394e");
        // 回调地址
        requestParam.put("url", callbackUrl);

        String response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("qiye-access-token", accessToken)
                .header("qiye-app-id", appConfig.getAppId())
                .header("qiye-org-open-id", appConfig.getOrgOpenId())
                .body(requestParam.toString())
                .execute()
                .body();
        System.out.println(response);
        return response != null && (Boolean) JSONUtil.parseObj(response).get("success");
    }

    /**
     * 增加订阅
     *
     * @param accessToken
     * @param accountName
     * @param domain
     * @return
     */
    public boolean addPushMailSubscribe(String accessToken, String accountName, String domain) {
        String url = "https://api.qiye.163.com/api/open/push/addPushMailSubscribe";

        JSONObject addPushMailSubscribeJson = new JSONObject();
        //邮箱账户名，邮箱格式的前缀
        addPushMailSubscribeJson.put("accountName", accountName);
        //域名
        addPushMailSubscribeJson.put("domain", domain);
        //接口地址
        String response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("qiye-access-token", accessToken)
                .header("qiye-app-id", appConfig.getAppId())
                .header("qiye-org-open-id", appConfig.getOrgOpenId())
                .body(addPushMailSubscribeJson.toString())
                .execute().body();
        System.out.println("addPushMailSubscribe:" + response);
        return response != null && (Boolean) JSONUtil.parseObj(response).get("success");
    }

    /**
     * 统计邮件标题中含有“日报”，且没有发送邮件的人员
     */
    public List<String> getEmployeesWhoDidNotSendDailyReport() throws MessagingException {
        LocalDateTime startOfDay = LocalDateTime.now().minusDays(1).withHour(6).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(6).withMinute(0).withSecond(0);
        long startOfDayTimeStamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endOfDayTimeStamp = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 以邮件标题含有“日报”为条件进行过滤，统计当日已发送日报的员工
        List<Message> messages = messageMapper.selectList(new QueryWrapper<>()).stream()
                .filter(message -> Long.parseLong(message.getSentDate()) < endOfDayTimeStamp)
                .filter(message -> Long.parseLong(message.getSentDate()) > startOfDayTimeStamp)
                .filter(message -> message.getSubject().contains("日报"))
                .collect(Collectors.toList());
        List<String> employeesWhoSentReport = messages.stream()
                .map(Message::getFromWho)
                .collect(Collectors.toList());
        List<String> allEmployees = getAllEmployees(needSendEpy);
        // 返回没有发送日报的员工
        List<String> report = allEmployees.stream()
                .filter(email -> !employeesWhoSentReport.contains(email))
                .collect(Collectors.toList());

        System.out.println(report);
        senMessage(report);
        return report;
    }

    /**
     * 将未发送日报的员工以邮件形式发送
     *
     * @throws MessagingException
     */
    public void senMessage(List<String> report) throws MessagingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom(mailFrom);
        mimeMessageHelper.setTo(mailTo);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.CHINA);
        String weekDayString = LocalDateTime.now().minusDays(1).format(formatter);
        StringBuilder stringBuilder = new StringBuilder();
        mimeMessageHelper.setSubject(weekDayString + "日报未发送员工名单如下");
        if (report.isEmpty()) {
            stringBuilder.append("耶~大家都积极提交日报了！");
        } else {
            stringBuilder.append(getNameFromEmail(report));
//            stringBuilder.append("以上为测试邮件");
        }
        mimeMessageHelper.setText(stringBuilder.toString(), true);
        javaMailSender.send(mimeMailMessage);
        System.out.println("名单邮件已发送");
    }


    /**
     * 添加需要发送日报的员工邮箱
     *
     * @param needSendEpy
     * @return
     */
    private List<String> getAllEmployees(String[] needSendEpy) {
        // 返回所有员工的邮箱
        return Arrays.stream(needSendEpy).map(s -> s + "@twxszkj.com").collect(Collectors.toList());
    }

    /**
     * 员工名称获取
     *
     * @param report
     * @return
     */
    private String getNameFromEmail(List<String> report) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String email : report) {
            stringBuilder.append(EmployeeEnum.getChineseNameByEmail(email));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 保存邮件到数据库中
     *
     * @param message
     */
    public void saveMessage(Message message) {
        messageMapper.insert(message);
    }
}
