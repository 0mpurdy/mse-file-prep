package mse.common.log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mj_pu_000 on 28/09/2015.
 */
public class Logger implements ILogger {

    public Logger(LogLevel logLevel, String logFilePath) {
    }

    @Override
    public synchronized void log(LogLevel logLevel, String message) {

    }

    @Override
    public synchronized void log(LogRow logRow) {

    }

    @Override
    public void closeLog() {

    }

    @Override
    public void logException(Exception e) {
        System.out.println("ERROR " + e.getMessage());
    }

}
