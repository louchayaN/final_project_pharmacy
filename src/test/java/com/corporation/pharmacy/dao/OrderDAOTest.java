package com.corporation.pharmacy.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;

public class OrderDAOTest {

    private static final OrderDAO orderDAO = DAOFactory.getFactory().getNonTransactionalDAOManager().getOrderDAO();

    private static final String GET_ID_OF_ORDERED_PRODUCTS = "SELECT `id_product` FROM orders WHERE `id_user` = ?";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestUtil.initializeConnectionPool();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ConnectionPool.getInstance().destroy();
    }

    @Before
    public void setUp() throws Exception {
        TestUtil.initializeDB();
    }

    @Test
    public void testFormOrderFromBasket() throws DaoException, ConnectionPoolException, SQLException {
        Integer idUser = 1;
        List<Integer> expectedIdProducts = new ArrayList<>(Arrays.asList(7, 6, 2));

        orderDAO.formOrderFromBasket(idUser);
        List<Integer> gottenIdProducts = getIdOfOrderedProducts(idUser);

        assertTrue(CollectionUtils.isEqualCollection(expectedIdProducts, gottenIdProducts));
    }

    private List<Integer> getIdOfOrderedProducts(Integer idUser) throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(GET_ID_OF_ORDERED_PRODUCTS);
            statement.setInt(1, idUser);
            rs = statement.executeQuery();
            List<Integer> idProducts = new ArrayList<>();
            while (rs.next()) {
                idProducts.add(rs.getInt(1));
            }
            return idProducts;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @Test
    public void testGetOrders() throws DaoException {
        Integer idUser = 2;
        LocaleType locale = LocaleType.RU_BY;
        List<String> expectedProductsNames = new ArrayList<>(Arrays.asList("КСАРЕЛТО", "АЗИТРОМИЦИН"));

        List<Order> orders = orderDAO.getOrders(locale, idUser);
        List<String> gottenProductsNames = getNamesOfOrderedProducts(orders);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductsNames, gottenProductsNames));
    }

    private List<String> getNamesOfOrderedProducts(List<Order> orders) {
        List<String> productsNames = new ArrayList<>();
        for (Order order : orders) {
            productsNames.add(order.getOrderedProduct().getName());
        }
        return productsNames;
    }

}
