package org.example.hr.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    /**
     * 添加自定义拦截器
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/error"); // 排除静态资源和错误页
        logger.info("LoggingInterceptor registered.");
    }

    /**
     * 添加自定义的日期格式化器
     * 这使得在Controller中可以直接使用 @RequestParam Date date 而不需要每次都加 @DateTimeFormat
     * 不过，对于表单提交的日期，通常还是建议在 @RequestParam 上使用 @DateTimeFormat 以明确指定格式
     * @param registry 格式化器注册表
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 全局日期格式化，主要影响从请求参数到Date类型的绑定（如果参数没有@DateTimeFormat）
        // 以及Model中Date类型到字符串的默认格式化（如果Thymeleaf没有用#dates.format指定）
        DateFormatter dateFormatter = new DateFormatter("yyyy-MM-dd");
        // dateFormatter.setLenient(true); // 设置是否宽松解析
        registry.addFormatter(dateFormatter);
        logger.info("Global DateFormatter for 'yyyy-MM-dd' registered.");
    }

    /**
     * 配置静态资源处理
     * Spring Boot 默认会从 classpath:/static/, classpath:/public/, classpath:/resources/, classpath:/META-INF/resources/ 加载
     * 通常不需要额外配置，除非你有特殊需求，比如自定义路径或缓存策略。
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 示例：如果你想将 /my-custom-static/** 映射到 classpath:/custom-static/
        // registry.addResourceHandler("/my-custom-static/**")
        //         .addResourceLocations("classpath:/custom-static/");
        // logger.info("Custom static resource handler registered for /my-custom-static/**.");

        // Spring Boot 默认的静态资源处理已经很好了，这里可以不加。
        // 如果加了，要确保不要覆盖掉默认的，或者明确配置所有需要的。
    }

    /**
     * 简单的日志拦截器示例
     */
    static class LoggingInterceptor implements HandlerInterceptor {
        private static final Logger interceptorLogger = LoggerFactory.getLogger(LoggingInterceptor.class);

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);
            interceptorLogger.info("Request URL::{}:: Start Time={}", request.getRequestURL(), System.currentTimeMillis());
            return true; // 继续执行后续的拦截器和处理器
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            interceptorLogger.info("Request URL::{}:: Sent to Handler :: Current Time={}", request.getRequestURL(), System.currentTimeMillis());
            // 可以在这里修改 ModelAndView
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            long startTime = (Long) request.getAttribute("startTime");
            long endTime = System.currentTimeMillis();
            interceptorLogger.info("Request URL::{}:: End Time={}:: Time Taken={}",
                    request.getRequestURL(), endTime, (endTime - startTime));
            if (ex != null) {
                interceptorLogger.error("Request URL::{}:: Exception occurred: {}", request.getRequestURL(), ex.getMessage());
            }
        }
    }
}