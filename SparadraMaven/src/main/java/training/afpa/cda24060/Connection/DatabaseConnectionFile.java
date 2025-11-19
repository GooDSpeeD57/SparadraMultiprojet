package training.afpa.cda24060.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectionFile {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnectionFile.class.getName());

    public static Connection connecfile() {
        Connection connection = null;
        final String pathconfig = "conf.properties";
        Properties prop = new Properties();

        try (InputStream is = DatabaseConnectionFile.class.getClassLoader().getResourceAsStream(pathconfig)) {
            if (is == null) {
                throw new RuntimeException("Fichier " + pathconfig + " introuvable !");
            }
            prop.load(is);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier properties", e);
            throw new RuntimeException(e);
        }

        try {
            // Chargement du driver
            Class.forName(prop.getProperty("jdbc.driver.class"));

            // Création de la connexion
            String url = prop.getProperty("jdbc.url");
            String user = prop.getProperty("jdbc.login");
            String password = prop.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, user, password);

            LOGGER.log(Level.INFO, "Connexion réussie via fichier config Properties : {0}", connection);

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Erreur chargement du Driver JDBC", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la connexion à la base de données", e);
        }

        return connection;
    }

    public static void close_BDD(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.log(Level.INFO, "Connexion fermée avec succès");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", e);
            }
        }
    }
}
