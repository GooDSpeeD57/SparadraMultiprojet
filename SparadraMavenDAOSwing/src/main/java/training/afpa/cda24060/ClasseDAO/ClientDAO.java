package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected String getTableName() {
        return "Client"; // Nom exact de la table
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Client"; // Nom exact de la PK
    }

    @Override
    protected Client map(ResultSet rs) {
        try {
            Client c = new Client();
            c.setIdClient(rs.getInt("id_Client"));
            c.setNom(rs.getString("nomClient"));
            c.setPrenom(rs.getString("prenomClient"));
            c.setAdresse(rs.getString("adresseClient"));
            c.setCodePostal(rs.getString("codePostalClient"));
            c.setVille(rs.getString("villeClient"));
            c.setTelephone(rs.getString("telephoneClient"));
            c.setEmail(rs.getString("mailClient"));
            c.setNss(rs.getString("nssClient"));

            Date sqlDate = rs.getDate("dateNaissance");
            if (sqlDate != null) {
                c.setDateNaissance(sqlDate.toLocalDate().format(FORMATTER));
            }

            c.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

            Regime r = new Regime();
            r.setIdRegime(rs.getInt("id_Regime"));
            r.setNomRegime(rs.getString("nomRegime") != null ? rs.getString("nomRegime") : "");
            c.setRegime(r);

            Medecin m = new Medecin();
            m.setIdMedecin(rs.getInt("id_Medecin"));
            m.setNom(rs.getString("nomMedecin") != null ? rs.getString("nomMedecin") : "");
            c.setMedecin(m);

            Mutuelle mu = new Mutuelle();
            mu.setIdMutuelle(rs.getInt("id_Mutuelle"));
            mu.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
            c.setMutuelle(mu);

            return c;
        } catch (Exception e) {
            System.err.println("Erreur mapping Client : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Client c, Connection conn) {
        try {
            String sql = "INSERT INTO Client " +
                    "(nomClient, prenomClient, adresseClient, codePostalClient, villeClient, telephoneClient, mailClient, nssClient, dateNaissance, " +
                    "id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getAdresse());
            pst.setString(4, c.getCodePostal());
            pst.setString(5, c.getVille());
            pst.setString(6, c.getTelephone());
            pst.setString(7, c.getEmail());
            pst.setString(8, c.getNss());
            pst.setDate(9, Date.valueOf(c.getDateNaissance()));
            pst.setInt(10, c.getRegime().getIdRegime());
            pst.setInt(11, c.getMedecin().getIdMedecin());
            pst.setInt(12, c.getMutuelle().getIdMutuelle());
            pst.setString(13, c.getIdTitulaireMutuelle());
            return pst;
        } catch (Exception e) {
            System.err.println("Erreur prepareInsert Client : " + e.getMessage());
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Client c, Connection conn) {
        try {
            String sql = "UPDATE Client SET " +
                    "nomClient=?, prenomClient=?, adresseClient=?, codePostalClient=?, villeClient=?, telephoneClient=?, mailClient=?, nssClient=?, dateNaissance=?, " +
                    "id_Regime=?, id_Medecin=?, id_Mutuelle=?, idTitulaireMutuelle=? " +
                    "WHERE id_Client=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getAdresse());
            pst.setString(4, c.getCodePostal());
            pst.setString(5, c.getVille());
            pst.setString(6, c.getTelephone());
            pst.setString(7, c.getEmail());
            pst.setString(8, c.getNss());
            pst.setDate(9, Date.valueOf(c.getDateNaissance()));
            pst.setInt(10, c.getRegime().getIdRegime());
            pst.setInt(11, c.getMedecin().getIdMedecin());
            pst.setInt(12, c.getMutuelle().getIdMutuelle());
            pst.setString(13, c.getIdTitulaireMutuelle());
            pst.setInt(14, c.getIdClient());
            return pst;
        } catch (Exception e) {
            System.err.println("Erreur prepareUpdate Client : " + e.getMessage());
            return null;
        }
    }

    public Client findById(int idClient) {
        String sql = "SELECT " +
                "c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient, " +
                "c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle, " +
                "r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, mu.nomMutuelle AS nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle " +
                "WHERE c.id_Client=?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idClient);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            System.err.println("Erreur findById Client : " + e.getMessage());
        }
        return null;
    }

    public List<Client> findByNom(String nom) {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT " +
                "c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient, " +
                "c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle, " +
                "r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, mu.nomMutuelle AS nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle " +
                "WHERE c.nomClient LIKE ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Client c = map(rs);
                    if (c != null) list.add(c);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur findByNom Client : " + e.getMessage());
        }
        return list;
    }

    public Client findByNSS(String nss) {
        String sql = "SELECT " +
                "c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient, " +
                "c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle, " +
                "r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, mu.nomMutuelle AS nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle " +
                "WHERE c.nssClient = ?";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nss);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            System.err.println("Erreur findByNSS Client : " + e.getMessage());
        }
        return null;
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT " +
                "c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient, " +
                "c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle, " +
                "r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, mu.nomMutuelle AS nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Client c = map(rs);
                if (c != null) list.add(c);
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll Client : " + e.getMessage());
        }
        return list;
    }

    public int countClient() {
        String sql = "SELECT COUNT(*) AS total FROM Client";
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            System.err.println("Erreur countClients : " + e.getMessage());
        }
        return 0;
    }
}