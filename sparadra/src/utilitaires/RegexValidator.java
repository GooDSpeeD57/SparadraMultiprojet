package utilitaires;

import exception.SaisieException;

public class RegexValidator {


    public static final String REGEX_MOTS = "^[A-Za-zÀ-ÿ]+(?:[ \\-][A-Za-zÀ-ÿ]+)*$";
    public static final String REGEX_NSS = "^[12]\\d{2}(0[1-9]|1[0-2])\\d{5}\\d{2}$";
    public static final String REGEX_DATE_NAISSANCE = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    public static final String REGEX_EMAIL = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    public static final String REGEX_CODE_POSTAL = "\\d{5}";
    public static final String REGEX_TELEPHONE = "^(?:\\+33\\s?[1-9](?:\\s?\\d{2}){4}|0[1-9](?:\\s?\\d{2}){4})$";
    public static final String REGEX_ADRESSE = "^(n°\\s*)?(\\d{1,4})(\\s*([Bb]is|[Tt]er|[a-gA-G]))?\\s+([Rr]ue|[Aa]venue|[Gg]rand|av|[Bb]oulevard|bd|[Cc]hemin|[Aa]llée?|[Ii]mpasse|[Rr]oute|[Pp]lace|pl)\\s+([\\p{L}][\\p{L}0-9'\\-\\s]*[\\p{L}0-9])$";
    public static final String REGEX_VILLE = "^[\\p{L}][\\p{L} \\-']*$";
    public static final String REGEX_RPPS = "^10\\d{9}$";




    public static boolean validerMots(String nom) {
        return nom != null && !nom.trim().isEmpty() && nom.matches(REGEX_MOTS);
    }

    public static boolean validerNSS(String nss) {
       return nss != null && nss.matches(REGEX_NSS);
    }

    public static boolean validerDateNaissance(String date) {
        return date != null  && date.matches(REGEX_DATE_NAISSANCE);
    }

    public static boolean validerEmail(String email) {
        return email != null && email.trim().length() >= 2 && email.matches(REGEX_EMAIL);
    }

    public static boolean validerCodePostal(String codePostal) {
        return codePostal != null && codePostal.trim().length() == 5 && codePostal.matches(REGEX_CODE_POSTAL);
    }

    public static boolean validerTelephone(String telephone) {
        return telephone != null && telephone.trim().length() >= 10 && telephone.matches(REGEX_TELEPHONE);
    }

    public static boolean validerAdresse(String adresse) {
        return adresse != null && !adresse.trim().isEmpty() && adresse.matches(REGEX_ADRESSE);
    }

    public static boolean validerVille(String ville) {
        return ville != null && !ville.trim().isEmpty() && ville.matches(REGEX_VILLE);
    }

    public static boolean validerRPPS(String rpps) {
        return rpps != null && rpps.trim().length() == 11 && rpps.matches(REGEX_RPPS);
    }

    public static boolean validerNomMedicament(String nom) {
        return nom != null && nom.trim().length() >= 3 && nom.matches(REGEX_MOTS);
    }

    public static boolean validerCategorieMedicament(String categorie) {
        return categorie != null && categorie.trim().length() >= 3 && categorie.matches(REGEX_MOTS);
    }

    public static boolean validerDepartement(String departement) {
        return departement != null && !departement.trim().isEmpty() && departement.matches(REGEX_MOTS);
    }

    public static boolean validerTauxRemboursement(int taux) {
        return taux >= 0 && taux <= 100;
    }

    public static boolean validerPrix(double prix) {
        return prix >= 0;
    }

    public static boolean validerQuantite(int quantite) {
        return quantite >= 0;
    }

    public static boolean validerSansOrdonnance(String value) {
        return "oui".equalsIgnoreCase(value) || "non".equalsIgnoreCase(value);
    }
}
