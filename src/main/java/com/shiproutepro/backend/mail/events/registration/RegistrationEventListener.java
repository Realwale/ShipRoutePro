package com.shiproutepro.backend.mail.events.registration;


import com.shiproutepro.backend.constant.MailConstants;
import com.shiproutepro.backend.constant.UrlConstants;
import com.shiproutepro.backend.mail.events.common.EventType;
import com.shiproutepro.backend.mail.events.common.SendEmail;
import com.shiproutepro.backend.mail.events.common.onApplicationEvent;
import com.shiproutepro.backend.utils.EmailUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<onApplicationEvent> {

    private final HttpServletRequest request;

    private final SendEmail sendEmail;

    @Override
    public void onApplicationEvent(onApplicationEvent event) {
        try {
            if (event.getEventType() == EventType.REGISTRATION) {
                confirmRegistration(event);
            } else if (event.getEventType() == EventType.INVITATION) {
                inviteUser(event);
            }else if (event.getEventType() == EventType.RESEND) {
                resendLink(event);
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private void confirmRegistration(onApplicationEvent event) throws MessagingException,
            UnsupportedEncodingException {

        String subject = MailConstants.REGISTRATION_EMAIL_CONFIRMATION_SUBJECT;
        String urlVariableName = "confirmationEmailUrl";
        String templateName = "registrationConfirmation";
        String recipientEmail = event.getUser().getEmail();
        String url = EmailUtils.frontEndAppUrl(request) + UrlConstants.BASE_URL+ "/account/registration/verify?t=" +
                event.getToken()+"&email="+recipientEmail;

        sendEmail.send(recipientEmail, event.getUser().getFirstName(),
                subject, templateName, urlVariableName, url);

    }

    private void inviteUser(onApplicationEvent event) throws MessagingException,
            UnsupportedEncodingException {

        String subject = MailConstants.INVITE_USER_REGISTRATION_EMAIL_SUBJECT;
        String urlVariableName = "inviteUserEmailUrl";
        String templateName = "inviteUserEmail";
        String recipientEmail = event.getUser().getEmail();
        String url = EmailUtils.frontEndAppUrl(request) + UrlConstants.BASE_URL+ "/account/registration/set-password?t=" +
                event.getToken()+"&email="+recipientEmail;

        sendEmail.send(recipientEmail, event.getUser().getFirstName(),
                subject, templateName, urlVariableName, url);
    }

    private void resendLink(onApplicationEvent event) throws MessagingException,
            UnsupportedEncodingException {

        String subject = MailConstants.RESEND_CONFIRMATION_MAIL_SUBJECT;
        String urlVariableName = "newConfirmationEmailUrl";
        String templateName = "newConfirmationEmail";
        String recipientEmail = event.getUser().getEmail();
        String url = EmailUtils.frontEndAppUrl(request) + UrlConstants.BASE_URL+ "/account/registration/verify?t=" +
                event.getToken()+"&email="+recipientEmail;

        sendEmail.send(recipientEmail, event.getUser().getFirstName(),
                subject, templateName, urlVariableName, url);
    }
}
