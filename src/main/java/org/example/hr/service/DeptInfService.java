package org.example.hr.service;

import org.example.hr.pojo.DeptInf;
import java.util.List;

public interface DeptInfService {

    /**
     * 获取所有部门信息
     * @return 部门列表
     */
    List<DeptInf> getAllDepts();

    /**
     * 根据ID获取部门信息
     * @param deptId 部门ID
     * @return 部门对象，如果不存在则返回null
     */
    DeptInf getDeptById(Integer deptId);

    /**
     * 根据名称获取部门信息
     * @param deptName 部门名称
     * @return 部门对象，如果不存在则返回null
     */
    DeptInf getDeptByName(String deptName);

    /**
     * 添加新部门
     * @param deptInf 待添加的部门信息 (deptId 应为null)
     * @return true 如果添加成功，false 如果部门名称已存在或添加失败
     */
    boolean addDept(DeptInf deptInf);

    /**
     * 更新部门信息
     * @param deptInf 待更新的部门信息 (deptId 必须有效)
     * @return true 如果更新成功，false 如果部门不存在、部门名称冲突或更新失败
     */
    boolean updateDept(DeptInf deptInf);

    /**
     * 根据ID删除部门
     * 注意：实际项目中可能需要检查该部门下是否有员工，如果有则不允许删除或提示级联删除。
     * 此处为简化，直接删除。
     * @param deptId 部门ID
     * @return true 如果删除成功，false 如果部门不存在或删除失败
     */
    boolean deleteDeptById(Integer deptId);
}