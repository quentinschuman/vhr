package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.service.DepartmentService;
import org.javaboy.vhr.service.EmployeeService;
import org.javaboy.vhr.service.JobLevelService;
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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JobLevelService jobLevelService;

    @GetMapping("/getAllEmployeeNames")
    public List<Employee> getAllEmployeeNames() {
        return employeeService.getAllEmployeeNames();
    }

    @GetMapping("/getAllDeps")
    public List<Department> getAllDeps() {
        return departmentService.getAllDepartmentsWithOutChildren();
    }

    @GetMapping("/getDepartmentById")
    public Department getDepartmentById(Integer id) {
        return departmentService.getAllDepartments().get(id);
    }

    @GetMapping("/getAllJobs")
    public List<JobLevel> getAllJobs() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("getJobById")
    public JobLevel getJobById(Integer id) {
        return jobLevelService.getAllJobLevels().get(id);
    }
}
