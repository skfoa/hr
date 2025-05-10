package org.example.hr.controller;

import org.example.hr.pojo.DeptInf;
import org.example.hr.pojo.EmployeeInf;
import org.example.hr.pojo.JobInf;
import org.example.hr.service.impl.DeptInfServicelmpl;
import org.example.hr.service.impl.EmployeeInfServicelmpl;
import org.example.hr.service.impl.JobInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Controller
@RequestMapping("/employees")
public class EmployeeInfController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeInfController.class);

    private final EmployeeInfServicelmpl employeeInfServicelmpl;
    private final DeptInfServicelmpl deptInfService;
    private final JobInfServicelmpl jobInfServicelmpl;

    @Autowired
    public EmployeeInfController(EmployeeInfServicelmpl employeeInfServicelmpl,
                                 DeptInfServicelmpl deptInfService,
                                 JobInfServicelmpl jobInfServicelmpl) {
        this.employeeInfServicelmpl = employeeInfServicelmpl;
        this.deptInfService = deptInfService;
        this.jobInfServicelmpl = jobInfServicelmpl;
    }

    /**
     * 公共方法，用于加载部门和岗位列表到Model，供表单使用
     */
    private void loadFormDependencies(Model model) {
        List<DeptInf> departments = deptInfService.getAllDepts();
        List<JobInf> jobs = jobInfServicelmpl.getAllJobs();
        model.addAttribute("departments", departments);
        model.addAttribute("jobs", jobs);
    }

    /**
     * 显示员工列表页面 (包含部门和岗位名称)
     * 也支持根据条件进行搜索
     */
    @GetMapping("/list")
    public String listEmployees(
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String empPhone,
            @RequestParam(required = false) String empCardId,
            @RequestParam(required = false) Integer empSex,
            @RequestParam(required = false) Integer deptId,
            @RequestParam(required = false) Integer jobId,
            Model model) {

        logger.info("Request to list employees with criteria - Name: {}, Phone: {}, CardID: {}, Sex: {}, DeptID: {}, JobID: {}",
                empName, empPhone, empCardId, empSex, deptId, jobId);

        Map<String, Object> params = new HashMap<>();
        if (empName != null && !empName.trim().isEmpty()) params.put("empName", empName.trim());
        if (empPhone != null && !empPhone.trim().isEmpty()) params.put("empPhone", empPhone.trim());
        if (empCardId != null && !empCardId.trim().isEmpty()) params.put("empCardId", empCardId.trim());
        if (empSex != null) params.put("empSex", empSex);
        if (deptId != null) params.put("deptId", deptId);
        if (jobId != null) params.put("jobId", jobId);

        List<EmployeeInf> employees;
        if (params.isEmpty()) {
            employees = employeeInfServicelmpl.getAllEmployeesWithDetails();
        } else {
            employees = employeeInfServicelmpl.findEmployeesByCriteria(params);
        }

        model.addAttribute("employees", employees);
        model.addAttribute("pageTitle", "员工列表");
        // 将搜索条件也传回给视图，以便在搜索框中保留
        model.addAttribute("searchParams", params);
        loadFormDependencies(model); // 加载部门和岗位列表，用于搜索表单的下拉框

        return "employees/employee-list";
    }


    /**
     * 显示添加员工的表单页面
     */
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        logger.info("Request to show add employee form");
        model.addAttribute("employee", new EmployeeInf()); // 新员工对象
        loadFormDependencies(model); // 加载部门和岗位列表
        model.addAttribute("formAction", "/employees/save");
        model.addAttribute("pageTitle", "添加新员工");
        model.addAttribute("isEdit", false);
        return "employees/employee-form";
    }

    /**
     * 显示编辑员工的表单页面
     */
    @GetMapping("/edit/{empId}")
    public String showEditEmployeeForm(@PathVariable("empId") Integer empId, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Request to show edit employee form for ID: {}", empId);
        EmployeeInf employee = employeeInfServicelmpl.getEmployeeByIdWithDetails(empId);
        if (employee == null) {
            logger.warn("Employee not found for ID: {}", empId);
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + empId + " 的员工。");
            return "redirect:/employees/list";
        }
        // 为了表单回显部门和岗位ID
        if (employee.getDepartment() != null) {
            employee.setEmpDeptIdInput(employee.getDepartment().getDeptId());
        }
        if (employee.getJob() != null) {
            employee.setEmpJobIdInput(employee.getJob().getJobId());
        }

        model.addAttribute("employee", employee);
        loadFormDependencies(model);
        model.addAttribute("formAction", "/employees/update/" + empId);
        model.addAttribute("pageTitle", "编辑员工信息");
        model.addAttribute("isEdit", true);
        return "employees/employee-form";
    }

    /**
     * 保存新员工信息
     * 注意：@DateTimeFormat 用于将前端传来的字符串日期转换为Date对象
     */
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") EmployeeInf employeeInf,
                               @RequestParam(value = "empBirthDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date empBirthDate,
                               RedirectAttributes redirectAttributes) {
        logger.info("Request to save new employee: {}", employeeInf.getEmpName());
        employeeInf.setEmpBirth(empBirthDate); // 设置生日

        boolean success = employeeInfServicelmpl.addEmployee(employeeInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "员工 '" + employeeInf.getEmpName() + "' 添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "添加员工 '" + employeeInf.getEmpName() + "' 失败。请检查输入信息。");
            // 理想情况下，应返回表单并显示具体错误
            // model.addAttribute("employee", employeeInf); // 返回已填数据
            // loadFormDependencies(model);
            // model.addAttribute("pageTitle", "添加新员工");
            // model.addAttribute("isEdit", false);
            // return "employees/employee-form";
        }
        return "redirect:/employees/list";
    }

    /**
     * 更新员工信息
     */
    @PostMapping("/update/{empId}")
    public String updateEmployee(@PathVariable("empId") Integer empId,
                                 @ModelAttribute("employee") EmployeeInf employeeInf,
                                 @RequestParam(value = "empBirthDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date empBirthDate,
                                 RedirectAttributes redirectAttributes, Model model) {
        logger.info("Request to update employee ID {}: {}", empId, employeeInf.getEmpName());
        employeeInf.setEmpId(empId);
        employeeInf.setEmpBirth(empBirthDate);

        boolean success = employeeInfServicelmpl.updateEmployee(employeeInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "员工 '" + employeeInf.getEmpName() + "' 更新成功！");
            return "redirect:/employees/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "更新员工 '" + employeeInf.getEmpName() + "' 失败。请检查输入信息。");
            // 返回编辑表单并显示错误
            employeeInf.setEmpId(empId); // 确保ID还在
            model.addAttribute("employee", employeeInf);
            loadFormDependencies(model);
            model.addAttribute("formAction", "/employees/update/" + empId);
            model.addAttribute("pageTitle", "编辑员工信息");
            model.addAttribute("isEdit", true);
            return "employees/employee-form";
        }
    }

    /**
     * 删除员工信息
     */
    @GetMapping("/delete/{empId}")
    public String deleteEmployee(@PathVariable("empId") Integer empId, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete employee ID: {}", empId);
        EmployeeInf empToDelete = employeeInfServicelmpl.getEmployeeByIdWithDetails(empId);
        if (empToDelete == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：未找到ID为 " + empId + " 的员工。");
            return "redirect:/employees/list";
        }

        boolean success = employeeInfServicelmpl.deleteEmployeeById(empId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "员工 '" + empToDelete.getEmpName() + "' 删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "删除员工 '" + empToDelete.getEmpName() + "' 失败。");
        }
        return "redirect:/employees/list";
    }
}