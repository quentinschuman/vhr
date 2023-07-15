package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EmployeeSalary;

import java.util.Date;
import java.util.List;

public interface EmployeeSalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeSalary record);

    int insertSelective(EmployeeSalary record);

    EmployeeSalary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeSalary record);

    int updateByPrimaryKey(EmployeeSalary record);

    List<EmployeeSalary> getEmployeeSalaryByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employeeSalary") EmployeeSalary employeeSalary, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("employeeSalary") EmployeeSalary employeeSalary, @Param("beginDateScope") Date[] beginDateScope);

    EmployeeSalary getEmployeeSalaryById(Integer id);
}