package com.codes.Help_desk_backend.service;

import reactor.core.publisher.Flux;

public interface AiService {

    String getResponseFromAssistant(String message, String email);

    Flux<String> streamResponseFromAssistant(String message, String email);
}