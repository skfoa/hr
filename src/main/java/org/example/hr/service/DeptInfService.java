package org.example.hr.service;

import org.example.hr.mapper.DeptInfMapper;
import org.example.hr.pojo.DeptInf;
import org.example.hr.service.impl.DeptInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 用于事务管理

import java.util.List;
import java.util.Objects;

@Service // 标记为Spring的Service组件
public class DeptInfService implements DeptInfServicelmpl {

    private static final Logger logger = LoggerFactory.getLogger(DeptInfService.class);

    private final DeptInfMapper deptInfMapper;

    @Autowired // 自动注入DeptInfMapper实例
    public DeptInfService(DeptInfMapper deptInfMapper) {
        this.deptInfMapper = deptInfMapper;
    }

    @Override
    public List<DeptInf> getAllDepts() {
        logger.debug("Fetching all departments");
        return deptInfMapper.findAll();
    }

    @Override
    public DeptInf getDeptById(Integer deptId) {
        logger.debug("Fetching department by id: {}", deptId);
        if (deptId == null) {
            return null;
        }
        return deptInfMapper.findById(deptId);
    }

    @Override
    public DeptInf getDeptByName(String deptName) {
        logger.debug("Fetching department by name: {}", deptName);
        if (deptName == null || deptName.trim().isEmpty()) {
            return null;
        }
        return deptInfMapper.findByName(deptName);
    }

    @Override
    @Transactional // 声明此方法参与事务管理
    public boolean addDept(DeptInf deptInf) {
        if (deptInf == null || deptInf.getDeptName() == null || deptInf.getDeptName().trim().isEmpty()) {
            logger.warn("Attempted to add department with null or empty name.");
            return false;
        }
        // 检查部门名称是否已存在
        DeptInf existingDept = deptInfMapper.findByName(deptInf.getDeptName().trim());
        if (existingDept != null) {
            logger.warn("Attempted to add department with existing name: {}", deptInf.getDeptName());
            return false; // 部门名称已存在
        }
        try {
            int result = deptInfMapper.insert(deptInf);
            logger.info("Department added successfully: {}, ID: {}", deptInf.getDeptName(), deptInf.getDeptId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error adding department: {}", deptInf.getDeptName(), e);
            // 可以抛出自定义异常
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateDept(DeptInf deptInf) {
        if (deptInf == null || deptInf.getDeptId() == null || deptInf.getDeptName() == null || deptInf.getDeptName().trim().isEmpty()) {
            logger.warn("Attempted to update department with invalid data.");
            return false;
        }
        // 检查要更新的部门是否存在
        DeptInf currentDept = deptInfMapper.findById(deptInf.getDeptId());
        if (currentDept == null) {
            logger.warn("Attempted to update non-existent department with ID: {}", deptInf.getDeptId());
            return false; // 部门不存在
        }

        // 检查更新后的部门名称是否与其他部门冲突 (排除自身)
        DeptInf existingDeptWithNewName = deptInfMapper.findByName(deptInf.getDeptName().trim());
        if (existingDeptWithNewName != null && !Objects.equals(existingDeptWithNewName.getDeptId(), deptInf.getDeptId())) {
            logger.warn("Attempted to update department ID {} with a name '{}' that already exists for department ID {}",
                    deptInf.getDeptId(), deptInf.getDeptName(), existingDeptWithNewName.getDeptId());
            return false; // 新部门名称已存在于其他部门
        }

        try {
            int result = deptInfMapper.update(deptInf);
            logger.info("Department updated successfully: ID {}", deptInf.getDeptId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error updating department with ID: {}", deptInf.getDeptId(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteDeptById(Integer deptId) {
        if (deptId == null) {
            logger.warn("Attempted to delete department with null ID.");
            return false;
        }
        // 实际项目中，这里应该检查该部门下是否有员工
        // 例如: if (employeeMapper.countByDeptId(deptId) > 0) { return false; // 或抛出异常 }
        DeptInf deptToDelete = deptInfMapper.findById(deptId);
        if (deptToDelete == null) {
            logger.warn("Attempted to delete non-existent department with ID: {}", deptId);
            return false; // 部门不存在
        }
        try {
            int result = deptInfMapper.deleteById(deptId);
            logger.info("Department deleted successfully: ID {}", deptId);
            return result > 0;
        } catch (Exception e) {
            // 特别注意：如果存在外键约束（例如员工表关联了部门表），直接删除可能会失败
            // 需要处理 DataIntegrityViolationException
            logger.error("Error deleting department with ID: {}. Possible foreign key constraint.", deptId, e);
            return false;
        }
    }
}