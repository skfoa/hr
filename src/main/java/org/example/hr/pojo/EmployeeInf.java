package org.example.hr.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

/**
 * 员工信息实体类 (对应数据库表 employee_inf)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInf {

    /**
     * 员工ID (主键，自增)
     * 对应数据库字段: emp_id
     */
    private Integer empId;

    /**
     * 员工姓名 (非空)
     * 对应数据库字段: emp_name
     */
    private String empName;

    /**
     * 身份证号 (非空)
     * 对应数据库字段: emp_card_id
     */
    private String empCardId;

    /**
     * 地址 (非空)
     * 对应数据库字段: emp_address
     */
    private String empAddress;

    /**
     * 邮编
     * 对应数据库字段: emp_post_code
     */
    private String empPostCode;

    /**
     * 固话
     * 对应数据库字段: emp_tel
     */
    private String empTel;

    /**
     * 手机
     * 对应数据库字段: emp_phone
     */
    private String empPhone;

    /**
     * QQ号
     * 对应数据库字段: emp_qq
     */
    private String empQq;

    /**
     * 邮箱
     * 对应数据库字段: emp_email
     */
    private String empEmail;

    /**
     * 性别 (1-男，2-女)
     * 对应数据库字段: emp_sex
     */
    private Integer empSex;

    /**
     * 政治面貌
     * 对应数据库字段: emp_party
     */
    private String empParty;

    /**
     * 生日
     * 对应数据库字段: emp_birth (datetime)
     */
    private Date empBirth;

    /**
     * 民族 (默认为汉族)
     * 对应数据库字段: emp_race
     */
    private String empRace;

    /**
     * 学历
     * 对应数据库字段: emp_edu
     */
    private String empEdu;

    /**
     * 专业
     * 对应数据库字段: emp_speciality
     */
    private String empSpeciality;

    /**
     * 爱好
     * 对应数据库字段: emp_hobby
     */
    private String empHobby;

    /**
     * 备注
     * 对应数据库字段: emp_remark
     */
    private String empRemark;

    /**
     * 创建时间
     * 对应数据库字段: emp_create_date (datetime)
     */
    private Date empCreateDate;

    // --- 关联对象 ---
    /**
     * 员工所属部门信息
     * 通过 emp_dept_id 关联 dept_inf 表
     */
    private DeptInf department;

    /**
     * 员工所属岗位信息
     * 通过 emp_job_id 关联 job_inf 表
     */
    private JobInf job;

    private Integer empDeptIdInput; // 用于表单提交部门ID
    private Integer empJobIdInput;  // 用于表单提交岗位ID

}