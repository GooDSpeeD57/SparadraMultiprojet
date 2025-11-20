package training.afpa.cda24060.controleur;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainSQBD {

    private static final Logger LOGGER = Logger.getLogger(MainSQBD.class.getName());

    public static void main(String[] args) {

        String selectSQL = "SELECT * FROM client";
        StringBuilder sb = new StringBuilder();

        // === SELECT ===
        try (Connection con = DCSingletonHikaricp.getInstanceDB();
             PreparedStatement pstmt = con.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                sb.append("=== Client ===\n")
                        .append("Nom : ").append(rs.getString("nomClient")).append("\n")
                        .append("Prénom : ").append(rs.getString("prenomClient")).append("\n")
                        .append("Adresse : ").append(rs.getString("adresseClient")).append("\n")
                        .append("Code postal : ").append(rs.getString("codePostalClient")).append("\n")
                        .append("Ville : ").append(rs.getString("villeClient")).append("\n")
                        .append("Téléphone : ").append(rs.getString("telephoneClient")).append("\n")
                        .append("Mail : ").append(rs.getString("mailClient")).append("\n")
                        .append("NSS : ").append(rs.getString("nssClient")).append("\n")
                        .append("Date de naissance : ").append(rs.getString("dateNaissance")).append("\n")
                        .append("Régime : ").append(rs.getString("id_Regime")).append("\n")
                        .append("Médecin : ").append(rs.getString("id_Medecin")).append("\n")
                        .append("Mutuelle : ").append(rs.getString("id_Mutuelle")).append("\n")
                        .append("Titulaire mutuelle : ").append(rs.getString("idTitulaireMutuelle")).append("\n")
                        .append("-----------------------------\n");
            }
            LOGGER.info("\n" + sb);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la requête SELECT client :", e);
        } finally {
            DatabaseConnectionSingleton.closeInstanceDB();
        }

        // === INSERT ===
        String insertSQL = """
                INSERT INTO client 
                (nomClient, prenomClient, adresseClient, codePostalClient, villeClient, telephoneClient, 
                 mailClient, nssClient, dateNaissance, id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {

            pstmt.setString(1, "Dupont");
            pstmt.setString(2, "Jean");
            pstmt.setString(3, "10 rue des Lilas");
            pstmt.setString(4, "75001");
            pstmt.setString(5, "Paris");
            pstmt.setString(6, "0102030405");
            pstmt.setString(7, "jean.dupont@mail.com");
            pstmt.setString(8, "1234567890123");
            pstmt.setString(9, "1980-01-01");
            pstmt.setInt(10, 1);
            pstmt.setInt(11, 2);
            pstmt.setInt(12, 3);
            pstmt.setInt(13, 4);

            int rowsInserted = pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "{0} client(s) inséré(s)", rowsInserted);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion du client :", e);
        } finally {
            DatabaseConnectionSingleton.closeInstanceDB();
        }

        // === UPDATE ===
        String updateSQL = "UPDATE client SET adresseClient = ?, telephoneClient = ? WHERE nssClient = ?";

        try (Connection con = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            pstmt.setString(1, "20 avenue des Champs");
            pstmt.setString(2, "0607080910");
            pstmt.setString(3, "1234567890123");

            int rowsUpdated = pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "{0} client(s) mis à jour", rowsUpdated);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du client :", e);
        } finally {
            DatabaseConnectionSingleton.closeInstanceDB();
        }

        // === DELETE ===
        String deleteSQL = "DELETE FROM client WHERE nssClient = ?";

        try (Connection con = DatabaseConnectionSingleton.getInstanceDB();
             PreparedStatement pstmt = con.prepareStatement(deleteSQL)) {

            pstmt.setString(1, "1234567890123");

            int rowsDeleted = pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "{0} client(s) supprimé(s)", rowsDeleted);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du client :", e);
        } finally {
            DatabaseConnectionSingleton.closeInstanceDB();
        }

    }
}