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
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.service.PrescriptionService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command for sending user's request for getting or extending prescription
 * to a doctor.
 */
public class RequestPrescriptionCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(RequestPrescriptionCommand.class);

    /** Request parameter */
    private static final String ID_PRODUCT_PARAM = "idProduct";

    /**
     * Defines the type of Command - send request for getting or just extending
     * prescription.
     **/
    private final PrescriptionRequestType requestType;

    public RequestPrescriptionCommand(PrescriptionRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * <p>
     * Gets <code>user id</code> from the session and <code>product id</code> from
     * the request parameters. Sends request to a doctor for getting or extending
     * prescription for product with this id by this user. The type of the
     * request(getting or extending) is defined by the constructor of this class.
     * Sends redirect to the the basket view page.
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
        Integer idProduct = Integer.valueOf(request.getParameter(ID_PRODUCT_PARAM));
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);

        StringBuilder viewPath = new StringBuilder();
        try {
            PrescriptionService prescriptionService = ServiceFactory.getInstance().getPrescriptionService();
            prescriptionService.requestPrescription(idUser, idProduct, requestType);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET);
        } catch (ServiceException e) {
            LOGGER.error("Exception during sending request for prescription.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

}
