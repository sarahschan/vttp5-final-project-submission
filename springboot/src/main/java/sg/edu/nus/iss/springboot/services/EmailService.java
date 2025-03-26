package sg.edu.nus.iss.springboot.services;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.mail.username}")
    private String goodPlateEmail;

    public void sendWelcomeEmail(String toEmail, String recipientName) throws MessagingException {

        try {

            Resource resource = resourceLoader.getResource("classpath:templates/WelcomeEmail.html");
            String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
            
            if (recipientName != null && !recipientName.isEmpty()) {
                String capitalizedName = recipientName.substring(0, 1).toUpperCase() + recipientName.substring(1);
                htmlContent = htmlContent.replace("{{name}}", capitalizedName);
            } else {
                htmlContent = htmlContent.replace("{{name}}", "there");
            }
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(goodPlateEmail, "The Good Plate");
            helper.setTo(toEmail);
            helper.setSubject("Welcome To The Good Plate");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        
        } catch (IOException e) {
            throw new MessagingException("Failed to load email template", e);

        }
    }

}
