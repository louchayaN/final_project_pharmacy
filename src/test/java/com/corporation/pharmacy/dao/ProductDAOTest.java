package com.corporation.pharmacy.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocalProductInfo;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class ProductDAOTest {

    private static final ProductDAO productDAO = DAOFactory.getFactory().getNonTransactionalDAOManager().getProductDAO();

    private static final String GET_PRODUCT_NAME = "SELECT `name` FROM products_local WHERE `id_product` = ? AND `locale` = ?;";
    private static final String GET_PRODUCT_QUANTITY = "SELECT `pr_quantity` FROM products WHERE `id_product` = ?;";

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
    public void testAddProduct() throws DaoException, ConnectionPoolException, SQLException {
        LocalizedProduct product = initializeNewTestLocalizedProduct();

        Integer idProduct = productDAO.addProduct(product);
        Integer gottenProductQuantity = getProductQuantity(idProduct);

        assertEquals(product.getQuantity(), gottenProductQuantity);
    }

    @Test
    public void testAddLocalProductInfo() throws DaoException, ConnectionPoolException, SQLException {
        Integer idProduct = 14;
        LocalizedProduct product = initializeNewTestLocalizedProduct();

        productDAO.addLocalProductInfo(product.getProductInfoForDifferentLocales(), idProduct);
        String gottenProductFormRU = getProductName(LocaleType.RU_BY, idProduct);
        String gottenProductFormEN = getProductName(LocaleType.EN_US, idProduct);

        assertEquals(product.getProductInfoForDifferentLocales().get(0).getName(), gottenProductFormRU);
        assertEquals(product.getProductInfoForDifferentLocales().get(1).getName(), gottenProductFormEN);
    }

    private LocalizedProduct initializeNewTestLocalizedProduct() {
        LocalizedProduct product = new LocalizedProduct();
        product.setNeedPrescription(true);
        product.setQuantity(10);
        product.setPrice(new BigDecimal(10.10));

        List<LocalProductInfo> productInfoForDifferentLocales = new ArrayList<>();

        LocalProductInfo productInfoRU = new LocalProductInfo();
        productInfoRU.setLocale(LocaleType.RU_BY);
        productInfoRU.setName("тестПродукт");
        productInfoRU.setNonPatentName("продукт");
        productInfoRU.setProducer("Беларусь");
        productInfoRU.setForm("таблетки");
        productInfoRU.setInstructionFileName("product_ru.pdf");

        LocalProductInfo productInfoEN = new LocalProductInfo();
        productInfoEN.setLocale(LocaleType.EN_US);
        productInfoEN.setName("testProduct");
        productInfoEN.setNonPatentName("product");
        productInfoEN.setProducer("Belarus");
        productInfoEN.setForm("tablets");
        productInfoEN.setInstructionFileName("product_en.pdf");

        productInfoForDifferentLocales.add(productInfoRU);
        productInfoForDifferentLocales.add(productInfoEN);
        product.setProductInfoForDifferentLocales(productInfoForDifferentLocales);

        return product;
    }

    private String getProductName(LocaleType locale, Integer idProduct) throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(GET_PRODUCT_NAME);
            statement.setInt(1, idProduct);
            statement.setString(2, locale.name());
            rs = statement.executeQuery();
            rs.next();
            String productName = rs.getString(1);
            return productName;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @Test
    public void testGetProducts() throws DaoException {
        int currentPage = 2;
        int itemsPerPage = 2;
        LocaleType locale = LocaleType.RU_BY;
        Set<Integer> expectedProductsId = new HashSet<>(Arrays.asList(9, 1));

        List<Product> gottenProducts = productDAO.getProducts(locale, currentPage, itemsPerPage);
        Set<Integer> gottenProductsId = getProductsId(gottenProducts);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductsId, gottenProductsId));
    }

    private Set<Integer> getProductsId(List<Product> products) {
        Set<Integer> productsId = new HashSet<>();
        for (Product product : products) {
            productsId.add(product.getIdProduct());
        }
        return productsId;
    }

    @Test
    public void testGetProductTotalCount() throws DaoException {
        LocaleType locale = LocaleType.RU_BY;
        int expectedTotalCount = 13;

        int gottenProductTotalCount = productDAO.getProductTotalCount(locale);

        assertEquals(expectedTotalCount, gottenProductTotalCount);
    }

    @Test
    public void testGetProductsById() throws DaoException {
        Set<Integer> productsId = new HashSet<>(Arrays.asList(6, 7));
        LocaleType locale = LocaleType.RU_BY;
        List<String> expectedProductNames = new ArrayList<>(Arrays.asList("КСАРЕЛТО", "АЗИТРОМИЦИН"));

        List<Product> gottenProducts = productDAO.getProductsById(locale, productsId);
        List<String> gottenProductNames = getProductsNames(gottenProducts);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductNames, gottenProductNames));
    }

    private List<String> getProductsNames(List<Product> products) {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    @DataProvider
    public static Object[][] provideFindProductsByName() {
        return new Object[][] { { "милдронат", false, new ArrayList<>(Arrays.asList(10, 11)) },
                { "милдрон", false, new ArrayList<>(Arrays.asList(10, 11)) }, { "милдран", false, new ArrayList<>() },
                { "милдронат", true, new ArrayList<>(Arrays.asList(10, 11, 12, 13)) } };
    }

    @Test
    @UseDataProvider("provideFindProductsByName")
    public void testFindProductsByName(String productName, boolean includingAnalogs, List<Integer> expectedProductsId)
            throws DaoException {

        boolean searchingBySounding = false;
        List<Integer> gottenProductsId = findProducts(productName, includingAnalogs, searchingBySounding);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductsId, gottenProductsId));
    }

    @DataProvider
    public static Object[][] provideFindProductsBySounding() {
        return new Object[][] { { "азитрамецын", false, new ArrayList<>(Arrays.asList(7, 8)) },
                { "азитрамецын", true, new ArrayList<>(Arrays.asList(7, 8, 9)) } };
    }

    @Test
    @UseDataProvider("provideFindProductsBySounding")
    public void testFindProductsBySounding(String productName, boolean includingAnalogs, List<Integer> expectedProductsId)
            throws DaoException {

        boolean searchingBySounding = true;
        List<Integer> gottenProductsId = findProducts(productName, includingAnalogs, searchingBySounding);

        assertTrue(CollectionUtils.isEqualCollection(expectedProductsId, gottenProductsId));
    }

    private List<Integer> findProducts(String productName, boolean includingAnalogs, boolean searchBySounding)
            throws DaoException {
        List<Product> products = new ArrayList<>();
        if (searchBySounding) {
            products = productDAO.findProductsBySounding(productName, includingAnalogs);
        } else {
            products = productDAO.findProductsByName(productName, includingAnalogs);
        }
        List<Integer> gottenProductsId = new ArrayList<>();
        for (Product product : products) {
            gottenProductsId.add(product.getIdProduct());
        }
        return gottenProductsId;
    }

    @Test
    public void testUpdateProduct() throws DaoException, ConnectionPoolException, SQLException {
        Product product = initializeNewProduct();

        productDAO.updateProduct(product);
        Integer gottenProductQuantity = getProductQuantity(product.getIdProduct());

        assertEquals(product.getQuantity(), gottenProductQuantity);
    }

    private Product initializeNewProduct() {
        Product product = new Product();
        product.setIdProduct(6);
        product.setName("тестПродукт");
        product.setNonPatentName("продукт");
        product.setProducer("Беларусь");
        product.setForm("таблетки");
        product.setInstructionFileName("product_ru.pdf");
        product.setNeedPrescription(true);
        product.setQuantity(10);
        product.setPrice(new BigDecimal(10.10));
        product.setLocale(LocaleType.RU_BY);
        return product;
    }

    private Integer getProductQuantity(Integer idProduct) throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(GET_PRODUCT_QUANTITY);
            statement.setInt(1, idProduct);
            rs = statement.executeQuery();
            rs.next();
            Integer productQuantity = rs.getInt(1);
            return productQuantity;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @Test
    public void testDecreaseQuantityOnBuyedProducts() throws DaoException, ConnectionPoolException, SQLException {
        Integer idUser = 4;
        Integer idProduct = 7;
        Integer expectedProductQuantity = 10;

        productDAO.decreaseQuantityOnBuyedProducts(idUser);
        Integer gottenProductQuantity = getProductQuantity(idProduct);

        assertEquals(expectedProductQuantity, gottenProductQuantity);
    }

}
