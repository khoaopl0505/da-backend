package net.javaguids.da09.service.imp;

import net.javaguids.da09.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmailVerifyAccount(String to, String tokenMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Confirm to Verify Create Account");
        String verificationLink =  "http://localhost:8080/confirm?tokenMail="+tokenMail;
        String emailContent = "Xin chào,\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản. Để hoàn tất quá trình đăng ký, "
                + "vui lòng nhấp vào liên kết dưới đây để xác minh địa chỉ email của bạn:\n\n"
                + verificationLink + "\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n";
        message.setText(emailContent);
        message.setFrom("kuromahiru0505@gmail.com");
        mailSender.send(message);
    }

    @Override
    public void sendEmailVerifyToUnlock(String to, String tokenMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Confirm to Verify Unlock Account");
        String verificationLink =  "http://localhost:8080/confirm?tokenMail="+tokenMail;
        String emailContent = "Xin chào,\n\n"
                + "Để mở khóa thành công, "
                + "vui lòng nhấp vào liên kết dưới đây để xác minh địa chỉ email của bạn:\n\n"
                + verificationLink + "\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n";
        message.setText(emailContent);
        message.setFrom("kuromahiru0505@gmail.com");
        mailSender.send(message);
    }

    @Override
    public void sendEmailVerifyToChangePassword(String to, String tokenMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Confirm to Verify Change Password");
        String verificationLink =  "http://localhost:8080/confirm?tokenMail="+tokenMail;
        String emailContent = "Xin chào,\n\n"
                + "Để hoàn tất quá trình đổi mật khẩu, "
                + "vui lòng nhấp vào liên kết dưới đây để xác minh địa chỉ email của bạn:\n\n"
                + verificationLink + "\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n";
        message.setText(emailContent);
        message.setFrom("kuromahiru0505@gmail.com");
        mailSender.send(message);
    }
}
