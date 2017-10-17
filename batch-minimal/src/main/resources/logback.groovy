def LOG_PATH="logs"


appender("Console-Appender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%C{15} - %msg%n"
    }
}

appender("File-Appender", FileAppender) {
    file = "${LOG_PATH}/logfile.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%msg%n"
        outputPatternAsHeader = true
    }
}


logger("net.sinou",DEBUG,["File-Appender", "Console-Appender"])
logger("org.springframework",INFO,["File-Appender", "Console-Appender"])

// root(INFO,["Console-Appender"])
