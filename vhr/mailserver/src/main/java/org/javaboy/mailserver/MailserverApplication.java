package org.javaboy.mailserver;

import org.javaboy.vhr.model.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MailserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailserverApplication.class, args);
    }

    @Bean
    Queue queueEmp() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMP);
    }

    @Bean
    Queue queueEmpec() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMPEC);
    }
}
