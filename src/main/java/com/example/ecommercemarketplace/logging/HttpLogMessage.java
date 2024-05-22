package com.example.ecommercemarketplace.logging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class HttpLogMessage {

    private String requestId;
    private Request request;
    private Response response;

    @Data
    @Builder
    public static class Request {
        private String method;
        private String url;
        private String clientIp;
        private String body;
        private Map<String, String> headers;
    }

    @Data
    @Builder
    public static class Response {
        private int status;
        private String body;
    }
}



