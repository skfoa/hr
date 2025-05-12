package org.example.hr.controller;

import org.example.hr.pojo.DeptInf;
import org.example.hr.service.impl.DeptInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/departments") // 模块的基础路径
public class DeptInfController {

    private static final Logger logger = LoggerFactory.getLogger(DeptInfController.class);

    private final DeptInfServicelmpl deptInfService;

    @Autowired
    public DeptInfController(DeptInfServicelmpl deptInfService) {
        this.deptInfService = deptInfService;
    }

    /**
     * 显示部门列表页面
     * @param model 用于向视图传递数据
     * @return 部门列表视图的逻辑名称
     */
    @GetMapping("/list")
    public String listDepartments(Model model) {
        logger.info("Request to list all departments");
        List<DeptInf> depts = deptInfService.getAllDepts();
        model.addAttribute("departments", depts);
        model.addAttribute("pageTitle", "部门列表"); // 设置页面标题
        return "departments/department-list"; // Thymeleaf模板路径: templates/departments/department-list.html
    }

    /**
     * 显示添加部门的表单页面
     * @param model 用于向视图传递一个空的DeptInf对象，以便表单绑定
     * @return 添加部门表单视图的逻辑名称
     */
    @GetMapping("/add")
    public String showAddDepartmentForm(Model model) {
        logger.info("Request to show add department form");
        model.addAttribute("department", new DeptInf()); // 新建一个空对象给表单
        model.addAttribute("formAction", "/departments/save"); // 表单提交的动作URL
        model.addAttribute("pageTitle", "添加新部门");
        model.addAttribute("isEdit", false); // 标记为非编辑模式
        return "departments/department-form"; // Thymeleaf模板路径: templates/departments/department-form.html
    }

    /**
     * 显示编辑部门的表单页面
     * @param deptId 要编辑的部门ID，从路径变量获取
     * @param model 用于向视图传递数据
     * @param redirectAttributes 用于重定向时传递消息
     * @return 编辑部门表单视图的逻辑名称，如果部门不存在则重定向到列表页
     */
    @GetMapping("/edit/{deptId}")
    public String showEditDepartmentForm(@PathVariable("deptId") Integer deptId, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Request to show edit department form for ID: {}", deptId);
        DeptInf dept = deptInfService.getDeptById(deptId);
        if (dept == null) {
            logger.warn("Department not found for ID: {}", deptId);
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + deptId + " 的部门。");
            return "redirect:/departments/list";
        }
        model.addAttribute("department", dept);
        model.addAttribute("formAction", "/departments/update/" + deptId); // 表单提交的动作URL
        model.addAttribute("pageTitle", "编辑部门信息");
        model.addAttribute("isEdit", true); // 标记为编辑模式
        return "departments/department-form";
    }

    /**
     * 保存新部门信息 (处理添加表单的提交)
     * @param deptInf 从表单绑定的部门对象
     * @param redirectAttributes 用于重定向时传递成功或失败的消息
     * @return 重定向到部门列表页
     */
    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department") DeptInf deptInf, RedirectAttributes redirectAttributes) {
        logger.info("Request to save new department: {}", deptInf.getDeptName());
        boolean success = deptInfService.addDept(deptInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "部门 '" + deptInf.getDeptName() + "' 添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "添加部门 '" + deptInf.getDeptName() + "' 失败，可能名称已存在或输入无效。");
        }
        return "redirect:/departments/list";
    }

    /**
     * 更新部门信息 (处理编辑表单的提交)
     * @param deptId 要更新的部门ID
     * @param deptInf 从表单绑定的部门对象
     * @param redirectAttributes 用于重定向时传递消息
     * @return 重定向到部门列表页
     */
    @PostMapping("/update/{deptId}")
    public String updateDepartment(@PathVariable("deptId") Integer deptId,
                                   @ModelAttribute("department") DeptInf deptInf,
                                   RedirectAttributes redirectAttributes) {
        logger.info("Request to update department ID {}: {}", deptId, deptInf.getDeptName());
        deptInf.setDeptId(deptId); // 确保ID被设置到要更新的对象上
        boolean success = deptInfService.updateDept(deptInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "部门 '" + deptInf.getDeptName() + "' 更新成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "更新部门 '" + deptInf.getDeptName() + "' 失败，可能名称冲突或部门不存在。");
            // return "redirect:/departments/edit/" + deptId; // 或者返回表单页并显示错误
        }
        return "redirect:/departments/list";
    }

    /**
     * 删除部门信息
     * @param deptId 要删除的部门ID
     * @param redirectAttributes 用于重定向时传递消息
     * @return 重定向到部门列表页
     */
    @GetMapping("/delete/{deptId}")
    public String deleteDepartment(@PathVariable("deptId") Integer deptId, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete department ID: {}", deptId);
        DeptInf deptToDelete = deptInfService.getDeptById(deptId); // 获取部门名称用于提示
        if (deptToDelete == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：未找到ID为 " + deptId + " 的部门。");
            return "redirect:/departments/list";
        }

        boolean success = deptInfService.deleteDeptById(deptId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "部门 '" + deptToDelete.getDeptName() + "' 删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "删除部门 '" + deptToDelete.getDeptName() + "' 失败。可能该部门下仍有员工，或部门不存在。");
        }
        return "redirect:/departments/list";
    }
}