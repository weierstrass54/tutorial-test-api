package com.weierstrass54.api.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class WebErrorHandler {
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage any(HttpServletRequest request, Throwable t) {
        return getErrorMessage(request, t);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage httpNotFound(HttpServletRequest request, NoHandlerFoundException e) {
        return getErrorMessage(request, e);
    }

    @ExceptionHandler({
        MissingPathVariableException.class,
        MissingRequestCookieException.class,
        MissingRequestHeaderException.class,
        MissingMatrixVariableException.class,
        ServletRequestBindingException.class,
        MethodArgumentNotValidException.class,
        MissingServletRequestParameterException.class,
        UnsatisfiedServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage httpBadRequest(HttpServletRequest request, Throwable e) {
        return defaultClientException(request, e);
    }

    protected ErrorMessage defaultClientException(HttpServletRequest request, Throwable t) {
        return getErrorMessage(request, t);
    }

    protected ErrorMessage getErrorMessage(HttpServletRequest request, Throwable t) {
        String method = request.getMethod();
        String query = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) != null ?
            request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString() : request.getRequestURI() + getQueryString(request);
        String errorMessage = t.getMessage();
        return new ErrorMessage(method, query, errorMessage);
    }

    private String getQueryString(HttpServletRequest request) {
        if (request.getQueryString() != null) {
            return "?" + request.getQueryString();
        }
        return "";
    }

    @Getter
    @RequiredArgsConstructor
    protected static class ErrorMessage {
        private final String method;
        private final String query;
        private final String errorMessage;

        @Override
        public String toString() {
            return "ErrorMessage{" +
                "method='" + method + '\'' +
                ", query='" + query + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
        }
    }

}
