package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.model.EmployeeTrain;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeTrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * vhr
 *
 * @author qianshu
 * @date 2023/6/10
 */
@RestController
@RequestMapping("/employee/empTrain")
public class EmployeeTrainController {

    @Autowired
    private EmployeeTrainService employeeTrainService;

    @GetMapping("/")
    public RespPageBean getEmployeeTrainByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, EmployeeTrain employeeTrain, Date[] beginDateScope) {
        return employeeTrainService.getEmployeeTrainByPage(page, size, employeeTrain, beginDateScope);
    }

    @PostMapping("/")
    public RespBean addEmpEc(@RequestBody EmployeeTrain employeeTrain) {
        if (employeeTrainService.addEmployeeTrain(employeeTrain) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateEmp(@RequestBody EmployeeTrain employeeTrain) {
        if (employeeTrainService.updateEmployeeTrain(employeeTrain) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmployeeTrainById(@PathVariable Integer id) {
        if (employeeTrainService.deleteEmployeeTrainById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
