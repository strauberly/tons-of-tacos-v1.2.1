package com.adamstraub.tonsoftacos.tasks;

import com.adamstraub.tonsoftacos.respository.RefreshTokenRepository;
import com.adamstraub.tonsoftacos.services.utilityService.emailService.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
@Component
public class CleanUp {
@Autowired
    RefreshTokenRepository refreshTokenRepository;
@Autowired
    EmailService emailService;

    private void endOfDay() {
        String devTeamEmail  = "superduper.devteam@manyme.com";
        String[] ownerEmails = {
                "superduper.jim@manyme.com",
                "superduper.jenny@manyme.com"
        };

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String today = df.format(new Date());
        refreshTokenRepository.deleteAll();
        emailService.logsToDevTeam(devTeamEmail, "End of day report: " + today);
        emailService.salesSummaryToOwners(ownerEmails, "Today's Sales");
    }

    @Scheduled(cron = "0 1 22 * * *")
    public void executeTask(){
        endOfDay();
    }
}
