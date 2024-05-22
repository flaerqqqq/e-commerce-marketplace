package com.example.ecommercemarketplace.filters;

import com.example.ecommercemarketplace.logging.HttpLogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class HttpLoggingFilter extends OncePerRequestFilter {

    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);
        log(requestWrapper, responseWrapper);
    }

    private void log(ContentCachingRequestWrapper requestWrapper,
                     ContentCachingResponseWrapper responseWrapper) throws JsonProcessingException {
        HttpLogMessage httpLogMessage = HttpLogMessage.builder()
                .requestId(requestWrapper.getRequestId())
                .request(HttpLogMessage.Request.builder()
                        .method(requestWrapper.getMethod())
                        .url(requestWrapper.getRequestURI())
                        .clientIp(requestWrapper.getRemoteAddr())
                        .body(new String(requestWrapper.getContentAsByteArray()))
                        .headers(getHeaders(requestWrapper))
                        .build())
                .response(HttpLogMessage.Response.builder()
                        .status(responseWrapper.getStatus())
                        .body(new String(responseWrapper.getContentAsByteArray()))
                        .build())
                .build();
        System.out.println(objectMapper.writeValueAsString(httpLogMessage));
        log.info(objectMapper.writeValueAsString(httpLogMessage));
    }

    private Map<String, String> getHeaders(ContentCachingRequestWrapper requestWrapper){
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();

        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = requestWrapper.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        return headers;
    }

    private Map<String, String> getHeaders(ContentCachingResponseWrapper responseWrapper){
        Map<String, String> headers = new HashMap<>();

        for (String headerName : responseWrapper.getHeaderNames()) {
            String headerValue = responseWrapper.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        return headers;
    }
}