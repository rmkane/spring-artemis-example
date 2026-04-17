package org.acme.parse.common.web;

import org.acme.parse.consumer.jms.JmsListenerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(JmsListenerNotFoundException.class)
    public ProblemDetail jmsListenerNotFound(JmsListenerNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("JMS listener not found");
        return detail;
    }
}
