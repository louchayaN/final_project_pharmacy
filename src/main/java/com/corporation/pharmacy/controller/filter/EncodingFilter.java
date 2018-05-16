package com.corporation.pharmacy.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The encoding filter. Sets encoding to request and response
 */
public class EncodingFilter implements Filter {

    /** The encoding by default */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /** The name of the context-wide initialization parameter. */
    private static final String CONTEXT_PARAM_NAME = "encoding";

    /** The final name of character encoding that is set to request and response. */
    private String encodingName;

    /**
     * Gets the value of context-wide initialization parameter keeps the name of
     * character encoding or takes the default name if it's not defined in context
     * parameters.
     * 
     * @see {@link Filter#init}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encodingName = filterConfig.getServletContext().getInitParameter(CONTEXT_PARAM_NAME);
        if (encodingName == null) {
            encodingName = DEFAULT_ENCODING;
        }
    }

    /**
     * Sets character encoding to the request and response.
     * 
     * @see {@link Filter#doFilter}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding(encodingName);
        response.setCharacterEncoding(encodingName);
        chain.doFilter(request, response);
    }

}
