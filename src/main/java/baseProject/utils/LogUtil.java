package baseProject.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
	private static Logger logger = LogManager.getLogger(LogUtil.class);
	private static Logger exceptionLogger = LogManager.getLogger(LogUtil.class);

	public static Logger getLogger() {
		return logger;
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, Exception e) {
		logger.info(msg, e);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void warn(String msg, Exception e) {
		logger.warn(msg, e);
	}

	public static void error(String msg) {
		exceptionLogger.error(msg);
	}

	public static void error(String msg, Exception e) {
		exceptionLogger.error(msg, e);
	}

}
