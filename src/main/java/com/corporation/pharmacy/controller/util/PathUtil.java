package com.corporation.pharmacy.controller.util;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;

/**
 * Defines static methods for working with the path to the view page.
 */
public class PathUtil {

    private static final Logger LOGGER = LogManager.getLogger(Command.class);

    /**
     * The name of HTTP request header that keeps information about page from which
     * request has come
     */
    private static final String REFERER_HEADER_NAME = "referer";

    /**
     * Not instantiate utility class.
     */
    private PathUtil() {
        throw new AssertionError("Class contains static methods only. You should not instantiate it!");
    }

    /**
     * Gets the path of referer page (identifies the address of the webpage (i.e.
     * the URI or IRI) that linked to the resource being requested).
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @return the address of the webpage that linked to the resource being
     *         requested
     */
    public static String getRefererPage(HttpServletRequest request) {
        String refererPage = request.getHeader(REFERER_HEADER_NAME);
        return refererPage;
    }

    /**
     * Adds the parameter to URI.
     *
     * @param uri
     *            String that represents URI
     * @param paramName
     *            the parameter name
     * @param paramValue
     *            the parameter value
     * @return the URI with added parameter
     */
    public static URI addParameterToUri(String uri, String paramName, String paramValue) {
        try {
            URI uriWithParam = new URIBuilder(uri).setParameter(paramName, paramValue).build();
            return uriWithParam;
        } catch (URISyntaxException e) {
            LOGGER.error("Exception during parsing uri string.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the parameter to the referer page.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param paramName
     *            the parameter name
     * @param paramValue
     *            the parameter value
     * @return the address of the webpage (URI) that linked to the resource being
     *         requested with added parameter
     */
    public static URI addParametertoRefererPage(HttpServletRequest request, String paramName, String paramValue) {
        URI uriWithParam = addParameterToUri(getRefererPage(request), paramName, paramValue);
        return uriWithParam;
    }

}
