package training.afpa.cda24060.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectionSingleton {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnectionSingleton.class.getName());
    private static final String cheminconf = "conf.properties";
    private static final Properties props = new Properties();
    private static Connection connection;

    private DatabaseConnectionSingleton() {
        // Constructeur privé pour le singleton
    }

    public static synchronized Connection getInstanceDB() {
        if (connection == null) {
            try (InputStream is = DatabaseConnectionSingleton.class.getClassLoader().getResourceAsStream(cheminconf)) {
                if (is == null) {
                    throw new RuntimeException("Fichier " + cheminconf + " introuvable !");
                }
                props.load(is);
                Class.forName(props.getProperty("jdbc.driver.class"));
                String url = props.getProperty("jdbc.url");
                String login = props.getProperty("jdbc.login");
                String password = props.getProperty("jdbc.password");
                connection = DriverManager.getConnection(url, login, password);
                LOGGER.log(Level.INFO, "Connexion établie avec succès : {0}", connection);

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier properties", e);
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Driver JDBC introuvable", e);
                throw new RuntimeException(e);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la connexion à la base", e);
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (connection.isClosed()) {
                    connection = null;
                    return getInstanceDB(); // relancer la création
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'état de la connexion", e);
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static void closeInstanceDB() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.log(Level.INFO, "Connexion fermée avec succès");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", e);
            } finally {
                connection = null;
            }
        }
    }
}