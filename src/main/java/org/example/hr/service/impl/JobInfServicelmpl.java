package org.example.hr.service.impl;

import org.example.hr.pojo.JobInf;
import java.util.List;

public interface JobInfServicelmpl {

    /**
     * 获取所有岗位信息
     * @return 岗位列表
     */
    List<JobInf> getAllJobs();

    /**
     * 根据ID获取岗位信息
     * @param jobId 岗位ID
     * @return 岗位对象，如果不存在则返回null
     */
    JobInf getJobById(Integer jobId);

    /**
     * 根据名称获取岗位信息
     * @param jobName 岗位名称
     * @return 岗位对象，如果不存在则返回null
     */
    JobInf getJobByName(String jobName);

    /**
     * 添加新岗位
     * @param jobInf 待添加的岗位信息 (jobId 应为null)
     * @return true 如果添加成功，false 如果岗位名称已存在或添加失败
     */
    boolean addJob(JobInf jobInf);

    /**
     * 更新岗位信息
     * @param jobInf 待更新的岗位信息 (jobId 必须有效)
     * @return true 如果更新成功，false 如果岗位不存在、岗位名称冲突或更新失败
     */
    boolean updateJob(JobInf jobInf);

    /**
     * 根据ID删除岗位
     * 注意：实际项目中可能需要检查该岗位下是否有员工。
     * @param jobId 岗位ID
     * @return true 如果删除成功，false 如果岗位不存在或删除失败
     */
    boolean deleteJobById(Integer jobId);
}