package com.corporation.pharmacy.controller.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.corporation.pharmacy.entity.FileInfo;

/**
 * Defines static methods for working with uploaded files to the server storage.
 */
public class UploadsUtil {

    /** The format of current date for adding to the name of uploaded file */
    private static final String DATE_FORMAT = "dd-MM-yyyy_HH-mm-ss_";

    /**
     * Not instantiate utility class.
     */
    private UploadsUtil() {
        throw new AssertionError("Class contains static methods only. You should not instantiate it!");
    }

    /**
     * Saves file to server storage. The information about file keeps specified
     * {@code FileInfo}.
     *
     * @param fileInfo
     *            consists information about the file
     * @throws IOException
     *             if I/O exception during working with uploaded file has occurred
     * @throws ServletException
     *             if {@link HttpServletRequest#getPart} throws this exception
     */
    public static void saveFileToServerStorage(FileInfo fileInfo) throws IOException {
        try (InputStream fileContent = fileInfo.getFilePart().getInputStream()) {
            File file = new File(fileInfo.getDirectory(), fileInfo.getFileName());
            Files.copy(fileContent, file.toPath());
        }
    }

    /**
     * Delete file from the server storage.
     *
     * @param directory
     *            the directory keeps the file (real path)
     * @param fileName
     *            the name of the file
     * @throws IOException
     *             if I/O exception during working with uploaded file has occurred
     */
    public static void deleteFile(String directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        Files.deleteIfExists(file.toPath());
    }

    /**
     * Gets the file name from the specified {@code filePart}. Returns it with the
     * added current date in the format specified by {@link #DATE_FORMAT}. The
     * current date is added to the name of the file for guarantee the uniqueness of
     * file name.
     *
     * @param filePart
     *            the file Part
     * @return the file name with added current date in the format specified by
     *         {@link #DATE_FORMAT}
     */
    public static String getFileNameWithDate(Part filePart) {
        String fileName = getFileName(filePart);
        String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        return date + fileName;
    }

    /**
     * Gets the file name from the specified {@code filePart}.
     *
     * @param filePart
     *            the file Part
     * @return the file name
     */
    public static String getFileName(Part filePart) {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        return fileName;
    }

}
