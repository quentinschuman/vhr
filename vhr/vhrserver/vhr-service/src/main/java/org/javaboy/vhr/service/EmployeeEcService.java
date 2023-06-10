package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeEcMapper;
import org.javaboy.vhr.model.EmployeeEc;
import org.javaboy.vhr.model.MailConstants;
import org.javaboy.vhr.model.MailSendLog;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/5/16
 */
@Service
public class EmployeeEcService {

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    @Autowired
    private EmployeeEcMapper employeeEcMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MailSendLogService mailSendLogService;

    public RespPageBean getEmployeeEcByPage(Integer page, Integer size, EmployeeEc employeeEc, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<EmployeeEc> data = employeeEcMapper.getEmployeeEcByPage(page, size, employeeEc, beginDateScope);
        Long total = employeeEcMapper.getTotal(employeeEc, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmpEc(EmployeeEc employeeEc) {
        int result = employeeEcMapper.insertSelective(employeeEc);
        if (result == 1) {
            EmployeeEc empec = employeeEcMapper.getEmployeeEcById(employeeEc.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME_EMP);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME_EMP);
            mailSendLog.setEmpEcId(empec.getId());
            mailSendLog.setEmpId(empec.getEid());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME_EMP, MailConstants.MAIL_ROUTING_KEY_NAME_EMP, empec, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer updateEmpEc(EmployeeEc employeeEc) {
        return employeeEcMapper.updateByPrimaryKeySelective(employeeEc);
    }

    public Integer deleteEmpEcById(Integer id) {
        return employeeEcMapper.deleteByPrimaryKey(id);
    }
}
