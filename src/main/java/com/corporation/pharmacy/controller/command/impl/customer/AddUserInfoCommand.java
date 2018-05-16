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
import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.UserService;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The Command for adding additional information about user.
 */
public class AddUserInfoCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddUserInfoCommand.class);

    /** Request parameters */
    private static final String NAME_PARAM = "name";
    private static final String MIDDLE_NAME_PARAM = "middleName";
    private static final String SURNAME_PARAM = "surname";
    private static final String ADDRESS_PARAM = "address";
    private static final String PASSPORT_PARAM = "passport";
    private static final String TELEPHONE_PARAM = "telephone";

    /** The result message of unsuccessful Command execution */
    private static final String FAILED_PARAM_MESSAGE = "?attention=fillInfo";

    /**
     * <p>
     * Forms {@link User} object with full information about user from the specified
     * request. Sends it to the service layer for validation and saving in the data
     * base. If no exceptions occurred sends redirect to the basket view page.
     * </p>
     * <p>
     * If <code>ValidationException</code> has happened sends redirect to the basket
     * view page with failed message {@link #FAILED_PARAM_MESSAGE}.
     * </p>
     * <p>
     * If <code>ServiceException</code> has happened sends redirect to the error
     * page.
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
            User user = formUser(request);
            UserService userService = ServiceFactory.getInstance().getUserService();
            userService.setUserFullInfo(user);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET);
        } catch (ValidationException e) {
            LOGGER.warn("An attempt to send invalid data to data base, validation failed.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_BASKET).append(FAILED_PARAM_MESSAGE);
        } catch (ServiceException e) {
            LOGGER.error("Error during filling full info about user.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Forms {@link User} object from the parameters of the specified request and
     * user id getting from the session.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @return {@link User} object contained full information about user.
     */
    private User formUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute(SessionAttribute.ID_USER);
        User user = new User();
        user.setIdUser(idUser);
        user.setName(request.getParameter(NAME_PARAM));
        user.setMiddleName(request.getParameter(MIDDLE_NAME_PARAM));
        user.setSurname(request.getParameter(SURNAME_PARAM));
        user.setAdress(request.getParameter(ADDRESS_PARAM));
        user.setPassport(request.getParameter(PASSPORT_PARAM));
        user.setTelephone(request.getParameter(TELEPHONE_PARAM));
        return user;
    }

}
