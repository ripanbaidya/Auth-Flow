package org.astrobrains.authflow.controller;

import org.astrobrains.authflow.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/response")
    public ResponseEntity<ApiResponse<String>> getResponse() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Hello World")
                .data("this is a test response")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
