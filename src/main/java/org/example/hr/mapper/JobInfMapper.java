package org.example.hr.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.hr.pojo.JobInf;
import java.util.List;

// 由于 HrApplication 中有 @MapperScan("org.example.hr.mapper")，这里无需 @Mapper
public interface JobInfMapper {

    /**
     * 查询所有岗位信息
     * @return 岗位列表
     */
    List<JobInf> findAll();

    /**
     * 根据岗位ID查询岗位信息
     * @param jobId 岗位ID
     * @return 对应的岗位信息，如果不存在则返回null
     */
    JobInf findById(Integer jobId);

    /**
     * 根据岗位名称查询岗位信息 (可以使用注解或XML实现)
     * @param jobName 岗位名称
     * @return 对应的岗位信息，如果不存在则返回null
     */
    @Select("SELECT job_id, job_name, job_remark FROM job_inf WHERE job_name = #{jobName}")
    JobInf findByName(String jobName);

    /**
     * 新增岗位信息
     * @param jobInf 待新增的岗位对象 (jobId应为null或不设置)
     * @return 受影响的行数
     */
    int insert(JobInf jobInf);

    /**
     * 更新岗位信息
     * @param jobInf 待更新的岗位对象 (jobId必须存在且有效)
     * @return 受影响的行数
     */
    int update(JobInf jobInf);

    /**
     * 根据岗位ID删除岗位信息
     * @param jobId 待删除的岗位ID
     * @return 受影响的行数
     */
    int deleteById(Integer jobId);
}