package com.corporation.pharmacy.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.OrderDAO;
import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.dao.mysql.AbstractDAO;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;
import com.corporation.pharmacy.entity.Product;

/**
 * Defines methods for working with table 'orders' in the data base.
 */
public class OrderDAOImpl extends AbstractDAO implements OrderDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String ADD_ORDER = "INSERT INTO orders (`id_user`, `id_product`, `order_quantity`, `order_price`, `order_date`) SELECT `id_user`, `id_product`, `basket_quantity`, `price`, CURRENT_TIMESTAMP()"
            + "FROM basket JOIN products USING(`id_product`) WHERE `id_user` = ?;";

    private static final String GET_ORDERS = "SELECT `products_local`.`name`, `orders`.`order_quantity`, `orders`.`order_price`, `orders`.`order_date` "
            + "FROM orders JOIN products_local USING (`id_product`) WHERE `id_user` = ? AND `locale` = ?;";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int PRODUCT_NAME = 1;
    private static final int ORDER_QUANTITY = 2;
    private static final int ORDER_PRICE = 3;
    private static final int ORDER_DATE = 4;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new OrderDAOImpl for non transactional operations.
     */
    public OrderDAOImpl() {
    }

    /**
     * Instantiates a new OrderDAOImpl for transactional operations.
     *
     * @param connection
     *            the connection that can be transferred between different DAO
     */
    public OrderDAOImpl(Connection connection) {
        super(connection);
    }

    /**
     * Copies all items from the basket of the user with specified <code>id</code>
     * to table 'orders' in the data base.
     *
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void formOrderFromBasket(Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD_ORDER);
            statement.setInt(1, idUser);
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during adding order to table 'orders' in DB.", e);
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
     * Returns the orders of the user with the specified <code>id</code> consisting
     * info in according with the <code>locale</code>. Returns empty List if the
     * user don't have any orders.
     *
     * @param locale
     *            the locale (language)
     * @param idUser
     *            the id of user
     * @return the List of user orders. Returns empty List if the user don't have
     *         any orders.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<Order> getOrders(LocaleType locale, Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<Order> orders;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ORDERS);
            statement.setInt(1, idUser);
            statement.setString(2, locale.name());
            rs = statement.executeQuery();
            orders = formOrders(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting orders by user id from DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }

        return ListUtils.emptyIfNull(orders);
    }

    private List<Order> formOrders(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order order = new Order();
            Product product = new Product();
            product.setName(rs.getString(PRODUCT_NAME));
            order.setOrderedProduct(product);
            order.setOrderedQuantity(rs.getInt(ORDER_QUANTITY));
            order.setPrice(rs.getBigDecimal(ORDER_PRICE));
            order.setDate(rs.getTimestamp(ORDER_DATE));
            orders.add(order);
        }
        return orders;
    }

}
