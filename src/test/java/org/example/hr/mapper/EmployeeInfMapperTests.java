package org.example.hr.mapper;

import org.example.hr.pojo.DeptInf;
import org.example.hr.pojo.EmployeeInf;
import org.example.hr.pojo.JobInf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeInfMapperTests {

    @Autowired
    private EmployeeInfMapper employeeInfMapper;

    @Autowired // 需要注入Dept和Job Mapper来准备测试数据
    private DeptInfMapper deptInfMapper;

    @Autowired
    private JobInfMapper jobInfMapper;

    private DeptInf testDept;
    private JobInf testJob;
    private EmployeeInf emp1;

    @BeforeEach
    @Transactional // 确保 setup 内的操作如果失败或测试需要回滚时能正确处理
    void setUp() {
        // 准备测试用的部门和岗位数据 (确保它们存在于数据库中，或者在此处插入并获取ID)
        // 为简单起见，我们假设ID为1的部门和岗位已存在
        // 更健壮的测试会先清理并插入测试数据
        testDept = deptInfMapper.findById(1); // 假设技术部ID为1
        if (testDept == null) {
            testDept = new DeptInf(null, "测试临时部门", "用于员工测试");
            deptInfMapper.insert(testDept);
        }

        testJob = jobInfMapper.findById(2); // 假设Java开发工程师ID为2
        if (testJob == null) {
            testJob = new JobInf(null, "测试临时岗位", "用于员工测试");
            jobInfMapper.insert(testJob);
        }

        emp1 = new EmployeeInf();
        emp1.setEmpName("测试员工甲");
        emp1.setEmpCardId("1234567890");
        emp1.setEmpAddress("测试地址1");
        emp1.setEmpPhone("13800138001");
        emp1.setEmpSex(1);
        emp1.setEmpBirth(new Date());
        emp1.setEmpCreateDate(new Date());
        emp1.setDepartment(testDept); // 设置关联部门
        emp1.setJob(testJob);         // 设置关联岗位
    }

    @Test
    @Transactional
    public void testInsertAndFindByIdWithDetails() {
        int result = employeeInfMapper.insert(emp1);
        assertTrue(result > 0, "插入员工应成功");
        assertNotNull(emp1.getEmpId(), "插入后员工ID不应为null");

        EmployeeInf foundEmp = employeeInfMapper.findByIdWithDetails(emp1.getEmpId());
        assertNotNull(foundEmp, "根据ID应能找到员工");
        assertEquals(emp1.getEmpName(), foundEmp.getEmpName());
        assertNotNull(foundEmp.getDepartment(), "员工的部门信息不应为null");
        assertEquals(testDept.getDeptName(), foundEmp.getDepartment().getDeptName(), "部门名称应匹配");
        assertNotNull(foundEmp.getJob(), "员工的岗位信息不应为null");
        assertEquals(testJob.getJobName(), foundEmp.getJob().getJobName(), "岗位名称应匹配");
    }

    @Test
    @Transactional
    public void testFindAllWithDetails() {
        employeeInfMapper.insert(emp1); // 插入一个测试员工

        List<EmployeeInf> employees = employeeInfMapper.findAllWithDetails();
        assertNotNull(employees);
        assertTrue(employees.size() >= 1, "员工列表至少应包含一个员工");

        boolean foundInserted = employees.stream().anyMatch(e -> e.getEmpId().equals(emp1.getEmpId()));
        assertTrue(foundInserted, "列表中应包含刚插入的员工");

        EmployeeInf firstEmpInList = employees.stream().filter(e -> e.getDepartment() != null && e.getJob() != null).findFirst().orElse(null);
        if (firstEmpInList != null) {
            assertNotNull(firstEmpInList.getDepartment().getDeptName(), "列表中员工的部门名称不应为null");
            assertNotNull(firstEmpInList.getJob().getJobName(), "列表中员工的岗位名称不应为null");
        }
    }

    @Test
    @Transactional
    public void testUpdateEmployee() {
        employeeInfMapper.insert(emp1);
        assertNotNull(emp1.getEmpId());

        EmployeeInf empToUpdate = employeeInfMapper.findByIdWithDetails(emp1.getEmpId());
        assertNotNull(empToUpdate);

        String updatedName = "更新员工甲姓名";
        String updatedAddress = "更新后的测试地址";
        empToUpdate.setEmpName(updatedName);
        empToUpdate.setEmpAddress(updatedAddress);

        // 假设我们想更换部门和岗位
        DeptInf newDept = deptInfMapper.findById(2); // 假设运营部ID为2
        if (newDept == null) {
            newDept = new DeptInf(null, "新测试部门", "用于更新测试");
            deptInfMapper.insert(newDept);
        }
        JobInf newJob = jobInfMapper.findById(1);   // 假设职员ID为1
        if (newJob == null) {
            newJob = new JobInf(null, "新测试岗位", "用于更新测试");
            jobInfMapper.insert(newJob);
        }
        empToUpdate.setDepartment(newDept);
        empToUpdate.setJob(newJob);

        int result = employeeInfMapper.update(empToUpdate);
        assertTrue(result > 0, "更新员工应成功");

        EmployeeInf updatedEmpFromDb = employeeInfMapper.findByIdWithDetails(emp1.getEmpId());
        assertEquals(updatedName, updatedEmpFromDb.getEmpName());
        assertEquals(updatedAddress, updatedEmpFromDb.getEmpAddress());
        assertEquals(newDept.getDeptId(), updatedEmpFromDb.getDepartment().getDeptId());
        assertEquals(newJob.getJobId(), updatedEmpFromDb.getJob().getJobId());
    }

    @Test
    @Transactional
    public void testUpdateEmployeeWithPartialFields() { // 测试动态SQL
        employeeInfMapper.insert(emp1);
        assertNotNull(emp1.getEmpId());

        EmployeeInf partialUpdate = new EmployeeInf();
        partialUpdate.setEmpId(emp1.getEmpId());
        String newPhone = "13999999999";
        partialUpdate.setEmpPhone(newPhone);
        // 其他字段为null，不应被更新

        int result = employeeInfMapper.update(partialUpdate);
        assertTrue(result > 0);

        EmployeeInf updatedEmp = employeeInfMapper.findByIdWithDetails(emp1.getEmpId());
        assertEquals(newPhone, updatedEmp.getEmpPhone());
        assertEquals(emp1.getEmpName(), updatedEmp.getEmpName()); // 姓名不应改变
        assertEquals(emp1.getDepartment().getDeptId(), updatedEmp.getDepartment().getDeptId()); // 部门不应改变
    }


    @Test
    @Transactional
    public void testDeleteEmployeeById() {
        employeeInfMapper.insert(emp1);
        assertNotNull(emp1.getEmpId());
        Integer idToDelete = emp1.getEmpId();

        int result = employeeInfMapper.deleteById(idToDelete);
        assertTrue(result > 0);

        assertNull(employeeInfMapper.findByIdBasic(idToDelete));
    }

    @Test
    @Transactional
    public void testFindByCriteria() {
        // 插入一些不同的员工数据
        employeeInfMapper.insert(emp1); // 姓名: 测试员工甲, 部门: testDept, 岗位: testJob

        EmployeeInf emp2 = new EmployeeInf();
        emp2.setEmpName("李四");
        emp2.setEmpCardId("2345678901");
        emp2.setEmpAddress("测试地址2");
        emp2.setEmpPhone("13700137002");
        emp2.setEmpSex(1);
        DeptInf otherDept = deptInfMapper.findById(2); // 运营部
        if (otherDept == null) {
            otherDept = new DeptInf(null, "其他临时部门", "用于员工测试");
            deptInfMapper.insert(otherDept);
        }
        emp2.setDepartment(otherDept);
        emp2.setJob(testJob);
        employeeInfMapper.insert(emp2);

        // 1. 按姓名模糊查询
        Map<String, Object> paramsName = new HashMap<>();
        paramsName.put("empName", "测试员工");
        List<EmployeeInf> foundByName = employeeInfMapper.findByCriteria(paramsName);
        assertEquals(1, foundByName.size());
        assertEquals(emp1.getEmpName(), foundByName.get(0).getEmpName());

        // 2. 按部门ID查询
        Map<String, Object> paramsDept = new HashMap<>();
        paramsDept.put("deptId", testDept.getDeptId());
        List<EmployeeInf> foundByDept = employeeInfMapper.findByCriteria(paramsDept);
        assertTrue(foundByDept.stream().allMatch(e -> e.getDepartment().getDeptId().equals(testDept.getDeptId())));
        // 根据 setUp 中的数据，应该至少有一个
        assertTrue(foundByDept.size() >= 1);


        // 3. 按岗位ID查询
        Map<String, Object> paramsJob = new HashMap<>();
        paramsJob.put("jobId", testJob.getJobId());
        List<EmployeeInf> foundByJob = employeeInfMapper.findByCriteria(paramsJob);
        assertTrue(foundByJob.stream().allMatch(e -> e.getJob().getJobId().equals(testJob.getJobId())));
        // 根据 setUp 中的数据，应该至少有两个
        assertTrue(foundByJob.size() >= 2);


        // 4. 组合查询 (姓名和部门)
        Map<String, Object> paramsCombined = new HashMap<>();
        paramsCombined.put("empName", "员工甲");
        paramsCombined.put("deptId", testDept.getDeptId());
        List<EmployeeInf> foundCombined = employeeInfMapper.findByCriteria(paramsCombined);
        assertEquals(1, foundCombined.size());
        assertEquals(emp1.getEmpName(), foundCombined.get(0).getEmpName());
    }

    @Test
    @Transactional
    public void testCountByDeptId() {
        employeeInfMapper.insert(emp1); // emp1 在 testDept
        int count = employeeInfMapper.countByDeptId(testDept.getDeptId());
        assertTrue(count >= 1);
    }

    @Test
    @Transactional
    public void testCountByJobId() {
        employeeInfMapper.insert(emp1); // emp1 在 testJob
        int count = employeeInfMapper.countByJobId(testJob.getJobId());
        assertTrue(count >= 1);
    }
}