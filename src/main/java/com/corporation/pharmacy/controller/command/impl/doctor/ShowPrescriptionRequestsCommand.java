package com.corporation.pharmacy.controller.command.impl.doctor;

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
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.entity.dto.PrescriptionRequestTO;
import com.corporation.pharmacy.service.PrescriptionService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Command to show requests sended to a doctor for getting or extending
 * prescription.
 */
public class ShowPrescriptionRequestsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowPrescriptionRequestsCommand.class);

    /** Request parameter and its default value if it's not defined */
    private static final String CURRENT_PAGE_PARAM = "currentPage";
    private static final int CURRENT_PAGE_DEFAULT_VALUE = 1;

    /** Request parameter and its default value if it's not defined */
    private static final String ITEMS_PER_PAGE_PARAM = "itemsPerPage";
    private static final int ITEMS_PER_PAGE_DEFAULT_VALUE = 3;

    /** The name of attribute which is set to the request */
    private static final String PRESCRIPTION_REQUESTS_VIEW_ATTR = "prescriptionRequestsView";

    /**
     * Defines the type of Command - show requests for getting or extending
     * prescription.
     **/
    private final PrescriptionRequestType requestType;

    public ShowPrescriptionRequestsCommand(PrescriptionRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * <p>
     * Gets <code>current page</code>, <code>items per page</code> from the request
     * parameters. If they are <code>null</code> takes their default values. Gets
     * <code>locale</code> from attributes of the session. Than gets requests for
     * getting or extending prescription sended to a doctor considering the current
     * page, items per page and locale. The type of the requests (getting or
     * extending) is defined by the constructor of this class. Makes forward to view
     * page of getting or extending prescription requests.
     * </p>
     * <p>
     * If <code>ServiceException</code> has happened makes forward to error page.
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
            PrescriptionService prescriptionService = ServiceFactory.getInstance().getPrescriptionService();
            PrescriptionRequestTO requests = prescriptionService.getPrescriptionRequests(locale, currentPage, itemsPerPage,
                    requestType);
            request.setAttribute(PRESCRIPTION_REQUESTS_VIEW_ATTR, requests);
            if (PrescriptionRequestType.GETTING == requestType) {
                viewPath = ViewPath.FORWARD_GETTING_REQUESTS;
            } else {
                viewPath = ViewPath.FORWARD_EXTENDING_REQUESTS;
            }
        } catch (ServiceException e) {
            LOGGER.error("Exception during getting prescription requests for current page.", e);
            viewPath = ViewPath.FORWARD_503_ERROR;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

}
