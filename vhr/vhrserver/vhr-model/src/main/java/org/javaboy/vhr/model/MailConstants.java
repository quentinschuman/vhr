package org.javaboy.vhr.model;

public class MailConstants {
    public static final Integer DELIVERING = 0;//消息投递中
    public static final Integer SUCCESS = 1;//消息投递成功
    public static final Integer FAILURE = 2;//消息投递失败
    public static final Integer MAX_TRY_COUNT = 3;//最大重试次数
    public static final Integer MSG_TIMEOUT = 1;//消息超时时间
    public static final String MAIL_QUEUE_NAME_EMP = "javaboy.mail.queue.emp";
    public static final String MAIL_QUEUE_NAME_EMPEC = "javaboy.mail.queue.empec";
    public static final String MAIL_EXCHANGE_NAME_EMP = "javaboy.mail.exchange.emp";
    public static final String MAIL_EXCHANGE_NAME_EMPEC = "javaboy.mail.exchange.empec";
    public static final String MAIL_ROUTING_KEY_NAME_EMP = "javaboy.mail.routing.key.emp";
    public static final String MAIL_ROUTING_KEY_NAME_EMPEC = "javaboy.mail.routing.key.empec";
}
