package com.corporation.pharmacy.controller.command.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.controller.util.PathUtil;

/**
 * The command for changing locale for the user.
 */
public class ChangeLocaleCommand implements Command {

    /** Request parameter */
    private static final String LOCALE_PARAM = "locale";

    /**
     * Gets local parameter from the request and puts it as an attribute with name
     * {@link SessionAttribute#LOCALE} to the session. Sends redirect to the the
     * same page from which request has come. The path to this page is defined using
     * static method {@link PathUtil#getRefererPage}.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param response
     *            an {@link HttpServletResponse} object that contains the response
     *            the servlet sends to the client
     * @throws IOException
     *             if {@link HttpServletResponse#sendRedirect} throws this exception
     * 
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String locale = request.getParameter(LOCALE_PARAM);
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.LOCALE, locale);
        String viewPath = PathUtil.getRefererPage(request);
        response.sendRedirect(viewPath);
    }

}
