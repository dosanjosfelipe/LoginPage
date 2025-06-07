package me.LoginPage.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.LoginPage.util.HtmlLoader;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Constrói e envia o email para o usuário
    public void sendEmail(String userEmail, String userName, String emailPath, String subject, String propose, String token)
            throws MessagingException, IOException {
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true);
        
        if (propose.equals("register")) {
            String body = HtmlLoader.RegisterEmailFormatter(userName, emailPath);
            email.setFrom("umbrellaweatherapp@gmail.com");
            email.setTo(userEmail);
            email.setSubject(subject);
            email.setText(body, true);
        } else if (propose.equals("reset")) {
            String body = HtmlLoader.ResetTokenEmailFormatter(emailPath, token);
            email.setFrom("umbrellaweatherapp@gmail.com");
            email.setTo(userEmail);
            email.setSubject(subject);
            email.setText(body, true);
        }
        
        mailSender.send(mimeMessage);
    }
}
