package com.shiproutepro.backend.mail.events.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Component
public class SendEmail {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public void send(String email, String name, String subject,
                          String templateName, String urlVariableName,
                          String url) throws MessagingException,
            UnsupportedEncodingException {

        String senderName = "EntryGenius";
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable(urlVariableName, url);
        String mailContent = templateEngine.process(templateName, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("documentapprovalsystem@gmail.com", senderName);
        helper.setSubject(subject);
        helper.setTo(email);
        helper.setText(mailContent, true);
        javaMailSender.send(mimeMessage);
    }
}
