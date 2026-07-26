package com.codes.Help_desk_backend.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class EmailTool {
    @Tool(description="This tool help to send email to support team regarding new ticket")
    public void sendEmailToSupportTeam(
            @ToolParam(description = "Email id associated with ticket for contact information") String email,
            @ToolParam(description = "Short description of ticket summery") String summery){

    }
}
