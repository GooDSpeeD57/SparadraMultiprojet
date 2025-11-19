package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimePaternFr {

    private static final Logger logger = LoggerFactory.getLogger(DateTimePaternFr.class);
    private static final DateTimeFormatter FORMATTERDATEFR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("La date et le pattern ne peuvent pas être null ou vides");
        }
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return date.format(myFormatObj);
    }

    public static LocalDate parseDateFromString(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTERDATEFR);
        } catch (DateTimeParseException e) {
            logger.error("Format de date invalide. Utilisez \"dd/MM/yyyy\".", e);
        }
        return null;
    }
}