
package pProject.pPro.smtp;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	@Value("${naver.email}")
	private String Email;
	@Value("${naver.pwd}")
	private String pwd;
	@Bean
	public JavaMailSender NaverMailService() {
		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		jms.setHost("smtp.naver.com");
		jms.setUsername(Email);
		jms.setPassword(pwd);
		jms.setPort(465);
		jms.setJavaMailProperties(getMailProperties());
		return jms;
	}
	private Properties getMailProperties(){
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // smtp strattles 사용
        properties.setProperty("mail.debug", "true"); // 디버그 사용
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com"); // ssl 인증 서버 (smtp 서버명)
        properties.setProperty("mail.smtp.ssl.enable", "true"); // ssl 사용

        return properties;
    }
}
