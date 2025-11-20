package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexValidator {

    private static final Logger logger = LoggerFactory.getLogger(RegexValidator.class);

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
        try {
            return nom != null && !nom.trim().isEmpty() && nom.matches(REGEX_MOTS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du mot : " + nom, e);
            return false;
        }
    }

    public static boolean validerNSS(String nss) {
        try {
            return nss != null && nss.matches(REGEX_NSS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du NSS : " + nss, e);
            return false;
        }
    }

    public static boolean validerDateNaissance(String date) {
        try {
            return date != null && date.matches(REGEX_DATE_NAISSANCE);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la date de naissance : " + date, e);
            return false;
        }
    }

    public static boolean validerEmail(String email) {
        try {
            return email != null && email.trim().length() >= 2 && email.matches(REGEX_EMAIL);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de l'email : " + email, e);
            return false;
        }
    }

    public static boolean validerCodePostal(String codePostal) {
        try {
            return codePostal != null && codePostal.trim().length() == 5 && codePostal.matches(REGEX_CODE_POSTAL);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du code postal : " + codePostal, e);
            return false;
        }
    }

    public static boolean validerTelephone(String telephone) {
        try {
            return telephone != null && telephone.trim().length() >= 10 && telephone.matches(REGEX_TELEPHONE);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du téléphone : " + telephone, e);
            return false;
        }
    }

    public static boolean validerAdresse(String adresse) {
        try {
            return adresse != null && !adresse.trim().isEmpty() && adresse.matches(REGEX_ADRESSE);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de l'adresse : " + adresse, e);
            return false;
        }
    }

    public static boolean validerVille(String ville) {
        try {
            return ville != null && !ville.trim().isEmpty() && ville.matches(REGEX_VILLE);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la ville : " + ville, e);
            return false;
        }
    }

    public static boolean validerRPPS(String rpps) {
        try {
            return rpps != null && rpps.trim().length() == 11 && rpps.matches(REGEX_RPPS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du RPPS : " + rpps, e);
            return false;
        }
    }

    public static boolean validerNomMedicament(String nom) {
        try {
            return nom != null && nom.trim().length() >= 3 && nom.matches(REGEX_MOTS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du nom de médicament : " + nom, e);
            return false;
        }
    }

    public static boolean validerCategorieMedicament(String categorie) {
        try {
            return categorie != null && categorie.trim().length() >= 3 && categorie.matches(REGEX_MOTS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de la catégorie du médicament : " + categorie, e);
            return false;
        }
    }

    public static boolean validerDepartement(String departement) {
        try {
            return departement != null && !departement.trim().isEmpty() && departement.matches(REGEX_MOTS);
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du département : " + departement, e);
            return false;
        }
    }

    public static boolean validerTauxRemboursement(double taux) {
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