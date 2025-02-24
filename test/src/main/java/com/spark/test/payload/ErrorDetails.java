package com.spark.test.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error Details Information")
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
}
