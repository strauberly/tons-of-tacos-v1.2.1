package com.adamstraub.tonsoftacos.exceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

// search for unused handlers and eliminate
@Data
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String message;
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map <String, Object> handleNumberFormatException(
            NumberFormatException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, webRequest);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map <String, Object> handleEntityNotFoundException(
            EntityNotFoundException e, WebRequest webRequest) {
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.NOT_FOUND, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map <String, Object> handleIllegalArgumentException(
            IllegalArgumentException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUsernameNotFoundException(
            UsernameNotFoundException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest).toString());
        return  createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleSignatureException(
            SignatureException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest).toString());
        return  createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAuthenticationException(
            AuthenticationException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest).toString());
        return  createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleJwtException(
            JwtException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest).toString());
        return  createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleMalformedJwtException(
            MalformedJwtException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest).toString());
        return  createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleExpiredJwtException(
            ExpiredJwtException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest).toString());
        return  createExceptionMessage(e.getMessage(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, Object> handleBadCredentialsException(
            BadCredentialsException e, WebRequest webRequest
    ){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.FORBIDDEN, webRequest);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleRuntimeException(
            HttpServerErrorException.InternalServerError e, WebRequest webRequest
    ){
        logger.error(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException e, WebRequest webRequest){
        logger.debug(createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest).toString());
        return createExceptionMessage(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED, webRequest);
    }


    private Map<String,Object> createExceptionMessage(String e, HttpStatus status, WebRequest webRequest) {

    Map <String, Object> error = new HashMap<>();
    String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

    if(webRequest instanceof ServletWebRequest){
        error.put("uri",
                ((ServletWebRequest)webRequest).getRequest().getRequestURI());
    }
    error.put("message", e);
    error.put("status code", status.toString());
    error.put("timestamp", timestamp);
    return error;
    }
}
