package np.com.debid.ipoinsightplusapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${ipo-insight-plus.notification.from.email}")
    private String fromEmail;

    @Value("${ipo-insight-plus.notification.from.name}")
    private String fromEmailName;

    void sendEmail(String toEmail, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            helper.setFrom(new InternetAddress(fromEmail, fromEmailName));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content);
            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw e;
        }
    }
}
