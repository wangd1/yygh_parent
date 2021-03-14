package com.syt.yygh.common.exception;

import com.syt.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: wangdi
 * @Date: 2021/3/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result errorYygh(YyghException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }
}
