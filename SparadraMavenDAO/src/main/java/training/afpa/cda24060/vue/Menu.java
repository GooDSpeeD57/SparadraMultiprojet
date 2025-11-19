package training.afpa.cda24060.vue;

import training.afpa.cda24060.ClasseDAO.*;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.*;
import training.afpa.cda24060.utilitaires.RegexValidator;
import training.afpa.cda24060.utilitaires.Saisie;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    // DAO
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final MedecinDAO medecinDAO = new MedecinDAO();
    private static final MutuelleDAO mutuelleDAO = new MutuelleDAO();
    private static final MedicamentDAO medicamentDAO = new MedicamentDAO();

    public static void menuPrincipal() throws SaisieException {
        boolean fin = false;
        while (!fin) {
            Vue.vueMenu();
            Vue.vueMenuglobal();
            int choix = Saisie.lireEntier("Votre Choix [1-2] ou [0] pour quitter : ", "Choix entre 0, 1 et 2");
            switch (choix) {
                case 0 -> fin = quitter();
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

    private static void menuAvecOrdonnance() {
        List<String> resultatsGlobaux = new ArrayList<>();
        String recherche;

        // Recherche client via DAO
        do {
            System.out.print("Entrez un client à rechercher : \n");
            recherche = scanner.nextLine();
            List<Client> clientsTrouves = clientDAO.findByNom(recherche);

            if (clientsTrouves.isEmpty()) {
                System.out.println("Aucun client trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                if (!scanner.nextLine().trim().equalsIgnoreCase("oui")) break;
            } else {
                clientsTrouves.forEach(c -> resultatsGlobaux.add("Client : " + c.getNom() + " " + c.getPrenom()));
                Vue.afficherResultatsClients(clientsTrouves, recherche);
                break;
            }
        } while (true);

        // Recherche médecin via DAO
        do {
            System.out.print("Entrez un médecin à rechercher : \n");
            recherche = scanner.nextLine();
            List<Medecin> medecinsTrouves = medecinDAO.findByNom(recherche);

            if (medecinsTrouves.isEmpty()) {
                System.out.println("Aucun médecin trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                if (!scanner.nextLine().trim().equalsIgnoreCase("oui")) break;
            } else {
                medecinsTrouves.forEach(m -> resultatsGlobaux.add("Médecin : " + m.getNom() + " " + m.getPrenom()));
                Vue.afficherResultatsMedecins(medecinsTrouves, recherche);
                break;
            }
        } while (true);

        // Recherche mutuelle via DAO
        do {
            System.out.print("Entrez une mutuelle à rechercher : \n");
            recherche = scanner.nextLine();
            List<Mutuelle> mutuellesTrouvees = mutuelleDAO.findByNom(recherche);

            if (mutuellesTrouvees.isEmpty()) {
                System.out.println("Aucune mutuelle trouvée pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                if (!scanner.nextLine().trim().equalsIgnoreCase("oui")) break;
            } else {
                mutuellesTrouvees.forEach(m -> resultatsGlobaux.add("Mutuelle : " + m.getNom() + " (" + m.getVille() + ")"));
                Vue.afficherResultatsMutuelles(mutuellesTrouvees, recherche);
                break;
            }
        } while (true);

        // Recherche médicaments via DAO et retrait du stock via DAO
        String choix;
        do {
            System.out.print("\nEntrez un médicament à rechercher : ");
            recherche = scanner.nextLine();
            List<Medicament> medicamentsTrouves = medicamentDAO.findByNom(recherche);

            if (!medicamentsTrouves.isEmpty()) {
                Vue.afficherResultatsMedicaments(medicamentsTrouves, recherche);

                for (Medicament medicament : medicamentsTrouves) {
                    System.out.println("Stock actuel : " + medicament.getQuantiteMedicament());
                    System.out.print("Entrez la quantité pour '" + medicament.getNomMedicament() + "' : ");
                    int quantite;
                    try {
                        quantite = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Quantité invalide. Quantité 1 assignée par défaut.");
                        quantite = 1;
                    }

                    try {
                        int stockRestant = medicamentDAO.retirerDuStock(medicament.getIdMedicament(), quantite);
                        resultatsGlobaux.add("Médicament : " + medicament.getNomMedicament()
                                + " | Quantité retirée : " + quantite
                                + " | Stock restant : " + stockRestant);
                    } catch (SaisieException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Aucun médicament trouvé pour : " + recherche);
            }

            System.out.print("Voulez-vous rechercher un autre médicament ? (oui/non) : ");
            choix = scanner.nextLine().trim().toLowerCase();
        } while (choix.equals("oui"));

        // Résultats globaux
        System.out.println("\n=== Résultats globaux de la recherche ===");
        resultatsGlobaux.forEach(r -> {
            System.out.println("==================================");
            System.out.println(r);
        });
    }

    private static void menuSansOrdonnance() {
        List<String> resultatsGlobaux = new ArrayList<>();
        String recherche;

        do {
            System.out.print("Entrez un client à rechercher : \n");
            recherche = scanner.nextLine();
            List<Client> clientsTrouves = clientDAO.findByNom(recherche);

            if (clientsTrouves.isEmpty()) {
                System.out.println("Aucun client trouvé pour : " + recherche);
                System.out.println("Souhaitez-vous réessayer ? (oui/non)");
                if (!scanner.nextLine().trim().equalsIgnoreCase("oui")) break;
            } else {
                clientsTrouves.forEach(c -> resultatsGlobaux.add("Client : " + c.getNom() + " " + c.getPrenom()));
                Vue.afficherResultatsClients(clientsTrouves, recherche);
                break;
            }
        } while (true);

        // Recherche médicaments sans ordonnance via DAO
        String choix;
        do {
            System.out.print("\nEntrez un médicament à rechercher : ");
            recherche = scanner.nextLine();
            List<Medicament> medicamentsTrouves = medicamentDAO.findSansOrdonnanceByNom(recherche);

            if (!medicamentsTrouves.isEmpty()) {
                Vue.afficherResultatsMedicaments(medicamentsTrouves, recherche);

                for (Medicament medicament : medicamentsTrouves) {
                    System.out.println("Stock actuel : " + medicament.getQuantiteMedicament());
                    System.out.print("Quantité à retirer : ");
                    int quantite;
                    try {
                        quantite = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Quantité invalide. 1 par défaut.");
                        quantite = 1;
                    }

                    try {
                        if (quantite > medicament.getQuantiteMedicament()) {
                            System.out.println("Stock insuffisant pour " + medicament.getNomMedicament());
                        } else {
                            int stockRestant = medicamentDAO.retirerDuStock(medicament.getIdMedicament(), quantite);
                            resultatsGlobaux.add("Médicament : " + medicament.getNomMedicament()
                                    + " | Quantité : " + quantite
                                    + " | Stock restant : " + stockRestant);
                        }
                    } catch (SaisieException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Aucun médicament trouvé pour : " + recherche);
            }

            System.out.print("Voulez-vous rechercher un autre médicament ? (oui/non) : ");
            choix = scanner.nextLine().trim().toLowerCase();
        } while (choix.equals("oui"));

        System.out.println("\n=== Résultats globaux ===");
        resultatsGlobaux.forEach(r -> System.out.println(r));
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
                    String nss = Menu.scanner.nextLine();
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
        String sql = "SELECT id_client, nom, prenom, adresse, email, telephone FROM Client WHERE LOWER(nom) LIKE ?";

        try (Connection con = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nom.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client")); // il faudra ajouter l'attribut idClient dans Client si ce n'est pas fait
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setAdresse(rs.getString("adresse"));
                client.setEmail(rs.getString("email"));
                client.setTelephone(rs.getString("telephone"));

                resultats.add(client);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des clients par nom");
        }
        return resultats;
    }


    public static List<Client> rechercherClientParNSS(String nSs) {
        List<Client> resultats = new ArrayList<>();
        String sql = "SELECT id_client, nom, prenom, adresse, email, telephone, nss FROM Client WHERE LOWER(nss) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nSs.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client")); // assure-toi que l'attribut idClient existe dans Client
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setAdresse(rs.getString("adresse"));
                client.setEmail(rs.getString("email"));
                client.setTelephone(rs.getString("telephone"));
                client.setNss(rs.getString("nss"));

                resultats.add(client);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des clients par NSS");
        }
        return resultats;
    }


    public static List<Client> rechercherClientParEmail(String email) {
        List<Client> resultats = new ArrayList<>();
        String sql = "SELECT id_client, nom, prenom, adresse, email, telephone, nss FROM Client WHERE LOWER(email) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + email.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));  // Assurez-vous que idClient existe
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setAdresse(rs.getString("adresse"));
                client.setEmail(rs.getString("email"));
                client.setTelephone(rs.getString("telephone"));
                client.setNss(rs.getString("nss"));

                resultats.add(client);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des clients par email");
        }

        return resultats;
    }

    public static List<Medecin> rechercherMedecinParNom(String nom) {
        List<Medecin> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Medecin WHERE LOWER(nomMedecin) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nom.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMedecin(rs.getInt("id_Medecin"));
                medecin.setNom(rs.getString("nomMedecin"));
                medecin.setPrenom(rs.getString("prenomMedecin"));
                medecin.setAdresse(rs.getString("adresseMedecin"));
                medecin.setCodePostal(rs.getString("codePostalMedecin"));
                medecin.setVille(rs.getString("villeMedecin"));
                medecin.setTelephone(rs.getString("telephoneMedecin"));
                medecin.setEmail(rs.getString("mailMedecin"));
                medecin.setRPPS(rs.getString("rppsMedecin"));

                resultats.add(medecin);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des médecins par nom");
        }

        return resultats;
    }

    public static List<Medecin> rechercherMedecinParRPPS(String RPPS) {
        List<Medecin> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Medecin WHERE LOWER(rppsMedecin) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + RPPS.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMedecin(rs.getInt("id_Medecin"));
                medecin.setNom(rs.getString("nomMedecin"));
                medecin.setPrenom(rs.getString("prenomMedecin"));
                medecin.setAdresse(rs.getString("adresseMedecin"));
                medecin.setCodePostal(rs.getString("codePostalMedecin"));
                medecin.setVille(rs.getString("villeMedecin"));
                medecin.setTelephone(rs.getString("telephoneMedecin"));
                medecin.setEmail(rs.getString("mailMedecin"));
                medecin.setRPPS(rs.getString("rppsMedecin"));

                resultats.add(medecin);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des médecins par RPPS");
        }
        return resultats;
    }

    private static void afficherTousLesMedecins() {
        String sql = "SELECT * FROM Medecin";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMedecin(rs.getInt("id_Medecin"));
                medecin.setNom(rs.getString("nomMedecin"));
                medecin.setPrenom(rs.getString("prenomMedecin"));
                medecin.setAdresse(rs.getString("adresseMedecin"));
                medecin.setCodePostal(rs.getString("codePostalMedecin"));
                medecin.setVille(rs.getString("villeMedecin"));
                medecin.setTelephone(rs.getString("telephoneMedecin"));
                medecin.setEmail(rs.getString("mailMedecin"));
                medecin.setRPPS(rs.getString("rppsMedecin"));

                System.out.println("==================================");
                System.out.println(medecin);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'affichage des médecins");
        }
    }

    public static List<Mutuelle> rechercherMutuelleParNom(String nom) {
        List<Mutuelle> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE LOWER(nomMutuelle) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nom.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mutuelle mutuelle = new Mutuelle();
                mutuelle.setIdMutuelle(rs.getInt("id_Mutuelle"));
                mutuelle.setNom(rs.getString("nomMutuelle"));
                mutuelle.setAdresse(rs.getString("adresseMutuelle"));
                mutuelle.setCodePostal(rs.getString("codePostalMutuelle"));
                mutuelle.setVille(rs.getString("villeMutuelle"));
                mutuelle.setTelephone(rs.getString("telephoneMutuelle"));
                mutuelle.setEmail(rs.getString("mailMutuelle"));
                mutuelle.setDepartement(rs.getString("departementMutuelle"));
                mutuelle.setTRemboursement(rs.getInt("tRemboursement"));
                resultats.add(mutuelle);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des mutuelles par nom");
        }
        return resultats;
    }

    public static List<Mutuelle> rechercherMutuelleParDepartement(String departement) {
        List<Mutuelle> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Mutuelle WHERE LOWER(departementMutuelle) = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, departement.toLowerCase());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mutuelle mutuelle = new Mutuelle();
                mutuelle.setIdMutuelle(rs.getInt("id_Mutuelle"));  // à ajouter dans la classe Mutuelle
                mutuelle.setNom(rs.getString("nomMutuelle"));
                mutuelle.setAdresse(rs.getString("adresseMutuelle"));
                mutuelle.setCodePostal(rs.getString("codePostalMutuelle"));
                mutuelle.setVille(rs.getString("villeMutuelle"));
                mutuelle.setTelephone(rs.getString("telephoneMutuelle"));
                mutuelle.setEmail(rs.getString("mailMutuelle"));
                mutuelle.setDepartement(rs.getString("departementMutuelle"));
                mutuelle.setTRemboursement(rs.getInt("tRemboursement"));

                resultats.add(mutuelle);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des mutuelles par département");
        }

        return resultats;
    }


    private static void afficherToutesLesMutuelles() {
        String sql = "SELECT * FROM Mutuelle";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mutuelle mutuelle = new Mutuelle();
                mutuelle.setIdMutuelle(rs.getInt("id_Mutuelle")); // Assure-toi que idMutuelle existe
                mutuelle.setNom(rs.getString("nomMutuelle"));
                mutuelle.setAdresse(rs.getString("adresseMutuelle"));
                mutuelle.setCodePostal(rs.getString("codePostalMutuelle"));
                mutuelle.setVille(rs.getString("villeMutuelle"));
                mutuelle.setTelephone(rs.getString("telephoneMutuelle"));
                mutuelle.setEmail(rs.getString("mailMutuelle"));
                mutuelle.setDepartement(rs.getString("departementMutuelle"));
                mutuelle.setTRemboursement(rs.getInt("tRemboursement"));

                System.out.println("==================================");
                System.out.println(mutuelle);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'affichage des mutuelles");
        }
    }


    public static List<Medicament> rechercherNomMedicament(String nom) {
        List<Medicament> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Medicament WHERE LOWER(nomMedicament) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nom.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setIdMedicament(rs.getInt("id_Medicament"));
                medicament.setNomMedicament(rs.getString("nomMedicament"));
                medicament.setCategorieMedicament(rs.getString("categorieMedicament"));
                medicament.setPrixMedicament(rs.getDouble("prixMedicament"));
                medicament.setDateMiseEnCirculation(rs.getString("dateMiseEnCirculation"));
                medicament.setQuantiteMedicament(rs.getInt("quantiteMedicament"));
                medicament.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

                resultats.add(medicament);
            }
        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des médicaments par nom");
        }
        return resultats;
    }

    public static List<Medicament> rechercherParCategorie(String categorie) {
        List<Medicament> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Medicament WHERE LOWER(categorieMedicament) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + categorie.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setIdMedicament(rs.getInt("id_Medicament"));
                medicament.setNomMedicament(rs.getString("nomMedicament"));
                medicament.setCategorieMedicament(rs.getString("categorieMedicament"));
                medicament.setPrixMedicament(rs.getDouble("prixMedicament"));
                medicament.setDateMiseEnCirculation(rs.getString("dateMiseEnCirculation"));
                medicament.setQuantiteMedicament(rs.getInt("quantiteMedicament"));
                medicament.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

                resultats.add(medicament);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des médicaments par catégorie");
        }

        return resultats;
    }


    private static void afficherTousLesMedicaments() {
        String sql = "SELECT * FROM Medicament";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medicament medicament = new Medicament();
                medicament.setIdMedicament(rs.getInt("id_Medicament"));
                medicament.setNomMedicament(rs.getString("nomMedicament"));
                medicament.setCategorieMedicament(rs.getString("categorieMedicament"));
                medicament.setPrixMedicament(rs.getDouble("prixMedicament"));
                medicament.setDateMiseEnCirculation(rs.getString("dateMiseEnCirculation"));
                medicament.setQuantiteMedicament(rs.getInt("quantiteMedicament"));
                medicament.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

                System.out.println("==================================");
                System.out.println(medicament);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'affichage des médicaments");
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
        String sql = "SELECT * FROM Medicament WHERE sansOrdonnance = TRUE AND LOWER(nomMedicament) LIKE ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nom.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicament med = new Medicament();
                med.setIdMedicament(rs.getInt("id_Medicament"));
                med.setNomMedicament(rs.getString("nomMedicament"));
                med.setCategorieMedicament(rs.getString("categorie"));
                med.setPrixMedicament(rs.getDouble("prix"));
                med.setDateMiseEnCirculation(rs.getString("dateCirculation"));
                med.setQuantiteMedicament(rs.getInt("stock"));
                med.setSansOrdonnanceMedicament(rs.getBoolean("sansOrdonnance"));

                resultats.add(med);
            }

        } catch (SQLException | SaisieException e) {
            e.printStackTrace();
        }

        return resultats;
    }

    private static boolean quitter() {
        System.out.println("Au revoir !");
        return true;
    }
}