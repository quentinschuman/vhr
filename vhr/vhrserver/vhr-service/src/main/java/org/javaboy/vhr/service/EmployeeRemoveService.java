package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeRemoveMapper;
import org.javaboy.vhr.model.EmployeeRemove;
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
 * @date 2023/6/11
 */
@Service
public class EmployeeRemoveService {

    @Resource
    private EmployeeRemoveMapper employeeRemoveMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    MailSendLogService mailSendLogService;

    public RespPageBean getEmployeeRemoveByPage(Integer page, Integer size, EmployeeRemove employeeRemove, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<EmployeeRemove> data = employeeRemoveMapper.getEmployeeRemoveByPage(page, size, employeeRemove, beginDateScope);
        Long total = employeeRemoveMapper.getTotal(employeeRemove, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmployeeRemove(EmployeeRemove employeeRemove) {
        int result = employeeRemoveMapper.insertSelective(employeeRemove);
        if (result == 1) {
            EmployeeRemove empRemove = employeeRemoveMapper.getEmployeeRemoveById(employeeRemove.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME_EMPREMOVE);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME_EMPREMOVE);
            mailSendLog.setEmpEcId(empRemove.getId());
            mailSendLog.setEmpId(empRemove.getEid());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME_EMPREMOVE, MailConstants.MAIL_ROUTING_KEY_NAME_EMPREMOVE, empRemove, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer updateEmployeeRemove(EmployeeRemove employeeRemove) {
        return employeeRemoveMapper.updateByPrimaryKeySelective(employeeRemove);
    }

    public Integer deleteEmployeeRemoveById(Integer id) {
        return employeeRemoveMapper.deleteByPrimaryKey(id);
    }
}
