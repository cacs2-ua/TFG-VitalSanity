package vitalsanity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Metodo para enviar email de confirmacion usando Mailtrap
    public void sendConfirmationEmail(String to, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmation Code");
        message.setText("Your confirmation code is: " + confirmationCode);
        mailSender.send(message);
    }
}
