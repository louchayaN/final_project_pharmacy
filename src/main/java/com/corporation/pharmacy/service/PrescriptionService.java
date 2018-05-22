package com.corporation.pharmacy.service;

import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.entity.dto.PrescriptionRequestTO;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * Works with the prescriptions.
 */
public interface PrescriptionService {

    /**
     * Adds the request for getting or extending prescription (defines by
     * <code>requestType</code>) on product (defines by <code>idProduct</code>) by
     * the user (defines by <code>idUser</code>).
     *
     * @param idUser
     *            the id of user who wants to extend or get prescription for the
     *            product
     * @param idProduct
     *            the id of product which for prescription request is sended
     * @param requestType
     *            defines getting or extending type of prescription request
     * @throws ServiceException
     *             the service exception
     */
    void requestPrescription(Integer idUser, Integer idProduct, PrescriptionRequestType requestType) throws ServiceException;

    /**
     * Gets the prescription requests for view for <code>current page</code>
     * according with defined <code>items per page</code> and <code>locale</code>.
     * THe type of prescriptions for view is defined by <code>requestType</code>.
     *
     * @param locale
     *            the locale (language)
     * @param currentPage
     *            the current page
     * @param itemsPerPage
     *            the items per page
     * @param requestType
     *            defines getting or extending type of prescription request
     * @return [@link PrescriptionRequestTO} object consisting the List of
     *         prescription requests of defined <code>PrescriptionRequestType</code>
     *         for current page and total count of prescription requests.
     * @throws ServiceException
     *             the service exception
     */
    PrescriptionRequestTO getPrescriptionRequests(LocaleType locale, int currentPage, int itemsPerPage,
            PrescriptionRequestType requestType) throws ServiceException;

    /**
     * Satisfy prescription request for getting or extending prescription (defines
     * by <code>requestType</code>) on the product (defines by
     * <code>idProduct</code>) sended by the user (defines by <code>idUser</code>).
     *
     * @param idUser
     *            the id of user who wants to extend or get prescription for the
     *            product
     * @param idProduct
     *            the id of product which for prescription request is sended
     * @param requestType
     *            defines getting or extending type of prescription request
     * @throws ServiceException
     *             the service exception
     */
    void satisfyPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws ServiceException;

}
