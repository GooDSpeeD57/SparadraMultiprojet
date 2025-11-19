package training.afpa.cda24060.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    public static void premierCo() {
        String BDD = "sparadra";
        String url = "jdbc:mysql://localhost:3306/" + BDD;
        String user = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            LOGGER.log(Level.INFO, "Connexion à la base de données réussie.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver JDBC non trouvé.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la connexion à la base de données.", e);
        }
    }
}