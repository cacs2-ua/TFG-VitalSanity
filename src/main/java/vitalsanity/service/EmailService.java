package vitalsanity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Reemplaza los metodos anteriores por el siguiente metodo unificado:
    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vital@sanity.es");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    // Metodo para enviar email de confirmacion usando Mailtrap
    public void sendConfirmationEmail(String to, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vital@sanity.es");
        message.setTo(to);
        message.setSubject("Registration Confirmation Code");
        message.setText("Your registration confirmation code is: " + confirmationCode);
        mailSender.send(message);
    }

    // Metodo para enviar email al centro medico con la contrasenya generada
    public void sendCentroMedicoPasswordEmail(String to, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vital@sanity.es");
        message.setTo(to);
        message.setSubject("Registro Centro Medico");
        message.setText("Se ha registrado su centro medico. Su contrasenya de acceso es: " + password +
                ". Cuando inicie sesion por primera vez, debera cambiarla por una nueva.");
        mailSender.send(message);
    }

}
