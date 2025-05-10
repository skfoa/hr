package org.example.hr.mapper;

import org.example.hr.pojo.DeptInf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest; // 专用于MyBatis测试的注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException; // 用于捕获唯一约束等异常
import org.springframework.test.annotation.Rollback; // 控制事务回滚，默认为true
import org.springframework.transaction.annotation.Transactional; // 确保测试在事务中运行

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest // 专注于 MyBatis 组件的测试，它会配置一个内存数据库(默认H2)或使用现有数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 重要：禁用内存数据库替换，使用application.properties中配置的真实数据库
// @Transactional // 可以加在类级别，让所有测试方法都在事务中运行并默认回滚
// @Rollback(true) // 默认就是true，测试完成后回滚数据库操作，保持数据库清洁
public class DeptInfMapperTests {

    @Autowired
    private DeptInfMapper deptInfMapper;

    private DeptInf dept1, dept2;

    @BeforeEach // JUnit 5: 在每个测试方法执行前运行
    @Transactional // 确保 setup 方法也在事务中，如果需要清除数据
    @Rollback(false) // 通常 setup 方法不希望回滚，除非是创建临时数据
    void setUp() {
        // 清理可能存在的测试数据，避免测试间干扰 (更健壮的做法是使用数据库清理工具或脚本)
        // 为简单起见，这里假设测试前数据库是相对干净的，或者依赖测试的回滚
        // deptInfMapper.deleteByName("测试部门1"); // 这种方式可能不安全，如果名称不存在
        // deptInfMapper.deleteByName("测试部门2");
        // deptInfMapper.deleteByName("注解测试部门");

        dept1 = new DeptInf(null, "测试部门1", "这是第一个测试部门");
        dept2 = new DeptInf(null, "测试部门2", "这是第二个测试部门，用于更新和删除");
    }

    @Test
    @Transactional // 确保测试在事务中运行，并默认回滚
    public void testInsertAndFindById() {
        int result = deptInfMapper.insert(dept1);
        assertTrue(result > 0, "插入部门1应成功");
        assertNotNull(dept1.getDeptId(), "插入后部门1的ID不应为null");

        DeptInf foundDept = deptInfMapper.findById(dept1.getDeptId());
        assertNotNull(foundDept, "根据ID应能找到部门1");
        assertEquals(dept1.getDeptName(), foundDept.getDeptName(), "找到的部门名称应与插入时一致");
        assertEquals(dept1.getDeptRemark(), foundDept.getDeptRemark(), "找到的部门备注应与插入时一致");
    }

    @Test
    @Transactional
    public void testFindAll() {
        deptInfMapper.insert(dept1);
        deptInfMapper.insert(dept2);

        List<DeptInf> depts = deptInfMapper.findAll();
        assertNotNull(depts, "部门列表不应为null");
        // 注意：由于测试可能并行或数据库中已有数据，这里不好精确断言数量
        // 更好的做法是先查询总数，插入后再查询总数，比较差值
        assertTrue(depts.size() >= 2, "部门列表至少应包含刚插入的两个部门");
    }

    @Test
    @Transactional
    public void testFindByNameUsingAnnotation() {
        DeptInf annotatedDept = new DeptInf(null, "注解测试部门", "通过注解查询");
        deptInfMapper.insert(annotatedDept);
        assertNotNull(annotatedDept.getDeptId(), "注解部门插入后ID不应为null");

        DeptInf foundDept = deptInfMapper.findByName("注解测试部门");
        assertNotNull(foundDept, "应能通过名称找到注解测试部门");
        assertEquals(annotatedDept.getDeptName(), foundDept.getDeptName());
    }

    @Test
    @Transactional
    public void testUpdate() {
        deptInfMapper.insert(dept2); // 先插入一个用于更新的部门
        assertNotNull(dept2.getDeptId(), "用于更新的部门ID不应为null");

        String updatedName = "更新后的部门2名称";
        String updatedRemark = "更新后的备注信息";
        dept2.setDeptName(updatedName);
        dept2.setDeptRemark(updatedRemark);

        int result = deptInfMapper.update(dept2);
        assertTrue(result > 0, "更新操作应成功");

        DeptInf updatedDeptFromDb = deptInfMapper.findById(dept2.getDeptId());
        assertNotNull(updatedDeptFromDb, "更新后应能找到该部门");
        assertEquals(updatedName, updatedDeptFromDb.getDeptName(), "部门名称应已更新");
        assertEquals(updatedRemark, updatedDeptFromDb.getDeptRemark(), "部门备注应已更新");
    }

    @Test
    @Transactional
    public void testDeleteById() {
        deptInfMapper.insert(dept2); // 先插入一个用于删除的部门
        assertNotNull(dept2.getDeptId(), "用于删除的部门ID不应为null");
        Integer idToDelete = dept2.getDeptId();

        int result = deptInfMapper.deleteById(idToDelete);
        assertTrue(result > 0, "删除操作应成功");

        DeptInf deletedDept = deptInfMapper.findById(idToDelete);
        assertNull(deletedDept, "删除后根据ID不应再找到该部门");
    }

    @Test
    @Transactional
    public void testInsertDuplicateName() {
        // 第一次插入
        deptInfMapper.insert(dept1);
        assertTrue(dept1.getDeptId() > 0);

        // 尝试插入同名部门
        DeptInf duplicateDept = new DeptInf(null, dept1.getDeptName(), "尝试插入同名部门");

        // 数据库层面有唯一约束 dept_name UNIQUE KEY `dept_name` (`dept_name`)
        // 所以这里会抛出异常
        assertThrows(DataIntegrityViolationException.class, () -> {
            deptInfMapper.insert(duplicateDept);
        }, "插入同名部门应抛出DataIntegrityViolationException");
    }
}