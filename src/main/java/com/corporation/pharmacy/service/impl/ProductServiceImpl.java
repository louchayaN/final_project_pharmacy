package com.corporation.pharmacy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.ProductDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;
import com.corporation.pharmacy.entity.dto.ProductsTO;
import com.corporation.pharmacy.service.ProductService;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;
import com.corporation.pharmacy.service.validation.Validator;

/**
 * It is a class for working with products.
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LogManager.getLogger(ProductServiceImpl.class);

    private static final DAOManager daoManager = DAOFactory.getFactory().getNonTransactionalDAOManager();
    private static final ProductDAO productDAO = daoManager.getProductDAO();

    /**
     * Adds a new localized product (product consisting information in different
     * locales).
     *
     * @param localizedProduct
     *            the localized product (product consisting information in different
     *            locales).
     * @throws ValidationException
     *             the exception during validation product parameters
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void addProduct(LocalizedProduct localizedProduct) throws ValidationException, ServiceException {
        if ( ! Validator.isProductDataValid(localizedProduct)) {
            throw new ValidationException("Sent data isn't valid");
        }

        DAOManager transactionalDAO = DAOFactory.getFactory().getTransactionalDAOManager();
        try {
            transactionalDAO.startTransaction();
            ProductDAO productDAO = transactionalDAO.getProductDAO();

            Integer idProduct = productDAO.addProduct(localizedProduct);
            productDAO.addLocalProductInfo(localizedProduct.getProductInfoForDifferentLocales(), idProduct);

            transactionalDAO.commit();
        } catch (DaoException e) {
            try {
                transactionalDAO.rollback();
            } catch (DaoException e1) {
                throw new ServiceException("Exception during rollback transaction of adding product.", e);
            }
            throw new ServiceException("Exception during adding new product for differnet locales.", e);
        } finally {
            try {
                transactionalDAO.close();
            } catch (DaoException e) {
                LOGGER.warn("Exception during closing transactional DAO.", e);
            }
        }
    }

    /**
     * Gets and returns the product by specified {@code id} in accordance with
     * specified {@code locale}.
     *
     * @param locale
     *            the locale (language)
     * @param idProduct
     *            the id of product
     * @return the product
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public Product getProduct(LocaleType locale, Integer idProduct) throws ServiceException {
        Product product;
        try {
            product = productDAO.getProduct(locale, idProduct);
        } catch (DaoException e) {
            throw new ServiceException("Exception during getting a product by id.", e);
        }
        return product;
    }

    /**
     * Gets and returns the products for view for specified {@code current page} and
     * {@code items per page} in accordance with specified {@code locale}.
     *
     * @param locale
     *            the locale
     * @param currentPage
     *            the current page
     * @param itemsPerPage
     *            the quantity of viewed items per page
     * @return the {link ProductTO} object consisting the List of products for view
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public ProductsTO getProductsforView(LocaleType locale, int currentPage, int itemsPerPage) throws ServiceException {
        ProductsTO productsTO = null;
        try {
            int productTotalCount = productDAO.getProductTotalCount(locale);
            int totalPageCount = (int) Math.ceil((double) productTotalCount / itemsPerPage);

            List<Product> products = productDAO.getProducts(locale, currentPage, itemsPerPage);

            productsTO = new ProductsTO();
            productsTO.setTotalPageCount(totalPageCount);
            productsTO.setProducts(products);
        } catch (DaoException e) {
            throw new ServiceException("Exception during getting products for view for current page.", e);
        }
        return productsTO;
    }

    /**
     * Find products by name. If <code>includingAnalogs</code> is true to the List
     * of found products will be also included analogues by the non patent name of
     * the product. If <code>includingAnalogs</code> is false the searching will be
     * in strict accordance with the defined <code>product name</code>. Returns the
     * List of found products. The List will be empty if no one product has been
     * found.
     *
     * @param productName
     *            the product name
     * @param includingAnalogs
     *            the boolean parameter defines if in the List of found products
     *            will be also included analogues found by the non patent name of
     *            the product.
     * @return the List of found products. The List will be empty if no one product
     *         has been found.
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public List<Product> findProductsByName(String productName, boolean includingAnalogs) throws ServiceException {
        List<Product> foundProducts;
        try {
            foundProducts = productDAO.findProductsByName(productName, includingAnalogs);
        } catch (DaoException e) {
            throw new ServiceException("Exception during finding products by name.", e);
        }
        return foundProducts;
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
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public List<Product> findProductsBySounding(String productName, boolean includingAnalogs) throws ServiceException {
        List<Product> foundResemblingProducts;
        try {
            foundResemblingProducts = productDAO.findProductsBySounding(productName, includingAnalogs);
        } catch (DaoException e) {
            throw new ServiceException("Exception during finding products by sounding.", e);
        }
        return foundResemblingProducts;
    }

    /**
     * Replace product with the specified product.
     *
     * @param product
     *            the product
     * @throws ValidationException
     *             the exception during validation of the product
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void replaceProduct(Product product) throws ValidationException, ServiceException {
        if ( ! Validator.isProductModificationDataValid(product)) {
            throw new ValidationException("Sent data isn't valid");
        }
        try {
            productDAO.updateProduct(product);
        } catch (DaoException e) {
            throw new ServiceException("Exception during replacing product.", e);
        }
    }

    /**
     * Gets the total price of products that are in the user's basket.
     *
     * @param detailedBasket
     *            the basket of the user that consists detailed info about products
     *            (price and quantity)
     * @return the total sum of products
     */
    @Override
    public BigDecimal getTotalSumOfProducts(List<BasketTO> detailedBasket) {
        BigDecimal sum = BigDecimal.ZERO;
        if (detailedBasket != null) {
            for (BasketTO basketItem : detailedBasket) {
                BigDecimal quantity = new BigDecimal(basketItem.getOrderedQuantity());
                BigDecimal price = basketItem.getProduct().getPrice();
                sum = sum.add(quantity.multiply(price));
            }
        }
        return sum;
    }

}
