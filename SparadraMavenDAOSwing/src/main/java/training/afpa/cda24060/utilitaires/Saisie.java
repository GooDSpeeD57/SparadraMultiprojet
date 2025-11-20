package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class Saisie {
    private static final Logger logger = LoggerFactory.getLogger(Saisie.class);
    private static final Scanner sc = new Scanner(System.in);

    public static int lireEntier(String message, String messageException) {
        int valeur = 0;
        boolean valide = false;

        while (!valide) {
            try {
                System.out.println(message);
                valeur = Integer.parseInt(sc.nextLine().trim());

                if (valeur < 0) {
                    System.err.println(messageException);
                } else {
                    valide = true;
                }

            } catch (NumberFormatException e) {
                System.err.println("Entrez un nombre valide");
                logger.error("Erreur de saisie : valeur non numérique entrée par l'utilisateur", e);
            } catch (Exception e) {
                logger.error("Erreur inattendue lors de la saisie d'un entier.", e);
            }
        }
        return valeur;
    }

    public static String lireChaine() {
        try {
            return sc.nextLine().trim();
        } catch (Exception e) {
            logger.error("Erreur lors de la lecture d'une chaîne saisie par l'utilisateur", e);
            return "";
        }
    }
}