package com.corporation.pharmacy.service;

import java.math.BigDecimal;
import java.util.List;

import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;
import com.corporation.pharmacy.entity.dto.ProductsTO;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The Interface ProductService.
 * 
 * Works with the products.
 */
public interface ProductService {

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
    void addProduct(LocalizedProduct localizedProduct) throws ValidationException, ServiceException;

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
    Product getProduct(LocaleType locale, Integer idProduct) throws ServiceException;

    /**
     * Gets and returns the products for view for specified {@code current page} and
     * {@code items per page} in accordance with specified {@code locale}. .
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
    ProductsTO getProductsforView(LocaleType locale, int currentPage, int itemsPerPage) throws ServiceException;

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
    List<Product> findProductsByName(String productName, boolean includingAnalogs) throws ServiceException;

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
    List<Product> findProductsBySounding(String productName, boolean includingAnalogs) throws ServiceException;

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
    void replaceProduct(Product product) throws ValidationException, ServiceException;

    /**
     * Gets the total price of products that are in the user's basket.
     *
     * @param detailedBasket
     *            the basket of the user that consists detailed info about products
     *            (price and quantity)
     * @return the total sum of products
     */
    BigDecimal getTotalSumOfProducts(List<BasketTO> detailedBasket);

}
