package com.corporation.pharmacy.controller.command.impl.customer;

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
 * The Command for registration of the user.
 */
public class RegistrationCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(RegistrationCommand.class);

    /** Request parameters */
    private static final String EMAIL_PARAM = "reg_email";
    private static final String LOGIN_PARAM = "reg_login";
    private static final String PASSWORD_PARAM = "reg_password";

    /** The result messages of this Command execution */
    private static final String RESULT_MESSAGE = "message";
    private static final String SUCCESSFUL = "registrSuccessful";
    private static final String FAILED = "registrFailed";

    /**
     * <p>
     * Gets a new {@link User} object from the request parameters. Sends it to the
     * server layer for further validation and saving in the database. Gets a
     * <code>user id</code> as a result of registration of this user. Puts user id,
     * login and role of this user to the session attributes and migrates user's
     * basket from the session to the data base (see method
     * {@link BasketUtil#migrateSessionBasketToDB}. Makes redirect to the the same
     * page from which request has come with successful registration message (with
     * using method {@link PathUtil#addParametertoRefererPage}).
     * </p>
     * <p>
     * If <code>ValidationException</code> has happened sends redirect to the same
     * page from which request has come with unsuccessful registration message.
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
        User user = formUser(request);
        HttpSession session = request.getSession();

        StringBuilder viewPath = new StringBuilder();
        try {
            UserService userService = ServiceFactory.getInstance().getUserService();
            Integer idUser = userService.registrate(user);

            session.setAttribute(SessionAttribute.ID_USER, idUser);
            session.setAttribute(SessionAttribute.LOGIN, user.getLogin());
            session.setAttribute(SessionAttribute.ROLE, Role.CUSTOMER);

            BasketUtil.migrateSessionBasketToDB(session, idUser);

            URI uriWithParam = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, SUCCESSFUL);
            viewPath.append(uriWithParam);
        } catch (ValidationException e) {
            LOGGER.warn("Invalid data was sent, validation failed.", e);
            URI uriWithParam = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, FAILED);
            viewPath.append(uriWithParam);
        } catch (ServiceException e) {
            LOGGER.error("Error during user registration. Registration was canceled.", e);
            viewPath.append(request.getContextPath()).append(ViewPath.REDIRECT_503_ERROR);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Forms {@link User} object from the parameters of the specified request.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @return {@link User} object that contains email, login and password of the
     *         user.
     */
    private User formUser(HttpServletRequest request) {
        User user = new User();
        user.setEmail(request.getParameter(EMAIL_PARAM));
        user.setLogin(request.getParameter(LOGIN_PARAM));
        user.setPassword(request.getParameter(PASSWORD_PARAM));
        return user;
    }

}
