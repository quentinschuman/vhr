package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeSalaryMapper;
import org.javaboy.vhr.model.EmployeeSalary;
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
public class EmployeeSalaryService {

    @Resource
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    MailSendLogService mailSendLogService;

    public RespPageBean getEmployeeSalaryByPage(Integer page, Integer size, EmployeeSalary employeeSalary, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<EmployeeSalary> data = employeeSalaryMapper.getEmployeeSalaryByPage(page, size, employeeSalary, beginDateScope);
        Long total = employeeSalaryMapper.getTotal(employeeSalary, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmployeeSalary(EmployeeSalary employeeSalary) {
        int result = employeeSalaryMapper.insertSelective(employeeSalary);
        if (result == 1) {
            EmployeeSalary empSalary = employeeSalaryMapper.getEmployeeSalaryById(employeeSalary.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME_EMPSALARY);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME_EMPSALARY);
            mailSendLog.setEmpEcId(empSalary.getId());
            mailSendLog.setEmpId(empSalary.getEid());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME_EMPSALARY, MailConstants.MAIL_ROUTING_KEY_NAME_EMPSALARY, empSalary, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer updateEmployeeSalary(EmployeeSalary employeeSalary) {
        return employeeSalaryMapper.updateByPrimaryKeySelective(employeeSalary);
    }

    public Integer deleteEmployeeSalaryById(Integer id) {
        return employeeSalaryMapper.deleteByPrimaryKey(id);
    }
}
