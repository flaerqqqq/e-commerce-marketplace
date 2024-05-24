package com.example.ecommercemarketplace.logging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    public static class Request {
        private String method;
        private String url;
        private String clientIp;
        private JsonObject body;
        private Map<String, String> headers;

        public Request(String method, String url, String clientIp, String body, Map<String, String> headers) {
            this.method = method;
            this.url = url;
            this.clientIp = clientIp;
            this.body = new Gson().fromJson(body, JsonObject.class);
            this.headers = headers;
        }
    }

    @Data
    public static class Response {
        private int status;
        private JsonObject body;

        public Response(int status, String body) {
            this.status = status;
            this.body = new Gson().fromJson(body, JsonObject.class);
        }
    }
}



