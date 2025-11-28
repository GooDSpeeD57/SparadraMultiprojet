package training.afpa.cda24060.connection;

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

    private static final Logger LOGGER = Logger.getLogger(DCSingletonHikaricp.class.getName());
    private static final String CHEMIN_CONF = "conf.properties";

    // Pool HikariCP statique
    private static HikariDataSource dataSource;

    /**
     * Retourne une connexion depuis le pool
     */
    public static Connection getConnection() {
        if (dataSource == null) {
            initDataSource();
        }

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Impossible d'obtenir une connexion", e);
            throw new RuntimeException(e);
        }
    }

    // Initialisation du pool
    private static void initDataSource() {
        try (InputStream is = DCSingletonHikaricp.class.getClassLoader().getResourceAsStream(CHEMIN_CONF)) {
            if (is == null) {
                throw new RuntimeException("Fichier " + CHEMIN_CONF + " introuvable !");
            }

            Properties props = new Properties();
            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.login"));
            config.setPassword(props.getProperty("jdbc.password"));
            config.setDriverClassName(props.getProperty("jdbc.driver.class"));
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
     * Ferme le pool
     */
    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
            LOGGER.log(Level.INFO, "Pool HikariCP fermé.");
        }
    }
}
