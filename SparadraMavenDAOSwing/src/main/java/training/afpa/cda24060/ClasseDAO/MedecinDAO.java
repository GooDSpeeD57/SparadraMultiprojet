package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.Connection.DCSingletonHikaricp;
import training.afpa.cda24060.modele.Medecin;
import training.afpa.cda24060.utilitaires.LogUtils;
import training.afpa.cda24060.utilitaires.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO extends AbstractDAO<Medecin> {

    private static final Logger logger = LoggerFactory.getLogger(MedecinDAO.class);

    @Override
    protected String getTableName() {
        return "Medecin";
    }

    @Override
    protected String getPrimaryKey() {
        return "id_Medecin";
    }

    @Override
    protected Medecin map(ResultSet rs) {
        try {
            Medecin m = new Medecin();
            m.setIdMedecin(rs.getInt("id_Medecin"));
            m.setNom(rs.getString("nomMedecin"));
            m.setPrenom(rs.getString("prenomMedecin"));
            m.setAdresse(rs.getString("adresseMedecin"));
            m.setCodePostal(rs.getString("codePostalMedecin"));
            m.setVille(rs.getString("villeMedecin"));
            m.setTelephone(rs.getString("telephoneMedecin"));
            m.setEmail(rs.getString("mailMedecin"));
            m.setRPPS(rs.getString("rppsMedecin"));
            return m;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur mapping Medecin", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareInsert(Medecin m, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("INSERT INTO Medecin (")
                    .append("nomMedecin, prenomMedecin, adresseMedecin, codePostalMedecin, villeMedecin,")
                    .append("telephoneMedecin, mailMedecin, rppsMedecin)")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNom());
            pst.setString(2, m.getPrenom());
            pst.setString(3, m.getAdresse());
            pst.setString(4, m.getCodePostal());
            pst.setString(5, m.getVille());
            pst.setString(6, m.getTelephone());
            pst.setString(7, m.getEmail());
            pst.setString(8, m.getRPPS());

            LogUtils.debug(logger, "PreparedStatement insert Medecin prêt pour : " + m);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareInsert Medecin", e);
            return null;
        }
    }

    @Override
    protected PreparedStatement prepareUpdate(Medecin m, Connection conn) {
        try {
            String sql = new SqlBuilder()
                    .append("UPDATE Medecin SET")
                    .append("nomMedecin=?, prenomMedecin=?, adresseMedecin=?, codePostalMedecin=?, villeMedecin=?,")
                    .append("telephoneMedecin=?, mailMedecin=?, rppsMedecin=?")
                    .append("WHERE id_Medecin=?")
                    .toString();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, m.getNom());
            pst.setString(2, m.getPrenom());
            pst.setString(3, m.getAdresse());
            pst.setString(4, m.getCodePostal());
            pst.setString(5, m.getVille());
            pst.setString(6, m.getTelephone());
            pst.setString(7, m.getEmail());
            pst.setString(8, m.getRPPS());
            pst.setInt(9, m.getIdMedecin());

            LogUtils.debug(logger, "PreparedStatement update Medecin prêt pour : " + m);
            return pst;
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur prepareUpdate Medecin", e);
            return null;
        }
    }

    public Medecin findById(int idMedecin) {
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medecin WHERE id_Medecin=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idMedecin);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findById Medecin id=" + idMedecin, e);
        }
        return null;
    }

    public List<Medecin> findByNom(String nom) {
        List<Medecin> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medecin WHERE nomMedecin LIKE ?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Medecin m = map(rs);
                    if (m != null) list.add(m);
                }
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findByNom Medecin nom=" + nom, e);
        }
        return list;
    }

//    public List<Medecin> findByRPPS(String rpps) {
//        List<Medecin> list = new ArrayList<>();
//        String sql = new SqlBuilder()
//                .append("SELECT * FROM Medecin WHERE rppsMedecin LIKE ?")
//                .toString();
//
//        try (Connection conn = DCSingletonHikaricp.getConnection();
//             PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setString(1, "%" + rpps + "%");
//            try (ResultSet rs = pst.executeQuery()) {
//                while (rs.next()) {
//                    Medecin m = map(rs);
//                    if (m != null) list.add(m);
//                }
//            }
//        } catch (Exception e) {
//            LogUtils.error(logger, "Erreur findByRPPS Medecin rpps=" + rpps, e);
//        }
//        return list;
//    }

    public Medecin findByRPPS(String rpps) {
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medecin WHERE rppsMedecin=?")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rpps);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findOneByRPPS Medecin rpps=" + rpps, e);
        }
        return null;
    }

    public List<Medecin> findAll() {
        List<Medecin> list = new ArrayList<>();
        String sql = new SqlBuilder()
                .append("SELECT * FROM Medecin")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Medecin m = map(rs);
                if (m != null) list.add(m);
            }
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur findAll Medecin", e);
        }
        return list;
    }

    public int countMedecins() {
        String sql = new SqlBuilder()
                .append("SELECT COUNT(*) AS total FROM Medecin")
                .toString();

        try (Connection conn = DCSingletonHikaricp.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            LogUtils.error(logger, "Erreur countMedecins", e);
        }
        return 0;
    }
}
