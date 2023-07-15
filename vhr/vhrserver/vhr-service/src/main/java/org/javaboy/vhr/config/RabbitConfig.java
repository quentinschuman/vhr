package org.javaboy.vhr.config;

import org.javaboy.vhr.model.MailConstants;
import org.javaboy.vhr.service.MailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
    @Autowired
    CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    MailSendLogService mailSendLogService;

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                logger.info(msgId + ":消息发送成功");
                mailSendLogService.updateMailSendLogStatus(msgId, 1);//修改数据库中的记录，消息投递成功
            } else {
                logger.info(msgId + ":消息发送失败");
            }
        });
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingkey) -> {
            logger.info("消息发送失败");
        });
        return rabbitTemplate;
    }

    @Bean
    Queue mailQueueEmp() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMP, true);
    }

    @Bean
    Queue mailQueueEmpec() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMPEC, true);
    }

    @Bean
    Queue mailQueueEmpTrain() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMPTRAIN, true);
    }

    @Bean
    Queue mailQueueEmpRemove() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMPREMOVE, true);
    }

    @Bean
    Queue mailQueueEmpSalary() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME_EMPSALARY, true);
    }

    @Bean
    DirectExchange mailExchangeEmp() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME_EMP, true, false);
    }

    @Bean
    DirectExchange mailExchangeEmpec() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME_EMPEC, true, false);
    }

    @Bean
    DirectExchange mailExchangeEmpTrain() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME_EMPTRAIN, true, false);
    }

    @Bean
    Binding mailBindingEmp() {
        return BindingBuilder.bind(mailQueueEmp()).to(mailExchangeEmp()).with(MailConstants.MAIL_ROUTING_KEY_NAME_EMP);
    }

    @Bean
    Binding mailBindingEmpec() {
        return BindingBuilder.bind(mailQueueEmpec()).to(mailExchangeEmpec()).with(MailConstants.MAIL_ROUTING_KEY_NAME_EMPEC);
    }

    @Bean
    Binding mailBindingEmpTrain() {
        return BindingBuilder.bind(mailQueueEmpTrain()).to(mailExchangeEmpTrain()).with(MailConstants.MAIL_ROUTING_KEY_NAME_EMPTRAIN);
    }
}
