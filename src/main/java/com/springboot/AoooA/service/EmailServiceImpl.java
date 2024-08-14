package com.springboot.AoooA.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
   
   private static final Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

     @Autowired
       private JavaMailSender emailSender;
     
       @Override
       public void sendTemporaryPassword(String userEmail, String temporaryPassword) {
           MimeMessage mimeMessage = emailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
           try {
               helper.setTo(userEmail);
               helper.setSubject("임시 비밀번호 발급 안내");
               helper.setText("안녕하세요,\n임시 비밀번호는 다음과 같습니다: " + temporaryPassword);
               logger.info("Sending temporary password to " + userEmail);
           } catch (MessagingException e) {
               e.printStackTrace();
           }
           emailSender.send(mimeMessage);
       }
}