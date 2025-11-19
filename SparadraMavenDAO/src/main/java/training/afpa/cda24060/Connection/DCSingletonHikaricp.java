package training.afpa.cda24060.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DCSingletonHikaricp {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnectionSingleton.class.getName());
    private static final String cheminconf = "conf.properties";

    private static final Properties props = new Properties();
    private static HikariDataSource dataSource; // plus de Connection unique !

    private DCSingletonHikaricp() {
        // Constructeur privé
    }

    /**
     * Initialise le pool HikariCP si nécessaire.
     */
    private static void initDataSource() {
        if (dataSource != null) return;

        try (InputStream is = DatabaseConnectionSingleton.class.getClassLoader().getResourceAsStream(cheminconf)) {
            if (is == null) {
                throw new RuntimeException("Fichier " + cheminconf + " introuvable !");
            }

            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.login"));
            config.setPassword(props.getProperty("jdbc.password"));
            config.setDriverClassName(props.getProperty("jdbc.driver.class"));

            // Options recommandées
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(60000);
            config.setConnectionTimeout(30000);
            config.setPoolName("AFPA-HikariPool");

            dataSource = new HikariDataSource(config);

            LOGGER.log(Level.INFO, "Pool HikariCP initialisé avec succès.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier properties", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne une connexion provenant du pool.
     */
    public static Connection getInstanceDB() {
        if (dataSource == null) {
            initDataSource();
        }

        try {
            Connection conn = dataSource.getConnection();
            LOGGER.log(Level.FINE, "Connexion obtenue depuis le pool.");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Impossible d'obtenir une connexion depuis HikariCP", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Ferme le pool (à appeler uniquement à l'arrêt de l'application).
     */
    public static void closeInstanceDB() {
        if (dataSource != null) {
            dataSource.close();
            LOGGER.log(Level.INFO, "Pool HikariCP fermé.");
            dataSource = null;
        }
    }
}
