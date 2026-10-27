package com.fintech.backend.infrastructure.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.coreConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class LoggingConfig {

    @Value("${spring.application.name:backend}")
    private String applicationName;

    @Value("${logging.file.path:${LOG_PATH:-logs}}")
    private String logFilePath;

    @Value("${logging.file.name:${LOG_FILE:-application.log}}")
    private String logFileName;

    @Value("${logging.pattern.console:%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n}")
    private String consolePattern;

    @Value("${logging.pattern.file:%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n}")
    private String filePattern;

    @Value("${logging.level.root:INFO}")
    private String rootLevel;

    @Value("${logging.level.com.fintech.backend:DEBUG}")
    private String applicationLevel;

    @Bean
    public LoggerContext loggerContext() {
        LoggerContext context = new LoggerContext();
        context.setName("fintech-logger-context");
        return context;
    }

    @Bean
    public Appender<ILoggingEvent> consoleAppender(LoggerContext loggerContext) {
        PatternLayout consoleLayout = new PatternLayout();
        consoleLayout.setContext(loggerContext);
        consoleLayout.setPattern(consolePattern);
        consoleLayout.start();

        LayoutWrappingEncoder<ILoggingEvent> consoleEncoder = new LayoutWrappingEncoder<>();
        consoleEncoder.setContext(loggerContext);
        consoleEncoder.setLayout(consoleLayout);

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setName("CONSOLE_APPENDER");
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.start();

        return consoleAppender;
    }

    @Bean
    public Appender<ILoggingEvent> fileAppender(LoggerContext loggerContext) {
        File logDirectory = new File(logFilePath);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

        String fullLogPath = logFilePath + File.separator + logFileName;

        PatternLayout fileLayout = new PatternLayout();
        fileLayout.setContext(loggerContext);
        fileLayout.setPattern(filePattern);
        fileLayout.start();

        LayoutWrappingEncoder<ILoggingEvent> fileEncoder = new LayoutWrappingEncoder<>();
        fileEncoder.setContext(loggerContext);
        fileEncoder.setLayout(fileLayout);

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setName("FILE_APPENDER");
        fileAppender.setContext(loggerContext);
        fileAppender.setEncoder(fileEncoder);
        fileAppender.setFile(fullLogPath);
        fileAppender.setAppend(true);
        fileAppender.setPrudent(false);
        fileAppender.start();

        return fileAppender;
    }

    @Bean
    public Logger rootLogger(LoggerContext loggerContext, Appender<ILoggingEvent> consoleAppender) {
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.valueOf(rootLevel));
        rootLogger.addAppender(consoleAppender);
        return rootLogger;
    }

    @Bean
    public Logger applicationLogger(
            LoggerContext loggerContext,
            Appender<ILoggingEvent> consoleAppender,
            Appender<ILoggingEvent> fileAppender) {
        Logger appLogger = loggerContext.getLogger("com.fintech.backend");
        appLogger.setLevel(Level.valueOf(applicationLevel));
        appLogger.addAppender(consoleAppender);
        appLogger.addAppender(fileAppender);
        appLogger.setAdditive(false);
        return appLogger;
    }

    @Bean
    public Logger performanceLogger(LoggerContext loggerContext, Appender<ILoggingEvent> fileAppender) {
        Logger perfLogger = loggerContext.getLogger("com.fintech.backend.performance");
        perfLogger.setLevel(Level.INFO);
        perfLogger.addAppender(fileAppender);
        perfLogger.setAdditive(false);
        return perfLogger;
    }

    @Bean
    public Logger securityLogger(LoggerContext loggerContext, Appender<ILoggingEvent> fileAppender) {
        Logger secLogger = loggerContext.getLogger("com.fintech.backend.security");
        secLogger.setLevel(Level.INFO);
        secLogger.addAppender(fileAppender);
        secLogger.setAdditive(false);
        return secLogger;
    }
}