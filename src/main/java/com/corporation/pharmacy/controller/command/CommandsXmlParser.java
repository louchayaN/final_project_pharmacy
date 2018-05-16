package com.corporation.pharmacy.controller.command;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.corporation.pharmacy.controller.command.exception.CommandsXmlParserException;

/**
 * Parses xml file with the Commands using DOM Parser.
 */
public class CommandsXmlParser {

    /** The path to xml file with the Commands */
    private static final String XML_FILE_NAME = "/commands.xml";

    /** Tag names in the xml file with the Commands */
    private static final String COMMAND_XML_TAG = "command";
    private static final String NAME_XML_TAG = "name";
    private static final String CLASS_XML_TAG = "class";
    private static final String CONSTRUCTOR_PARAM_TYPE_XML_TAG = "constructor-param-type";
    private static final String CONSTRUCTOR_PARAM_VALUE_XML_TAG = "constructor-param-value";

    /**
     * Parses xml file with the Commands using DOM Parser. Returns the Map of the
     * commands where key is the command name representing URL the client sent when
     * he made a request, value is a Command itself.
     *
     * @return the Map of the commands where key is the command name representing
     *         URL the client sent when he made a request, value is a Command
     *         itself.
     * @throws CommandsXmlParserException
     *             if any exception happens during parsing xml file
     */
    public Map<String, Command> getCommands() throws CommandsXmlParserException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(XML_FILE_NAME);
            Document document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            Map<String, Command> commands = new HashMap<>();
            NodeList commandNodes = root.getElementsByTagName(COMMAND_XML_TAG);
            for (int i = 0; i < commandNodes.getLength(); i++) {
                Element commandElement = (Element) commandNodes.item(i);
                String commandName = getSingleChildContent(commandElement, NAME_XML_TAG);
                Command command = formCommand(commandElement);
                commands.put(commandName, command);
            }
            return commands;

        } catch (ParserConfigurationException | SAXException | IOException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | ClassNotFoundException e) {
            throw new CommandsXmlParserException("Exception during reading and interpretated XML file with commands.", e);
        }
    }

    /**
     * Returns the text content of the single child Element found by the specified
     * name in this Element. If the child Element hasn't been found returns null.
     *
     * @param element
     *            the Element where the child tag is looking for
     * @param childName
     *            the name of the child xml tag that is looking for
     * @return the text content of the single child Element or null if it wasn't
     *         found
     */
    private String getSingleChildContent(Element element, String childName) {
        Element child = getSingleChild(element, childName);
        String childContent = (child == null) ? null : child.getTextContent().trim();
        return childContent;
    }

    /**
     * Returns the single child Element found by the specified name in this Element.
     * If the child Element with this name hasn't been found returns null.
     *
     * @param element
     *            the Element where the child tag is looking for
     * @param childName
     *            the name of the child xml tag that is looking for
     * @return the found single child Element or null if it wasn't found
     */
    private Element getSingleChild(Element element, String childName) {
        NodeList nodeList = element.getElementsByTagName(childName);
        Element child = (Element) nodeList.item(0);
        return child;
    }

    /**
     * Instantiates a new Command using the specified {@code commandElement}. As
     * information about a Command keeps in the xml file reflection is used for
     * instantiation. If the type of Command constructor is not defined in the xml
     * file instantiation of a command with default constructor takes place.
     * Otherwise instantiation happens in accordance with constructor parameter
     * types and their value.
     *
     * @param commandElement
     *            the Element formed from the xml file that represents Command
     * @return the Command instance
     * @throws InstantiationException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalAccessException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalArgumentException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws InvocationTargetException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws NoSuchMethodException
     *             if the constructor in the specified class is not found
     * @throws SecurityException
     *             he security exception during getting the constructor
     * @throws ClassNotFoundException
     *             if the class of the constructor cannot be located
     */
    private Command formCommand(Element commandElement)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, ClassNotFoundException {

        String commandClassName = getSingleChildContent(commandElement, CLASS_XML_TAG);
        Class<?> commandClass = Class.forName(commandClassName);

        String constructorParamType = getSingleChildContent(commandElement, CONSTRUCTOR_PARAM_TYPE_XML_TAG);
        String constructorParamValue = getSingleChildContent(commandElement, CONSTRUCTOR_PARAM_VALUE_XML_TAG);
        Command command;
        if (constructorParamType == null) {
            command = instantiateCommandWithDefaultConstructor(commandClass);
        } else {
            command = instantiateCommandWithDefinedConstructor(commandClass, constructorParamType, constructorParamValue);
        }
        return command;
    }

    /**
     * Creates a new instance of specified Command class using a default
     * constructor.
     *
     * @param commandClass
     *            the class of a Command
     * @return the instance of the Command class created with the default
     *         constructor
     * @throws InstantiationException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalAccessException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalArgumentException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws InvocationTargetException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws NoSuchMethodException
     *             if the constructor in the specified class is not found
     * @throws SecurityException
     *             the security exception during getting the constructor
     */
    private Command instantiateCommandWithDefaultConstructor(Class<?> commandClass)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
        return command;
    }

    /**
     * Creates and initializes a new instance of specified Command class using the
     * constructor with specified parameter type and value.
     *
     * @param commandClass
     *            the class of a Command
     * @param constructorParamType
     *            the constructor parameter type
     * @param constructorParamValue
     *            the constructor parameter value
     * @return the instance of the Command class initialized with the specified
     *         constructor
     * @throws ClassNotFoundException
     *             if the class of the constructor cannot be located
     * @throws NoSuchMethodException
     *             if the constructor in the specified class is not found
     * @throws SecurityException
     *             the security exception during getting the constructor
     * @throws InstantiationException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalAccessException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws IllegalArgumentException
     *             if {@link Constructor#newInstance} throws this exception
     * @throws InvocationTargetException
     *             if {@link Constructor#newInstance} throws this exception
     */
    private Command instantiateCommandWithDefinedConstructor(Class<?> commandClass, String constructorParamType,
            String constructorParamValue) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class<?> paramClass = Class.forName(constructorParamType);
        Constructor<?> constructor = commandClass.getDeclaredConstructor(paramClass);
        Command command;
        if (paramClass.isEnum()) {
            command = (Command) constructor.newInstance(getEnumConstant(paramClass, constructorParamValue));
        } else {
            command = (Command) constructor.newInstance(constructorParamValue);
        }
        return command;
    }

    /**
     * Returns the enum constant of the specified enum class with the specified
     * name. If the constant with specified name haven't been found returns null.
     *
     * @param enumClass
     *            the enum class
     * @param enumConstantName
     *            the enum constant name
     * @return the enum constant of the specified enum class with the specified name
     *         or null if it hasn't been found
     * 
     */
    private Enum<?> getEnumConstant(Class<?> enumClass, String enumConstantName) {
        for (Object obj : enumClass.getEnumConstants()) {
            Enum<?> enumConstant = (Enum<?>) obj;
            if (enumConstantName.equals(enumConstant.name())) {
                return enumConstant;
            }
        }
        return null;
    }

}
