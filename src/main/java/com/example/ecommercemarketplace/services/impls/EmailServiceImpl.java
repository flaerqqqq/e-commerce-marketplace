package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;


    @Override
    public void sendMessageWithVerificationCode(String to, String code) {
        String subject = "Email Confirmation!";
        String body = """
                    <!DOCTYPE html>
                    <html>
                    <body>
                        <p>Hello there!</p>
                        <p>Click the button below to confirm your email address:</p>
                        <a href="https://localhost:8080/api/confirm?token=%s">
                            <button style="background-color: #007bff; color: white; border: none; padding: 10px 20px; cursor: pointer;">
                                Confirm Email
                            </button>
                        </a>
                    </body>
                    </html>
                """.formatted(code);
        sendMail(to, subject, body);
    }

    @Override
    public void sendMail(String to, String subject, String body) {
        var message = mailSender.createMimeMessage();
        message.setTo()
    }


}
