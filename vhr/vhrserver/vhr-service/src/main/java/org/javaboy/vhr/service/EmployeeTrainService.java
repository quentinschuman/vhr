package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeTrainMapper;
import org.javaboy.vhr.model.EmployeeTrain;
import org.javaboy.vhr.model.MailConstants;
import org.javaboy.vhr.model.MailSendLog;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/6/10
 */
@Service
public class EmployeeTrainService {

    @Resource
    private EmployeeTrainMapper employeeTrainMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    MailSendLogService mailSendLogService;

    public RespPageBean getEmployeeTrainByPage(Integer page, Integer size, EmployeeTrain employeeTrain, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<EmployeeTrain> data = employeeTrainMapper.getEmployeeTrainByPage(page, size, employeeTrain, beginDateScope);
        Long total = employeeTrainMapper.getTotal(employeeTrain, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmployeeTrain(EmployeeTrain employeeTrain) {
        int result = employeeTrainMapper.insertSelective(employeeTrain);
        if (result == 1) {
            EmployeeTrain empTrain = employeeTrainMapper.getEmployeeTrainById(employeeTrain.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME_EMPTRAIN);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME_EMPTRAIN);
            mailSendLog.setEmpEcId(empTrain.getId());
            mailSendLog.setEmpId(empTrain.getEid());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME_EMPTRAIN, MailConstants.MAIL_ROUTING_KEY_NAME_EMPTRAIN, empTrain, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer updateEmployeeTrain(EmployeeTrain employeeTrain) {
        return employeeTrainMapper.updateByPrimaryKeySelective(employeeTrain);
    }

    public Integer deleteEmployeeTrainById(Integer id) {
        return employeeTrainMapper.deleteByPrimaryKey(id);
    }
}
