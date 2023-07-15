package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EmployeeRemove;

import java.util.Date;
import java.util.List;

public interface EmployeeRemoveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeRemove record);

    int insertSelective(EmployeeRemove record);

    EmployeeRemove selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeRemove record);

    int updateByPrimaryKey(EmployeeRemove record);

    List<EmployeeRemove> getEmployeeRemoveByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employeeRemove") EmployeeRemove employeeRemove, @Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("employeeRemove") EmployeeRemove employeeRemove, @Param("beginDateScope") Date[] beginDateScope);

    EmployeeRemove getEmployeeRemoveById(Integer id);
}