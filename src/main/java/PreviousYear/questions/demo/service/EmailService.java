package PreviousYear.questions.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a formatted HTML verification email to a new user.
     * 
     * @param toEmail The recipient's email address.
     * @param token   The unique verification token.
     */
    public void sendVerificationEmail(String toEmail, String token) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            // Use MimeMessageHelper for HTML emails
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String verificationUrl = "https://qp-backend-sg1x.onrender.com/auth/verify?token=" + token;

            // --- THIS IS THE NEW HTML EMAIL BODY ---
            String htmlBody = "<html>"
                    + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<h2>Greetings from the QPArchive Team!</h2>"
                    + "<p>Thank you for creating an account with us. We're excited to have you on board.</p>"
                    + "<p>To complete your registration, please click the button below to verify your email address. This link is valid for 24 hours.</p>"
                    + "<br>"
                    + "<a href='" + verificationUrl + "' "
                    + "style='display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>"
                    + "Verify Your Account"
                    + "</a>"
                    + "<br><br>"
                    + "<p>If you did not request this, please ignore this email.</p>"
                    + "<br>"
                    + "<p>Best regards,<br>The QPArchive Team</p>"
                    + "</body>"
                    + "</html>";

            helper.setFrom("qparchive.team@gmail.com"); // This must match your configured email in
                                                        // application.properties
            helper.setTo(toEmail);
            helper.setSubject("Complete Your QPArchive Registration");
            helper.setText(htmlBody, true); // The 'true' argument specifies that the content is HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // In a real application, you would have more robust error logging here
            System.err.println("Error sending HTML verification email: " + e.getMessage());
        }
    }
}