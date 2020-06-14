package com.hq.myblog.Handler;

import com.hq.myblog.Exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, CustomException e) {
        log.error("Request URL: {},Exception :{}", request.getRequestURL(), e.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code", e.getCode());
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
