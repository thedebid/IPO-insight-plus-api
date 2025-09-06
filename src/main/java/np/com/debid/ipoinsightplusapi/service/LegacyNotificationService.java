package np.com.debid.ipoinsightplusapi.service;

import np.com.debid.ipoinsightplusapi.dto.IPODetailDTO;
import np.com.debid.ipoinsightplusapi.entity.LegacyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LegacyNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(LegacyNotificationService.class);
    /**
     * Send notification for legacy user
     */

    @Autowired
    private EmailService emailService;

    @Value("${ipo-insight-plus.notification.subject.new-ipo}")
    private String newIPOSubject;

    @Value("${ipo-insight-plus.notification.subject.closing-ipo}")
    private String closingIPOSubject;
    @Value("${ipo-insight-plus.notification.from.name}")
    private String fromEmailName;


    public void notifyLegacyUser(LegacyUser legacyUser, boolean isNewIPO, IPODetailDTO ipoDetailDTO) {
        logger.info("Notifying legacy notifications for legacy user {}", legacyUser);
        try {
            String subject = buildEmailSubject(isNewIPO, ipoDetailDTO);
            String content = isNewIPO ? String.format(
                    "Hi there 👋,\n\n" +
                            "New IPO Alert: %s (%s)!\n" +
                            "%s has opened its IPO for the general public starting today, %s.\n" +
                            "Total Shares Issued: %s units\n" +
                            "IPO Closing Date: %s\n\n" +
                            "Apply now, visit: http://meroshare.cdsc.com.np\n\n" +
                            "Best Regards,\n" +
                            "%s",
                    ipoDetailDTO.getCompanyName(),
                    ipoDetailDTO.getSymbol(),
                    ipoDetailDTO.getCompanyName(),
                    ipoDetailDTO.getOpenDate(),
                    ipoDetailDTO.getTotalShares(),
                    ipoDetailDTO.getCloseDate(),
                    fromEmailName
            ) : String.format(
                    "Hi there 👋,\n\n" +
                            "- %s (%s) is closing today. Don't miss out!\n\n" +
                            "Best Regards,\n" +
                            "%s", ipoDetailDTO.getCompanyName(), ipoDetailDTO.getSymbol(), fromEmailName);

            emailService.sendEmail(legacyUser.getEmail(), subject, content);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", legacyUser.getEmail(), e.getMessage());
        }
    }

    private String buildEmailSubject(boolean isNewIPO, IPODetailDTO ipoDetailDTO) {
        String subjectPrefix = isNewIPO ? newIPOSubject : closingIPOSubject;
        return subjectPrefix + ": " + ipoDetailDTO.getCompanyName() + " (" + ipoDetailDTO.getSymbol() + ")";
    }
}
