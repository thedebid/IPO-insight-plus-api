package np.com.debid.ipoinsightplusapi.service;


import np.com.debid.ipoinsightplusapi.dto.IPODetailDTO;
import np.com.debid.ipoinsightplusapi.entity.LegacyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static np.com.debid.ipoinsightplusapi.utils.IPOInsightUtility.DATE_FORMATTER;
import static np.com.debid.ipoinsightplusapi.utils.IPOInsightUtility.getTodaysDate;

@Service
public class LegacyJobService {
    private static final Logger logger = LoggerFactory.getLogger(LegacyJobService.class);

    @Autowired
    private IpoScraperService ipoScraperService;

    @Autowired
    private LegacyNotificationService legacyNotificationService;

    @Autowired
    private LegacyUserService legacyUserService;
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Kathmandu")
    public void runJobEveryMorning() {
        logger.info("Executing scheduled morning notification job at {}", LocalDateTime.now().format(DATE_FORMATTER));
        logger.info("Fetching legacy users list from database...");
        List<LegacyUser> legacyUsers = legacyUserService.getAllLegacyUser();
        logger.info("Total legacy users fetched: {}", legacyUsers.size());

        logger.info("Start extracting IPO details...");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            List<IPODetailDTO> existingIpos = ipoScraperService.getCurrentIpos();

            List<IPODetailDTO> todayIssuedIpos = getTodayIssuedIpos(existingIpos);
            if (!todayIssuedIpos.isEmpty()) {
                for (LegacyUser legacyUser : legacyUsers) {
                    for (IPODetailDTO ipoDetailDTO : todayIssuedIpos) {
                        executorService.submit(() -> {
                            try {
                                logger.info("Sending IPO expiring notification to user: {} for IPO: {}", legacyUser.getEmail(), ipoDetailDTO.getCompanyName());
                                legacyNotificationService.notifyLegacyUser(legacyUser, true, ipoDetailDTO);
                                logger.info("Notification sent successfully to user: {}", legacyUser.getEmail());
                            } catch (Exception e) {
                                logger.error("Failed to send notification to user: {}", legacyUser.getEmail(), e);
                            }
                        });
                    }
                }
            }

            List<IPODetailDTO> todayExpiringIpos = getIPOsExpiringToday(existingIpos);
            if (!todayExpiringIpos.isEmpty()) {
                for (LegacyUser legacyUser : legacyUsers) {
                    for (IPODetailDTO ipoDetailDTO : todayExpiringIpos) {
                        executorService.submit(() -> {
                            try {
                                logger.info("Sending IPO expiring notification to user: {} for IPO: {}", legacyUser.getEmail(), ipoDetailDTO.getCompanyName());
                                legacyNotificationService.notifyLegacyUser(legacyUser, false, ipoDetailDTO);
                                logger.info("Notification sent successfully to user: {}", legacyUser.getEmail());
                            } catch (Exception e) {
                                logger.error("Failed to send notification to user: {}", legacyUser.getEmail(), e);
                            }
                        });
                    }
                }
            }
            logger.info("Scheduled job completed.");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }


    public List<IPODetailDTO> getTodayIssuedIpos(List<IPODetailDTO> existingIssuesIpos) {
        return existingIssuesIpos.stream()
                .filter(ipo -> "Open".equalsIgnoreCase(ipo.getStatus()))
                .filter(ipo -> {
                    try {
                        return getTodaysDate().equals(LocalDate.parse(ipo.getOpenDate(), DATE_FORMATTER));
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private List<IPODetailDTO> getIPOsExpiringToday(List<IPODetailDTO> existingIssuesIpos) {
        return existingIssuesIpos.stream()
                .filter(ipo -> "Open".equalsIgnoreCase(ipo.getStatus()))
                .filter(ipo -> {
                    try {
                        return getTodaysDate().equals(LocalDate.parse(ipo.getCloseDate(), DATE_FORMATTER));
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
