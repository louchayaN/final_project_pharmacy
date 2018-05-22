package com.corporation.pharmacy.dao.mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.ProductDAO;
import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.dao.mysql.AbstractDAO;
import com.corporation.pharmacy.entity.LocalProductInfo;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;

/**
 * Defines methods for work with 'products' and 'products_local' data base
 * tables.
 */
public class ProductDAOImpl extends AbstractDAO implements ProductDAO {

    private static final Logger LOGGER = LogManager.getLogger(ProductDAOImpl.class);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String ADD_PRODUCT = "INSERT INTO products (`is_need_prescription`, `pr_quantity`, `price`) VALUES (?, ?, ?)";

    private static final String ADD_PRODUCT_LOCAL_INFO = "INSERT INTO products_local (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_PRODUCT = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `id_product` = ? AND `locale` = ?;";
    private static final String GET_ALL_PRODUCTS = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `locale` = ? ORDER BY `name`LIMIT ? OFFSET ?";
    private static final String GET_PRODUCTS_TOTAL_COUNT = "SELECT COUNT(*) FROM `products_local` WHERE `locale` = ?";

    private static final String FIND_PRODUCTS = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `name` LIKE ? ;";
    private static final String FIND_PRODUCT_WITH_ANALOGS = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `non_patent_name` IN(SELECT `non_patent_name` FROM products_local WHERE `name` LIKE ?);";
    private static final String FIND_BY_SOUNDING = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE SOUNDEX(`name`) =  SOUNDEX(?);";
    private static final String FIND_BY_SOUNDING_WITH_ANALOGS = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `non_patent_name` IN(SELECT `non_patent_name` FROM products_local WHERE SOUNDEX(`name`) =  SOUNDEX(?));";

    private static final String DROP_TEMPORARY_TABLE = "DROP TEMPORARY TABLE IF EXISTS temporary_values;";
    private static final String CREATE_TEMPORARY_TABLE = "CREATE TEMPORARY TABLE IF NOT EXISTS temporary_values (`product_id` INT(11) NOT NULL);";
    private static final String INSERT_IDs_IN_TEMPORARY_TABLE = "INSERT INTO temporary_values (`product_id`) VALUES (?);";
    private static final String GET_PRODUCTS_BY_ID_FROM_TEMPORARY_TABLE = "SELECT `id_product`, `is_need_prescription`, `pr_quantity`, `price`, `name`, `non_patent_name`, `producer`, `form`, `instruction` "
            + "FROM products JOIN products_local USING (`id_product`) WHERE `id_product` IN(SELECT `product_id` FROM temporary_values) AND `locale` = ?;";

    private static final String UPDATE_PRODUCT = "UPDATE products SET `is_need_prescription` = ?, `pr_quantity` = ?, `price` = ? WHERE `id_product` = ?;";
    private static final String UPDATE_PRODUCT_LOCALE = "UPDATE products_local  SET `name` = ?, `non_patent_name` = ?, `producer` = ?, `form` = ?, `instruction` = ? WHERE `id_product` = ? AND `locale` = ?;";

    private static final String CHANGE_QUANTITY_ON_BUYED_PRODUCTS = "UPDATE products, basket SET `products`.`pr_quantity` = `products`.`pr_quantity` - `basket`.`basket_quantity` "
            + "WHERE `products`.`id_product` = `basket`.`id_product` AND `basket`.`id_user` = ?;";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int ID_PRODUCT = 1;
    private static final int IS_NEED_PRESCRIPTION = 2;
    private static final int PR_QUANTITY = 3;
    private static final int PRICE = 4;
    private static final int NAME = 5;
    private static final int NON_PATENT_NAME = 6;
    private static final int PRODUCER = 7;
    private static final int FORM = 8;
    private static final int INSTRUCTION_FILE_NAME = 9;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new ProductDAOImpl for non transactional operations.
     */
    public ProductDAOImpl() {
    }

    /**
     * Instantiates a new ProductDAOImpl for transactional operations.
     *
     * @param connection
     *            the connection that can be transferred between different DAO
     */
    public ProductDAOImpl(Connection connection) {
        super(connection);
    }

    /**
     * Adds a new product consisting non localized (non String) information to the
     * data base. Returns product id generated by the data base.
     *
     * @param localizedProduct
     *            the localized product
     * @return the id of added product generated by the data base
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public Integer addProduct(LocalizedProduct localizedProduct) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, localizedProduct.getNeedPrescription());
            statement.setInt(2, localizedProduct.getQuantity());
            statement.setBigDecimal(3, localizedProduct.getPrice());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            rs.next();
            Integer idProduct = rs.getInt(1);
            return idProduct;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during adding a new product to table 'products' in DB.", e);
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
     * Adds product info in the different locales (languages) to the product with
     * the specified id.
     *
     * @param productInfoForDifferentLocales
     *            product info in the different locales (languages)
     * @param idProduct
     *            the id product
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void addLocalProductInfo(List<LocalProductInfo> productInfoForDifferentLocales, Integer idProduct)
            throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD_PRODUCT_LOCAL_INFO);
            for (LocalProductInfo productInfo : productInfoForDifferentLocales) {
                statement.setInt(1, idProduct);
                statement.setString(2, productInfo.getLocale().name());
                statement.setString(3, productInfo.getName());
                statement.setString(4, productInfo.getNonPatentName());
                statement.setString(5, productInfo.getProducer());
                statement.setString(6, productInfo.getForm());
                statement.setString(7, productInfo.getInstructionFileName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during adding a new product local info to table 'products_local' in DB.", e);
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
     * Gets the product by id for this locale.
     *
     * @param locale
     *            the locale (language)
     * @param idProduct
     *            the id product
     * @return the product consisting info according defined locale
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public Product getProduct(LocaleType locale, Integer idProduct) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_PRODUCT);
            statement.setInt(1, idProduct);
            statement.setString(2, locale.name());
            rs = statement.executeQuery();
            rs.next();
            return formProduct(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting product by id from DB.", e);
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
     * Gets the products List for current page, according specified quantity of
     * items per page and locale (language). Returns empty List if there are no
     * products.
     *
     * @param locale
     *            the locale (language)
     * @param currentPage
     *            the current page
     * @param itemsPerPage
     *            the items per page
     * @return the products for current page consisting info according defined
     *         locale. Returns empty List if there are no products
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<Product> getProducts(LocaleType locale, int currentPage, int itemsPerPage) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        List<Product> products;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_ALL_PRODUCTS);
            statement.setString(1, locale.name());
            statement.setInt(2, itemsPerPage);
            int startIndex = (currentPage - 1) * itemsPerPage;
            statement.setInt(3, startIndex);
            rs = statement.executeQuery();
            products = formProducts(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting portion of products from DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }

        return ListUtils.emptyIfNull(products);
    }

    /**
     * Returns the total count of products that there are in the data base in the
     * specified <code>locale</code>.
     *
     * @param locale
     *            the locale (language)
     * @return the total count of products in the data base in the defined locale
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public int getProductTotalCount(LocaleType locale) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(GET_PRODUCTS_TOTAL_COUNT);
            statement.setString(1, locale.name());
            rs = statement.executeQuery();
            rs.next();
            int productTotalCount = rs.getInt(1);
            return productTotalCount;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting total count of products from DB.", e);
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
     * Gets the List of products from the data base by their <code>ids</code>
     * consisting info according with defined <code>locale</code>.
     *
     * @param locale
     *            the locale (language)
     * @param productsId
     *            the products id
     * @return products found by their ids in the data base and consisting info
     *         according with defined <code>locale</code>.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<Product> getProductsById(LocaleType locale, Set<Integer> productsId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        PreparedStatement statement4 = null;
        ResultSet rs = null;
        try {
            connection = getConnection();

            statement1 = connection.prepareStatement(DROP_TEMPORARY_TABLE);
            statement1.executeUpdate();

            statement2 = connection.prepareStatement(CREATE_TEMPORARY_TABLE);
            statement2.executeUpdate();

            statement3 = connection.prepareStatement(INSERT_IDs_IN_TEMPORARY_TABLE);
            for (Integer productId : productsId) {
                statement3.setInt(1, productId);
                statement3.addBatch();
            }
            statement3.executeBatch();

            statement4 = connection.prepareStatement(GET_PRODUCTS_BY_ID_FROM_TEMPORARY_TABLE);
            statement4.setString(1, locale.name());
            rs = statement4.executeQuery();

            List<Product> products = formProducts(rs);
            return products;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during getting info about products in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement1, statement2, statement3, statement4);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

    /**
     * Finds products by name. If <code>includingAnalogs</code> is true to the List
     * of found products will be also included analogues by the non patent name of
     * the product. If <code>includingAnalogs</code> is false the searching will be
     * in strict accordance with the defined <code>product name</code>. Returns the
     * List of found products. The List will be empty if no products has been found.
     *
     * @param productName
     *            the product name
     * @param includingAnalogs
     *            the boolean parameter defines if in the List of found products
     *            will be also included analogues found by the non patent name of
     *            the product.
     * @return the List of found products. The List will be empty if no one product
     *         has been found.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<Product> findProductsByName(String productName, boolean includingAnalogs) throws DaoException {
        String searchParam = "%" + productName + "%";
        if (includingAnalogs) {
            return findProducts(FIND_PRODUCT_WITH_ANALOGS, searchParam);
        }
        return findProducts(FIND_PRODUCTS, searchParam);
    }

    /**
     * Finds products by sounding. If <code>includingAnalogs</code> is true to the
     * List of found products will be also included analogues by the non patent name
     * of the product. If <code>includingAnalogs</code> is false the searching will
     * not include analogues. Returns the List of found products. The List will be
     * empty if no one product has been found.
     *
     * @param productName
     *            the product name
     * @param includingAnalogs
     *            the boolean parameter defines if in the List of found products
     *            will be also included analogues found by the non patent name of
     *            the product.
     * @return the List of found products. The List will be empty if no one product
     *         has been found
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public List<Product> findProductsBySounding(String productName, boolean includingAnalogs) throws DaoException {
        if (includingAnalogs) {
            return findProducts(FIND_BY_SOUNDING_WITH_ANALOGS, productName);
        }
        return findProducts(FIND_BY_SOUNDING, productName);
    }

    private List<Product> findProducts(String sqlQuery, String searchParam) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Product> foundProducts;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, searchParam);
            rs = statement.executeQuery();
            foundProducts = formProducts(rs);
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during finding products in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(rs, statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }

        return ListUtils.emptyIfNull(foundProducts);
    }

    /**
     * Updates all info about the product.
     *
     * @param product
     *            the product
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void updateProduct(Product product) throws DaoException {
        Connection connection = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            statement1 = connection.prepareStatement(UPDATE_PRODUCT);
            statement1.setBoolean(1, product.getNeedPrescription());
            statement1.setInt(2, product.getQuantity());
            statement1.setBigDecimal(3, product.getPrice());
            statement1.setInt(4, product.getIdProduct());
            statement1.executeUpdate();

            statement2 = connection.prepareStatement(UPDATE_PRODUCT_LOCALE);
            statement2.setString(1, product.getName());
            statement2.setString(2, product.getNonPatentName());
            statement2.setString(3, product.getProducer());
            statement2.setString(4, product.getForm());
            statement2.setString(5, product.getInstructionFileName());
            statement2.setInt(6, product.getIdProduct());
            statement2.setString(7, product.getLocale().name());
            statement2.executeUpdate();

            connection.commit();
        } catch (SQLException | ConnectionPoolException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DaoException("Exception during rollback transaction of replacing product.", e1);
            }
            throw new DaoException("Exception during replacing product in tables 'products' and 'products_local' in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(statement1, statement2);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

    /**
     * Decrease quantity of products in the data base on the quantity of products
     * that was buyed by user with this <code>id</code>.
     *
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    @Override
    public void decreaseQuantityOnBuyedProducts(Integer idUser) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(CHANGE_QUANTITY_ON_BUYED_PRODUCTS);
            statement.setInt(1, idUser);
            statement.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException("Exception during changing product quantity in table 'products' in DB.", e);
        } finally {
            try {
                closeNonTransactionalConnection(connection);
                ConnectionPool.getInstance().closeDBResources(statement);
            } catch (SQLException e) {
                LOGGER.warn("Exception during closing DB resources.", e);
            }
        }
    }

    private List<Product> formProducts(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product product = formProduct(rs);
            products.add(product);
        }
        return products;
    }

    private Product formProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setIdProduct(rs.getInt(ID_PRODUCT));
        product.setNeedPrescription(rs.getBoolean(IS_NEED_PRESCRIPTION));
        product.setQuantity(rs.getInt(PR_QUANTITY));
        product.setPrice(rs.getBigDecimal(PRICE));
        product.setName(rs.getString(NAME));
        product.setNonPatentName(rs.getString(NON_PATENT_NAME));
        product.setProducer(rs.getString(PRODUCER));
        product.setForm(rs.getString(FORM));
        product.setInstructionFileName(rs.getString(INSTRUCTION_FILE_NAME));
        return product;
    }

}
