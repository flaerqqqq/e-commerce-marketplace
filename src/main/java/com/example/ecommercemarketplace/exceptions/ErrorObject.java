package com.example.ecommercemarketplace.exceptions;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorObject {

    private LocalDateTime timestamp;

    private Integer status;

    private Object message;
}
