package PreviousYear.questions.demo.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendVerificationEmail(String toEmail, String token) throws IOException {
        Email from = new Email("qparchive.team@gmail.com");
        String subject = "Complete Your QPArchive Registration";
        Email to = new Email(toEmail);
        String verificationUrl = "https://qp-backend-sg1x.onrender.com/auth/verify?token=" + token;
        String htmlBody = "<html>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>"
                + "<h2>Welcome to QPArchive!</h2>"
                + "<p>Hi there,</p>"
                + "<p>Thanks for signing up with QPArchive. We're excited to help you explore and archive past exam papers with ease.</p>"
                + "<p>To activate your account, please verify your email address by clicking the button below. This link will expire in 24 hours.</p>"
                + "<br>"
                + "<a href='" + verificationUrl + "' "
                + "style='display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>"
                + "Verify Your Account"
                + "</a>"
                + "<br><br>"
                + "<p>If you didn’t sign up for QPArchive, you can safely ignore this email.</p>"
                + "<hr style='margin-top: 30px;'>"
                + "<p style='font-size: 12px; color: #777;'>"
                + "Sent by QPArchive • qparchive.team@gmail.com<br>"
                + "This is an automated message. Please do not reply directly to this email."
                + "</p>"
                + "</body>"
                + "</html>";

        Content content = new Content("text/html", htmlBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("SendGrid response: " + response.getStatusCode());
    }
}