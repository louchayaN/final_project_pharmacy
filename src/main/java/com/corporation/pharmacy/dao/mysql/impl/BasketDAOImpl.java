package com.corporation.pharmacy.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.BasketDAO;
import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.dao.mysql.AbstractDAO;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Prescription;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.Status;
import com.corporation.pharmacy.entity.dto.BasketTO;

/**
 * Defines methods for working with table 'basket'.
 */
public class BasketDAOImpl extends AbstractDAO implements BasketDAO {

    private static final Logger LOGGER = LogManager.getLogger(BasketDAOImpl.class);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** MySQL Queries */

    private static final String INSERT_OR_UPDATE_QUANTITY = "INSERT INTO basket (`id_user`, `id_product`, `basket_quantity`) VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE `basket_quantity` = `basket_quantity`+ ?;";

    private static final String GET_USER_BASKET = "SELECT `products`.`id_product`, `products_local`.`name`, `products_local`.`form`, `products`.`is_need_prescription`, `products`.`pr_quantity`, "
            + "`products`.`price`, `basket`.`basket_quantity`, `prescriptions`.`date_end`, `prescriptions`.`extending_request_status`, `prescriptions`.`getting_request_status` "
            + "FROM  basket JOIN  products USING (`id_product`) JOIN products_local USING (`id_product`) LEFT JOIN prescriptions USING (`id_product`, `id_user`) WHERE `id_user`= ? AND `locale` = ?;";

    private static final String GET_BASKET_ITEM_COUNT_THAT_NEED_PRESCRIPTION = "SELECT COUNT(*) FROM basket JOIN products USING (`id_product`) LEFT JOIN prescriptions USING (`id_product`, `id_user`) "
            + "WHERE `basket`.`id_user` = ? AND `products`.`is_need_prescription` = 1  AND (`getting_request_status` IS NULL OR 'WAITING' OR `date_end` < CURRENT_DATE());";

    private static final String CHANGE_BASKET_ITEM_QUANITY = "UPDATE basket SET `basket_quantity` = ? WHERE `id_user`=? AND `id_product`=?;";

    private static final String DELETE_BASKET_ITEM = "DELETE FROM basket WHERE `id_user`= ? AND `id_product`= ?;";
    private static final String DELETE_ALL_BASKET_ITEMS = "DELETE FROM basket WHERE `id_user`= ?;";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Defines the order of table columns */

    private static final int ID_PRODUCT = 1;
    private static final int NAME = 2;
    private static final int FORM = 3;
    private static final int IS_NEED_PRESCRIPTION = 4;
    private static final int PR_QUANTITY = 5;
    private static final int PRICE = 6;
    private static final int BASKET_QUANTITY = 7;
    private static final int DATE_END = 8;
    private static final int EXTENDING_REQUEST_STATUS = 9;
    private static final int GETTING_REQUEST_STATUS = 10;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new BasketDAOImpl for non transactional operations.
     */
    public BasketDAOImpl() {
    }

    /**
     * Instantiates a new BasketDAOImpl for transactional operations.
     *
     * @param connection
     *            the connection that can be transferred between different DAO
     */
    public BasketDAOImpl(Connection connection) {
        super(connection);
    }

    /**
     * Adds {@code  basketItem} to the data base or if it is already in user's
     * basket increases only its quantity.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void addBasketItem(BasketItem basketItem) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(INSERT_OR_UPDATE_QUANTITY);
            statement.setInt(1, basketItem.getIdUser());
            statement.setInt(2, basketItem.getIdProduct());
            statement.setInt(3, basketItem.getQuantity());
            statement.setInt(4, basketItem.getQuantity());
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during adding a new item to table 'basket' in DB.", e);
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
     * Adds all basket items to user's basket. Or if one of them is already in
     * user's basket increases only their quantity.
     *
     * @param basket
     *            the basket of user that need to be added to the data base
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void addAllBasketItems(Map<Integer, Integer> basket, Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(INSERT_OR_UPDATE_QUANTITY);
            for (Map.Entry<Integer, Integer> entry : basket.entrySet()) {
                statement.setInt(1, idUser);
                statement.setInt(2, entry.getKey());
                statement.setInt(3, entry.getValue());
                statement.setInt(4, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException | ConnectionPoolException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException("Exception during rollback.", e);
            }
            throw new DaoException("Exception during inserting basket items to DB.", e);
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
     * Returns the basket of the user (specified by {@code idUser}) according with
     * the {@code locale}. Returns empty List if user's basket is empty.
     *
     * @param locale
     *            the locale
     * @param idUser
     *            the id of user
     * @return the basket of specified user representing as List of {@link BasketTO}
     *         objects consisting info according specified locale. Returns empty
     *         List if user's basket is empty
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<BasketTO> getBasket(LocaleType locale, Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<BasketTO> basket;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_USER_BASKET);
            statement.setInt(1, idUser);
            statement.setString(2, locale.name());
            rs = statement.executeQuery();
            basket = formBasket(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting user basket from DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }

        return ListUtils.emptyIfNull(basket);
    }

    /**
     * Returns the quantity of basket items in the basket of the specified user (by
     * {@code idUser}) that need getting or extending prescription.
     * 
     * @return the quantity of basket items in the basket of the specified user (by
     *         {@code idUser}) that need getting or extending prescription.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public int getBasketItemsCountThatNeedPrescription(Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_BASKET_ITEM_COUNT_THAT_NEED_PRESCRIPTION);
            statement.setInt(1, idUser);
            rs = statement.executeQuery();
            rs.next();
            int prescriptionCount = rs.getInt(1);
            return prescriptionCount;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting from DB total count of basket items that need prescription.", e);
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
     * Updates quantity of the specified basket item in user's basket.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void updateQuantityInBasket(BasketItem basketItem) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(CHANGE_BASKET_ITEM_QUANITY);
            statement.setInt(1, basketItem.getQuantity());
            statement.setInt(2, basketItem.getIdUser());
            statement.setInt(3, basketItem.getIdProduct());
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during updating product quantity in table 'basket' in DB.", e);
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
     * Deletes specified basket item from the basket.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void deleteBasketItem(BasketItem basketItem) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(DELETE_BASKET_ITEM);
            statement.setInt(1, basketItem.getIdUser());
            statement.setInt(2, basketItem.getIdProduct());
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during deleting product from table 'basket' in DB.", e);
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
     * Deletes all basket items from the basket of the specified user.
     *
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void deleteAllBasketItems(Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(DELETE_ALL_BASKET_ITEMS);
            statement.setInt(1, idUser);
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during deleting user basket items from table 'basket' in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

    private List<BasketTO> formBasket(ResultSet rs) throws SQLException {
        List<BasketTO> basket = new ArrayList<>();
        while (rs.next()) {
            BasketTO basketItem = new BasketTO();

            Product product = new Product();
            product.setIdProduct(rs.getInt(ID_PRODUCT));
            product.setName(rs.getString(NAME));
            product.setForm(rs.getString(FORM));

            product.setNeedPrescription(rs.getBoolean(IS_NEED_PRESCRIPTION));
            product.setQuantity(rs.getInt(PR_QUANTITY));
            product.setPrice(rs.getBigDecimal(PRICE));
            basketItem.setProduct(product);
            basketItem.setOrderedQuantity(rs.getInt(BASKET_QUANTITY));

            Prescription prescription = new Prescription();
            prescription.setDateEnd(rs.getDate(DATE_END));
            String extendingStatus = rs.getString(EXTENDING_REQUEST_STATUS);
            if (extendingStatus != null) {
                prescription.setExtendingStatus(Status.valueOf(extendingStatus));
            }
            String gettingStatus = rs.getString(GETTING_REQUEST_STATUS);
            if (gettingStatus != null) {
                prescription.setGettingStatus(Status.valueOf(gettingStatus));
            }
            basketItem.setPrescription(prescription);
            basket.add(basketItem);
        }
        return basket;
    }

}
