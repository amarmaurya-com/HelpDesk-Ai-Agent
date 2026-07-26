package com.codes.Help_desk_backend.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class EmailTool {
    private static final Logger logger = LoggerFactory.getLogger(EmailTool.class);

    @Tool(description="This tool help to send email to support team regarding new ticket")
    public String sendEmailToSupportTeam(
            @ToolParam(description = "Email id associated with ticket for contact information") String email,
            @ToolParam(description = "Short description of ticket summary") String summary){

        logger.info("Support notification requested for {}: {}", email, summary);
        return "Support team notification was recorded for " + email + ".";
    }
}
