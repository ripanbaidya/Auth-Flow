package org.astrobrains.authflow.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.astrobrains.authflow.service.EmailService;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String DEFAULT_FROM = "no-reply@astrobrains.com";

    @Override
    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) {
        try {
            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariables(Map.of(
                    "otp", otp,
                    "email", userEmail,
                    "subject", subject,
                    "text", text
            ));

            // Render HTML template
            String htmlContent = templateEngine.process("otp-email", context);

            // Debug: print HTML content
            log.debug("Generated OTP email content:\n{}", htmlContent);

            // Prepare MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            helper.setFrom(DEFAULT_FROM);
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML content

            // Send email
            javaMailSender.send(mimeMessage);
            log.info("OTP email sent successfully to {}", userEmail);

        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", userEmail, e.getMessage(), e);
            throw new MailSendException("Failed to send verification OTP email", e);
        }
    }
}
