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
        String sql = "INSERT INTO Client (" +
                "nomClient, prenomClient, adresseClient, codePostalClient, villeClient," +
                "telephoneClient, mailClient, nssClient, dateNaissance," +
                "id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        }
    }

    @Override
    public boolean update(Client client) throws SQLException {
        String sql = "UPDATE Client SET " +
                "nomClient=?, prenomClient=?, adresseClient=?, codePostalClient=?, villeClient=?," +
                "telephoneClient=?, mailClient=?, nssClient=?, dateNaissance=?," +
                "id_Regime=?, id_Medecin=?, id_Mutuelle=?, idTitulaireMutuelle=? " +
                "WHERE id_Client=?";

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

            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id_Client=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public Client findById(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id_Client=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
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
                    client.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());

                    int idRegime = rs.getInt("id_Regime");
                    if (!rs.wasNull()) client.setRegime(new Regime(idRegime));

                    int idMedecin = rs.getInt("id_Medecin");
                    if (!rs.wasNull()) client.setMedecin(new Medecin(idMedecin));

                    int idMutuelle = rs.getInt("id_Mutuelle");
                    if (!rs.wasNull()) client.setMutuelle(new Mutuelle(idMutuelle));

                    client.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

                    return client;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";

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
                client.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());

                int idRegime = rs.getInt("id_Regime");
                if (!rs.wasNull()) client.setRegime(new Regime(idRegime));

                int idMedecin = rs.getInt("id_Medecin");
                if (!rs.wasNull()) client.setMedecin(new Medecin(idMedecin));

                int idMutuelle = rs.getInt("id_Mutuelle");
                if (!rs.wasNull()) client.setMutuelle(new Mutuelle(idMutuelle));

                client.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

                clients.add(client);
            }
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
}
