package com.corporation.pharmacy.controller.command.impl.pharmacist;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.controller.command.Command;
import com.corporation.pharmacy.controller.constant.ViewPath;
import com.corporation.pharmacy.controller.util.UploadsUtil;
import com.corporation.pharmacy.entity.FileInfo;
import com.corporation.pharmacy.entity.LocalProductInfo;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.ServiceFactory;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The Command for adding a new product consisting information in different
 * locales. Includes saving uploaded files representing product instructions to
 * the server storage.
 */
public class AddProductCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddProductCommand.class);

    /** Request parameters */
    private static final String PRESCRIPTION_RADIO_OPTION = "prescriptionRadioOption";
    private static final String TRUE_OPTION = "yes";
    private static final String FALSE_OPTION = "no";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String PRICE_PARAM = "price";

    /** Request parameters in RU_BY locale */
    private static final String NAME_PARAM_RU = "name_ru";
    private static final String NON_PATENT_NAME_PARAM_RU = "nonPatentName_ru";
    private static final String PRODUCER_PARAM_RU = "producer_ru";
    private static final String FORM_PARAM_RU = "form_ru";
    private static final String INSTRUCTION_FILE_PARAM_RU = "file_ru";

    /** Request parameters in EN_US locale */
    private static final String NAME_PARAM_EN = "name_en";
    private static final String NON_PATENT_NAME_PARAM_EN = "nonPatentName_en";
    private static final String PRODUCER_PARAM_EN = "producer_en";
    private static final String FORM_PARAM_EN = "form_en";
    private static final String INSTRUCTION_FILE_PARAM_EN = "file_en";

    /** The result messages of this Command execution */
    private static final String SUCCESSFUL_PARAM_MESSAGE = "?message=addSuccessful";
    private static final String FAILED_PARAM_MESSAGE = "?message=addFailed";

    /** The path to properties file with the upload files configurations */
    private static final String UPLOAD_PROPERTIES_FILE_PATH = "uploads";
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
        ResourceBundle resource = ResourceBundle.getBundle(UPLOAD_PROPERTIES_FILE_PATH);
        INSTRUCTIONS_PATH = resource.getString(INSTRUCTIONS_PROPERTY_KEY);
    }

    /** Empty String */
    private static final String EMPTY_STRING = "";

    /**
     * <p>
     * Forms information about downloaded files with product instructions in
     * {@link LocaleType#RU_BY} and {@link LocaleType#EN_US} locales from the
     * specified request. Forms {@link LocalizedProduct} object with this objects
     * and other information about product from the specified request. Saves this
     * {@link LocalizedProduct} to the data base. Saves files representing product
     * instructions in different locales(languages) to the server storage. Sends
     * redirect to the view page of adding product with succesful message
     * {@link #SUCCESSFUL_PARAM_MESSAGE}.
     * </p>
     * <p>
     * If {@code ValidationException} has happened sends redirect to the view page
     * of adding product form with failed message {@link #FAILED_PARAM_MESSAGE}.
     * </p>
     * <p>
     * If {@code ServiceException} has happened sends redirect to error page.
     * </p>
     * <p>
     * {@code ServletException} or {@code IOException} have happened during working
     * with the instruction file are caught with logging. Sends redirect to the view
     * page of adding product form with succesful message
     * {@link #SUCCESSFUL_PARAM_MESSAGE}. Doesn't send redirect to the error page as
     * these exceptions aren't considered as critical.
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
        StringBuilder viewPath = new StringBuilder(request.getContextPath());
        try {
            FileInfo instructionRu = formInstruction(request, INSTRUCTION_FILE_PARAM_RU);
            FileInfo instructionEn = formInstruction(request, INSTRUCTION_FILE_PARAM_EN);

            LocalizedProduct localizedProduct = formLocalizedProduct(request, instructionRu, instructionEn);
            ProductService productService = ServiceFactory.getInstance().getProductService();
            productService.addProduct(localizedProduct);

            UploadsUtil.saveFileToServerStorage(instructionRu);
            UploadsUtil.saveFileToServerStorage(instructionEn);

            viewPath.append(ViewPath.REDIRECT_ADD_PRODUCT).append(SUCCESSFUL_PARAM_MESSAGE);
        } catch (ValidationException e) {
            LOGGER.warn("Sending invalid data, validation failed.", e);
            viewPath.append(ViewPath.REDIRECT_ADD_PRODUCT).append(FAILED_PARAM_MESSAGE);
        } catch (ServiceException e) {
            LOGGER.error("Exception during adding a new product.", e);
            viewPath.append(ViewPath.REDIRECT_503_ERROR);
        } catch (ServletException | IOException e) {
            LOGGER.error("Exception during saving the uploaded file with the product instruction to server storage.", e);
            viewPath.append(ViewPath.REDIRECT_ADD_PRODUCT).append(SUCCESSFUL_PARAM_MESSAGE);
        }
        response.sendRedirect(viewPath.toString());
    }

    /**
     * Forms information about downloaded file with the instruction from the
     * parameter of the specified request. The name of the parameter specified by
     * {@code fileParamName}.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param fileParamName
     *            the name of the request parameter represented file {@code Part}
     * @return the {@link FileInfo} consisted information about downloaded file.
     * @throws IOException
     *             if IOException during getting and working with downloaded file
     *             has occurred
     * @throws ServletException
     *             if {@link HttpServletRequest#getPart} throws this exception
     * @throws ValidationException
     *             if no files were downloaded
     */
    private FileInfo formInstruction(HttpServletRequest request, String fileParamName)
            throws IOException, ServletException, ValidationException {
        FileInfo fileInfo = new FileInfo();

        Part filePart = request.getPart(fileParamName);
        if (EMPTY_STRING.equals(UploadsUtil.getFileName(filePart))) {
            throw new ValidationException("File should be upload.");
        }
        fileInfo.setFilePart(filePart);

        String savingDirectory = request.getServletContext().getRealPath(INSTRUCTIONS_PATH);
        fileInfo.setDirectory(savingDirectory);

        String fileNameWithDate = UploadsUtil.getFileNameWithDate(filePart);
        fileInfo.setFileName(fileNameWithDate);

        return fileInfo;
    }

    /**
     * Forms {@link LocalizedProduct} using non String product info and String
     * information about product in different locales from the parameters of the
     * specified request.
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request the
     *            client has made of the servlet
     * @param instructionRU
     *            information about downloaded file with the instruction in RU_BY
     *            locale
     * @param instructionEN
     *            information about downloaded file with the instruction in EN_US
     *            locale
     * @return the localized product consisting non String information that the same
     *         for different locales and String information in different locales.
     * @throws ValidationException
     *             if price or quantity parameters of the specified request are not
     *             valid (not a number)
     */
    private LocalizedProduct formLocalizedProduct(HttpServletRequest request, FileInfo instructionRU, FileInfo instructionEN)
            throws ValidationException, IOException, ServletException {
        LocalizedProduct product = new LocalizedProduct();
        String needPrescription = request.getParameter(PRESCRIPTION_RADIO_OPTION);
        if (TRUE_OPTION.equals(needPrescription)) {
            product.setNeedPrescription(true);
        }
        if (FALSE_OPTION.equals(needPrescription)) {
            product.setNeedPrescription(false);
        }

        try {
            String quantity = request.getParameter(QUANTITY_PARAM);
            product.setQuantity(Integer.valueOf(quantity));

            String price = request.getParameter(PRICE_PARAM);
            product.setPrice(new BigDecimal(price));
        } catch (NumberFormatException e) {
            throw new ValidationException(e);
        }

        List<LocalProductInfo> productInfoForDifferentLocales = new ArrayList<>();

        LocalProductInfo productInfoRU = new LocalProductInfo();
        productInfoRU.setLocale(LocaleType.RU_BY);
        productInfoRU.setName(request.getParameter(NAME_PARAM_RU));
        productInfoRU.setNonPatentName(request.getParameter(NON_PATENT_NAME_PARAM_RU));
        productInfoRU.setProducer(request.getParameter(PRODUCER_PARAM_RU));
        productInfoRU.setForm(request.getParameter(FORM_PARAM_RU));
        productInfoRU.setInstructionFileName(instructionRU.getFileName());

        LocalProductInfo productInfoEN = new LocalProductInfo();
        productInfoEN.setLocale(LocaleType.EN_US);
        productInfoEN.setName(request.getParameter(NAME_PARAM_EN));
        productInfoEN.setNonPatentName(request.getParameter(NON_PATENT_NAME_PARAM_EN));
        productInfoEN.setProducer(request.getParameter(PRODUCER_PARAM_EN));
        productInfoEN.setForm(request.getParameter(FORM_PARAM_EN));
        productInfoEN.setInstructionFileName(instructionEN.getFileName());

        productInfoForDifferentLocales.add(productInfoRU);
        productInfoForDifferentLocales.add(productInfoEN);

        product.setProductInfoForDifferentLocales(productInfoForDifferentLocales);

        return product;
    }

}
