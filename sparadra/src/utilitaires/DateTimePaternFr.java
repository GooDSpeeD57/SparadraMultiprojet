package utilitaires;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimePaternFr {


    private static final DateTimeFormatter FORMATTERDATEFR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("La date et le pattern ne peut pas etre null ou vide");
        }
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return date.format(myFormatObj);
    }



    public static LocalDate parseDateFromString(String dateStr) {
        try {

            return LocalDate.parse(dateStr, FORMATTERDATEFR);
        } catch (DateTimeParseException e) {
            System.err.println("Format de date invalide. Utilisez \"dd/MM/yyyy\".\n" + e.getMessage());
        }
        return null;
    }

}