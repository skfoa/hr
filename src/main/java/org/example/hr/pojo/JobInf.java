package org.example.hr.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 岗位信息实体类 (对应数据库表 job_inf)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInf {

    /**
     * 岗位ID (主键，自增)
     * 对应数据库字段: job_id
     */
    private Integer jobId;

    /**
     * 岗位名称 (唯一, 非空)
     * 对应数据库字段: job_name
     */
    private String jobName;

    /**
     * 岗位介绍
     * 对应数据库字段: job_remark
     */
    private String jobRemark;
}