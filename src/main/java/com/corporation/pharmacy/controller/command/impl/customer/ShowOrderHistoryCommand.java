package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;
import com.corporation.pharmacy.service.OrderService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command to get user's history of orders for showing.
 */
public class ShowOrderHistoryCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowOrderHistoryCommand.class);

    /** The name of attribute which are set to the request */
    private static final String ORDERS_ATTR = "orders";

    /**
     * <p>
     * If the user is authenticated (see {@link SessionUtil#isAuthenticatedUser})
     * gets user's history of orders from the data base and sets it as an attribute
     * of the request. If there is no orders of this user sets an empty List. Makes
     * forward to the order history view page.
     * </p>
     * <p>
     * If <code>ServiceException</code> has happened makes forward to the error
     * page.
     * </p>
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param response
     *            an {@link HttpServletResponse} object that contains the response
     * @throws ServletException
     *             if {@link RequestDispatcher#forward} throws this exception
     * @throws IOException
     *             if {@link RequestDispatcher#forward} throws this exception
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath;
        try {
            HttpSession session = request.getSession();
            if (SessionUtil.isAuthenticatedUser(session)) {
                Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);
                LocaleType locale = SessionUtil.getLocale(session);

                OrderService orderService = ServiceFactory.getInstance().getOrderService();
                List<Order> orders = orderService.getOrderHistory(locale, idUser);
                request.setAttribute(ORDERS_ATTR, ListUtils.emptyIfNull(orders));
            }
            viewPath = ViewPath.FORWARD_ORDERS;
        } catch (ServiceException e) {
            LOGGER.error("Exception during getting order history of the user.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

}
