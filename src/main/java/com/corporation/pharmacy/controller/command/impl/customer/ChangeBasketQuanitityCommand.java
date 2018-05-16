package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.util.Map;

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
 * The Command for changing product's quantity in the user's basket.
 */
public class ChangeBasketQuanitityCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ChangeBasketQuanitityCommand.class);

    /** Request parameters */
    private static final String ID_PRODUCT_PARAM = "idProduct";
    private static final String QUANTITY_PARAM = "quantity";

    /**
     * <p>
     * If the user is authenticated (see {@link SessionUtil#isAuthenticatedUser})
     * changes product's quantity in user's basket in the data base (with method
     * {@link #changeQuantityInDB}). If the user isn't authenticated changes
     * product's quantity in user's basket in the session (with method
     * {@link #changeQuantityInSession}). Sends redirect to the same page from which
     * request has come.
     * </p>
     * <p>
     * If <code>ServiceException</code> during changing product quantity in the
     * user's basket in the data base has happened, sends redirect to error page.
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder viewPath = new StringBuilder();
        try {
            HttpSession session = request.getSession();

            if (SessionUtil.isAuthenticatedUser(session)) {
                changeQuantityInDB(request);
            } else {
                changeQuantityInSession(request);
            }

            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET);
        } catch (ServiceException e) {
            LOGGER.error("Exception during changing quantity of product in the customer basket.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Gets parameters of the basket item from the specified request. Changes the
     * quantity of this basket item (product) in user's basket in the data base.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @throws ServiceException
     *             the exception during changing product's quantity in user's basket
     *             in the data base.
     */
    public void changeQuantityInDB(HttpServletRequest request) throws ServiceException {
        BasketItem basketItem = BasketUtil.formBasketItem(request);
        BasketService orderService = ServiceFactory.getInstance().getBasketService();
        orderService.changeQuantityInBasket(basketItem);
    }

    /**
     * Gets parameters of the basket item from the specified request. Changes the
     * quantity of this basket item (product) in user's basket in the session.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     */
    public void changeQuantityInSession(HttpServletRequest request) {
        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));
        Integer quantity = Integer.valueOf(request.getParameter(QUANTITY_PARAM));
        HttpSession session = request.getSession();
        Map<Integer, Integer> basket = SessionUtil.getBasket(session);
        basket.replace(idProduct, quantity);
    }

}
