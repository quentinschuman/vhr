package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EmployeeTrain;

import java.util.Date;
import java.util.List;

public interface EmployeeTrainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeTrain record);

    int insertSelective(EmployeeTrain record);

    EmployeeTrain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeTrain record);

    int updateByPrimaryKey(EmployeeTrain record);

    List<EmployeeTrain> getEmployeeTrainByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employeeTrain") EmployeeTrain employeeTrain, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("employeeTrain") EmployeeTrain employeeTrain, @Param("beginDateScope") Date[] beginDateScope);

    EmployeeTrain getEmployeeTrainById(Integer id);
}