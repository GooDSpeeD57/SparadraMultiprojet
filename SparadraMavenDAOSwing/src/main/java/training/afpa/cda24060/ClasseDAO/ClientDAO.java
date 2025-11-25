package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected String getTableName() {
        return "Client";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Client";
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
            m.setPrenom(rs.getString("prenomMedecin") != null ? rs.getString("prenomMedecin") : "");
            c.setMedecin(m);

            Mutuelle mu = new Mutuelle();
            mu.setIdMutuelle(rs.getInt("id_Mutuelle"));
            mu.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
            c.setMutuelle(mu);
            return c;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Client", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Client c, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Client (")
                    .append("nomClient, prenomClient, adresseClient, codePostalClient, villeClient,")
                    .append("telephoneClient, mailClient, nssClient, dateNaissance,")
                    .append("id_Regime, id_Medecin, id_Mutuelle, idTitulaireMutuelle)")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                    .toString();

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

            if (c.getRegime() != null) pst.setInt(10, c.getRegime().getIdRegime());
            else pst.setNull(10, java.sql.Types.INTEGER);

            if (c.getMedecin() != null) pst.setInt(11, c.getMedecin().getIdMedecin());
            else pst.setNull(11, java.sql.Types.INTEGER);

            if (c.getMutuelle() != null) pst.setInt(12, c.getMutuelle().getIdMutuelle());
            else pst.setNull(12, java.sql.Types.INTEGER);

            pst.setString(13, c.getIdTitulaireMutuelle());

            LogUtils.debug(logger, "PreparedStatement insert Client prêt pour : " + c);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Client", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Client c, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Client SET")
                    .append("nomClient=?, prenomClient=?, adresseClient=?, codePostalClient=?, villeClient=?,")
                    .append("telephoneClient=?, mailClient=?, nssClient=?, dateNaissance=?,")
                    .append("id_Regime=?, id_Medecin=?, id_Mutuelle=?, idTitulaireMutuelle=?")
                    .append("WHERE id_Client=?")
                    .toString();

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

            if (c.getRegime() != null) pst.setInt(10, c.getRegime().getIdRegime());
            else pst.setNull(10, java.sql.Types.INTEGER);

            if (c.getMedecin() != null) pst.setInt(11, c.getMedecin().getIdMedecin());
            else pst.setNull(11, java.sql.Types.INTEGER);

            if (c.getMutuelle() != null) pst.setInt(12, c.getMutuelle().getIdMutuelle());
            else pst.setNull(12, java.sql.Types.INTEGER);

            pst.setString(13, c.getIdTitulaireMutuelle());
            pst.setInt(14, c.getIdClient());

            LogUtils.debug(logger, "PreparedStatement update Client prêt pour : " + c);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Client", e);
            return null;
        }
    }

    public Client findById(int idClient) {
        String sql = new SqlBuilder()
                .append("SELECT c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient,")
                .append("c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle,")
                .append("r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, m.prenomMedecin AS prenomMedecin, mu.nomMutuelle AS nomMutuelle")
                .append("FROM Client c")
                .append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin")
                .append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime")
                .append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle")
                .append("WHERE c.id_Client=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idClient);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Client id=" + idClient, e);
        }
        return null;
    }

    public List<Client> findByNom(String nom) {
        List<Client> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient,")
                .append("c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle,")
                .append("r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, m.prenomMedecin AS prenomMedecin, mu.nomMutuelle AS nomMutuelle")
                .append("FROM Client c")
                .append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin")
                .append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime")
                .append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle")
                .append("WHERE c.nomClient LIKE ?")
                .toString();

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
            LogUtils.error(logger, "Erreur findByNom Client nom=" + nom, e);
        }
        return list;
    }

    public Client findByNSS(String nss) {
        String sql = new SqlBuilder()
                .append("SELECT c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient,")
                .append("c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle,")
                .append("r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, m.prenomMedecin AS prenomMedecin, mu.nomMutuelle AS nomMutuelle")
                .append("FROM Client c")
                .append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin")
                .append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime")
                .append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle")
                .append("WHERE c.nssClient = ?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nss);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByNSS Client nss=" + nss, e);
        }
        return null;
    }

    public Client findByEmail(String email) {
        String sql = new SqlBuilder()
                .append("SELECT c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient,")
                .append("c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle,")
                .append("r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, m.prenomMedecin AS prenomMedecin, mu.nomMutuelle AS nomMutuelle")
                .append("FROM Client c")
                .append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin")
                .append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime")
                .append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle")
                .append("WHERE c.mailClient = ?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByEmail Client email=" + email, e);
        }
        return null;
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT c.id_Client, c.nomClient, c.prenomClient, c.adresseClient, c.codePostalClient, c.villeClient,")
                .append("c.telephoneClient, c.mailClient, c.nssClient, c.dateNaissance, c.id_Regime, c.id_Medecin, c.id_Mutuelle, c.idTitulaireMutuelle,")
                .append("r.nomRegime AS nomRegime, m.nomMedecin AS nomMedecin, m.prenomMedecin AS prenomMedecin, mu.nomMutuelle AS nomMutuelle")
                .append("FROM Client c")
                .append("LEFT JOIN Medecin m ON c.id_Medecin = m.id_Medecin")
                .append("LEFT JOIN Regime r ON c.id_Regime = r.id_Regime")
                .append("LEFT JOIN Mutuelle mu ON c.id_Mutuelle = mu.id_Mutuelle")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Client c = map(rs);
                if (c != null) list.add(c);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Client", e);
        }
        return list;
    }

    public int countClient() {
        String sql = new SqlBuilder()
                .append("SELECT COUNT(*) AS total FROM Client")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countClients", e);
        }
        return 0;
    }
}