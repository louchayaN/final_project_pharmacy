package com.corporation.pharmacy.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.entity.Role;

/**
 * The main access filter. Defines by user's role the possibility to get access
 * to doctor's and pharmacist's operations.
 */
public class AccessFilter implements Filter {

    /**
     * Gets the user's role from the session. If the role is not defined
     * ({@code null}) or {@code CUSTOMER} sends redirect to web-app home page. If
     * the role is PHARMACIST or DOCTOR calls the next filter in the chain.
     * 
     * @throws IOException
     *             if an I/O related error has occurred during the processing
     * @throws ServletException
     *             <li>if an exception occurs that interferes with the filter's
     *             normal operation</li> or
     *             <li>if {@link HttpServletResponse#sendRedirect} throws this
     *             exception</li>
     * 
     * @see {@link Filter#doFilter}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        Role role = (Role) session.getAttribute(SessionAttribute.ROLE);
        if (role == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + ViewPath.REDIRECT_HOME_PAGE);
            return;
        }

        switch (role) {
        case PHARMACIST:
            chain.doFilter(request, response);
            break;
        case DOCTOR:
            chain.doFilter(request, response);
            break;
        case CUSTOMER:
            httpResponse.sendRedirect(httpRequest.getContextPath() + ViewPath.REDIRECT_HOME_PAGE);
            break;
        }
    }

}
