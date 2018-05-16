package com.corporation.pharmacy.controller.command.impl.pharmacist;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command to show all information about the product.
 */
public class ShowProductCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowProductCommand.class);

    /** Request parameter */
    private static final String ID_PRODUCT_PARAM = "idProduct";

    /** The name of attribute which is set to the request */
    private static final String PRODUCT_ATTR = "product";

    /**
     * Gets {@code product id} from the parameters of the specified request and
     * {@code locale} from the session attributes. Gets product with this id in
     * according with this locale and sets it as the attribute of the request. Makes
     * forward to page with view of product for further changing.
     * <p>
     * If {@code ServiceException} has occurred makes forward to the error page.
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
        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));

        HttpSession session = request.getSession();
        LocaleType locale = SessionUtil.getLocale(session);

        String viewPath;
        try {
            ProductService productService = ServiceFactory.getInstance().getProductService();
            Product product = productService.getProduct(locale, idProduct);
            request.setAttribute(PRODUCT_ATTR, product);
            viewPath = ViewPath.FORWARD_CHANGE_PRODUCT_FORM;
        } catch (ServiceException e) {
            LOGGER.error("Exception during getting info about product.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
