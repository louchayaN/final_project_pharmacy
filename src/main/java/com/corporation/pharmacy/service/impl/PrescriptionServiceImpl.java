package com.corporation.pharmacy.service.impl;

import java.util.List;

import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.PrescriptionDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.PrescriptionRequest;
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.entity.dto.PrescriptionRequestTO;
import com.corporation.pharmacy.service.PrescriptionService;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Class PrescriptionServiceImpl is a class for working with the
 * prescriptions.
 */
public class PrescriptionServiceImpl implements PrescriptionService {

    /**
     * The single instance of non transactional DAOManager witch is a factory of DAO
     * that is used in non transactional operations.
     */
    private static final DAOManager daoManager = DAOFactory.getFactory().getNonTransactionalDAOManager();

    /**
     * The single instance of DAO for non transactional operations.
     */
    private static final PrescriptionDAO prescriptionDAO = daoManager.getPrescriptionDAO();

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
    @Override
    public void requestPrescription(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws ServiceException {
        try {
            prescriptionDAO.addPrescriptionRequest(idUser, idProduct, requestType);
        } catch (DaoException e) {
            throw new ServiceException("Exception during sending request for prescription", e);
        }
    }

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
    @Override
    public PrescriptionRequestTO getPrescriptionRequests(LocaleType locale, int currentPage, int itemsPerPage,
            PrescriptionRequestType requestType) throws ServiceException {
        PrescriptionRequestTO prescriptionRequestTO = null;
        try {
            int prescriptionTotalCount = prescriptionDAO.getPrescriptionTotalCount(requestType);
            int totalPageCount = (int) Math.ceil((double) prescriptionTotalCount / itemsPerPage);

            List<PrescriptionRequest> requests = prescriptionDAO.getPrescriptionRequests(locale, currentPage, itemsPerPage,
                    requestType);

            prescriptionRequestTO = new PrescriptionRequestTO();
            prescriptionRequestTO.setTotalPageCount(totalPageCount);
            prescriptionRequestTO.setPrescriptionRequests(requests);
        } catch (DaoException e) {
            throw new ServiceException("Exception during getting prescription requests for view for current page.", e);
        }
        return prescriptionRequestTO;
    }

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
    @Override
    public void satisfyPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws ServiceException {
        try {
            prescriptionDAO.satisfyPrescriptionRequest(idUser, idProduct, requestType);
        } catch (DaoException e) {
            throw new ServiceException("Exception during satisfying prescription request.", e);
        }

    }

}
