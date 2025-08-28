package com.enesguzel.authify.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromMail;

    public void sendWelcomeMail(String email,String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Aramıza Hoşgeldin"+name);
        message.setText("Hoşgeldin "+name+"!\n\nBizi tercih ettiğiniz için teşekkür ederiz!\n\n Saygılarımızla,\nGUZEL Developer Team♥");
        mailSender.send(message);
    }

    public void sendResetOtpMail(String email,String resetOtp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Şifre Sıfırlama Maili");
        message.setText("Şifrenizi sıfırlama isteği aldık, eğer siz değilseniz bu maili dikkate almayınız.\n\n Kod : "+resetOtp);
        mailSender.send(message);
    }

    public void sendVerifyOtpMail(String email,String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Emailinizi Doğrulayın");
        message.setText("Aşağıdaki kodu kullanarak Emailinizi doğrulayabilirsiniz!\n\n Kod : "+otp);
        mailSender.send(message);
    }

}
