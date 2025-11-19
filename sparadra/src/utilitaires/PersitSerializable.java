package utilitaires;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PersitSerializable {
    public static void sauvegarder(Map<String, Object> donnees, String chemin) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chemin))) {
            oos.writeObject(donnees);
            System.out.println("Données sauvegardées dans " + chemin);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde :");
            e.printStackTrace();
        }
    }
    public static Map<String, Object> charger(String chemin) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            return (Map<String, Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Impossible de charger les données. Fichier absent ou corrompu.");
            return new HashMap<>();
        }
    }
}