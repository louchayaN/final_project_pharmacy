package com.corporation.pharmacy.controller.command.impl.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command for finding products by its name including or not including
 * analogues found by non patent name.
 */
public class FindProductsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(FindProductsCommand.class);

    /** Request parameters */
    private static final String PRODUCT_NAME_PARAM = "productName";
    private static final String ANALOGS_CHECKBOX = "includeAnalogs";
    private static final String CHECKBOX_CHECKED = "on";

    /** The names of attributes which are set to the request */
    private static final String FOUND_PRODUCTS_ATTR = "foundProducts";
    private static final String FOUND_RESEMBLING_PRODUCTS_ATTR = "foundResemblingProducts";

    /**
     * <p>
     * Gets <code>product name</code> from the specified request parameters. Checks
     * if the checkbox of finding products by analogues has been checked. If yes,
     * puts the value of variable <code>includingAnalogs</code> to the
     * <code>true</code>, if not to <code>false</code>.
     * </p>
     * <p>
     * Sends this two parameters to the server layer for strict searching product in
     * the data base. Founded list of products is saved to request attribute with
     * name {@link #FOUND_PRODUCTS_ATTR}. If no one product was found makes
     * searching by sounding and saves products to request attribute with name
     * {@link #FOUND_RESEMBLING_PRODUCTS_ATTR}. Makes forward to found products view
     * page.
     * </p>
     * <p>
     * If <code>ServiceException</code> has happened, makes forward to error page.
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
        String productName = request.getParameter(PRODUCT_NAME_PARAM);
        String analogsCheckboxStatus = request.getParameter(ANALOGS_CHECKBOX);
        boolean includingAnalogs = false;
        if (CHECKBOX_CHECKED.equals(analogsCheckboxStatus)) {
            includingAnalogs = true;
        }

        String viewPath;
        try {
            ProductService productService = ServiceFactory.getInstance().getProductService();
            List<Product> foundProducts = productService.findProductsByName(productName, includingAnalogs);
            request.setAttribute(FOUND_PRODUCTS_ATTR, foundProducts);
            if (foundProducts.isEmpty()) {
                List<Product> foundResemblingProducts = productService.findProductsBySounding(productName, includingAnalogs);
                request.setAttribute(FOUND_RESEMBLING_PRODUCTS_ATTR, foundResemblingProducts);
            }
            viewPath = ViewPath.FORWARD_FOUND_PRODUCTS;
        } catch (ServiceException e) {
            LOGGER.error("Exception during finding products by name.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

}
