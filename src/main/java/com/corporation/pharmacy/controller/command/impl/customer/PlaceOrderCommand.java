package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.service.OrderService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command to form the order from user's basket.
 */
public class PlaceOrderCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(PlaceOrderCommand.class);

    /**
     * Gets the <code>user id</code> from the session, forms and places the order
     * from the basket of this user. Sends redirect to the basket view page. If
     * <code>ServiceException</code> during placing order has happened sends
     * redirect to error page.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param response
     *            an {@link HttpServletResponse} object that contains the response
     *            the servlet sends to the client
     * @throws IOException
     *             if {@link HttpServletResponse#sendRedirect} throws this exception
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);

        StringBuilder viewPath = new StringBuilder();
        try {
            OrderService orderService = ServiceFactory.getInstance().getOrderService();
            orderService.placeOrder(idUser);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET);
        } catch (ServiceException e) {
            LOGGER.error("Exception during forming order from basket.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

}
