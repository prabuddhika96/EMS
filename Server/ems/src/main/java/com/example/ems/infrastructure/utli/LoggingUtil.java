package com.example.ems.infrastructure.utli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoggingUtil {
    @Value("#{'${logging.lists.levels-where-debug-logs-are-displayed}'.split(',')}")
    private List<String> levelsWhereDebugLogsAreDisplayed;

    @Value("${logging.level.com.ems.util}")
    private String currentLogLevel;

    public void info(String logMessage) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String fullClassName = stackTraceElements[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        String methodName = stackTraceElements[2].getMethodName();
        int lineNumber = stackTraceElements[2].getLineNumber();

        try {
            log.info(
                    "Logged     = Source : {}.{}  ::  Line : {}  ::  Logs : {}  ::  Source Path : {}",
                    className,
                    methodName,
                    String.format("%7d", lineNumber),
                    logMessage,
                    fullClassName + "." + methodName
            );
        } catch (Exception e) {
            logError(className, methodName, lineNumber, fullClassName);
        }
    }

    public void debug(String logMessage) {
        if (Boolean.FALSE.equals(levelsWhereDebugLogsAreDisplayed.contains(currentLogLevel))) return;

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String fullClassName = stackTraceElements[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        String methodName = stackTraceElements[2].getMethodName();
        int lineNumber = stackTraceElements[2].getLineNumber();

        try {
            log.debug(
                    "Logged     = Source : {}.{}  ::  Line : {}  ::  Logs : {}  ::  Source Path : {}",
                    className,
                    methodName,
                    String.format("%7d", lineNumber),
                    logMessage,
                    fullClassName + "." + methodName
            );
        } catch (Exception e) {
            logError(className, methodName, lineNumber, fullClassName);
        }
    }

    public void error(String errorLogMessage) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String fullClassName = stackTraceElements[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        String methodName = stackTraceElements[2].getMethodName();
        int lineNumber = stackTraceElements[2].getLineNumber();

        try {
            log.error(
                    "Exception  = Source : {}.{}  ::  Line : {}  ::  Error Logs : {}  ::  Source Path : {}",
                    className,
                    methodName,
                    String.format("%7d", lineNumber),
                    errorLogMessage,
                    fullClassName + "." + methodName
            );
        } catch (Exception e) {
            logError(className, methodName, lineNumber, fullClassName);
        }
    }

    private static void logError(String className, String methodName, int lineNumber, String fullClassName) {
        log.error(
                "Log Error  = Source : {}.{}  ::  Line : {}  ::  Source Path : {}",
                className,
                methodName,
                String.format("%7d", lineNumber),
                fullClassName + "." + methodName
        );
    }
}
