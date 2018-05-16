package com.corporation.pharmacy.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.PrescriptionDAO;
import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.dao.mysql.AbstractDAO;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Prescription;
import com.corporation.pharmacy.entity.PrescriptionRequest;
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.User;

/**
 * Defines methods for working with table 'prescriptions'.
 */
public class PrescriptionDAOImpl extends AbstractDAO implements PrescriptionDAO {

    private static final Logger LOGGER = LogManager.getLogger(PrescriptionDAOImpl.class);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** MySQL Queries */

    private static final String ADD_GETTING_REQUEST = "INSERT INTO prescriptions (`id_user`, `id_product`, `request_date`) VALUES (?, ?, CURRENT_TIMESTAMP()) "
            + "ON DUPLICATE KEY UPDATE `id_user` = `id_user`;";
    private static final String ADD_EXTENDING_REQUEST = "UPDATE prescriptions SET `extending_request_status` = 'WAITING', `request_date` = CURRENT_TIMESTAMP() "
            + "WHERE `id_user` = ? AND `id_product` = ?;";

    private static final String GET_GETTING_REQUESTS = "SELECT u.id_user, u.name, u.middlename, u.surname, u.passport, u.telephone, p.id_product, p.name, p.form, prescriptions.request_date "
            + "FROM  prescriptions JOIN  users AS `u` USING (`id_user`) JOIN products_local AS `p` USING (`id_product`) "
            + "WHERE `getting_request_status` = 'WAITING' AND `locale` = ? ORDER BY `request_date` LIMIT ? OFFSET ?;";
    private static final String GET_EXTENDING_REQUESTS = "SELECT u.id_user, u.name, u.middlename, u.surname, u.passport, u.telephone, p.id_product, p.name, p.form, prescriptions.request_date "
            + "FROM  .prescriptions JOIN  users AS u USING (`id_user`) JOIN products_local AS p USING (`id_product`) "
            + "WHERE extending_request_status = 'WAITING'  AND `locale` = ? ORDER BY `request_date` LIMIT ? OFFSET ?;";

    private static final String GETTING_REQUESTS_COUNT = "SELECT COUNT(*) AS count FROM prescriptions WHERE `getting_request_status` = 'WAITING';";
    private static final String EXTENDING_REQUESTS_COUNT = "SELECT COUNT(*) AS count FROM prescriptions WHERE `extending_request_status` = 'WAITING';";

    private static final String SATISFY_GETTING_REQUEST = "UPDATE prescriptions SET `getting_request_status` = 'SATISFY', `date_start` = CURRENT_DATE(), "
            + "`date_end` = DATE_ADD(CURRENT_DATE(), INTERVAL 3 MONTH) WHERE `id_user` = ? AND `id_product` = ?;";
    private static final String SATISFY_EXTENDING_REQUEST = "UPDATE prescriptions SET `extending_request_status` = 'SATISFY', `date_start` = CURRENT_DATE(), "
            + "`date_end` = DATE_ADD(CURRENT_DATE(), INTERVAL 3 MONTH) WHERE `id_user` = ? AND `id_product` = ?;";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Defines the order of table columns */

    private static final int ID_USER = 1;
    private static final int USER_NAME = 2;
    private static final int UDER_MIDDLENAME = 3;
    private static final int USER_SURNAME = 4;
    private static final int PASSPORT = 5;
    private static final int TELEPHONE = 6;
    private static final int ID_PRODUCT = 7;
    private static final int PRODUCT_NAME = 8;
    private static final int FORM = 9;
    private static final int REQUEST_DATE = 10;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new PrescriptionDAOImpl for non transactional operations.
     */
    public PrescriptionDAOImpl() {
    }

    /**
     * Instantiates a new PrescriptionDAOImpl for transactional operations.
     *
     * @param connection
     *            the connection that can be transferred between different DAO
     */
    public PrescriptionDAOImpl(Connection connection) {
        super(connection);
    }

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
    @Override
    public void addPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            if (PrescriptionRequestType.GETTING == requestType) {
                statement = connection.prepareStatement(ADD_GETTING_REQUEST);
            } else if (PrescriptionRequestType.EXTENDING == requestType) {
                statement = connection.prepareStatement(ADD_EXTENDING_REQUEST);
            }
            statement.setInt(1, idUser);
            statement.setInt(2, idProduct);
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during adding prescription request to table 'prescriptions' in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
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
     * @return the List of prescription requests of defined
     *         <code>PrescriptionRequestType</code>. Returns empty list if there are
     *         no any prescription requests of the specified type.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<PrescriptionRequest> getPrescriptionRequests(LocaleType locale, int currentPage, int itemsPerPage,
            PrescriptionRequestType requestType) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<PrescriptionRequest> prescriptionRequests;
        try {
            connection = getConnection();
            if (PrescriptionRequestType.GETTING == requestType) {
                statement = connection.prepareStatement(GET_GETTING_REQUESTS);
            } else if (PrescriptionRequestType.EXTENDING == requestType) {
                statement = connection.prepareStatement(GET_EXTENDING_REQUESTS);
            }
            statement.setString(1, locale.name());
            statement.setInt(2, itemsPerPage);
            int startIndex = (currentPage - 1) * itemsPerPage;
            statement.setInt(3, startIndex);
            rs = statement.executeQuery();
            prescriptionRequests = formPrescriptionRequests(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during gettings portion of prescription requests from DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }

        return ListUtils.emptyIfNull(prescriptionRequests);
    }

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
    @Override
    public int getPrescriptionTotalCount(PrescriptionRequestType requestType) throws DaoException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            if (PrescriptionRequestType.GETTING == requestType) {
                rs = statement.executeQuery(GETTING_REQUESTS_COUNT);
            } else if (PrescriptionRequestType.EXTENDING == requestType) {
                rs = statement.executeQuery(EXTENDING_REQUESTS_COUNT);
            }
            rs.next();
            int prescriptionTotalCount = rs.getInt(1);
            return prescriptionTotalCount;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting total count of prescription requests from DB", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

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
    @Override
    public void satisfyPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            if (PrescriptionRequestType.GETTING == requestType) {
                statement = connection.prepareStatement(SATISFY_GETTING_REQUEST);
            } else if (PrescriptionRequestType.EXTENDING == requestType) {
                statement = connection.prepareStatement(SATISFY_EXTENDING_REQUEST);
            }
            statement.setInt(1, idUser);
            statement.setInt(2, idProduct);
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during change status of prescription request from 'waiting' to satisfying'.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

    private List<PrescriptionRequest> formPrescriptionRequests(ResultSet rs) throws SQLException {
        List<PrescriptionRequest> prescriptionRequests = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setIdUser(rs.getInt(ID_USER));
            user.setName(rs.getString(USER_NAME));
            user.setMiddleName(rs.getString(UDER_MIDDLENAME));
            user.setSurname(rs.getString(USER_SURNAME));
            user.setPassport(rs.getString(PASSPORT));
            user.setTelephone(rs.getString(TELEPHONE));
            Product product = new Product();
            product.setIdProduct(rs.getInt(ID_PRODUCT));
            product.setName(rs.getString(PRODUCT_NAME));
            product.setForm(rs.getString(FORM));
            Prescription prescription = new Prescription();
            prescription.setRequestDate(rs.getTimestamp(REQUEST_DATE));

            PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
            prescriptionRequest.setUser(user);
            prescriptionRequest.setProduct(product);
            prescriptionRequest.setPrescription(prescription);
            prescriptionRequests.add(prescriptionRequest);
        }
        return prescriptionRequests;
    }

}
