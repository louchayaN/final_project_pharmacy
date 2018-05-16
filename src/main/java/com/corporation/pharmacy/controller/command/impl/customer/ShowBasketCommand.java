package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.corporation.pharmacy.service.BasketService;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command for getting user's basket for showing.
 */
public class ShowBasketCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowBasketCommand.class);

    /** The names of attributes which are set to the request */
    private static final String DETAILED_BASKET_ATTR = "detailedBasket";
    private static final String TOTAL_PRODUCT_SUM_ATTR = "totalProductSum";

    /**
     * <p>
     * If the user is authenticated (see {@link SessionUtil#isAuthenticatedUser})
     * gets user's basket from the data base (see {@link #getBasketFromDB}). If the
     * user isn't authenticated gets user's basket from the session (see
     * {@link #getBasketFromSession}). Calculate the total sum of the ordered
     * products in basket. Sets basket and total sum like attributes of the request.
     * Makes forward to the basket view page.
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

            List<BasketTO> detailedBasket;
            if (SessionUtil.isAuthenticatedUser(session)) {
                detailedBasket = getBasketFromDB(session);
            } else {
                detailedBasket = getBasketFromSession(session);
            }
            request.setAttribute(DETAILED_BASKET_ATTR, detailedBasket);

            ProductService productService = ServiceFactory.getInstance().getProductService();
            BigDecimal totalProductSum = productService.getTotalSumOfProducts(detailedBasket);
            request.setAttribute(TOTAL_PRODUCT_SUM_ATTR, totalProductSum);

            viewPath = ViewPath.FORWARD_BASKET;
        } catch (ServiceException e) {
            LOGGER.error("Exception during getting detailed information about shopping cart.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    /**
     * Gets <code>user id</code> and <code>locale</code> from the attributes of the
     * specified session. Returns basket of user with this id in according with this
     * locale getting from the data base or empty List if there is no products in
     * user's basket.
     *
     * @param session
     *            the user's session
     * @throws ServiceException
     *             the exception during getting user's basket from the data base.
     * @return the user's basket representation or empty List if there is no
     *         products in user's basket
     */
    public List<BasketTO> getBasketFromDB(HttpSession session) throws ServiceException {
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);
        LocaleType locale = SessionUtil.getLocale(session);

        BasketService basketService = ServiceFactory.getInstance().getBasketService();
        List<BasketTO> detailedBasket = basketService.getBasket(locale, idUser);
        return ListUtils.emptyIfNull(detailedBasket);
    }

    /**
     * Gets <code>basket</code> and <code>locale</code> from the session attributes.
     * If basket is not empty gets from the data base full information about
     * products that are in the basket and forms detailed basket in according with
     * the locale. Returns detailed basket or empty <code>List</code> if it's null.
     * 
     * @param session
     *            the session
     * @throws ServiceException
     *             the exception during getting user's basket from the data base.
     * @return the user's basket representation or empty List if there is no
     *         products in user's basket
     */
    public List<BasketTO> getBasketFromSession(HttpSession session) throws ServiceException {
        Map<Integer, Integer> basket = SessionUtil.getBasket(session);
        LocaleType locale = SessionUtil.getLocale(session);

        List<BasketTO> detailedBasket = null;
        if (MapUtils.isNotEmpty(basket)) {
            BasketService basketService = ServiceFactory.getInstance().getBasketService();
            detailedBasket = basketService.formDetailedBasket(locale, basket);
        }
        return ListUtils.emptyIfNull(detailedBasket);
    }
}
