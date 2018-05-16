package com.corporation.pharmacy.controller.command.impl.pharmacist;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.PathUtil;
import com.corporation.pharmacy.controller.util.SessionUtil;
import com.corporation.pharmacy.controller.util.UploadsUtil;
import com.corporation.pharmacy.entity.FileInfo;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The Command for changing any information about the product.
 */
public class ChangeProductCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ChangeProductCommand.class);

    /** Request parameters */
    private static final String PRODUCT_ID_PARAM = "idProduct";
    private static final String NAME_PARAM = "name";
    private static final String NON_PATENT_NAME_PARAM = "nonPatentName";
    private static final String PRODUCER_PARAM = "producer";
    private static final String FORM_PARAM = "form";

    private static final String PRESCRIPTION_RADIO_OPTION = "prescriptionRadioOption";
    private static final String TRUE_OPTION = "yes";
    private static final String FALSE_OPTION = "no";

    private static final String INSTRUCTION_FILE_PARAM = "file";
    private static final String INSTRUCTION_FILE_NAME = "instructionFileName";
    private static final String PRICE_PARAM = "price";
    private static final String QUANTITY_PARAM = "quantity";

    /** The result messages of this Command execution */
    private static final String RESULT_MESSAGE = "message";
    private static final String SUCCESSFUL = "changeSuccessful";
    private static final String FAILED = "changeFailed";

    /** The path to properties file with the upload files configurations */
    private static final String UPLOAD_PROPERTIES_FILE_NAME = "uploads";
    /**
     * The property key of instructions for getting value represents the path to
     * instructions files (the virtual path, not real)
     */
    private static final String INSTRUCTIONS_PROPERTY_KEY = "instructions";
    private static final String INSTRUCTIONS_PATH;

    /**
     * Initializes {@link #INSTRUCTIONS_DIRECTORY_PATH} with the path where
     * instructions are saved in the server (the virtual path, not real)
     */
    static {
        ResourceBundle resource = ResourceBundle.getBundle(UPLOAD_PROPERTIES_FILE_NAME);
        INSTRUCTIONS_PATH = resource.getString(INSTRUCTIONS_PROPERTY_KEY);
    }

    /** Empty String */
    private static final String EMPTY_STRING = "";

    /**
     * <p>
     * Forms information about downloaded files with product instruction from the
     * specified request. Forms {@link Product} including this information and and
     * other information about product from the specified request. Replaces existing
     * product with this {@code Product}. If a new instruction file was downloaded
     * saves it to the server storage. Deletes the old one. Sends redirect to the
     * product view page with succesful message {@link #SUCCESSFUL}.
     * </p>
     * <p>
     * If {@code ValidationException} has happened sends redirect to the product
     * view page with failed message {@link #FAILED}.
     * </p>
     * <p>
     * If {@code ServiceException} has happened sends redirect to error page.
     * </p>
     * <p>
     * If {@code ServletException} or {@code IOException} during working with the
     * instruction file have happened they are caught with logging. Sends redirect
     * to the product view page with succesful message {@link #SUCCESSFUL}. Doesn't
     * send redirect to the error page as these exceptions aren't considered as
     * critical.
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
            FileInfo instructionInfo = formInstruction(request);

            Product product = formProduct(request, instructionInfo);
            ProductService productService = ServiceFactory.getInstance().getProductService();
            productService.replaceProduct(product);

            if (fileIsDownloaded(request)) {
                UploadsUtil.saveFileToServerStorage(instructionInfo);
                deleteInstructionFromServer(request);
            }
            viewPath.append(PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, SUCCESSFUL));
        } catch (ValidationException e) {
            LOGGER.warn("Sending invalid data, validation failed.", e);
            viewPath.append(PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, FAILED));
        } catch (ServiceException e) {
            LOGGER.error("Exception during changing the product.", e);
            viewPath.append(ViewPath.REDIRECT_503_ERROR);
        } catch (ServletException | IOException e) {
            LOGGER.error("Exception during working with the uploaded file with the product instruction.", e);
            viewPath.append(PathUtil.addParametertoRefererPage(request, RESULT_MESSAGE, SUCCESSFUL));
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Forms information about downloaded file with the instruction from the
     * parameter of the specified request. The name of this parameter specified by
     * {@link #INSTRUCTION_FILE_PARAM}. If the file wasn't downloaded saves to file
     * information the name of previously downloaded file which name is defined by
     * the value of parameter {@link #INSTRUCTION_FILE_NAME}.
     *
     * @param request
     *            the request
     * @return the {@link FileInfo} consisted information about downloaded file or
     *         it wasn't downloaded anything returns with the name of previously
     *         downloaded instruction.
     * @throws IOException
     *             if IOException during getting and working with downloaded file
     *             has occurred
     * @throws ServletException
     *             if {@link HttpServletRequest#getPart} throws this exception
     */
    private FileInfo formInstruction(HttpServletRequest request) throws IOException, ServletException {
        FileInfo fileInfo = new FileInfo();

        Part filePart = request.getPart(INSTRUCTION_FILE_PARAM);
        fileInfo.setFilePart(filePart);

        String fileName = UploadsUtil.getFileName(filePart);
        if (EMPTY_STRING.equals(fileName)) {
            fileName = request.getParameter(INSTRUCTION_FILE_NAME);
        } else {
            fileName = UploadsUtil.getFileNameWithDate(filePart);
        }
        fileInfo.setFileName(fileName);

        String savingDirectory = request.getServletContext().getRealPath(INSTRUCTIONS_PATH);
        fileInfo.setDirectory(savingDirectory);

        return fileInfo;
    }

    /**
     * Forms the product from the specified {@code request} including information
     * about its instruction specified by {@code instructionInfo}.
     *
     * @param request
     *            the request
     * @param instructionInfo
     *            consists information about downloaded file with the instruction
     * @return the product
     * @throws ValidationException
     *             if price or quantity parameters of the specified request are not
     *             valid (not a number)
     * @throws IOException
     *             if {@link IOException} has occurred during getting and working
     *             with downloaded file
     * @throws ServletException
     *             if {@link HttpServletRequest#getPart} throws this exception
     */
    private Product formProduct(HttpServletRequest request, FileInfo instructionInfo)
            throws ValidationException, IOException, ServletException {
        Product product = new Product();

        try {
            String productId = request.getParameter(PRODUCT_ID_PARAM);
            product.setIdProduct(Integer.valueOf(productId));

            String quantity = request.getParameter(QUANTITY_PARAM);
            product.setQuantity(Integer.valueOf(quantity));

            String price = request.getParameter(PRICE_PARAM);
            product.setPrice(new BigDecimal(price));
        } catch (NumberFormatException e) {
            throw new ValidationException(e);
        }

        product.setName(request.getParameter(NAME_PARAM));
        product.setNonPatentName(request.getParameter(NON_PATENT_NAME_PARAM));
        product.setProducer(request.getParameter(PRODUCER_PARAM));
        product.setForm(request.getParameter(FORM_PARAM));

        String needPrescription = request.getParameter(PRESCRIPTION_RADIO_OPTION);
        if (TRUE_OPTION.equals(needPrescription)) {
            product.setNeedPrescription(true);
        }
        if (FALSE_OPTION.equals(needPrescription)) {
            product.setNeedPrescription(false);
        }

        product.setInstructionFileName(instructionInfo.getFileName());

        LocaleType locale = SessionUtil.getLocale(request.getSession());
        product.setLocale(locale);

        return product;
    }

    /**
     * Checks if the file with instruction is downloaded. If yes returns
     * {@code true}.
     *
     * @param request
     *            the request
     * @return {@code true}, if file with the instruction was downloaded
     * @throws IOException
     *             if {@link IOException} has occurred during getting and working
     *             with downloaded file
     * @throws ServletException
     *             if {@link HttpServletRequest#getPart} throws this exception
     */
    private boolean fileIsDownloaded(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart(INSTRUCTION_FILE_PARAM);
        String fileName = UploadsUtil.getFileName(filePart);
        return ! EMPTY_STRING.equals(fileName);
    }

    /**
     * Deletes file with the product instruction from the server storage.
     *
     * @param request
     *            the request
     * @throws IOException
     *             if {@link IOException} has occurred during working with the file
     */
    private void deleteInstructionFromServer(HttpServletRequest request) throws IOException {
        String directory = request.getServletContext().getRealPath(INSTRUCTIONS_PATH);
        String fileName = request.getParameter(INSTRUCTION_FILE_NAME);
        UploadsUtil.deleteFile(directory, fileName);
    }

}
