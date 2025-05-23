package pProject.pPro.smtp;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public interface MailServiceInter {
	MimeMessage createMessage(String to) throws MessagingException,UnsupportedEncodingException;
	String createKey();
	void sendSimpleMessage(String to) throws Exception;
	void authValid(String to,String code);
}
