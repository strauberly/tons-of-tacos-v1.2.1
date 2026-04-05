package com.adamstraub.tonsoftacos.services.utilityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Slf4j
@Service
public class UtilityService {


    public void convertLogToPDF(String logFilePath, String pdfFilePath) {
//        primarily generated with chatGPT and then refined by myself to address unresolved issues dependencies,
//        and looking for improvements
        final float MARGIN = 50;
        final float FONT_SIZE = 10;
        final PDType1Font FONT = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        final float LEADING = 1.2f * FONT_SIZE;

        try (PDDocument document = new PDDocument();
             BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(FONT, FONT_SIZE);
            float y = page.getMediaBox().getHeight() - MARGIN;
            contentStream.newLineAtOffset(MARGIN, y);

            String line;
            float usableWidth = page.getMediaBox().getWidth() - 2 * MARGIN;

            while ((line = br.readLine()) != null) {
                // Split the line into words and build wrapped lines
                line = line.replace("\t", "    "); //
                String[] words = line.split(" ");
                StringBuilder current = new StringBuilder();
                for (String w : words) {
                    String potential = current.isEmpty() ? w : current + " " + w;
                    float width = FONT.getStringWidth(potential) / 1000 * FONT_SIZE;
                    if (width > usableWidth) {
                        // write current and move down
                        contentStream.showText(current.toString());
                        contentStream.newLineAtOffset(0, -LEADING);
                        y -= LEADING;
                        if (y <= MARGIN) {
                            // end current page
                            contentStream.endText();
                            contentStream.close();
                            // add new page
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(FONT, FONT_SIZE);
                            y = page.getMediaBox().getHeight() - MARGIN;
                            contentStream.newLineAtOffset(MARGIN, y);
                        }
                        current = new StringBuilder(w);
                    } else {
                        if (!current.isEmpty()) current.append(" ");
                        current.append(w);
                    }
                }
                // write remaining part of the line
                if (!current.isEmpty()) {
                    contentStream.showText(current.toString());
                    contentStream.newLineAtOffset(0, -LEADING);
                    y -= LEADING;
                    if (y <= MARGIN) {
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(FONT, FONT_SIZE);
                        y = page.getMediaBox().getHeight() - MARGIN;
                        contentStream.newLineAtOffset(MARGIN, y);
                    }
                } else {
                    // empty line -> just move down
                    contentStream.newLineAtOffset(0, -LEADING);
                    y -= LEADING;
                    if (y <= MARGIN) {
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(FONT, FONT_SIZE);
                        y = page.getMediaBox().getHeight() - MARGIN;
                        contentStream.newLineAtOffset(MARGIN, y);
                    }
                }
            }
            contentStream.endText();
            contentStream.close();
            document.save(pdfFilePath);
        } catch (IOException e) {
            log.debug("Investigate: {}", String.valueOf(e));
        }
    }
}
