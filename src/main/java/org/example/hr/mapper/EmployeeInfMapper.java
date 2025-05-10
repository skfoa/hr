package org.example.hr.mapper;

import org.example.hr.pojo.EmployeeInf;
import java.util.List;
import java.util.Map; // 用于动态条件查询

// 由于 HrApplication 中有 @MapperScan("org.example.hr.mapper")，这里无需 @Mapper
public interface EmployeeInfMapper {

    /**
     * 查询所有员工基本信息 (不含部门和岗位详情)
     * @return 员工列表
     */
    List<EmployeeInf> findAllBasic();

    /**
     * 根据员工ID查询员工基本信息
     * @param empId 员工ID
     * @return 员工对象
     */
    EmployeeInf findByIdBasic(Integer empId);

    /**
     * 查询所有员工信息，并包含其部门和岗位详情 (关联查询)
     * @return 包含详情的员工列表
     */
    List<EmployeeInf> findAllWithDetails();

    /**
     * 根据员工ID查询员工信息，并包含其部门和岗位详情 (关联查询)
     * @param empId 员工ID
     * @return 包含详情的员工对象
     */
    EmployeeInf findByIdWithDetails(Integer empId);

    /**
     * 新增员工信息
     * @param employeeInf 待新增的员工对象
     * @return 受影响的行数
     */
    int insert(EmployeeInf employeeInf);

    /**
     * 更新员工信息 (会使用动态SQL)
     * @param employeeInf 待更新的员工对象
     * @return 受影响的行数
     */
    int update(EmployeeInf employeeInf);

    /**
     * 根据员工ID删除员工信息
     * @param empId 员工ID
     * @return 受影响的行数
     */
    int deleteById(Integer empId);

    /**
     * 根据动态条件查询员工信息 (示例)
     * @param params 包含查询条件的Map，例如: empName, deptId, jobId
     * @return 符合条件的员工列表 (包含详情)
     */
    List<EmployeeInf> findByCriteria(Map<String, Object> params);

    /**
     * 根据部门ID统计员工数量 (用于删除部门/岗位前的检查)
     * @param deptId 部门ID
     * @return 该部门下的员工数量
     */
    int countByDeptId(Integer deptId);

    /**
     * 根据岗位ID统计员工数量
     * @param jobId 岗位ID
     * @return 该岗位下的员工数量
     */
    int countByJobId(Integer jobId);
}