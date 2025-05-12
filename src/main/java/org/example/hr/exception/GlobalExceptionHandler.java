package org.example.hr.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice // 声明这是一个控制器建言，用于全局处理异常
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的 BusinessException
     * @param req HTTP请求对象
     * @param ex BusinessException 实例
     * @param model Spring MVC Model
     * @return 错误页面的视图名称
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 可以根据业务异常的性质设置不同的HTTP状态码
    public ModelAndView handleBusinessException(HttpServletRequest req, BusinessException ex, Model model) {
        logger.warn("BusinessException Occurred:: URL={}, Message={}", req.getRequestURL(), ex.getMessage());
        ModelAndView mav = new ModelAndView();
        model.addAttribute("errorMessage", "业务操作失败：" + ex.getMessage());
        model.addAttribute("url", req.getRequestURL());
        mav.setViewName("error"); // 指向 templates/error.html
        return mav;
    }

    /**
     * 处理所有未被其他 @ExceptionHandler 捕获的异常 (通用异常处理)
     * @param req HTTP请求对象
     * @param ex Exception 实例
     * @param model Spring MVC Model
     * @return 错误页面的视图名称
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 对于未知异常，返回500状态码
    public ModelAndView handleGlobalException(HttpServletRequest req, Exception ex, Model model) {
        logger.error("Unhandled Exception Occurred:: URL={}, ExceptionClass={}, Message={}",
                req.getRequestURL(), ex.getClass().getName(), ex.getMessage(), ex); // 记录完整的堆栈信息

        ModelAndView mav = new ModelAndView();
        model.addAttribute("errorMessage", "系统发生了一个意外错误，请联系管理员。");
        model.addAttribute("detailMessage", "错误类型: " + ex.getClass().getSimpleName()); // 可以选择性显示
        model.addAttribute("url", req.getRequestURL());
        mav.setViewName("error"); // 指向 templates/error.html
        return mav;
    }
}