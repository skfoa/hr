package org.example.hr.controller;

import org.example.hr.pojo.JobInf;
import org.example.hr.service.impl.JobInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/jobs") // 模块的基础路径
public class JobInfController {

    private static final Logger logger = LoggerFactory.getLogger(JobInfController.class);

    private final JobInfServicelmpl jobInfServicelmpl;

    @Autowired
    public JobInfController(JobInfServicelmpl jobInfServicelmpl) {
        this.jobInfServicelmpl = jobInfServicelmpl;
    }

    /**
     * 显示岗位列表页面
     * @param model 用于向视图传递数据
     * @return 岗位列表视图的逻辑名称
     */
    @GetMapping("/list")
    public String listJobs(Model model) {
        logger.info("Request to list all jobs");
        List<JobInf> jobs = jobInfServicelmpl.getAllJobs();
        model.addAttribute("jobs", jobs);
        model.addAttribute("pageTitle", "岗位列表");
        return "jobs/job-list"; // Thymeleaf模板路径: templates/jobs/job-list.html
    }

    /**
     * 显示添加岗位的表单页面
     * @param model 用于向视图传递一个空的JobInf对象
     * @return 添加岗位表单视图的逻辑名称
     */
    @GetMapping("/add")
    public String showAddJobForm(Model model) {
        logger.info("Request to show add job form");
        model.addAttribute("job", new JobInf());
        model.addAttribute("formAction", "/jobs/save");
        model.addAttribute("pageTitle", "添加新岗位");
        model.addAttribute("isEdit", false);
        return "jobs/job-form"; // Thymeleaf模板路径: templates/jobs/job-form.html
    }

    /**
     * 显示编辑岗位的表单页面
     * @param jobId 要编辑的岗位ID
     * @param model 用于向视图传递数据
     * @param redirectAttributes 用于重定向时传递消息
     * @return 编辑岗位表单视图的逻辑名称
     */
    @GetMapping("/edit/{jobId}")
    public String showEditJobForm(@PathVariable("jobId") Integer jobId, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Request to show edit job form for ID: {}", jobId);
        JobInf job = jobInfServicelmpl.getJobById(jobId);
        if (job == null) {
            logger.warn("Job not found for ID: {}", jobId);
            redirectAttributes.addFlashAttribute("errorMessage", "未找到ID为 " + jobId + " 的岗位。");
            return "redirect:/jobs/list";
        }
        model.addAttribute("job", job);
        model.addAttribute("formAction", "/jobs/update/" + jobId);
        model.addAttribute("pageTitle", "编辑岗位信息");
        model.addAttribute("isEdit", true);
        return "jobs/job-form";
    }

    /**
     * 保存新岗位信息
     * @param jobInf 从表单绑定的岗位对象
     * @param redirectAttributes 用于重定向时传递消息
     * @return 重定向到岗位列表页
     */
    @PostMapping("/save")
    public String saveJob(@ModelAttribute("job") JobInf jobInf, RedirectAttributes redirectAttributes) {
        logger.info("Request to save new job: {}", jobInf.getJobName());
        boolean success = jobInfServicelmpl.addJob(jobInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "岗位 '" + jobInf.getJobName() + "' 添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "添加岗位 '" + jobInf.getJobName() + "' 失败，可能名称已存在或输入无效。");
        }
        return "redirect:/jobs/list";
    }

    /**
     * 更新岗位信息
     * @param jobId 要更新的岗位ID
     * @param jobInf 从表单绑定的岗位对象
     * @param redirectAttributes 用于重定向时传递消息
     * @return 重定向到岗位列表页
     */
    @PostMapping("/update/{jobId}")
    public String updateJob(@PathVariable("jobId") Integer jobId,
                            @ModelAttribute("job") JobInf jobInf,
                            RedirectAttributes redirectAttributes) {
        logger.info("Request to update job ID {}: {}", jobId, jobInf.getJobName());
        jobInf.setJobId(jobId);
        boolean success = jobInfServicelmpl.updateJob(jobInf);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "岗位 '" + jobInf.getJobName() + "' 更新成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "更新岗位 '" + jobInf.getJobName() + "' 失败，可能名称冲突或岗位不存在。");
        }
        return "redirect:/jobs/list";
    }

    /**
     * 删除岗位信息
     * @param jobId 要删除的岗位ID
     * @param redirectAttributes 用于重定向时传递消息
     * @return 重定向到岗位列表页
     */
    @GetMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable("jobId") Integer jobId, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete job ID: {}", jobId);
        JobInf jobToDelete = jobInfServicelmpl.getJobById(jobId);
        if (jobToDelete == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：未找到ID为 " + jobId + " 的岗位。");
            return "redirect:/jobs/list";
        }

        boolean success = jobInfServicelmpl.deleteJobById(jobId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "岗位 '" + jobToDelete.getJobName() + "' 删除成功！");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "删除岗位 '" + jobToDelete.getJobName() + "' 失败。可能该岗位下仍有员工，或岗位不存在。");
        }
        return "redirect:/jobs/list";
    }
}