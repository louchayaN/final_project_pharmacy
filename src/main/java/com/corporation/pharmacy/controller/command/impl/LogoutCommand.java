package com.corporation.pharmacy.controller.command.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.PathUtil;
import com.corporation.pharmacy.entity.Role;

/**
 * The Command for logging out a user.
 */
public class LogoutCommand implements Command {

    private static final String RESULT_MESSAGE = "message";
    private static final String SUCCESSFUL = "signOut";

    /**
     * Removes userId, login and role attributes from the session. If the user's
     * role was {@link Role#PHARMACIST} or {@link Role#DOCTOR} sends redirect to the
     * home-page of web-app for security purposes. In other cases the redirect is
     * sent to the same page from which specified request has come with the message
     * about successful sign out.
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
        HttpSession session = request.getSession();
        Role role = (Role) session.getAttribute(SessionAttribute.ROLE);
        session.removeAttribute(SessionAttribute.ID_USER);
        session.removeAttribute(SessionAttribute.LOGIN);
        session.removeAttribute(SessionAttribute.ROLE);

        String viewPath;
        if (Role.PHARMACIST == role || Role.DOCTOR == role) {
            viewPath = request.getContextPath() + ViewPath.REDIRECT_HOME_PAGE;
        } else {
            viewPath = PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, SUCCESSFUL).toString();
        }
        response.sendRedirect(viewPath);
    }

}
