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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.UserService;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The access filter to operations of placing order and making requests for
 * prescriptions by the user. Denies access to this operations if the user
 * hasn't filled full information about yourself.
 */
public class OrderAndPrescriptionAccessFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(OrderAndPrescriptionAccessFilter.class);

    /** The result message of this Filter execution */
    private static final String FAILED_PARAM_MESSAGE = "?message=fillInfo";

    /**
     * Checks if the user is authenticated (see
     * {@link SessionUtil#isAuthenticatedUser}) and if the user filled full
     * information about yourself (see {@link #isUserFillFullInfo}). If yes,
     * continues making of order or prescription operation. Otherwise stops further
     * operation by sending redirect to the basket view page with failed message.
     * <p>
     * If {@code ServiceException} has happened sends redirect to the error page.
     * </p>
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
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);
        try {
            if ( ! SessionUtil.isAuthenticatedUser(session) || ! isUserFillFullInfo(idUser)) {
                StringBuilder viewPath = new StringBuilder();
                viewPath.append(httpRequest.getContextPath()).append(ViewPath.REDIRECT_BASKET).append(FAILED_PARAM_MESSAGE);
                httpResponse.sendRedirect(viewPath.toString());
            } else {
                chain.doFilter(request, response);
            }
        } catch (ServiceException e) {
            LOGGER.error("Exception during checking of filling full info by the user.", e);
            StringBuilder viewPath = new StringBuilder();
            viewPath.append(httpRequest.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
            httpResponse.sendRedirect(viewPath.toString());
        }
    }

    /**
     * Checks if is user fill full information about yourself (name, surname,
     * telephone and so on).
     *
     * @param idUser
     *            the id of user
     * @return {@code true} if is user fill full information about yourself
     * @throws ServiceException
     *             the service exception
     */
    private boolean isUserFillFullInfo(Integer idUser) throws ServiceException {
        UserService userService = ServiceFactory.getInstance().getUserService();
        return userService.isUserFillFullInfo(idUser);
    }

}
