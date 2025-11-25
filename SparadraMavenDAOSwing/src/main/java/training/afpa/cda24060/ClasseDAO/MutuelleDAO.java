package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Mutuelle;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MutuelleDAO extends AbstractDAO<Mutuelle> {

    private static final Logger logger = LoggerFactory.getLogger(MutuelleDAO.class);

    @Override
    protected String getTableName() {
        return "Mutuelle";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Mutuelle";
    }

    @Override
    protected Mutuelle map(ResultSet rs) {
        try {
            Mutuelle m = new Mutuelle();
            m.setIdMutuelle(rs.getInt("id_Mutuelle"));
            m.setNomMutuelle(rs.getString("nomMutuelle") != null ? rs.getString("nomMutuelle") : "");
            m.setAdresseMutuelle(rs.getString("adresseMutuelle") != null ? rs.getString("adresseMutuelle") : "");
            m.setCodePostalMutuelle(rs.getString("codePostalMutuelle") != null ? rs.getString("codePostalMutuelle") : "");
            m.setVilleMutuelle(rs.getString("villeMutuelle") != null ? rs.getString("villeMutuelle") : "");
            m.setTelephoneMutuelle(rs.getString("telephoneMutuelle") != null ? rs.getString("telephoneMutuelle") : "");
            m.setMailMutuelle(rs.getString("mailMutuelle") != null ? rs.getString("mailMutuelle") : "");
            m.setDepartementMutuelle(rs.getString("departementMutuelle") != null ? rs.getString("departementMutuelle") : "");
            m.setTRemboursement(rs.getDouble("tRemboursement"));
            return m;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Mutuelle", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Mutuelle m, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Mutuelle (")
                    .append("nomMutuelle, adresseMutuelle, codePostalMutuelle, villeMutuelle,")
                    .append("telephoneMutuelle, mailMutuelle, departementMutuelle, tRemboursement)")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());

            LogUtils.debug(logger, "PreparedStatement insert Mutuelle prêt pour : " + m);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Mutuelle", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Mutuelle m, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Mutuelle SET")
                    .append("nomMutuelle=?, adresseMutuelle=?, codePostalMutuelle=?, villeMutuelle=?,")
                    .append("telephoneMutuelle=?, mailMutuelle=?, departementMutuelle=?, tRemboursement=?")
                    .append("WHERE id_Mutuelle=?")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNomMutuelle());
            pst.setString(2, m.getAdresseMutuelle());
            pst.setString(3, m.getCodePostalMutuelle());
            pst.setString(4, m.getVilleMutuelle());
            pst.setString(5, m.getTelephoneMutuelle());
            pst.setString(6, m.getMailMutuelle());
            pst.setString(7, m.getDepartementMutuelle());
            pst.setDouble(8, m.getTRemboursement());
            pst.setInt(9, m.getIdMutuelle());

            LogUtils.debug(logger, "PreparedStatement update Mutuelle prêt pour : " + m);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Mutuelle", e);
            return null;
        }
    }

    public Mutuelle findById(int idMutuelle) {
        String sql = new SqlBuilder()
                .append("SELECT * FROM Mutuelle WHERE id_Mutuelle=?")
                .toString();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMutuelle);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Mutuelle id=" + idMutuelle, e);
        }
        return null;
    }

    public List<Mutuelle> findByNom(String nom) {
        List<Mutuelle> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Mutuelle WHERE nomMutuelle LIKE ?")
                .toString();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByNom Mutuelle nom=" + nom, e);
        }
        return list;
    }

    public List<Mutuelle> findByDepartement(String departement) {
        List<Mutuelle> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Mutuelle WHERE departementMutuelle LIKE ?")
                .toString();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + departement + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByDepartement Mutuelle departement=" + departement, e);
        }
        return list;
    }

    public List<Mutuelle> findAll() {
        List<Mutuelle> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Mutuelle")
                .toString();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Mutuelle", e);
        }
        return list;
    }

    public int countMutuelles() {
        String sql = new SqlBuilder()
                .append("SELECT COUNT(*) AS total FROM Mutuelle")
                .toString();
        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countMutuelles", e);
        }
        return 0;
    }
}