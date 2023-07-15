package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.model.EmployeeSalary;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/6/13
 */
@RestController
@RequestMapping("/employee/empSalary")
public class EmployeeSalaryController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @GetMapping("/")
    public RespPageBean getEmployeeSalaryByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, EmployeeSalary employeeSalary, Date[] beginDateScope) {
        return employeeSalaryService.getEmployeeSalaryByPage(page, size, employeeSalary, beginDateScope);
    }

    @PostMapping("/")
    public RespBean addEmployeeSalary(@RequestBody EmployeeSalary employeeSalary) {
        if (employeeSalaryService.addEmployeeSalary(employeeSalary) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateEmployeeSalary(@RequestBody EmployeeSalary employeeSalary) {
        if (employeeSalaryService.updateEmployeeSalary(employeeSalary) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmployeeSalaryById(@PathVariable Integer id) {
        if (employeeSalaryService.deleteEmployeeSalaryById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
