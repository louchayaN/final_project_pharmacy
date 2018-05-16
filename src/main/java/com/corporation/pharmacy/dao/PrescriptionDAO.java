package com.corporation.pharmacy.dao;

import java.util.List;

import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.PrescriptionRequest;
import com.corporation.pharmacy.entity.PrescriptionRequestType;

/**
 * The Interface PrescriptionDAO.
 * 
 * Defines methods for work with prescriptions.
 */
public interface PrescriptionDAO {

    // ADD operations

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
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void addPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType) throws DaoException;

    // GET operations

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
     * @return the List of prescription requests of defined
     *         <code>PrescriptionRequestType</code>
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    List<PrescriptionRequest> getPrescriptionRequests(LocaleType locale, int currentPage, int itemsPerPage,
            PrescriptionRequestType requestType) throws DaoException;

    /**
     * Gets the total count of prescriptions of defined
     * <code>PrescriptionRequestType</code>.
     *
     * @param requestType
     *            defines getting or extending type of prescription request
     * @return the total count of prescriptions of defined
     *         <code>PrescriptionRequestType</code>
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    int getPrescriptionTotalCount(PrescriptionRequestType requestType) throws DaoException;

    // UPDATE operations

    /**
     * Satisfy prescription request for getting or extending prescription (defines
     * by <code>requestType</code>) on product (defines by <code>idProduct</code>)
     * by the user (defines by <code>idUser</code>).
     *
     * @param idUser
     *            the id of user who wants to extend or get prescription for the
     *            product
     * @param idProduct
     *            the id of product which for prescription request is sended
     * @param requestType
     *            defines getting or extending type of prescription request
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void satisfyPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType) throws DaoException;

}
