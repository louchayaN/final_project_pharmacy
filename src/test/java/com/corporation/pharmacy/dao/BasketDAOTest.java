package com.corporation.pharmacy.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class BasketDAOTest {

    private static final BasketDAO basketDAO = DAOFactory.getFactory().getNonTransactionalDAOManager().getBasketDAO();

    private static final String GET_BASKET_ITEM_QUANTITY = "SELECT `basket_quantity` FROM basket WHERE `id_product` = ?";
    private static final String GET_ALL_BASKET_ITEMS = "SELECT * FROM basket";

    private static final int ID_USER = 1;
    private static final int ID_PRODUCT = 2;
    private static final int BASKET_QUANTITY = 3;

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

    @DataProvider
    public static Object[][] provideBasketItems() {
        BasketItem totallyNewItem = new BasketItem();
        totallyNewItem.setIdUser(3);
        totallyNewItem.setIdProduct(1);
        totallyNewItem.setQuantity(10);

        BasketItem existingInBasketItem = new BasketItem();
        existingInBasketItem.setIdUser(1);
        existingInBasketItem.setIdProduct(7);
        existingInBasketItem.setQuantity(10);
        int existingInBasketItemQuantity = 20;

        return new Object[][] { { totallyNewItem, totallyNewItem.getQuantity() },
                { existingInBasketItem, existingInBasketItem.getQuantity() + existingInBasketItemQuantity } };
    }

    @Test
    @UseDataProvider("provideBasketItems")
    public void testAddBasketItem(BasketItem basketItem, Integer expectedQuantity)
            throws DaoException, ConnectionPoolException, SQLException {

        basketDAO.addBasketItem(basketItem);
        Integer gottenQuantity = getBasketItemQuantity(basketItem.getIdProduct());

        assertEquals(expectedQuantity, gottenQuantity);
    }

    @Test
    public void testAddAllBasketItems() throws DaoException, ConnectionPoolException, SQLException {
        Integer idUser = 1;
        Map<Integer, Integer> basketItems = initializeNewTestBasket();
        List<Integer> expectedQuantity = new ArrayList<>(Arrays.asList(5, 25));

        basketDAO.addAllBasketItems(basketItems, idUser);
        List<Integer> gottenBasketItemsQuantity = getBasketItemsQuantity(basketItems.keySet());

        assertTrue(CollectionUtils.isEqualCollection(expectedQuantity, gottenBasketItemsQuantity));
    }

    private Map<Integer, Integer> initializeNewTestBasket() {
        Map<Integer, Integer> basketItems = new HashMap<>();
        Integer totallyNewItemId = 3;
        Integer existingInBasketItemId = 7;
        Integer quantity = 5;
        basketItems.put(totallyNewItemId, quantity);
        basketItems.put(existingInBasketItemId, quantity);
        return basketItems;
    }

    private List<Integer> getBasketItemsQuantity(Set<Integer> idProducts) throws ConnectionPoolException, SQLException {
        List<Integer> basketItemsQuantity = new ArrayList<>();
        for (Integer idProduct : idProducts) {
            Integer basketItemQuantity = getBasketItemQuantity(idProduct);
            basketItemsQuantity.add(basketItemQuantity);
        }
        return basketItemsQuantity;
    }

    private Integer getBasketItemQuantity(Integer idProduct) throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(GET_BASKET_ITEM_QUANTITY);
            statement.setInt(1, idProduct);
            rs = statement.executeQuery();
            rs.next();
            if (rs.getRow() == 0) {
                return null;
            }
            Integer quantity = rs.getInt(1);
            return quantity;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @Test
    public void testGetBasket() throws DaoException {
        Integer idUser = 1;
        LocaleType locale = LocaleType.RU_BY;
        Set<Integer> expectedProductsId = new HashSet<>(Arrays.asList(7, 6, 2));

        List<BasketTO> basket = basketDAO.getBasket(locale, idUser);
        Set<Integer> gottenProductsId = getProductsId(basket);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductsId, gottenProductsId));
    }

    private Set<Integer> getProductsId(List<BasketTO> basket) {
        Set<Integer> productsId = new HashSet<>(Arrays.asList(7, 6, 2));
        for (BasketTO basketItem : basket) {
            Integer idProduct = basketItem.getProduct().getIdProduct();
            productsId.add(idProduct);
        }
        return productsId;
    }

    @Test
    public void testUpdateQuantityInBasket() throws DaoException, ConnectionPoolException, SQLException {
        BasketItem basketItem = initializeNewTestBasketItem();
        Integer expectedBasketItemQuantity = basketItem.getQuantity();

        basketDAO.updateQuantityInBasket(basketItem);
        Integer gottenBasketItemQuantity = getBasketItemQuantity(basketItem.getIdProduct());

        assertEquals(expectedBasketItemQuantity, gottenBasketItemQuantity);
    }

    private BasketItem initializeNewTestBasketItem() {
        BasketItem basketItem = new BasketItem();
        basketItem.setIdUser(1);
        basketItem.setIdProduct(6);
        basketItem.setQuantity(15);
        return basketItem;
    }

    @Test
    public void testDeleteBasketItem() throws DaoException, ConnectionPoolException, SQLException {
        BasketItem basketItem = initializeNewTestBasketItem();

        basketDAO.deleteBasketItem(basketItem);
        Integer quantity = getBasketItemQuantity(basketItem.getIdProduct());

        Assert.assertNull(quantity);
    }

    @Test
    public void testDeleteAllBasketItems() throws DaoException, ConnectionPoolException, SQLException {
        Integer idUser = 1;
        List<BasketItem> epectedBasketItems = new ArrayList<>(Arrays.asList(new BasketItem(4, 7, 50)));

        basketDAO.deleteAllBasketItems(idUser);
        List<BasketItem> gottenBasketItems = getAllBasketItems();

        assertTrue(gottenBasketItems.equals(epectedBasketItems));
    }

    private List<BasketItem> getAllBasketItems() throws ConnectionPoolException, SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(GET_ALL_BASKET_ITEMS);
            List<BasketItem> basketItems = new ArrayList<>();
            while (rs.next()) {
                BasketItem basketItem = new BasketItem();
                basketItem.setIdUser(rs.getInt(ID_USER));
                basketItem.setIdProduct(rs.getInt(ID_PRODUCT));
                basketItem.setQuantity(rs.getInt(BASKET_QUANTITY));
                basketItems.add(basketItem);
            }
            return basketItems;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

}
