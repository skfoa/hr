package org.example.hr.service;

import org.example.hr.mapper.DeptInfMapper;
import org.example.hr.mapper.EmployeeInfMapper;
import org.example.hr.mapper.JobInfMapper;
import org.example.hr.pojo.DeptInf;
import org.example.hr.pojo.EmployeeInf;
import org.example.hr.pojo.JobInf;
import org.example.hr.service.impl.EmployeeInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Spring提供的字符串工具类

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeInfService implements EmployeeInfServicelmpl {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeInfService.class);

    private final EmployeeInfMapper employeeInfMapper;
    private final DeptInfMapper deptInfMapper; // 用于校验部门是否存在
    private final JobInfMapper jobInfMapper;   // 用于校验岗位是否存在

    @Autowired
    public EmployeeInfService(EmployeeInfMapper employeeInfMapper,
                              DeptInfMapper deptInfMapper,
                              JobInfMapper jobInfMapper) {
        this.employeeInfMapper = employeeInfMapper;
        this.deptInfMapper = deptInfMapper;
        this.jobInfMapper = jobInfMapper;
    }

    @Override
    public List<EmployeeInf> getAllEmployeesWithDetails() {
        logger.debug("Fetching all employees with details");
        return employeeInfMapper.findAllWithDetails();
    }

    @Override
    public EmployeeInf getEmployeeByIdWithDetails(Integer empId) {
        logger.debug("Fetching employee by id with details: {}", empId);
        if (empId == null) {
            return null;
        }
        return employeeInfMapper.findByIdWithDetails(empId);
    }

    @Override
    @Transactional
    public boolean addEmployee(EmployeeInf employeeInf) {
        if (employeeInf == null) {
            logger.warn("Attempted to add a null employee.");
            return false;
        }
        // 基本信息校验
        if (!StringUtils.hasText(employeeInf.getEmpName()) || !StringUtils.hasText(employeeInf.getEmpCardId())) {
            logger.warn("Employee name or card ID is empty.");
            return false;
        }

        // 校验部门和岗位是否存在，并设置到关联对象中
        // 假设 employeeInf.getEmpDeptIdInput() 和 employeeInf.getEmpJobIdInput() 存储了前端传来的ID
        if (employeeInf.getEmpDeptIdInput() != null) {
            DeptInf dept = deptInfMapper.findById(employeeInf.getEmpDeptIdInput());
            if (dept == null) {
                logger.warn("Department not found for ID: {}", employeeInf.getEmpDeptIdInput());
                return false;
            }
            employeeInf.setDepartment(dept); // 将查询到的部门对象设置进去
        } else if (employeeInf.getDepartment() == null || employeeInf.getDepartment().getDeptId() == null) {
            logger.warn("Department ID is required for adding employee.");
            return false;
        }


        if (employeeInf.getEmpJobIdInput() != null) {
            JobInf job = jobInfMapper.findById(employeeInf.getEmpJobIdInput());
            if (job == null) {
                logger.warn("Job not found for ID: {}", employeeInf.getEmpJobIdInput());
                return false;
            }
            employeeInf.setJob(job); // 将查询到的岗位对象设置进去
        } else if (employeeInf.getJob() == null || employeeInf.getJob().getJobId() == null) {
            logger.warn("Job ID is required for adding employee.");
            return false;
        }


        // 设置创建时间
        if (employeeInf.getEmpCreateDate() == null) {
            employeeInf.setEmpCreateDate(new Date());
        }

        try {
            int result = employeeInfMapper.insert(employeeInf);
            logger.info("Employee added successfully: {}, ID: {}", employeeInf.getEmpName(), employeeInf.getEmpId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error adding employee: {}", employeeInf.getEmpName(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateEmployee(EmployeeInf employeeInf) {
        if (employeeInf == null || employeeInf.getEmpId() == null) {
            logger.warn("Attempted to update employee with null ID or null employee object.");
            return false;
        }

        EmployeeInf existingEmployee = employeeInfMapper.findByIdBasic(employeeInf.getEmpId());
        if (existingEmployee == null) {
            logger.warn("Attempted to update non-existent employee with ID: {}", employeeInf.getEmpId());
            return false;
        }

        // 处理部门和岗位ID的更新
        if (employeeInf.getEmpDeptIdInput() != null) {
            DeptInf dept = deptInfMapper.findById(employeeInf.getEmpDeptIdInput());
            if (dept == null) {
                logger.warn("Department not found for ID: {} during update", employeeInf.getEmpDeptIdInput());
                return false; // 或者根据业务逻辑决定是否允许清空部门
            }
            employeeInf.setDepartment(dept);
        } else if (employeeInf.getDepartment() != null && employeeInf.getDepartment().getDeptId() != null) {
            // 如果 department 对象本身被设置了，也认为是有效的
        } else {
            employeeInf.setDepartment(null); // 允许清空部门，如果业务允许
        }


        if (employeeInf.getEmpJobIdInput() != null) {
            JobInf job = jobInfMapper.findById(employeeInf.getEmpJobIdInput());
            if (job == null) {
                logger.warn("Job not found for ID: {} during update", employeeInf.getEmpJobIdInput());
                return false; // 或者根据业务逻辑决定是否允许清空岗位
            }
            employeeInf.setJob(job);
        } else if (employeeInf.getJob() != null && employeeInf.getJob().getJobId() != null) {
            // 如果 job 对象本身被设置了，也认为是有效的
        } else {
            employeeInf.setJob(null); // 允许清空岗位，如果业务允许
        }


        try {
            // employeeInf 对象现在包含了要更新的所有字段 (包括关联对象的ID)
            // Mapper XML 中的动态SQL会处理哪些字段实际被更新
            int result = employeeInfMapper.update(employeeInf);
            logger.info("Employee updated successfully: ID {}", employeeInf.getEmpId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error updating employee with ID: {}", employeeInf.getEmpId(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteEmployeeById(Integer empId) {
        if (empId == null) {
            logger.warn("Attempted to delete employee with null ID.");
            return false;
        }
        EmployeeInf employeeToDelete = employeeInfMapper.findByIdBasic(empId);
        if (employeeToDelete == null) {
            logger.warn("Attempted to delete non-existent employee with ID: {}", empId);
            return false;
        }
        try {
            int result = employeeInfMapper.deleteById(empId);
            logger.info("Employee deleted successfully: ID {}", empId);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error deleting employee with ID: {}", empId, e);
            return false;
        }
    }

    @Override
    public List<EmployeeInf> findEmployeesByCriteria(Map<String, Object> params) {
        logger.debug("Finding employees by criteria: {}", params);
        return employeeInfMapper.findByCriteria(params);
    }

    @Override
    public boolean hasEmployeesInDepartment(Integer deptId) {
        if (deptId == null) return false;
        return employeeInfMapper.countByDeptId(deptId) > 0;
    }

    @Override
    public boolean hasEmployeesInJob(Integer jobId) {
        if (jobId == null) return false;
        return employeeInfMapper.countByJobId(jobId) > 0;
    }
}