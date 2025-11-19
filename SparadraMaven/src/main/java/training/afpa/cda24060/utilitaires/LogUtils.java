package training.afpa.cda24060.utilitaires;

import org.slf4j.Logger;

public class LogUtils {

    public static void trace(Logger logger, String message) {
        logger.trace(message);
    }

    public static void debug(Logger logger, String message) {
        logger.debug(message);
    }

    public static void info(Logger logger, String message) {
        logger.info(message);
    }

    public static void warn(Logger logger, String message) {
        logger.warn(message);
    }

    public static void error(Logger logger, String message, Exception e) {
        logger.error(message, e);
    }
}