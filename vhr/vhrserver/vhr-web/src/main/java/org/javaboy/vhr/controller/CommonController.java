package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/6/10
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getAllEmployeeNames")
    public List<Employee> getAllEmployeeNames() {
        return employeeService.getAllEmployeeNames();
    }
}
