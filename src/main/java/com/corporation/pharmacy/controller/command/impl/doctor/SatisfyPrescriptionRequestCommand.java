package com.corporation.pharmacy.controller.command.impl.doctor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.service.PrescriptionService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command for satisfying user's request for getting or extending
 * prescription by a doctor.
 */
public class SatisfyPrescriptionRequestCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(SatisfyPrescriptionRequestCommand.class);

    /** Request parameters */
    private static final String ID_PRODUCT_PARAM = "idProduct";
    private static final String ID_USER_PARAM = "idUser";

    /**
     * Defines the type of Command - satisfy request for getting or satisfy request
     * for extending prescription.
     **/
    private final PrescriptionRequestType requestType;

    public SatisfyPrescriptionRequestCommand(PrescriptionRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * <p>
     * Gets <code>user id</code> and <code>product id</code> from the request
     * parameters. Satisfy request for getting or extending prescription for product
     * with this id sended by this user. The type of the request(getting or
     * extending) is defined by the constructor of this class. Sends redirect to
     * view page of getting or extending prescription requests.
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
        Integer idUser = Integer.valueOf(request.getParameter(ID_USER_PARAM));

        StringBuilder viewPath = new StringBuilder();
        try {
            PrescriptionService prescriptionService = ServiceFactory.getInstance().getPrescriptionService();
            prescriptionService.satisfyPrescriptionRequest(idUser, idProduct, requestType);
            if (PrescriptionRequestType.GETTING == requestType) {
                viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_GETTING_REQUESTS);
            } else {
                viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_EXTENDING_REQUESTS);
            }
        } catch (ServiceException e) {
            LOGGER.error("Exception during satisfying request for prescription.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

}
