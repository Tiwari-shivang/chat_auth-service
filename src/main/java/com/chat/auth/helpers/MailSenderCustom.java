package com.chat.auth.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Setter
@Component
public class MailSenderCustom {
    @Autowired
    private JavaMailSender sender;

    private String to, subject, content;

    public void sendMail(){
        try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(this.to);
            helper.setSubject(this.subject);
            helper.setText(this.content, true);
            sender.send(message);
        }
        catch (MessagingException ex){
            System.out.println(ex);
        }
    }
}
