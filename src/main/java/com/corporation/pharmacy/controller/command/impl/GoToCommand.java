package com.corporation.pharmacy.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;

/**
 * The command for making forward to the defined page.
 */
public class GoToCommand implements Command {

    /** The page to which forward will be made. */
    private final String page;

    public GoToCommand(String page) {
        this.page = page;
    }

    /**
     * Makes only forward to the page whose name is defined by the constructor
     * {@link #GoToCommand(String)}.
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
        RequestDispatcher dispatcher = request.getRequestDispatcher(ViewPath.COMMON_PAGES_PATH + page);
        dispatcher.forward(request, response);
    }

}
