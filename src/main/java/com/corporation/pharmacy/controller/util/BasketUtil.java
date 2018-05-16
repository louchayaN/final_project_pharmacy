package com.corporation.pharmacy.controller.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;

import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.service.BasketService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * Defines static methods for working with the basket.
 */
public class BasketUtil {

    /** Request parameters */
    private static final String ID_PRODUCT_PARAM = "idProduct";
    private static final String QUANTITY_PARAM = "quantity";

    /**
     * Not instantiate utility class.
     */
    private BasketUtil() {
        throw new AssertionError("Class contains static methods only. You should not instantiate it!");
    }

    /**
     * Form basket item from the parameters of the request.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @return the basket item
     */
    public static BasketItem formBasketItem(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);

        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));
        Integer quantity = Integer.valueOf(request.getParameter(QUANTITY_PARAM));

        BasketItem basketItem = new BasketItem();
        basketItem.setIdProduct(idProduct);
        basketItem.setQuantity(quantity);
        basketItem.setIdUser(idUser);

        return basketItem;
    }

    /**
     * Migrates user's basket from the session to the data base if it's not null or
     * empty. Removes its from the session attributes.
     *
     * @param session
     *            the user's session
     * @param idUser
     *            the id of user
     * @throws ServiceException
     *             the service exception during migration basket to the data base
     */
    public static void migrateSessionBasketToDB(HttpSession session, Integer idUser) throws ServiceException {
        Map<Integer, Integer> basket = (HashMap<Integer, Integer>) session.getAttribute(SessionAttribute.BASKET);
        if (MapUtils.isNotEmpty(basket)) {
            BasketService basketService = ServiceFactory.getInstance().getBasketService();
            basketService.addAllBasketItems(basket, idUser);
            session.removeAttribute(SessionAttribute.BASKET);
        }
    }

}
