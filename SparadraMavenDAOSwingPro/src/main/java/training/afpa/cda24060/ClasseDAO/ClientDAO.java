package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);

    @Override
    public boolean insert(Client client) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Client (");
        sb.append("nomClient, prenomClient, adresseClient, codePostalClient, villeClient, ");
        sb.append("telephoneClient, mailClient, nssClient, dateNaissance, ");
        sb.append("id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle");
        sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getAdresse());
            pst.setString(4, client.getCodePostal());
            pst.setString(5, client.getVille());
            pst.setString(6, client.getTelephone());
            pst.setString(7, client.getEmail());
            pst.setString(8, client.getNss());
            pst.setDate(9, java.sql.Date.valueOf(client.getDateNaissance()));

            if (client.getRegime() != null) pst.setInt(10, client.getRegime().getIdRegime());
            else pst.setNull(10, Types.INTEGER);

            if (client.getMedecin() != null) pst.setInt(11, client.getMedecin().getId());
            else pst.setNull(11, Types.INTEGER);

            if (client.getMutuelle() != null) pst.setInt(12, client.getMutuelle().getIdMutuelle());
            else pst.setNull(12, Types.INTEGER);

            pst.setString(13, client.getIdTitulaireMutuelle());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) client.setId(rs.getInt(1));
            }

            LogUtils.debug(logger, "Client inséré : " + client);
            return true;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur insert Client : " + client, e);
            throw e;
        }
    }

    @Override
    public boolean update(Client client) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE Client SET ");
        sb.append("nomClient=?, prenomClient=?, adresseClient=?, codePostalClient=?, villeClient=?, ");
        sb.append("telephoneClient=?, mailClient=?, nssClient=?, dateNaissance=?, ");
        sb.append("id_Regime=?, id_Medecin=?, id_Mutuelle=?, idTitulaireMutuelle=? ");
        sb.append("WHERE id_Client=?;");

        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getAdresse());
            pst.setString(4, client.getCodePostal());
            pst.setString(5, client.getVille());
            pst.setString(6, client.getTelephone());
            pst.setString(7, client.getEmail());
            pst.setString(8, client.getNss());
            pst.setDate(9, java.sql.Date.valueOf(client.getDateNaissance()));

            if (client.getRegime() != null) pst.setInt(10, client.getRegime().getIdRegime());
            else pst.setNull(10, Types.INTEGER);

            if (client.getMedecin() != null) pst.setInt(11, client.getMedecin().getId());
            else pst.setNull(11, Types.INTEGER);

            if (client.getMutuelle() != null) pst.setInt(12, client.getMutuelle().getIdMutuelle());
            else pst.setNull(12, Types.INTEGER);

            pst.setString(13, client.getIdTitulaireMutuelle());
            pst.setInt(14, client.getId());

            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Client mis à jour : " + client + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur update Client : " + client, e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id_Client=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            boolean success = pst.executeUpdate() > 0;
            LogUtils.debug(logger, "Client supprimé id=" + id + " -> succès=" + success);
            return success;
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur delete Client id=" + id, e);
            throw e;
        }
    }

    @Override
    public Client findById(Integer id) throws SQLException {
        Client clients = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.*, r.nomRegime, m.nomMedecin, m.prenomMedecin, mu.nomMutuelle ");
        sb.append("FROM Client c ");
        sb.append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin ");
        sb.append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime ");
        sb.append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle ");
        sb.append("WHERE c.id_Client = ?;");

        String sql = sb.toString();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    clients = new Client();
                    clients.setId(rs.getInt("id_Client"));
                    clients.setNom(rs.getString("nomClient"));
                    clients.setPrenom(rs.getString("prenomClient"));
                    clients.setAdresse(rs.getString("adresseClient"));
                    clients.setCodePostal(rs.getString("codePostalClient"));
                    clients.setVille(rs.getString("villeClient"));
                    clients.setTelephone(rs.getString("telephoneClient"));
                    clients.setEmail(rs.getString("mailClient"));
                    clients.setNss(rs.getString("nssClient"));
                    Date dateNaissance = rs.getDate("dateNaissance");
                    if (dateNaissance != null) {
                        clients.setDateNaissance(dateNaissance.toLocalDate());
                    }
                    Integer idRegime = rs.getObject("id_Regime", Integer.class);
                    if (idRegime != null) {
                        Regime regime = new Regime(idRegime);
                        regime.setNomRegime(rs.getString("nomRegime"));
                        clients.setRegime(regime);
                    }
                    Integer idMedecin = rs.getObject("id_Medecin", Integer.class);
                    if (idMedecin != null) {
                        Medecin medecin = new Medecin(idMedecin);
                        medecin.setNom(rs.getString("nomMedecin"));
                        medecin.setPrenom(rs.getString("prenomMedecin"));
                        clients.setMedecin(medecin);
                    }
                    Integer idMutuelle = rs.getObject("id_Mutuelle", Integer.class);
                    if (idMutuelle != null) {
                        Mutuelle mutuelle = new Mutuelle(idMutuelle);
                        mutuelle.setNomMutuelle(rs.getString("nomMutuelle"));
                        clients.setMutuelle(mutuelle);
                    }
                    clients.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));
                    LogUtils.debug(logger, "Client trouvé id=" + id + " : " + clients);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return clients;
    }

    @Override
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.*, ");
        sb.append("r.nomRegime,m.nomMedecin, m.prenomMedecin, mu.nomMutuelle ");
        sb.append("FROM Client c ");
        sb.append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime ");
        sb.append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin ");
        sb.append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle;");

        String sql = sb.toString();

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id_Client"));
                client.setNom(rs.getString("nomClient"));
                client.setPrenom(rs.getString("prenomClient"));
                client.setAdresse(rs.getString("adresseClient"));
                client.setCodePostal(rs.getString("codePostalClient"));
                client.setVille(rs.getString("villeClient"));
                client.setTelephone(rs.getString("telephoneClient"));
                client.setEmail(rs.getString("mailClient"));
                client.setNss(rs.getString("nssClient"));
                Date dateNaissance = rs.getDate("dateNaissance");
                if (dateNaissance != null) {
                    client.setDateNaissance(dateNaissance.toLocalDate());
                }
                Integer idRegime = rs.getObject("id_Regime", Integer.class);
                if (idRegime != null) {
                    Regime regime = new Regime(idRegime);
                    regime.setNomRegime(rs.getString("nomRegime"));
                    client.setRegime(regime);
                }
                Integer idMedecin = rs.getObject("id_Medecin", Integer.class);
                if (idMedecin != null) {
                    Medecin medecin = new Medecin(idMedecin);
                    medecin.setNom(rs.getString("nomMedecin"));
                    medecin.setPrenom(rs.getString("prenomMedecin"));
                    client.setMedecin(medecin);
                }
                Integer idMutuelle = rs.getObject("id_Mutuelle", Integer.class);
                if (idMutuelle != null) {
                    Mutuelle mutuelle = new Mutuelle(idMutuelle);
                    mutuelle.setNomMutuelle(rs.getString("nomMutuelle"));
                    client.setMutuelle(mutuelle);
                }
                client.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));
                clients.add(client);
            }
            LogUtils.debug(logger, "Total clients trouvés : " + clients.size());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public List<Client> findBy(String column, String value, boolean exactMatch) throws SQLException {
        List<Client> clients = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.*, r.nomRegime, m.nomMedecin, m.prenomMedecin, mu.nomMutuelle ");
        sb.append("FROM Client c ");
        sb.append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin ");
        sb.append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime ");
        sb.append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle ");
        sb.append("WHERE c.").append(column);
        sb.append(exactMatch ? " = ?" : " LIKE ?;");

        String sql = sb.toString();

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, exactMatch ? value : "%" + value + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getInt("id_Client"));
                    client.setNom(rs.getString("nomClient"));
                    client.setPrenom(rs.getString("prenomClient"));
                    client.setAdresse(rs.getString("adresseClient"));
                    client.setCodePostal(rs.getString("codePostalClient"));
                    client.setVille(rs.getString("villeClient"));
                    client.setTelephone(rs.getString("telephoneClient"));
                    client.setEmail(rs.getString("mailClient"));
                    client.setNss(rs.getString("nssClient"));
                    Date dateNaissance = rs.getDate("dateNaissance");
                    if (dateNaissance != null) {
                        client.setDateNaissance(dateNaissance.toLocalDate());
                    }
                    Integer idRegime = rs.getObject("id_Regime", Integer.class);
                    if (idRegime != null) {
                        Regime regime = new Regime(idRegime);
                        regime.setNomRegime(rs.getString("nomRegime"));
                        client.setRegime(regime);
                    }
                    Integer idMedecin = rs.getObject("id_Medecin", Integer.class);
                    if (idMedecin != null) {
                        Medecin medecin = new Medecin(idMedecin);
                        medecin.setNom(rs.getString("nomMedecin"));
                        medecin.setPrenom(rs.getString("prenomMedecin"));
                        client.setMedecin(medecin);
                    }
                    Integer idMutuelle = rs.getObject("id_Mutuelle", Integer.class);
                    if (idMutuelle != null) {
                        Mutuelle mutuelle = new Mutuelle(idMutuelle);
                        mutuelle.setNomMutuelle(rs.getString("nomMutuelle"));
                        client.setMutuelle(mutuelle);
                    }
                    client.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));
                    clients.add(client);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            LogUtils.debug(logger, "Clients trouvés pour " + column + "='" + value + "' : " + clients.size());
        } catch (SQLException e) {
            LogUtils.error(logger, "Erreur findBy Client " + column + "=" + value, e);
            throw e;
        }
        return clients;
    }
    public int countClient() {
        String sql = "SELECT COUNT(*) AS total FROM Client";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countClients", e);
        }
        return 0;
    }
}