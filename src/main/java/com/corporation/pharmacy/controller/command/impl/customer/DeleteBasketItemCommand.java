package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.BasketUtil;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.service.BasketService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command for deleting a basket item (product) from user's basket.
 */
public class DeleteBasketItemCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(DeleteBasketItemCommand.class);

    /** Request parameter */
    private static final String ID_PRODUCT_PARAM = "idProduct";

    /**
     * <p>
     * If the user is authenticated (see {@link SessionUtil#isAuthenticatedUser})
     * deletes product from user's basket in the data base (with method
     * {@link #deleteBasketItemFromDB}). If the user isn't authenticated deletes
     * product from user's basket in the session (with method
     * {@link #deleteBasketItemFromSession}). Sends redirect to the same page from
     * which request has come.
     * </p>
     * <p>
     * If <code>ServiceException</code> during deleting product from user's basket
     * in the data base has been caught, sends redirect to the error page.
     * </p>
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder viewPath = new StringBuilder();
        try {
            HttpSession session = request.getSession();

            if (SessionUtil.isAuthenticatedUser(session)) {
                deleteBasketItemFromDB(request);
            } else {
                deleteBasketItemFromSession(request);
            }

            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET);
        } catch (ServiceException e) {
            LOGGER.error("Exception during deleting product item from the customer basket.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Gets parameters of the basket item (product) from specified request. Deletes
     * this product from user's basket in the data base.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @throws ServiceException
     *             the exception during deleting product from user's basket in the
     *             data base.
     */
    public void deleteBasketItemFromDB(HttpServletRequest request) throws ServiceException {
        BasketItem basketItem = BasketUtil.formBasketItem(request);
        BasketService orderService = ServiceFactory.getInstance().getBasketService();
        orderService.deleteBasketItem(basketItem);
    }

    /**
     * Gets parameters of the basket item (product) from the specified request.
     * Deletes this product from user's basket in the session.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     */
    public void deleteBasketItemFromSession(HttpServletRequest request) {
        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));
        HttpSession session = request.getSession();
        Map<Integer, Integer> basket = SessionUtil.getBasket(session);
        basket.remove(idProduct);
    }

}
