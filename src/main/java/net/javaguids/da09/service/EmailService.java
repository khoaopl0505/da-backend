package net.javaguids.da09.service;

public interface EmailService {
    void sendEmailVerifyAccount(String to, String tokenMail);
    void sendEmailVerifyToUnlock(String to, String tokenMail);
    void sendEmailVerifyToChangePassword(String to, String tokenMail);
}
