package com.codes.Help_desk_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiConfig {


    Logger logger = LoggerFactory.getLogger(AiConfig.class);




    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

        this.logger.info("Creating Chat Client bean");
        this.logger.info("Creating Chat memory bean {}",  chatMemory.getClass().getName());
            return builder
                    .defaultSystem("You are a teacher of every every subject. Response as smaller as possible")
                    .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor
                            .builder(chatMemory)
                            .build()
                    )
                    .build();
    }

}
