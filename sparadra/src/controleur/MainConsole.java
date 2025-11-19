package controleur;

import exception.SaisieException;
import vue.Menu;

public class MainConsole {
    public static void main(String[] args) {
        try {
            Menu.lancerApplication();
            Main.chargement();
            Menu.menuPrincipal();

            }catch (SaisieException e){
            System.err.println("il y a comme un probleme : "+e.getMessage());
        }

    }

}
