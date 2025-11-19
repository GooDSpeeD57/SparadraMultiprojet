package training.afpa.cda24060.controleur;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.vue.Menu;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainConsole {

    private static final Logger LOGGER = Logger.getLogger(MainConsole.class.getName());

    public static void main(String[] args) {
        try {
            Main.chargement();
            Menu.menuPrincipal();

        } catch (SaisieException e) {
            LOGGER.log(Level.SEVERE, "Erreur de saisie détectée : {0}", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Une erreur inattendue est survenue : " + e.getMessage(), e);
        }
    }
}