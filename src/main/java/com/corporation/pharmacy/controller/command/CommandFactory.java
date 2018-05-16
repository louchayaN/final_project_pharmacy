package com.corporation.pharmacy.controller.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.exception.CommandsXmlParserException;

/**
 * A factory for creating Commands for processing the request and forming the
 * response.
 */
public class CommandFactory {

    private static final Logger LOGGER = LogManager.getLogger(CommandFactory.class);

    /** The single instance for use. */
    private static final CommandFactory INSTANCE = new CommandFactory();

    /**
     * Key - the name of the Command representing URL the client sent when it made a
     * request, value - Command itself
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

    /** The default url-pattern of the command. */
    private static final String DEFAULT_COMMAND_NAME = "/unknownCommand";

    /**
     * Prevents instantiation of CommandFactory for using single instance.
     */
    private CommandFactory() {
    }

    /**
     * Gets the single instance of CommandFactory.
     */
    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the Command associated with the URL the client sent when it made
     * specified request in according with the path info from the Map of
     * {@link #COMMANDS}. If the command with this name hasn't been found (is
     * {@code null}) returns default Command.
     *
     * @param request
     *            the request
     * @return the Command associated with the URL the client sent when it made
     *         specified request. Or default Command if it wasn't found
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
