package com.corporation.pharmacy.controller.command.exception;

/**
 * Thrown to indicate that exception happened during parsing the xml file with
 * the Commands
 */
public class CommandsXmlParserException extends Exception {

    private static final long serialVersionUID = 406592464655056466L;

    public CommandsXmlParserException() {
    }

    public CommandsXmlParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CommandsXmlParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandsXmlParserException(String message) {
        super(message);
    }

    public CommandsXmlParserException(Throwable cause) {
        super(cause);
    }

}
