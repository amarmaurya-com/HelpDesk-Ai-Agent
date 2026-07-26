package com.codes.Help_desk_backend.controller;

import com.codes.Help_desk_backend.service.AiService;
import com.codes.Help_desk_backend.service.AiServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<String> getResponseFromAssistant(@RequestBody String message, @RequestHeader("email") String  email)
    {
        return ResponseEntity.ok(aiService.getResponseFromAssistant(message, email));
    }

    @PostMapping("/stream")
    public ResponseEntity<Flux<String>> stringFlux(@RequestBody String message, @RequestHeader("email") String  email)
    {
        return ResponseEntity.ok(aiService.streamResponseFromAssistant(message, email));
    }
}
