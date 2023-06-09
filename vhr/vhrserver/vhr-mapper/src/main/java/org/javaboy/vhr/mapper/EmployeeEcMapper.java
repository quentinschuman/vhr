package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EmployeeEc;

import java.util.Date;
import java.util.List;

public interface EmployeeEcMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeEc record);

    int insertSelective(EmployeeEc record);

    EmployeeEc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeEc record);

    int updateByPrimaryKey(EmployeeEc record);

    List<EmployeeEc> getEmployeeEcByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("empEc") EmployeeEc employeeec, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("empEc") EmployeeEc employeeec, @Param("beginDateScope") Date[] beginDateScope);

    EmployeeEc getEmployeeEcById(Integer id);
}