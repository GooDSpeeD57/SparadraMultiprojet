package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;

/**
 * Utilitaire pour centraliser et standardiser les logs.
 */
public class LogUtils {

    /**
     * Logge un message au niveau TRACE.
     */
    public static void trace(Logger logger, String message) {
        logger.trace(message);
    }

    /**
     * Logge un message au niveau DEBUG.
     */
    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Logge un message au niveau INFO.
     */
    public static void info(Logger logger, String message) {
        logger.info(message);
    }

    /**
     * Logge un message au niveau WARN.
     */
    public static void warn(Logger logger, String message) {
        logger.warn(message);
    }

    /**
     * Logge un message au niveau ERROR (sans exception).
     */
    public static void error(Logger logger, String message) {
        logger.error(message);
    }

    /**
     * Logge un message et une exception au niveau ERROR.
     */
    public static void error(Logger logger, String message, Exception e) {
        logger.error(message, e);
    }

    /**
     * Logge un message formaté au niveau ERROR (avec varargs).
     * Exemple : error(logger, "Valeur : {}", valeur);
     */
    public static void error(Logger logger, String format, Object... args) {
        if (args == null) {
            logger.error(format);
        } else {
            logger.error(format, args);
        }
    }
}