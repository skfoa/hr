package org.example.hr.mapper;

import org.example.hr.pojo.JobInf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 使用真实的数据库
public class JobInfMapperTests {

    @Autowired
    private JobInfMapper jobInfMapper;

    private JobInf job1, job2;

    @BeforeEach
        // @Transactional // setUp 默认不需要事务回滚，除非你在里面写了会持久化的数据
        // @Rollback(false)
    void setUp() {
        // 清理测试数据可以放在这里，或者依赖每个测试方法的 @Transactional 回滚
        job1 = new JobInf(null, "测试岗位1", "这是第一个测试岗位");
        job2 = new JobInf(null, "测试岗位2", "这是第二个测试岗位，用于更新和删除");
    }

    @Test
    @Transactional // 测试方法默认回滚
    public void testInsertAndFindById() {
        int result = jobInfMapper.insert(job1);
        assertTrue(result > 0, "插入岗位1应成功");
        assertNotNull(job1.getJobId(), "插入后岗位1的ID不应为null");

        JobInf foundJob = jobInfMapper.findById(job1.getJobId());
        assertNotNull(foundJob, "根据ID应能找到岗位1");
        assertEquals(job1.getJobName(), foundJob.getJobName());
        assertEquals(job1.getJobRemark(), foundJob.getJobRemark());
    }

    @Test
    @Transactional
    public void testFindAll() {
        jobInfMapper.insert(job1);
        jobInfMapper.insert(job2);

        List<JobInf> jobs = jobInfMapper.findAll();
        assertNotNull(jobs, "岗位列表不应为null");
        assertTrue(jobs.size() >= 2, "岗位列表至少应包含刚插入的两个岗位");
    }

    @Test
    @Transactional
    public void testFindByNameUsingAnnotation() {
        JobInf annotatedJob = new JobInf(null, "注解测试岗位", "通过注解查询");
        jobInfMapper.insert(annotatedJob);
        assertNotNull(annotatedJob.getJobId());

        JobInf foundJob = jobInfMapper.findByName("注解测试岗位");
        assertNotNull(foundJob);
        assertEquals(annotatedJob.getJobName(), foundJob.getJobName());
    }

    @Test
    @Transactional
    public void testUpdate() {
        jobInfMapper.insert(job2);
        assertNotNull(job2.getJobId());

        String updatedName = "更新后的岗位2名称";
        String updatedRemark = "更新后的备注";
        job2.setJobName(updatedName);
        job2.setJobRemark(updatedRemark);

        int result = jobInfMapper.update(job2);
        assertTrue(result > 0);

        JobInf updatedJobFromDb = jobInfMapper.findById(job2.getJobId());
        assertNotNull(updatedJobFromDb);
        assertEquals(updatedName, updatedJobFromDb.getJobName());
        assertEquals(updatedRemark, updatedJobFromDb.getJobRemark());
    }

    @Test
    @Transactional
    public void testDeleteById() {
        jobInfMapper.insert(job2);
        assertNotNull(job2.getJobId());
        Integer idToDelete = job2.getJobId();

        int result = jobInfMapper.deleteById(idToDelete);
        assertTrue(result > 0);

        JobInf deletedJob = jobInfMapper.findById(idToDelete);
        assertNull(deletedJob);
    }

    @Test
    @Transactional
    public void testInsertDuplicateName() {
        jobInfMapper.insert(job1);
        assertTrue(job1.getJobId() > 0);

        JobInf duplicateJob = new JobInf(null, job1.getJobName(), "尝试插入同名岗位");

        // 数据库层面 job_inf 表的 job_name 字段有唯一约束
        assertThrows(DataIntegrityViolationException.class, () -> {
            jobInfMapper.insert(duplicateJob);
        }, "插入同名岗位应抛出DataIntegrityViolationException");
    }
}