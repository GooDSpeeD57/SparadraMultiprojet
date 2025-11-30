package training.afpa.cda24060.ClasseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.afpa.cda24060.exception.SaisieException;
import training.afpa.cda24060.modele.Pharmacien;
import training.afpa.cda24060.utilitaires.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacienDAO extends AbstractDAO<Pharmacien> {

    private static final Logger logger = LoggerFactory.getLogger(PharmacienDAO.class);

    @Override
    public boolean insert(Pharmacien p) throws SQLException {
        String sql = "INSERT INTO Pharmacien (nomPharmacien, prenomPharmacien, rppsPharmacien) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRpps());

            int rows = pst.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next())
                    p.setId(rs.getInt(1));
            }

            LogUtils.debug(logger, "Pharmacien inséré : " + p);
            return true;
        }
    }

    @Override
    public boolean update(Pharmacien p) throws SQLException {
        String sql = "UPDATE Pharmacien SET nomPharmacien=?, prenomPharmacien=?, rppsPharmacien=? WHERE id_Pharmacien=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getRpps());
            pst.setInt(4, p.getId());

            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Pharmacien WHERE id_Pharmacien=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public Pharmacien findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Pharmacien WHERE id_Pharmacien=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setId(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRpps(rs.getString("rppsPharmacien"));
                    return p;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public List<Pharmacien> findAll() throws SQLException {
        String sql = "SELECT * FROM Pharmacien";
        List<Pharmacien> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Pharmacien p = new Pharmacien();
                p.setId(rs.getInt("id_Pharmacien"));
                p.setNom(rs.getString("nomPharmacien"));
                p.setPrenom(rs.getString("prenomPharmacien"));
                p.setRpps(rs.getString("rppsPharmacien"));
                list.add(p);
            }

        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Pharmacien> findByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM Pharmacien WHERE nomPharmacien LIKE ?";
        List<Pharmacien> list = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + nom + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setId(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRpps(rs.getString("rppsPharmacien"));
                    list.add(p);
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }

    public Pharmacien findByRPPS(String rpps) throws SQLException {
        String sql = "SELECT * FROM Pharmacien WHERE rppsPharmacien=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, rpps);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pharmacien p = new Pharmacien();
                    p.setId(rs.getInt("id_Pharmacien"));
                    p.setNom(rs.getString("nomPharmacien"));
                    p.setPrenom(rs.getString("prenomPharmacien"));
                    p.setRpps(rs.getString("rppsPharmacien"));
                    return p;
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
