package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessageWithVerificationCode(String toEmail, String code) {
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
    public void sendMessageWithPasswordResetCode(String toEmail, String code) {
        String subject = "Password Reset!";
        String body = """
                    <!DOCTYPE html>
                    <html>
                    <body>
                        <p>Hello there!</p>
                        <p>Click the button below to reset your password:</p>
                        <form action="http://localhost:8080/verification/password-reset" method="GET">
                            <input type="hidden" name="token" value="%s">
                            <button style="background-color: #007bff; color: white; border: none; padding: 10px 20px; cursor: pointer;" type="submit">
                                Reset Password
                            </button>
                        </form>
                    </body>
                    </html>
                """.formatted(code);
        sendMail(toEmail, subject, body);
    }

    @Override
    public void sendMail(String to, String subject, String body) {
        var message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom("local@email.com");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendSuccessfulRegistrationMessage(UserDto userDto) {
        String subject = "Successful Registration !";
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Registration Success</title>
                </head>
                <body>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="center">
                                <table cellpadding="0" cellspacing="0" border="0" style="max-width: 600px; width: 100%;">
                                    <tr>
                                        <td align="center" bgcolor="#f7f7f7" style="padding: 40px 0;">
                                            <h1>Welcome,""" + " " + userDto.getFirstName() + " " + userDto.getLastName() + """
                                        !</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" bgcolor="#ffffff" style="padding: 40px 0;">
                                            <p>Thank you for registering with us.</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" bgcolor="#f7f7f7" style="padding: 20px 0;">
                                            <p>If you have any questions, feel free to <a href="#">contact us</a>.</p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """;
        String email = userDto.getEmail();
        sendMail(email, subject, body);
    }

    @Override
    public void sendSuccessfulMerchantRegistrationMessage(MerchantDto merchantDto) {
        String subject = "Successful Registration !";
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Registration Success</title>
                </head>
                <body>
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="center">
                                <table cellpadding="0" cellspacing="0" border="0" style="max-width: 600px; width: 100%;">
                                    <tr>
                                        <td align="center" bgcolor="#f7f7f7" style="padding: 40px 0;">
                                            <h1>Welcome,""" + " " + merchantDto.getFirstName() + " " + merchantDto.getLastName() + """
                                        !</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" bgcolor="#ffffff" style="padding: 40px 0;">
                                            <p>Thank you for registering with us.</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" bgcolor="#f7f7f7" style="padding: 20px 0;">
                                            <p>If you have any questions, feel free to <a href="#">contact us</a>.</p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """;
        String email = merchantDto.getEmail();
        sendMail(email, subject, body);
    }

}
