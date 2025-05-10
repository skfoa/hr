package org.example.hr.service.impl;

import org.example.hr.pojo.EmployeeInf;
import java.util.List;
import java.util.Map;

public interface EmployeeInfServicelmpl {

    List<EmployeeInf> getAllEmployeesWithDetails();

    EmployeeInf getEmployeeByIdWithDetails(Integer empId);

    /**
     * 添加新员工
     * @param employeeInf 员工信息，其中 department.deptId 和 job.jobId 应该被设置
     * @return true 如果添加成功
     */
    boolean addEmployee(EmployeeInf employeeInf);

    /**
     * 更新员工信息
     * @param employeeInf 员工信息
     * @return true 如果更新成功
     */
    boolean updateEmployee(EmployeeInf employeeInf);

    boolean deleteEmployeeById(Integer empId);

    /**
     * 根据动态条件查询员工
     * @param params 查询参数 (empName, deptId, jobId 等)
     * @return 员工列表
     */
    List<EmployeeInf> findEmployeesByCriteria(Map<String, Object> params);

    // 用于检查外键依赖
    boolean hasEmployeesInDepartment(Integer deptId);
    boolean hasEmployeesInJob(Integer jobId);
}