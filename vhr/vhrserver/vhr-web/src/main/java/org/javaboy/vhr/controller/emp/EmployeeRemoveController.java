package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.model.EmployeeRemove;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeRemoveService;
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
@RequestMapping("/employee/empRemove")
public class EmployeeRemoveController {

    @Autowired
    private EmployeeRemoveService employeeRemoveService;

    @GetMapping("/")
    public RespPageBean getEmployeeRemoveByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, EmployeeRemove employeeRemove, Date[] beginDateScope) {
        return employeeRemoveService.getEmployeeRemoveByPage(page, size, employeeRemove, beginDateScope);
    }

    @PostMapping("/")
    public RespBean addEmployeeRemove(@RequestBody EmployeeRemove employeeRemove) {
        if (employeeRemoveService.addEmployeeRemove(employeeRemove) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateEmployeeRemove(@RequestBody EmployeeRemove employeeRemove) {
        if (employeeRemoveService.updateEmployeeRemove(employeeRemove) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmployeeRemoveById(@PathVariable Integer id) {
        if (employeeRemoveService.deleteEmployeeRemoveById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
