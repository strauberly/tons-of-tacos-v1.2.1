package com.adamstraub.tonsoftacos.services.utilityService.emailService;

public interface IEmailService {
    void salesSummaryToOwners(String[] recipients, String subject);
    void logsToDevTeam(String to, String subject);
}
