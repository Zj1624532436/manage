package com.sys.manage.common.exception;


import com.sys.manage.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义全局异常处理，当出现RuntimeException时，将错误信息封装到BackResult中进行返回
 * 这里主要针对的是自定义的LoginFilter和自定义的MyUserDetailService内部实现抛出的异常
 */
@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public R handler(RuntimeException e){
        log.error("运行时异常：---------------{}",e.getMessage());
        return R.error(500,e.getMessage());
    }

}
