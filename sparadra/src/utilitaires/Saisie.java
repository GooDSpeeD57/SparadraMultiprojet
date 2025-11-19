package utilitaires;

import java.util.Scanner;

public class Saisie {
    private static Scanner sc = new Scanner(System.in);

    public static int lireEntier(String message,String messageException) {
        int valeur=0;
        boolean valide=false;
        while(!valide){
            try {
                System.out.println(message);
                valeur = Integer.parseInt(sc.nextLine().trim());
                if(valeur<0){
                    System.err.println(messageException);
                }else {
                    valide = true;
                }
            }catch(NumberFormatException e){
                System.err.println("Entrez un nombre valide");
            }
        }
        return valeur;
    }
    public static String lireChaine() {
        return sc.nextLine().trim();
    }
}
