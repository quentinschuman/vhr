package org.javaboy.vhr.controller.hr;

import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.EmployeeEc;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeEcService;
import org.javaboy.vhr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/5/16
 */
@RestController
@RequestMapping("/employee/empEc")
public class EmpEcController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeEcService employeeEcService;

    @GetMapping("/")
    public RespPageBean getEmployeeEcByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, EmployeeEc employeeEc, Date[] beginDateScope) {
        return employeeEcService.getEmployeeEcByPage(page, size, employeeEc, beginDateScope);
    }

    @PostMapping("/")
    public RespBean addEmpEc(@RequestBody EmployeeEc employeeEc) {
        if (employeeEcService.addEmpEc(employeeEc) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateEmp(@RequestBody EmployeeEc employeeEc) {
        if (employeeEcService.updateEmpEc(employeeEc) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmpEcById(@PathVariable Integer id) {
        if (employeeEcService.deleteEmpById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @GetMapping("/getAllEmployeeNames")
    public List<Employee> getAllEmployeeNames() {
        return employeeService.getAllEmployeeNames();
    }
}
