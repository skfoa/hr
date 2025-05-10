package org.example.hr.pojo;

import lombok.Data; // 自动生成 getter, setter, toString, equals, hashCode
import lombok.NoArgsConstructor; // 自动生成无参构造函数
import lombok.AllArgsConstructor; // 自动生成全参构造函数

/**
 * 部门信息实体类 (对应数据库表 dept_inf)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptInf {

    /**
     * 部门ID (主键，自增)
     * 对应数据库字段: dept_id
     */
    private Integer deptId;

    /**
     * 部门名称 (唯一, 非空)
     * 对应数据库字段: dept_name
     */
    private String deptName;

    /**
     * 部门介绍
     * 对应数据库字段: dept_remark
     */
    private String deptRemark;
}
