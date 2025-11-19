package training.afpa.cda24060.ClasseDAO;

import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Client;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.modele.Regime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends AbstractDAO<Client> {

    @Override
    protected String getTableName() {
        return "client";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Client";
    }

    @Override
    protected Client map(ResultSet rs) throws Exception {
        return mapJoin(rs);
    }

    @Override
    protected PreparedStatement prepareInsert(Client obj, Connection conn) throws Exception {
        return null;
    }

    @Override
    protected PreparedStatement prepareUpdate(Client obj, Connection conn) throws Exception {
        return null;
    }

    private Client mapJoin(ResultSet rs) throws Exception {

        Client c = new Client();

        c.setIdClient(rs.getInt("id_Client"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setAdresse(rs.getString("adresse"));
        c.setCodePostal(rs.getString("codePostal"));
        c.setVille(rs.getString("ville"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        c.setNss(rs.getString("nss"));
        c.setDateNaissance(rs.getString("dateNaissance"));
        c.setIdTitulaireMutuelle(rs.getString("idTitulaireMutuelle"));

        // --- Regime ---
        Regime r = new Regime();
        r.setIdRegime(rs.getInt("idRegime"));
        r.setNomRegime(rs.getString("nomRegime"));
        c.setRegime(r);

        // --- Medecin ---
        Medecin m = new Medecin();
        m.setIdMedecin(rs.getInt("idMedecin"));
        m.setNom(rs.getString("nomMedecin"));
        c.setMedecin(m);

        // --- Mutuelle ---
        Mutuelle mu = new Mutuelle();
        mu.setIdMutuelle(rs.getInt("idMutuelle"));
        mu.setNom(rs.getString("nomMutuelle"));
        c.setMutuelle(mu);

        return c;
    }

    public Client findById(int idClient) {
        String sql =
                "SELECT c.*, " +
                        "m.nom AS nomMedecin, " +
                        "r.nomRegime AS nomRegime, " +
                        "mu.nom AS nomMutuelle " +
                        "FROM client c " +
                        "INNER JOIN medecin m ON c.idMedecin = m.idMedecin " +
                        "INNER JOIN regime r ON c.idRegime = r.idRegime " +
                        "INNER JOIN mutuelle mu ON c.idMutuelle = mu.idMutuelle " +
                        "WHERE c.id_Client = ?";

        try (Connection conn = DCSingletonHikaricp.getInstanceDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idClient);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return mapJoin(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> findByNom(String nom) {
        List<Client> liste = new ArrayList<>();

        String sql =
                "SELECT c.*, " +
                        "m.nom AS nomMedecin, " +
                        "r.nomRegime AS nomRegime, " +
                        "mu.nom AS nomMutuelle " +
                        "FROM client c " +
                        "INNER JOIN medecin m ON c.idMedecin = m.idMedecin " +
                        "INNER JOIN regime r ON c.idRegime = r.idRegime " +
                        "INNER JOIN mutuelle mu ON c.idMutuelle = mu.idMutuelle " +
                        "WHERE c.nom LIKE ?";

        try (Connection conn = DCSingletonHikaricp.getInstanceDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                liste.add(mapJoin(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int countClients() {
        String sql = "SELECT COUNT(*) AS total FROM client";

        try (Connection conn = DCSingletonHikaricp.getInstanceDB();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
