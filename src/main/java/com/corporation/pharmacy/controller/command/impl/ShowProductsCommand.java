package com.corporation.pharmacy.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.ProductsTO;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command to show products for current page taking into account the
 * quantity of items per page and chose locale.
 */
public class ShowProductsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowProductsCommand.class);

    /** Request parameter and its default value if it's not defined */
    private static final String CURRENT_PAGE_PARAM = "currentPage";
    private static final int CURRENT_PAGE_DEFAULT_VALUE = 1;

    /** Request parameter and its default value if it's not defined */
    private static final String ITEMS_PER_PAGE_PARAM = "itemsPerPage";
    private static final int ITEMS_PER_PAGE_DEFAULT_VALUE = 3;

    /** The name of attribute which is set to the request */
    private static final String PRODUCTS_VIEW_ATTR = "productsView";

    /**
     * Gets <code>number of current page</code> and <code>items per page</code> from
     * the request parameters. If this parameters are empty takes the default values
     * ({@link #CURRENT_PAGE_DEFAULT_VALUE} and
     * ({@link #ITEMS_PER_PAGE_DEFAULT_VALUE} respectively). Takes the attribute
     * <code>locale</code> from the user's session and if it <code>null</code> takes
     * the default value {@link SessionAttribute#DEFAULT_LOCALE}.Gets products for
     * view using this parameters, including list of products for current page and
     * total page count. Puts it to the request attributes. Makes forward to page
     * with view of products.
     * <p>
     * If {@code ServiceException} has occurred makes forward to error page.
     * </p>
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param response
     *            an {@link HttpServletResponse} object that contains the response
     *            the servlet sends to the client
     * @throws ServletException
     *             if {@link RequestDispatcher#forward} throws this exception
     * @throws IOException
     *             if {@link RequestDispatcher#forward} throws this exception
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPageParam = request.getParameter(CURRENT_PAGE_PARAM);
        int currentPage = (currentPageParam == null) ? CURRENT_PAGE_DEFAULT_VALUE : Integer.parseInt(currentPageParam);

        String itemsPerPageParam = request.getParameter(ITEMS_PER_PAGE_PARAM);
        int itemsPerPage = (itemsPerPageParam == null) ? ITEMS_PER_PAGE_DEFAULT_VALUE : Integer.parseInt(itemsPerPageParam);

        HttpSession session = request.getSession();
        LocaleType locale = SessionUtil.getLocale(session);

        String viewPath;
        try {
            ProductService productService = ServiceFactory.getInstance().getProductService();
            ProductsTO productsTO = productService.getProductsforView(locale, currentPage, itemsPerPage);
            request.setAttribute(PRODUCTS_VIEW_ATTR, productsTO);
            viewPath = ViewPath.FORWARD_PRODUCTS;
        } catch (ServiceException e) {
            LOGGER.error("Exception during getting info about products for current page.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

}
