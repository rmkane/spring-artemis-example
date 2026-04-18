package org.acme.parse.apps.consumerxml.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.acme.parse.jms.consumer.lifecycle.JmsListenerNotFoundException;

@RestControllerAdvice
public class ConsumerXmlRestExceptionHandler {

    @ExceptionHandler(JmsListenerNotFoundException.class)
    public ProblemDetail jmsListenerNotFound(JmsListenerNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("JMS listener not found");
        return detail;
    }
}
