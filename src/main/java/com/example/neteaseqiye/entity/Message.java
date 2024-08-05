package com.example.neteaseqiye.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;

/**
 * 描述：Message
 *
 * @author chenDeXing
 * 日期: 2024-07-30 17:33
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@TableName("MESSAGE")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 发件人
     */
    @JsonProperty("From")
    private String fromWho;
    /**
     * 标题
     */
    @JsonProperty("Subject")
    private String subject;
    /**
     * 收件人
     */
    @JsonProperty("To")
    private String toWho;
    /**
     * 邮件内容类型
     */
    @JsonProperty("Content-Type")
    private String contentType;
    /**
     * 邮件内容(摘要)
     */
    @JsonProperty("Content")
    private String content;
    /**
     * 邮件大小
     */
    @JsonProperty("mailsizebyte")
    private String mailSizeByte;
    /**
     * 发信时间
     */
    @JsonProperty("SentDate")
    private String sentDate;
    /**
     * 字符集
     */
    private String charset;
    /**
     * 附件
     */
    private String attachment;

}
