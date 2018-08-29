package com.fratics.precis.util;

import com.fratics.precis.fis.base.PrecisBase;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger extends PrecisBase {
    private static Logger logger = null;
    private LogLevel currentLogLevel;

    private Logger() {
        currentLogLevel = LogLevel.INFO;
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public boolean initialize() {
        if (PrecisConfigProperties.LOGGING_ENABLED) {
            if (PrecisConfigProperties.LOGGING_LEVEL.equalsIgnoreCase("DEBUG"))
                currentLogLevel = LogLevel.DEBUG;
            else if (PrecisConfigProperties.LOGGING_LEVEL.equalsIgnoreCase("INFO"))
                currentLogLevel = LogLevel.INFO;
            else if (PrecisConfigProperties.LOGGING_LEVEL.equalsIgnoreCase("WARN"))
                currentLogLevel = LogLevel.WARN;
            else if (PrecisConfigProperties.LOGGING_LEVEL.equalsIgnoreCase("ERROR"))
                currentLogLevel = LogLevel.ERROR;
        }
        return true;
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public void debug(String s) {
        if (PrecisConfigProperties.LOGGING_ENABLED && currentLogLevel.ordinal() <= LogLevel.DEBUG.ordinal())
            System.out.println(getDate() + " - DEBUG - " + s);
    }

    public void info(String s) {
        if (PrecisConfigProperties.LOGGING_ENABLED && currentLogLevel.ordinal() <= LogLevel.INFO.ordinal())
            System.out.println(getDate() + " - INFO - " + s);
    }

    public void warn(String s) {
        if (PrecisConfigProperties.LOGGING_ENABLED && currentLogLevel.ordinal() <= LogLevel.WARN.ordinal())
            System.out.println(getDate() + " - WARN - " + s);
    }

    public void error(String s) {
        if (PrecisConfigProperties.LOGGING_ENABLED && currentLogLevel.ordinal() <= LogLevel.ERROR.ordinal())
            System.out.println(getDate() + " - ERROR - " + s);
    }

    public enum LogLevel {DEBUG, INFO, WARN, ERROR}
}
