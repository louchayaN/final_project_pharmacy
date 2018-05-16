package com.corporation.pharmacy.controller.command.impl;

import java.io.IOException;
import java.net.URI;

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
import com.corporation.pharmacy.entity.Role;
import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.UserService;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The command for logging (authorising) the user.
 */
public class LoginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    /** Request parameters */
    private static final String LOGIN_OR_EMAIL_PARAM = "loginOrEmail";
    private static final String PASSWORD_PARAM = "password";

    /** The result messages of this Command execution */
    private static final String RESULT_MESSAGE = "message";
    private static final String SUCCESSFUL = "signInSuccessful";
    private static final String FAILED = "signInfailed";

    /**
     * <p>
     * Gets login or email (depends on user choice) and password from the parameters
     * request. Sends them to the server layer for following validation and getting
     * an object {@link User} from the database.
     * </p>
     * 
     * <p>
     * If such user is presented in the database (user is not null), puts userId,
     * login, role as attributes of the current session and makes migration of
     * user's basket from session to the database (with
     * {@link BasketUtil#migrateSessionBasketToDB} method). Depending on user's role
     * defines the page to which redirect will be sent (with {@link #defineViewPath}
     * method).
     * </p>
     * 
     * <p>
     * If such user is not presented in the database (user is null), makes redirect
     * to the same page from the request has come with message about failed
     * authentification.
     * </p>
     * 
     * <p>
     * If {@code ValidationException} has happened sends redirect to the the same
     * page from which request has come with message about incorrect sent parameters
     * (with using method {@link PathUtil#addParametertoRefererPage}).
     * </p>
     * 
     * <p>
     * If {@code ServiceException} has happened sends redirect to the error page.
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
        String loginOrEmail = request.getParameter(LOGIN_OR_EMAIL_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        StringBuilder viewPath = new StringBuilder();
        try {
            UserService userService = ServiceFactory.getInstance().getUserService();
            User user = userService.logIn(loginOrEmail, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute(SessionAttribute.ID_USER, user.getIdUser());
                session.setAttribute(SessionAttribute.LOGIN, user.getLogin());
                session.setAttribute(SessionAttribute.ROLE, user.getRole());

                BasketUtil.migrateSessionBasketToDB(session, user.getIdUser());

                viewPath.append(defineViewPath(user.getRole(), request));
            } else {
                URI uriWithParam = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, FAILED);
                viewPath.append(uriWithParam);
            }
        } catch (ValidationException e) {
            LOGGER.warn("Sending invalid data for loginning.", e);
            URI uriWithParam = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, FAILED);
            viewPath.append(uriWithParam);
        } catch (ServiceException e) {
            LOGGER.error("Exception during user loginning.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Define the path to view page depending on the user's specified role. If the
     * user is authorized like a {@link Role#PHARMACIST}, the view page will be the
     * pharmacist start page. If like a {@link Role#DOCTOR}, it will be the doctor
     * start page. In other cases the view page will be the same from which the
     * specified request has come with the message about successful
     * authentification..
     *
     * @param role
     *            the role of the user.
     * @param request
     *            the HttpRequest.
     * @return the path to the view page depending on the user's role.
     */
    private StringBuilder defineViewPath(Role role, HttpServletRequest request) {
        StringBuilder viewPath = new StringBuilder();
        switch (role) {
        case PHARMACIST:
            return viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_ADD_PRODUCT);
        case DOCTOR:
            return viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_GETTING_REQUESTS);
        default:
            URI uriWithParam = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, SUCCESSFUL);
            return viewPath.append(uriWithParam);
        }
    }

}
