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
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.UserService;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The access filter to operation of placing order by the user.
 */
public class OrderAccessFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(OrderAccessFilter.class);

    /**
     * Checks if user got prescriptions for all products in his basket using method
     * {@link #isAllPresriptionGotten}. If yes, continues making of order operation.
     * Otherwise stops further operation by sending redirect to the basket view
     * page.
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
            if ( ! isAllPresriptionGotten(idUser)) {
                String viewPath = httpRequest.getContextPath() + ViewPath.REDIRECT_BASKET;
                httpResponse.sendRedirect(viewPath);
            } else {
                chain.doFilter(request, response);
            }
        } catch (ServiceException e) {
            LOGGER.error("Exception during checking of getting by the user all prescriptions.", e);
            String viewPath = httpRequest.getContextPath() + ViewPath.REDIRECT_503_ERROR;
            httpResponse.sendRedirect(viewPath);
        }
    }

    /**
     * Checks if user got prescriptions for all products in his basket.
     *
     * @param idUser
     *            the id of user
     * @return {@code true} if user got prescriptions for all products in his basket
     * @throws ServiceException
     *             the service exception
     */
    private boolean isAllPresriptionGotten(Integer idUser) throws ServiceException {
        UserService userService = ServiceFactory.getInstance().getUserService();
        return userService.isUserGotAllPrescriptions(idUser);
    }

}
