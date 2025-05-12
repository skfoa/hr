package org.example.hr.mapper;

import org.apache.ibatis.annotations.Select; // 用于注解SQL
import org.example.hr.pojo.DeptInf;
import java.util.List;

// 在 HrApplication.java 中已经配置了 @MapperScan("org.example.hr.mapper")，所以这里不需要 @Mapper
public interface DeptInfMapper {

    /**
     * 查询所有部门信息
     * @return 部门列表
     */
    List<DeptInf> findAll();

    /**
     * 根据部门ID查询部门信息
     * @param deptId 部门ID
     * @return 对应的部门信息，如果不存在则返回null
     */
    DeptInf findById(Integer deptId);

    /**
     * 根据部门名称查询部门信息 (使用注解SQL示例)
     * @param deptName 部门名称
     * @return 对应的部门信息，如果不存在则返回null
     */
    @Select("SELECT dept_id, dept_name, dept_remark FROM dept_inf WHERE dept_name = #{deptName}")
    DeptInf findByName(String deptName);

    /**
     * 新增部门信息
     * @param deptInf 待新增的部门对象 (deptId应为null或不设置，会自动生成)
     * @return 受影响的行数 (通常为1表示成功)
     */
    int insert(DeptInf deptInf);

    /**
     * 更新部门信息
     * @param deptInf 待更新的部门对象 (deptId必须存在且有效)
     * @return 受影响的行数
     */
    int update(DeptInf deptInf);

    /**
     * 根据部门ID删除部门信息
     * @param deptId 待删除的部门ID
     * @return 受影响的行数
     */
    int deleteById(Integer deptId);
}