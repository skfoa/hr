package org.example.hr.exception;

/**
 * 自定义业务异常类
 * 用于封装业务逻辑执行过程中发生的、可预期的错误。
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // 可以添加错误码等属性
    // private Integer code;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    // 如果添加了错误码，可以提供相应的构造函数和getter
    // public BusinessException(Integer code, String message) {
    //     super(message);
    //     this.code = code;
    // }
    // public Integer getCode() {
    //     return code;
    // }
}