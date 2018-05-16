package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.BasketUtil;
import com.corporation.pharmacy.controller.util.PathUtil;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.service.BasketService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The {@code AddToBasketCommand} class is a command to add product to user's
 * basket.
 */
public class AddToBasketCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddToBasketCommand.class);

    /** Request parameters */
    private static final String ID_PRODUCT_PARAM = "idProduct";
    private static final String QUANTITY_PARAM = "quantity";

    /**
     * <p>
     * If the user is authenticated (see {@link SessionUtil#isAuthenticatedUser})
     * saves a product to user's basket in the data base. If the user isn't
     * authenticated saves a product to user's basket in the session. Sends redirect
     * to the same page from which request has come.
     * </p>
     * <p>
     * If <code>ServiceException</code> has happened sends redirect to error page.
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
        String viewPath;
        try {
            HttpSession session = request.getSession();

            if (SessionUtil.isAuthenticatedUser(session)) {
                saveBasketItemToDB(request);
            } else {
                saveBasketItemToSession(request);
            }

            viewPath = PathUtil.getRefererPage(request);
        } catch (ServiceException e) {
            LOGGER.error("Exception during adding product to basket.", e);
            viewPath = request.getContextPath() + ViewPath.REDIRECT_503_ERROR;
        }
        response.sendRedirect(viewPath);
    }

    /**
     * Gets parameters of basket item from the specified request. Saves gotten
     * basket item (a product) to user's basket in the data base.
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @throws ServiceException
     *             the exception during saving product to user's basket in the data
     *             base.
     */
    public void saveBasketItemToDB(HttpServletRequest request) throws ServiceException {
        BasketItem basketItem = BasketUtil.formBasketItem(request);
        BasketService basketService = ServiceFactory.getInstance().getBasketService();
        basketService.addBasketItem(basketItem);
    }

    /**
     * <p>
     * Saves a basket item (a product) to user's basket in the session (as
     * attribute).
     * </p>
     * <p>
     * User's basket in session is {@link Map} with product's id as key and quantity
     * of this product as value. If the product with the the same id is already in
     * basket, increase the quantity of this product, if not - puts a new pair
     * idProduct-orderedQuantity to the basket.
     * </p>
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     */
    public void saveBasketItemToSession(HttpServletRequest request) {
        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));
        Integer orderedQuantity = Integer.valueOf(request.getParameter(QUANTITY_PARAM));

        HttpSession session = request.getSession();
        Map<Integer, Integer> basket = SessionUtil.getBasket(session);
        if (basket.containsKey(idProduct)) {
            Integer quantity = basket.get(idProduct);
            basket.put(idProduct, quantity + orderedQuantity);
        } else {
            basket.put(idProduct, orderedQuantity);
        }
        session.setAttribute(SessionAttribute.BASKET, basket);
    }

}
