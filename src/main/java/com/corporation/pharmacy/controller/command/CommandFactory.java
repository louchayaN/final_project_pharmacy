package com.corporation.pharmacy.controller.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.exception.CommandsXmlParserException;

/**
 * A factory for creating Commands that process the request and form the
 * response.
 */
public class CommandFactory {

    private static final Logger LOGGER = LogManager.getLogger(CommandFactory.class);

    private static final CommandFactory INSTANCE = new CommandFactory();

    /**
     * Key - the name of the Command representing part of URL the client sent when
     * he made a request, value - Command itself
     */
    private static Map<String, Command> COMMANDS;

    /**
     * Instantiates all commands from the xml file. If an exception happened during
     * parsing xml file throws RuntimeException.
     */
    static {
        try {
            COMMANDS = new CommandsXmlParser().getCommands();
        } catch (CommandsXmlParserException e) {
            LOGGER.fatal("Fatal error during initializing commands from xml file.", e);
            throw new RuntimeException(e);
        }
    }

    /** The default name of the command. */
    private static final String DEFAULT_COMMAND_NAME = "/unknownCommand";

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the Command associated with the URL the client sent when he made
     * specified request. If the command hasn't been found (is {@code null}) returns
     * default Command.
     *
     * @param request
     *            the request
     * @return the Command associated with the URL the client sent when he made
     *         specified request. Returns default Command if it wasn't found
     */
    public Command getCommand(HttpServletRequest request) {
        String commandName = request.getPathInfo();
        Command command = COMMANDS.get(commandName);
        if (command == null) {
            command = COMMANDS.get(DEFAULT_COMMAND_NAME);
        }
        return command;
    }

}
