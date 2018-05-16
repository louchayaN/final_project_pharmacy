package com.corporation.pharmacy.controller.command;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Defines the common command method for processing the request and forming the
 * response.
 */
public interface Command {

    /**
     * Processes the request and forms the response to necessary page.
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
     *             if {@link HttpServletResponse#sendRedirect} or
     *             {@link RequestDispatcher#forward} throws this exception
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
