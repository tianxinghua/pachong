package com.shangpin.ep.order.util.logger;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class LoggerUtil {

	private final Logger logger;

	private static ResourceBundle bdl = null;
	private static String project = "";
	private static String module = "";
	private static String ip="";

	static {
		try {
			if (null == bdl) {
                bdl = ResourceBundle.getBundle("logger");
            }
			project = bdl.getString("logger.project");
			module = bdl.getString("logger.module");
			ip = bdl.getString("logger.ip");
			if("".equals(ip)){
				ip = getLocalIPForJava();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private LoggerUtil(Class<?> clazz) {
		logger = Logger.getLogger(clazz);
	}

	private   LoggerUtil(String name) {
		logger = LogManager.getLogger(name);
	}

	private LoggerUtil() {
		logger = Logger.getRootLogger();
	}

	public static LoggerUtil getLogger(Class<?> clazz) {
		return new LoggerUtil(clazz);
	}

	public static LoggerUtil getLogger(String name) {
		return new LoggerUtil(name);
	}

	public static LoggerUtil getRootLogger() {
		return new LoggerUtil();
	}

	public void trace(Object message) {
		if (logger.isTraceEnabled()) {
			forcedLog(logger, Level.TRACE, this.getLoggerMessage("TRACE",message));
		}
	}

	public void trace(Object message, Throwable t) {
		if (logger.isTraceEnabled()) {
			forcedLog(logger, Level.TRACE, this.getLoggerMessage("TRACE",message), t);
		}
	}

	public void trace(String pattern, Object... arguments) {
		if (logger.isTraceEnabled()) {
			forcedLog(logger, Level.TRACE, this.getLoggerMessage("TRACE",format(pattern, arguments)));
		}
	}
	public void trace(String pattern, Throwable t, Object... arguments) {
		if (logger.isTraceEnabled()) {
			forcedLog(logger, Level.TRACE, this.getLoggerMessage("TRACE",format(pattern, arguments)), t);
		}
	}

	public void debug(Object message) {
		if (logger.isDebugEnabled()) {
			forcedLog(logger, Level.DEBUG, this.getLoggerMessage("DEBUG",message));
		}
	}

	public void debug(Object message, Throwable t) {
		if (logger.isDebugEnabled()) {
			forcedLog(logger, Level.DEBUG, this.getLoggerMessage("DEBUG",message), t);
		}
	}

	public void debug(String pattern, Object... arguments) {
		if (logger.isDebugEnabled()) {
			forcedLog(logger, Level.DEBUG, this.getLoggerMessage("DEBUG",format(pattern, arguments)));
		}
	}
	public void debug(String pattern, Throwable t, Object... arguments) {
		if (logger.isDebugEnabled()) {
			forcedLog(logger, Level.DEBUG, this.getLoggerMessage("DEBUG",format(pattern, arguments)), t);
		}
	}

	public void info(Object message) {
		if (logger.isInfoEnabled()) {

			forcedLog(logger, Level.INFO, this.getLoggerMessage("INFO",message));
		}
	}

	public void info(Object message, Throwable t) {
		if (logger.isInfoEnabled()) {
			forcedLog(logger, Level.INFO, this.getLoggerMessage("INFO",message), t);
		}
	}

	public void info(String pattern, Object... arguments) {
		if (logger.isInfoEnabled()) {
			forcedLog(logger, Level.INFO, this.getLoggerMessage("INFO",format(pattern, arguments)));
		}
	}
	public void info(String pattern, Throwable t, Object... arguments) {
		if (logger.isInfoEnabled()) {
			forcedLog(logger, Level.INFO, this.getLoggerMessage("INFO",format(pattern, arguments)), t);
		}
	}
	public void warn(Object message) {
		if (logger.isEnabledFor(Level.WARN)) {
			forcedLog(logger, Level.WARN, this.getLoggerMessage("WARN",message));
		}
	}

	public void warn(Object message, Throwable t) {
		if (logger.isEnabledFor(Level.WARN)) {
			forcedLog(logger, Level.WARN, this.getLoggerMessage("WARN",message), t);
		}
	}

	public void warn(String pattern, Object... arguments) {
		if (logger.isEnabledFor(Level.WARN)) {
			forcedLog(logger, Level.WARN, this.getLoggerMessage("WARN",format(pattern, arguments)));
		}
	}
	public void warn(String pattern, Throwable t, Object... arguments) {
		if (logger.isEnabledFor(Level.WARN)) {
			forcedLog(logger, Level.WARN, this.getLoggerMessage("WARN",format(pattern, arguments)), t);
		}
	}

	public void error(Object message) {
		if (logger.isEnabledFor(Level.ERROR)) {
			forcedLog(logger, Level.ERROR, this.getLoggerMessage("ERROR",message));
		}
	}

	public void error(Object message, Throwable t) {
		if (logger.isEnabledFor(Level.ERROR)) {
			forcedLog(logger, Level.ERROR, this.getLoggerMessage("ERROR",message), t);
		}
	}

	public void error(String pattern, Object... arguments) {
		if (logger.isEnabledFor(Level.ERROR)) {
			forcedLog(logger, Level.ERROR, this.getLoggerMessage("ERROR",format(pattern, arguments)));
		}
	}
	public void error(String pattern, Throwable t, Object... arguments) {
		if (logger.isEnabledFor(Level.ERROR)) {
			forcedLog(logger, Level.ERROR, this.getLoggerMessage("ERROR",format(pattern, arguments)), t);
		}
	}

	public void fatal(Object message) {
		if (logger.isEnabledFor(Level.FATAL)) {
			forcedLog(logger, Level.FATAL, this.getLoggerMessage("FATAL",message));
		}
	}

	public void fatal(Object message, Throwable t) {
		if (logger.isEnabledFor(Level.FATAL)) {
			forcedLog(logger, Level.FATAL, this.getLoggerMessage("FATAL",message), t);
		}
	}

	public void fatal(String pattern, Object... arguments) {
		if (logger.isEnabledFor(Level.FATAL)) {
			forcedLog(logger, Level.FATAL, this.getLoggerMessage("FATAL",format(pattern, arguments)));
		}
	}
	public void fatal(String pattern, Throwable t, Object... arguments) {
		if (logger.isEnabledFor(Level.FATAL)) {
			forcedLog(logger, Level.FATAL, this.getLoggerMessage("FATAL",format(pattern, arguments)), t);
		}
	}

	public void assertLog(boolean assertion, String message) {
		if (!assertion) {
			forcedLog(logger, Level.ERROR, message);
		}
	}

	private static void forcedLog(Logger logger, Level level, Object message) {
		logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, null));
	}

	private static void forcedLog(Logger logger, Level level, Object message, Throwable t) {
		logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, t));
	}

	private static String format(String pattern, Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	private static final String FQCN;

	static {
		FQCN = LoggerUtil.class.getName();
	}

	private LoggerMessage getLoggerMessage(String level, Object message){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LoggerMessage loggerMessage = new LoggerMessage();
		loggerMessage.setProject(project);
		loggerMessage.setModule(module);
		loggerMessage.setLevel(level);
		loggerMessage.setMessage(message.toString());
		loggerMessage.setDateTime(sdf.format(new Date()));
		this.getClassMsg(loggerMessage);
		loggerMessage.setIp(ip);
		return loggerMessage;
	}


	private void getClassMsg(LoggerMessage loggerMessage){
		String location="";
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		location = "类名："+stacks[4].getClassName() + "\n函数名：" + stacks[4].getMethodName()
				+ "\n文件名：" + stacks[4].getFileName() + "\n行号："
				+ stacks[4].getLineNumber() + "";
//		 System.out.println("location = " + location);

		loggerMessage.setFunction(stacks[4].getClassName()+"."+ stacks[4].getMethodName()+"--"+stacks[4].getLineNumber()+"行");
	}

    //获取本地ip
	private static String getLocalIPForJava(){
		StringBuilder sb = new StringBuilder();
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						sb.append(inetAddress.getHostAddress().toString()+"-");
					}
				}
			}
		} catch (SocketException e) { }
		return sb.toString();
	}

}