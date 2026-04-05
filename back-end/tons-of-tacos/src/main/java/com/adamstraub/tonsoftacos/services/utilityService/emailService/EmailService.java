package com.adamstraub.tonsoftacos.services.utilityService.emailService;

import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.services.ownersService.orders.OwnersOrdersService;
import com.adamstraub.tonsoftacos.services.utilityService.UtilityService;
import com.adamstraub.tonsoftacos.services.utilityService.salesService.SalesService;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    SalesService salesService;
    @Autowired
    UtilityService utilityService;

    public void logsToDevTeam(String to, String subject){

        String body = """
                Hola, team!
                Here is yesterday's session, error and debug logs. Please review, evaluate, discuss any issues and
                share ideas on how to resolve before continuing with the rest of your work.
                
                Thanks!
                
                Adam Straub
                superduper.lead@manyme.com
                """;
//
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
            mimeMessage.setFrom("superduper.tacoapp@manyme.com");

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(body);
            multipart.addBodyPart(bodyPart);
            utilityService.convertLogToPDF("logs/session.log", "logs/session.pdf");
            utilityService.convertLogToPDF("logs/error.log", "logs/error.pdf");
            utilityService.convertLogToPDF("logs/debug.log", "logs/debug.pdf");
            String[] logFiles = {
                    "logs/session.pdf",
                    "logs/error.pdf",
                    "logs/debug.pdf"};
            for (String logFile : logFiles) {
                MimeBodyPart attachment = new MimeBodyPart();
                FileSystemResource fileSource = new FileSystemResource(logFile);

                try {
                    attachment.attachFile(fileSource.getFile());
                    multipart.addBodyPart(attachment);
                } catch (IOException e) {
                    log.error("Investigate: {}", logFile, e);
                }
            }
            mimeMessage.setContent(multipart);
        };
        try {
            mailSender.send(preparator);
        } catch (MailSendException e) {
            log.error("Failed to sendemail: ", e);
        }
    }

    public void salesSummaryToOwners(String[] recipients, String subject) {
        String largestSale = salesService.largestSale();
        String orderStats = salesService.ordersStats();
        DailySales salesToday = salesService.salesToday().getBody();

        assert salesToday != null;
        String body = buildBody(salesToday, largestSale, orderStats);


        InternetAddress[] addresses = Arrays.stream(recipients)
                .map(addr -> {
                    try { return new InternetAddress(addr); }
                    catch (AddressException e) { throw new RuntimeException(e); }
                })
                .toArray(InternetAddress[]::new);

        MimeMessagePreparator preparator = mimeMessage -> {
                    mimeMessage.setRecipients(MimeMessage.RecipientType.TO, addresses);
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setText(body, "UTF-8", "html");
            mimeMessage.setFrom("superduper.tacoapp@manyme.com");
        };
        mailSender.send(preparator);
        log.info("email sent: {}", Arrays.toString(recipients));
    }

    private static @NotNull String buildBody(DailySales salesToday, String largestSale, String orderStats) {
        assert salesToday != null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        String date = salesToday.getDate().format(dateFormatter);
        int sales = salesToday.getNumberOfSales();
        BigDecimal total = salesToday.getTotal();

        String template = """
            <html>
                <body>
                    <h2>Hola, Jim and Jenny!</h3>
                    <h3>Here is the sales summary for {0}.<h3>
                    <p><strong>Largest Sale:</strong> {1}</p>
                    <p><strong>Order Stats:</strong> {2}</p>
                    <p><strong>Total Sales:</strong> {3}</p>
                    <p><strong>For a total of: $</strong>${4}</p>
                    <p>Nice job, and hope you get some good rest!</p>
                </body>
            </html>
            """;

        return MessageFormat.format(template, date, largestSale, orderStats, sales, total );
        }
}
