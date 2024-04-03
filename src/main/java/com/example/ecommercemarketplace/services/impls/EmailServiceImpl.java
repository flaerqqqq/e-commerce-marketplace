package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.services.EmailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;


    @Override
    public void sendMessageWithVerificationCode(String toEmail, String code) throws MessagingException {
        String subject = "Email Confirmation!";
        String body = """
                    <!DOCTYPE html>
                    <html>
                    <body>
                        <p>Hello there!</p>
                        <p>Click the button below to confirm your email address:</p>
                        <form action="http://localhost:8080/api/confirm" method="GET">
                            <input type="hidden" name="token" value="%s">
                            <button style="background-color: #007bff; color: white; border: none; padding: 10px 20px; cursor: pointer;" type="submit">
                                Confirm Email
                            </button>
                        </form>
                    </body>
                    </html>
                """.formatted(code);
        sendMail(toEmail, subject, body);
    }

    @Override
    public void sendMessageWithPasswordResetCode(String toEmail, String code) throws MessagingException {
        String subject = "Password Reset!";
        String body = """
                    <!DOCTYPE html>
                    <html>
                    <body>
                        <p>Hello there!</p>
                        <p>Click the button below to confirm your email address:</p>
                        <form action="http://localhost:8080/verification/password-reset" method="GET">
                            <input type="hidden" name="token" value="%s">
                            <button style="background-color: #007bff; color: white; border: none; padding: 10px 20px; cursor: pointer;" type="submit">
                                Confirm Email
                            </button>
                        </form>
                    </body>
                    </html>
                """.formatted(code);
        sendMail(toEmail, subject, body);
    }


    @Override
    public void sendMail(String to, String subject, String body) throws MessagingException {
        var message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);

        mailSender.send(message);
    }


}
