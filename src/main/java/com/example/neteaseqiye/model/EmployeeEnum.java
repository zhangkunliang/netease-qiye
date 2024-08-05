package com.example.neteaseqiye.model;

import lombok.Getter;

public enum EmployeeEnum {
    JIFANGJUN("jifangjun@twxszkj.com", "计芳军"),
    JIFANGHAO("jifanghao@twxszkj.com", "计方豪"),
    LIHUI("lihui@twxszkj.com", "黎晖"),
    YANHONGYUN("yanhongyun@twxszkj.com", "晏鸿运"),
    HUANGXIUNI("huangxiuni@twxszkj.com", "黄秀泥"),
    HUANGJIANFANG("huangjianfang@twxszkj.com", "黄建芳"),
    XIEBAOHUI("xiebaohui@twxszkj.com", "谢宝辉"),
    ZHENGHONG("zhenghong@twxszkj.com", "郑泓"),
    JIWUHAN("jiwuhan@twxszkj.com", "计武汉"),
    CHENDEXING("chendexing@twxszkj.com", "陈德兴"),
    WANSHENGWU("wanshengwu@twxszkj.com", "万声鹉"),
    ZHANGKUNLIANG("zhangkunliang@twxszkj.com", "张坤梁"),
    ZHANGBINGXI("zhangbingxi@twxszkj.com", "张兵喜"),
    DINGCHENGSHENG("dingchengsheng@twxszkj.com", "丁成生"),
    CHENJIASHU("chenjiashu@twxszkj.com", "陈佳树"),
    ZHANGCHENCHEN("zhangchenchen@twxszkj.com", "张晨晨");



    private final String email;
    private final String chineseName;

    EmployeeEnum(String email, String chineseName) {
        this.email = email;
        this.chineseName = chineseName;
    }

    public String getEmail() {
        return email;
    }

    public String getChineseName() {
        return chineseName;
    }
    public static String getChineseNameByEmail(String email) {
        for (EmployeeEnum employee : EmployeeEnum.values()) {
            if (employee.getEmail().equalsIgnoreCase(email)) {
                return employee.getChineseName();
            }
        }
        return null;
    }
    public static String getChineseNameByName(String name) {
        for (EmployeeEnum user : EmployeeEnum.values()) {
            if (user.name().equalsIgnoreCase(name)) {
                return user.getChineseName();
            }
        }
        return null;
    }
}
