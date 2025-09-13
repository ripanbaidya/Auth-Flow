package org.astrobrains.authflow.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.astrobrains.authflow.config.properties.TwilioProperties;
import org.astrobrains.authflow.service.TwilioService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class TwilioServiceImpl implements TwilioService {
    private final TemplateEngine templateEngine;
    private final TwilioProperties twilioProperties;

    @Override
    public void sendOtpSms(String phoneNumber, String otp) {
        // prepare the thymeleaf context
        var context = new Context();
        context.setVariable("otp", otp);

        // process the template
        String messageBody = templateEngine.process("otp-sms.txt", context);

        // send message via twilio
        Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
        Message.creator(
                new PhoneNumber(phoneNumber), // send to this phone number
                new PhoneNumber(twilioProperties.getPhoneNumber()), // send from this phone number
                messageBody
        ).create();
    }
}