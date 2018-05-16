package com.corporation.pharmacy.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter for sending static resources (css files, images and so on) to default
 * filter, not to the
 * {@link com.corporation.pharmacy.controller.FrontController}. This filter
 * should be the last filter in th chain of filters.
 */
public class StaticResourcesFilter implements Filter {

    /**
     * Devides static and non-static resources. If static - calls another filter in
     * chain and resource is handled at he end by default servet. If resource is not
     * static - continues operation.
     * 
     * @throws IOException
     *             <li>if an I/O related error has occurred during the
     *             processing</li> or
     *             <li>if if {@link RequestDispatcher#forward} throws this exception
     *             throws this exception</li>
     * @throws ServletException
     *             <li>if an exception occurs that interferes with the filter's
     *             normal operation</li> or
     *             <li>if if {@link RequestDispatcher#forward} throws this exception
     *             throws this exception</li>
     * 
     * @see {@link Filter#doFilter}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (path != null && path.contains("/static")) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/show" + path).forward(request, response);
        }
    }

}
