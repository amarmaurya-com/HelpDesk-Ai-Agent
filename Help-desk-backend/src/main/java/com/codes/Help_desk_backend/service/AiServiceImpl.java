package com.codes.Help_desk_backend.service;

import com.codes.Help_desk_backend.tools.EmailTool;
import com.codes.Help_desk_backend.tools.TicketDatabaseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final TicketDatabaseTool ticketDatabaseTool;
    private final EmailTool emailTool;
    @Value("classpath:/helpdesk_system.st")
    private Resource systemPromptResource;

    public AiServiceImpl(ChatClient chatClient, TicketDatabaseTool ticketDatabaseTool, EmailTool emailTool) {
        this.chatClient = chatClient;
        this.ticketDatabaseTool = ticketDatabaseTool;
        this.emailTool = emailTool;
    }

    @Override
    public String getResponseFromAssistant(String message, String email) {

        try {
            return chatClient.prompt()
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, email))
                    .system(systemSpec ->
                            systemSpec.text(systemPromptResource)
                                    .param("userEmail", email)
                    )
                    .tools(ticketDatabaseTool, emailTool)
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Flux<String> streamResponseFromAssistant(String message, String email) {
        return this.chatClient.prompt()
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID, email))
                .system(systemPromptResource)
                .tools(ticketDatabaseTool, emailTool)
                .user(message)
                .stream()
                .content();
    }
}