package vue;

import exception.SaisieException;
import modele.*;
import utilitaires.PersitSerializable;
import utilitaires.RegexValidator;
import utilitaires.Saisie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FICHIER_PERSISTANCE = "donnees.bin";
    private static Map<String, Object> donnees;

    public static void lancerApplication() throws SaisieException {

        donnees = PersitSerializable.charger(FICHIER_PERSISTANCE);
        chargerDonnees();


    }

    private static void chargerDonnees() {

        List<Mutuelle> mutuelles = (List<Mutuelle>) donnees.getOrDefault("mutuelles", new java.util.ArrayList<>());
        List<Medicament> medicaments = (List<Medicament>) donnees.getOrDefault("medicaments", new java.util.ArrayList<>());
        List<Medecin> medecins = (List<Medecin>) donnees.getOrDefault("medecins", new java.util.ArrayList<>());
        List<Pharmacien> pharmaciens = (List<Pharmacien>) donnees.getOrDefault("pharmaciens", new java.util.ArrayList<>());
        List<Client> clients = (List<Client>) donnees.getOrDefault("clients", new java.util.ArrayList<>());

        Client.setClients(clients);
        Mutuelle.setMutuelles(mutuelles);
        Medicament.setMedicaments(medicaments);
        Medecin.setMedecins(medecins);
        Pharmacien.setPharmacien(pharmaciens);
    }



    public static void menuPrincipal() throws SaisieException {
         boolean fin = false;
            while (!fin) {
                Vue.vueMenu();
                Vue.vueMenuglobal();
                int choix = Saisie.lireEntier("Votre Choix [1-2] ou [0] pour quitter : ", "Choix entre 0, 1 et 2");
                switch (choix) {
                    case 0 -> fin = quitterEtSauvegarder();
                    case 1 -> menuAvecSansOrdonnance();
                    case 2 -> menuGestionPharmacie();
                    default -> System.err.println("! Choix incorrect ! [0-2] !");
            }
        }
    }

    private static void menuAvecSansOrdonnance() throws SaisieException {
        boolean fin = false;
            while (!fin) {
                Vue.vueMenuAvecSansOrdonnance();
                int choix = Saisie.lireEntier("Votre Choix [1-2] ou [0] pour retourner au menu principal : ", "Un nombre entre 0 et 2");
                switch (choix) {
                    case 0 -> fin = true;
                    case 1 -> menuAvecOrdonnance();
                    case 2 -> menuSansOrdonnance();
                    default -> System.err.println("Choix entre 0-2");
            }
        }
    }

    private  static void menuAvecOrdonnance() {
        List<Object> resultatsGlobaux = new ArrayList<>();

        String recherche;
        List<Client> clientsTrouves;
        do {
            System.out.print("Entrez un client à rechercher : \n");
            recherche = scanner.nextLine();

            clientsTrouves = rechercherClientParNom(recherche);

            if (clientsTrouves.isEmpty()) {
                System.out.println("Aucun client trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                String reponse = scanner.nextLine().trim().toLowerCase();
                if (!reponse.equals("oui")) {
                    break;
                }
            }

        } while (clientsTrouves.isEmpty());

        if (!clientsTrouves.isEmpty()) {
            for (Client client : clientsTrouves) {
                String info = "Client : " + client.getNom() + " " + client.getPrenom();
                resultatsGlobaux.add(info);
            }
            afficherResultatsClients(clientsTrouves, recherche);
        }
        List<Medecin> medecinsTrouves;

        do {
            System.out.print("Entrez un médecin à rechercher : \n");
            recherche = scanner.nextLine();

            medecinsTrouves = rechercherMedecinParNom(recherche);

            if (medecinsTrouves.isEmpty()) {
                System.out.println("Aucun médecin trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                String reponse = scanner.nextLine().trim().toLowerCase();
                if (!reponse.equals("oui")) {
                    break;
                }
            }

        } while (medecinsTrouves.isEmpty());

        if (!medecinsTrouves.isEmpty()) {
            for (Medecin medecin : medecinsTrouves) {
                String info = "Médecin : " + medecin.getNom() + " " + medecin.getPrenom();
                resultatsGlobaux.add(info);
            }
            afficherResultatsMedecins(medecinsTrouves, recherche);
        }

        List<Mutuelle> mutuellesTrouvees;

        do {
            System.out.print("Entrez une mutuelle à rechercher : \n");
            recherche = scanner.nextLine();

            mutuellesTrouvees = rechercherMutuelleParNom(recherche);

            if (mutuellesTrouvees.isEmpty()) {
                System.out.println("Aucune mutuelle trouvée pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                String reponse = scanner.nextLine().trim().toLowerCase();
                if (!reponse.equals("oui")) {
                    break;
                }
            }

        } while (mutuellesTrouvees.isEmpty());

        if (!mutuellesTrouvees.isEmpty()) {
            for (Mutuelle mutuelle : mutuellesTrouvees) {
                String info = "Mutuelle : " + mutuelle.getNom() + " (" + mutuelle.getVille() + ")";
                resultatsGlobaux.add(info);
            }
            afficherResultatsMutuelles(mutuellesTrouvees, recherche);
        }


        System.out.println("\nRecherche de médicaments\n");
        String choix;
        do {
            System.out.print("\nEntrez un médicament à rechercher : ");
            recherche = scanner.nextLine();
            List<Medicament> medicamentsTrouves = rechercherNomMedicament(recherche);

            if (!medicamentsTrouves.isEmpty()) {
                afficherResultatsMedicaments(medicamentsTrouves, recherche);

                for (Medicament medicament : medicamentsTrouves) {
                    System.out.println("Stock actuel : " + medicament.getQuantiteMedicament());
                    System.out.print("Entrez la quantité pour le médicament '" + medicament.getNomMedicament() + "' : ");
                    int quantite;
                    try {
                        quantite = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Quantité invalide. Quantité 1 assignée par défaut.");
                        quantite = 1;
                    }

                    if (quantite > medicament.getQuantiteMedicament()) {
                        System.out.println("Stock insuffisant pour " + medicament.getNomMedicament() +
                                ". Quantité disponible : " + medicament.getQuantiteMedicament());
                    } else {
                        medicament.retirerDuStock(quantite);
                        String info = "Médicament : " + medicament.getNomMedicament()
                                + " | Quantité : " + quantite
                                + " | Stock restant : " + medicament.getQuantiteMedicament();
                        resultatsGlobaux.add(info);
                    }
                }

            } else {
                System.out.println("Aucun médicament trouvé pour : " + recherche);
            }

            System.out.print("Voulez-vous rechercher un autre médicament ? (oui/non) : ");
            choix = scanner.nextLine().trim().toLowerCase();

        } while (choix.equals("oui"));


        System.out.println("\n=== Résultats globaux de la recherche ===");

        if (resultatsGlobaux.isEmpty()) {
            System.out.println("Aucun résultat trouvé.");
        } else {
            for (Object resultat : resultatsGlobaux) {
                System.out.println("==================================");
                System.out.println(resultat);
            }
        }
    }


    private static void menuSansOrdonnance()  {
        List<Object> resultatsGlobaux = new ArrayList<>();

        String recherche;
        List<Client> clientsTrouves;
        do {
            System.out.print("Entrez un client à rechercher : \n");
            recherche = scanner.nextLine();

            clientsTrouves = rechercherClientParNom(recherche);

            if (clientsTrouves.isEmpty()) {
                System.out.println("Aucun client trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                String reponse = scanner.nextLine().trim().toLowerCase();
                if (!reponse.equals("oui")) {
                    break;
                }
            }

        } while (clientsTrouves.isEmpty());

        if (!clientsTrouves.isEmpty()) {
            for (Client client : clientsTrouves) {
                String info = "Client : " + client.getNom() + " " + client.getPrenom();
                resultatsGlobaux.add(info);
            }
            afficherResultatsClients(clientsTrouves, recherche);
        }

        System.out.println("\nRecherche de médicaments sans ordonnace\n");
        String choix;
        do {
            System.out.print("\nEntrez un médicament à rechercher : ");
            recherche = scanner.nextLine();
            List<Medicament> medicamentsTrouves = rechercherMedicamentsSansOrdonnanceParNom(recherche);

            if (!medicamentsTrouves.isEmpty()) {
                afficherResultatsMedicaments(medicamentsTrouves, recherche);

                for (Medicament medicament : medicamentsTrouves) {
                    System.out.println("Stock actuel : " + medicament.getQuantiteMedicament());
                    System.out.print("Entrez la quantité pour le médicament '" + medicament.getNomMedicament() + "' : ");
                    int quantite;
                    try {
                        quantite = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Quantité invalide. Quantité 1 assignée par défaut.");
                        quantite = 1;
                    }

                    if (quantite > medicament.getQuantiteMedicament()) {
                        System.out.println("Stock insuffisant pour " + medicament.getNomMedicament() +
                                ". Quantité disponible : " + medicament.getQuantiteMedicament());
                    } else {
                        medicament.retirerDuStock(quantite);
                        String info = "Médicament : " + medicament.getNomMedicament()
                                + " | Quantité : " + quantite
                                + " | Stock restant : " + medicament.getQuantiteMedicament();
                        resultatsGlobaux.add(info);
                    }
                }

            } else {
                System.out.println("Aucun médicament trouvé pour : " + recherche);
            }

            System.out.print("Voulez-vous rechercher un autre médicament ? (oui/non) : ");
            choix = scanner.nextLine().trim().toLowerCase();

        } while (choix.equals("oui"));


        System.out.println("\n=== Résultats globaux de la recherche ===");

        if (resultatsGlobaux.isEmpty()) {
            System.out.println("Aucun résultat trouvé.");
        } else {
            for (Object resultat : resultatsGlobaux) {
                System.out.println("==================================");
                System.out.println(resultat);
            }
        }
    }

    private static void menuGestionPharmacie() throws SaisieException {
        boolean fin = false;
            while (!fin) {
                Vue.vueMenuMedicament();
                int choix = Saisie.lireEntier("Votre Choix [1-5] ou [0] pour retourner au menu principal : ", "Un nombre entre 0 et 5");
                switch (choix) {
                    case 0 -> fin = true;
                    case 1 -> Vue.vueCreationMedicament();
                    case 2 -> Vue.vueCreationMedecin();
                    case 3 -> Vue.vueCreationMutuelle();
                    case 4 -> Vue.vueCreationPharmacien();
                    case 5 -> Vue.afficherListeVentes();
                    default -> System.err.println("Choix entre 0-5");
            }
        }
    }

    private  static void menuRechercheClient(){
        boolean fin = false;
        while (!fin) {
            Vue.vueMenuRechercheClients();
            int choix = Saisie.lireEntier("Votre Choix [1-3] ou [0] pour retourner : ", "Un nombre entre 0 et 3");
            switch (choix) {
                case 0 -> fin = true;
                case 1 -> {
                    System.out.print("Nom du client à rechercher : \n");
                    String nom = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(nom)) {
                            List<Client> resultats = rechercherClientParNom(nom);
                            afficherResultatsClients(resultats, "nom \"" + nom + "\"");
                        } else {
                            System.out.println("Nom invalide Veuillez entrer un nom valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du client.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("N° de sécurité sociale du client : ");
                    String nss = scanner.nextLine();
                    try {
                        if (!RegexValidator.validerMots(nss)) {
                            List<Client> resultats = rechercherClientParNSS(nss);
                            afficherResultatsClients(resultats, "NSS \"" + nss + "\"");
                        } else {
                            System.out.println("NSS invalide Veuillez entrer un NSS valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du client.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Email du client à rechercher : ");
                    String email = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(email)) {
                            List<Client> resultats = rechercherClientParEmail(email);
                            afficherResultatsClients(resultats, "email \"" + email + "\"");
                        } else {
                            System.out.println("Email invalide Veuillez entrer un email valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du client.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
            }
        }
    }
    private static void menuRechercheMedecin(){
        boolean fin = false;
        while (!fin) {
            Vue.vueMenuRechercheMedecin();
            int choix = Saisie.lireEntier("Votre Choix [1-3] ou [0] pour retourner : ", "Un nombre entre 0 et 3");
            switch (choix) {
                case 0 -> fin = true;
                case 1 -> {
                    System.out.print("Nom du médecin à rechercher : ");
                    String nom = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(nom)) {
                            List<Medecin> resultats = rechercherMedecinParNom(nom);
                            afficherResultatsMedecins(resultats, "nom \"" + nom + "\"");
                        } else {
                            System.out.println("Nom invalide Veuillez entrer un nom valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du médecin.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("N° RPPS du médecin : ");
                    String rpps = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(rpps)) {
                            List<Medecin> resultats = rechercherMedecinParRPPS(rpps);
                            afficherResultatsMedecins(resultats, "RPPS \"" + rpps + "\"");
                        } else {
                            System.out.println("RPPS invalide Veuillez entrer un RPPS valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du médecin.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 3 -> afficherTousLesMedecins();
                default -> System.err.println("Choix entre 0-3");
            }
        }
    }

    private static void menuRechercheMutuelle() {
        boolean fin = false;
        while (!fin) {
            Vue.vueMenuRechercheMutuelle();
            int choix = Saisie.lireEntier("Votre Choix [1-3] ou [0] pour retourner : ", "Un nombre entre 0 et 3");
            switch (choix) {
                case 0 -> fin = true;
                case 1 -> {
                    System.out.print("Nom de la mutuelle à rechercher : ");
                    String nom = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(nom)) {
                            List<Mutuelle> resultats = rechercherMutuelleParNom(nom);
                            afficherResultatsMutuelles(resultats, "nom \"" + nom + "\"");
                        } else {
                            System.out.println("Nom invalide Veuillez entrer un nom valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche de la mutuelle.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Département de la mutuelle : ");
                    String departement = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(departement)) {
                            List<Mutuelle> resultats = rechercherMutuelleParDepartement(departement);
                            afficherResultatsMutuelles(resultats, "département \"" + departement + "\"");
                        } else {
                            System.out.println("Département invalide Veuillez entrer un département valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche de la mutuelle.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 3 -> afficherToutesLesMutuelles();
                default -> System.err.println("Choix entre 0-3");
            }
        }
    }

    private static void menuRechercheMedicament(){
        boolean fin = false;
        while (!fin) {
            Vue.vueMenuRechercheMedicament();
            int choix = Saisie.lireEntier("Votre Choix [1-3] ou [0] pour retourner : ", "Un nombre entre 0 et 3");
            switch (choix) {
                case 0 -> fin = true;
                case 1 -> {
                    System.out.print("Nom du médicament à rechercher : ");
                    String nom = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(nom)) {
                            List<Medicament> resultats = rechercherNomMedicament(nom);
                            afficherResultatsMedicaments(resultats, "nom \"" + nom + "\"");
                        } else {
                            System.out.println("Nom invalide Veuillez entrer un nom valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du médicament.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Catégorie du médicament : ");
                    String categorie = scanner.nextLine();
                    try {
                        if (RegexValidator.validerMots(categorie)) {
                            List<Medicament> resultats = rechercherParCategorie(categorie);
                            afficherResultatsMedicaments(resultats, "catégorie \"" + categorie + "\"");
                        } else {
                            System.out.println("Catégorie invalide Veuillez entrer une catégorie valide");
                        }
                    } catch (Exception e) {
                        System.out.println(" Une erreur est survenue pendant la recherche du médicament.");
                        System.out.println("Détails de l'erreur : " + e.getMessage());
                    }
                }
                case 3 -> afficherTousLesMedicaments();
                default -> System.err.println("Choix entre 0-3");
            }
        }
    }

    public static List<Client> rechercherClientParNom(String nom) {
        List<Client> resultats = new ArrayList<>();
        for (Client client : Client.getClients()) {
            if (client.getNom().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(client);
            }
        }
        return resultats;
    }


    public static List<Client> rechercherClientParNSS(String nSs) {
        List<Client> resultats = new ArrayList<>();
        for (Client client : Client.getClients()) {
            if (client.getNss().toLowerCase().contains(nSs.toLowerCase())) {
                resultats.add(client);
            }
        }
        return resultats;
    }

    public static List<Client> rechercherClientParEmail(String email) {
        List<Client> resultats = new ArrayList<>();
        for (Client client : Client.getClients()) {
            if (client.getEmail().toLowerCase().contains(email.toLowerCase())) {
                resultats.add(client);
            }
        }
        return resultats;
    }
    public static List<Medecin> rechercherMedecinParNom(String nom) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin medecin : Medecin.getMedecins()) {
            if (medecin.getNom().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(medecin);
            }
        }
        return resultats;
    }

    public static List<Medecin> rechercherMedecinParRPPS(String RPPS) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin medecins : Medecin.getMedecins()) {
            if (medecins.getRPPS().toLowerCase().contains(RPPS.toLowerCase())) {
                resultats.add(medecins);
            }
        }
        return resultats;
    }
    private static void afficherTousLesMedecins() {
        for (Medecin medecins : Medecin.getMedecins()) {
            System.out.println("==================================");
            System.out.println(medecins);
        }
    }

    public static List<Mutuelle> rechercherMutuelleParNom(String nom) {
        List<Mutuelle> resultats = new ArrayList<>();
        for (Mutuelle mutuelles : Mutuelle.getMutuelles()) {
            if (mutuelles.getNom().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(mutuelles);
            }
        }
        return resultats;
    }
    public static List<Mutuelle> rechercherMutuelleParDepartement(String departement) {
        List<Mutuelle> resultats = new ArrayList<>();
        for (Mutuelle mutuelles : Mutuelle.getMutuelles()) {
            if(mutuelles.getDepartement().equals(departement)) {
                resultats.add(mutuelles);
            }
        }
        return resultats;
    }

    private static void afficherToutesLesMutuelles() {
        for (Mutuelle mutuelles : Mutuelle.getMutuelles()) {
            System.out.println("==================================");
            System.out.println(mutuelles);
        }
    }

    public static List<Medicament> rechercherNomMedicament(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament medicament : Medicament.getMedicaments()) {
            if (medicament.getNomMedicament().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(medicament);
            }
        }
        return resultats;
    }

    public static List<Medicament> rechercherParCategorie(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament medicament : Medicament.getMedicaments()) {
            if (medicament.getCategorieMedicament().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(medicament);
            }
        }
        return resultats;
    }

    private static void afficherTousLesMedicaments() {
        for (Medicament medicament : Medicament.getMedicaments()) {
            System.out.println("==================================");
            System.out.println(medicament);
        }
    }

    public static void afficherResultatsMedicamentsParCategorie(String nomCategorie) {
        List<Medicament> resultats = rechercherParCategorie(nomCategorie);
            if (resultats.isEmpty()) {
                System.out.println("Aucun médicament trouvé pour la catégorie : " + nomCategorie);
            } else {
                System.out.println("Médicaments trouvés pour la catégorie : " + nomCategorie);
                for (Medicament medicament : resultats) {
                    System.out.println("==================================");
                    System.out.println(medicament);
                }
            }
        }



        private static void afficherResultatsClients(List<Client> clients, String critere) {
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé pour : " + critere);
        } else {
            System.out.println("Clients trouvés pour " + critere + " :");
            for (Client client : clients) {
                System.out.println("==================================");
                System.out.println(client);
            }
        }
    }

    private static void afficherResultatsMedecins(List<Medecin> medecins, String critere) {
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin trouvé pour : " + critere);
        } else {
            System.out.println("Médecins trouvés pour " + critere + " :");
            for (Medecin medecin : medecins) {
                System.out.println("==================================");
                System.out.println(medecin);
            }
        }
    }

    private static void afficherResultatsMutuelles(List<Mutuelle> mutuelles, String critere) {
        if (mutuelles.isEmpty()) {
            System.out.println("Aucune mutuelle trouvée pour : " + critere);
        } else {
            System.out.println("Mutuelles trouvées pour " + critere + " :");
            for (Mutuelle mutuelle : mutuelles) {
                System.out.println("==================================");
                System.out.println(mutuelle);
            }
        }
    }

    private static void afficherResultatsMedicaments(List<Medicament> medicaments, String critere) {
        if (medicaments.isEmpty()) {
            System.out.println("Aucun médicament trouvé pour : " + critere);
        } else {
            System.out.println("Médicaments trouvés pour " + critere + " :");
            for (Medicament medicament : medicaments) {
                System.out.println("==================================");
                System.out.println(medicament);
            }
        }
    }
    public static List<Medicament> rechercherMedicamentsSansOrdonnanceParNom(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        for (Medicament medicament : Medicament.getMedicaments()) {
            if (medicament.getSansOrdonnanceMedicament().equalsIgnoreCase("oui") &&
                    medicament.getNomMedicament().toLowerCase().contains(nom.toLowerCase())) {
                resultats.add(medicament);
            }
        }
        return resultats;
    }
    private static boolean quitterEtSauvegarder() {
        System.out.println("Sauvegarde en cours...");
        donnees.put("clients", Client.getClients());
        donnees.put("mutuelles", Mutuelle.getMutuelles());
        donnees.put("medicaments", Medicament.getMedicaments());
        donnees.put("pharmaciens", Pharmacien.getPharmacien());
        donnees.put("medecins", Medecin.getMedecins());
        PersitSerializable.sauvegarder(donnees, FICHIER_PERSISTANCE);
        System.out.println("Au revoir !");
        return true;
    }
}