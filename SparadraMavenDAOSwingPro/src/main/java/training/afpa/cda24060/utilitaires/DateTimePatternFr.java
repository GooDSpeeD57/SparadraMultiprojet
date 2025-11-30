package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateTimePatternFr {

    private static final Logger logger = LoggerFactory.getLogger(DateTimePatternFr.class);
    private static final DateTimeFormatter FORMATTERDATEFR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(FORMATTERDATEFR) : "";
    }

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("La date et le pattern ne peuvent pas être null ou vides");
        }
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return date.format(myFormatObj);
    }

    public static LocalDate parseDateFromString(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, FORMATTERDATEFR);
        } catch (DateTimeParseException e) {
            LogUtils.error(logger, "Format de date invalide : {}", dateStr);
            return null;
        }
    }
}