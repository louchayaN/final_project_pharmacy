package com.corporation.pharmacy.controller.command.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;

/**
 * The Command for showing any type of uploaded files. The type defines by the
 * constructor of the class.
 */
public class ShowUploadedFileCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ShowUploadedFileCommand.class);

    /** Request parameter */
    private static final String FILE_NAME_PARAM = "file";

    /** The path to properties file with the uploads configurations */
    private static final String UPLOAD_PROPERTIES_FILE_NAME = "uploads";

    /** The path to the specified type of uploaded files (not real path) */
    private final String uploadedFilePath;

    /**
     * Defines the path (not real path) to the specified type of uploaded files
     * using properties file with uploads configurations
     */
    public ShowUploadedFileCommand(String fileType) {
        ResourceBundle resource = ResourceBundle.getBundle(UPLOAD_PROPERTIES_FILE_NAME);
        uploadedFilePath = resource.getString(fileType);
    }

    /**
     * Shows already uploaded to the server file. The path to the file (not real)
     * defines by the constructor of the class, the name of the file defines by the
     * request parameter. Using {@link ServletContext#getRealPath} gets real path of
     * saving file in the server. Gets the content and mime type of the file and
     * sets them to the specified response.
     * 
     * <p>
     * {@code IOException} has happened during working with the file is caught with
     * logging. Doesn't send redirect to the error page as this exception isn't
     * considered as critical.
     * </p>
     * 
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param response
     *            an {@link HttpServletResponse} object that contains the response
     *            the servlet sends to the client
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter(FILE_NAME_PARAM);
        String uploadedFileRealPath = request.getServletContext().getRealPath(uploadedFilePath);
        StringBuilder path = new StringBuilder();
        path.append(uploadedFileRealPath).append(File.separator).append(fileName);

        try {
            String mimeType = Files.probeContentType(Paths.get(path.toString()));
            response.setContentType(mimeType);
            byte[] content = Files.readAllBytes(Paths.get(path.toString()));
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        } catch (IOException e) {
            LOGGER.error("Exception during showing the uploaded file", e);
        }
    }

}
