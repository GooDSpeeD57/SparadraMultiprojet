package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {

    @Override
    protected String getTableName() {
        return "Client";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Client";
    }

    @Override
    protected PreparedStatement prepareInsert(Client c, Connection conn) throws SQLException {
        String sql = "INSERT INTO Client " +
                "(nomClient, prenomClient, adresseClient, codePostalClient, villeClient, telephoneClient, mailClient, " +
                "nssClient, dateNaissance, id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, c.getNom());
        pst.setString(2, c.getPrenom());
        pst.setString(3, c.getAdresse());
        pst.setString(4, c.getCodePostal());
        pst.setString(5, c.getVille());
        pst.setString(6, c.getTelephone());
        pst.setString(7, c.getEmail());
        pst.setString(8, c.getNssClient());
        pst.setDate(9, Date.valueOf(c.getDateNaissance()));
        pst.setInt(10, c.getRegime().getIdRegime());
        pst.setInt(11, c.getMedecin().getIdMedecin());
        pst.setInt(12, c.getMutuelle().getIdMutuelle());
        pst.setString(13, c.getIdTitulaireMutuelle());
        return pst;
    }

    @Override
    protected PreparedStatement prepareUpdate(Client c, Connection conn) throws SQLException {
        String sql = "UPDATE Client SET " +
                "nomClient=?, prenomClient=?, adresseClient=?, codePostalClient=?, villeClient=?, " +
                "telephoneClient=?, mailClient=?, nssClient=?, dateNaissance=?, " +
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
        pst.setString(8, c.getNssClient());
        pst.setDate(9, Date.valueOf(c.getDateNaissance()));
        pst.setInt(10, c.getRegime().getIdRegime());
        pst.setInt(11, c.getMedecin().getIdMedecin());
        pst.setInt(12, c.getMutuelle().getIdMutuelle());
        pst.setString(13, c.getIdTitulaireMutuelle());
        pst.setInt(14, c.getId_Client());
        return pst;
    }

    public Client findClientId(int id_Client) {
        String sql = "SELECT c.*, r.nomRegime, m.nomMedecin, mu.nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle " +
                "WHERE c.id_Client=?";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id_Client);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Client c = new Client();
                    c.setId_Client(rs.getInt("id_Client"));
                    c.setNom(rs.getString("nomClient"));
                    c.setPrenom(rs.getString("prenomClient"));
                    c.setAdresse(rs.getString("adresseClient"));
                    c.setCodePostal(rs.getString("codePostalClient"));
                    c.setVille(rs.getString("villeClient"));
                    c.setTelephone(rs.getString("telephoneClient"));
                    c.setEmail(rs.getString("mailClient"));
                    c.setNssClient(rs.getString("nssClient"));

                    Date date = rs.getDate("dateNaissance");
                    if (date != null) c.setDateNaissance(date.toLocalDate());

                    c.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

                    Regime r = new Regime();
                    r.setIdRegime(rs.getInt("id_Regime"));
                    r.setNomRegime(rs.getString("nomRegime"));
                    c.setRegime(r);

                    Medecin m = new Medecin();
                    m.setIdMedecin(rs.getInt("id_Medecin"));
                    m.setNom(rs.getString("nomMedecin"));
                    c.setMedecin(m);

                    Mutuelle mu = new Mutuelle();
                    mu.setIdMutuelle(rs.getInt("id_Mutuelle"));
                    mu.setNomMutuelle(rs.getString("nomMutuelle"));
                    c.setMutuelle(mu);

                    return c;
                }
            }
        } catch (SQLException | SaisieException e) {
            System.err.println("Erreur findById Client : " + e.getMessage());
        }

        return null;
    }

    public List<Client> findAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT c.*, r.nomRegime, m.nomMedecin, mu.nomMutuelle " +
                "FROM Client c " +
                "LEFT JOIN Regime r ON c.id_Regime = r.id_Regime " +
                "LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin " +
                "LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle";

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Client c = new Client();
                c.setId_Client(rs.getInt("id_Client"));
                c.setNom(rs.getString("nomClient"));
                c.setPrenom(rs.getString("prenomClient"));
                c.setAdresse(rs.getString("adresseClient"));
                c.setCodePostal(rs.getString("codePostalClient"));
                c.setVille(rs.getString("villeClient"));
                c.setTelephone(rs.getString("telephoneClient"));
                c.setEmail(rs.getString("mailClient"));
                c.setNssClient(rs.getString("nssClient"));

                Date date = rs.getDate("dateNaissance");
                if (date != null) c.setDateNaissance(date.toLocalDate());

                c.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

                Regime r = new Regime();
                r.setIdRegime(rs.getInt("id_Regime"));
                r.setNomRegime(rs.getString("nomRegime"));
                c.setRegime(r);

                Medecin m = new Medecin();
                m.setIdMedecin(rs.getInt("id_Medecin"));
                m.setNom(rs.getString("nomMedecin"));
                c.setMedecin(m);

                Mutuelle mu = new Mutuelle();
                mu.setIdMutuelle(rs.getInt("id_Mutuelle"));
                mu.setNomMutuelle(rs.getString("nomMutuelle"));
                c.setMutuelle(mu);

                clients.add(c);
            }

        } catch (SQLException | SaisieException e) {
            System.err.println("Erreur findAll Client : " + e.getMessage());
        }

        return clients;
    }
}
