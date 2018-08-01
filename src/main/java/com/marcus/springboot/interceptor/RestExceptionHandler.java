package com.marcus.springboot.interceptor;

import com.marcus.springboot.exception.BookMisMatchException;
import com.marcus.springboot.exception.BookNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({BookNotFoundException.class})
    protected ResponseEntity handleNotFound(Exception ex, WebRequest request){
        return handleExceptionInternal(ex,"book not found",new HttpHeaders(),HttpStatus.NOT_FOUND,request);
    }

    @ExceptionHandler({BookMisMatchException.class,ConstraintViolationException.class,DataIntegrityViolationException.class})
    public ResponseEntity handleBadRequest(Exception ex,WebRequest request){
        return handleExceptionInternal(ex,ex.getLocalizedMessage(),new HttpHeaders(),HttpStatus.BAD_REQUEST,request);
    }
}
