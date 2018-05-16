package com.corporation.pharmacy.controller.constant;

/**
 * Specifies the path names to view pages for forwarding request and for sending
 * redirect request.
 */
public class ViewPath {

    public static final String COMMON_PAGES_PATH = "/WEB-INF/jsp/";

    public static final String FORWARD_FOUND_PRODUCTS = "/WEB-INF/jsp/user/found-products.jsp";
    public static final String FORWARD_BASKET = "/WEB-INF/jsp/user/shopping-cart.jsp";
    public static final String FORWARD_PRODUCTS = "/WEB-INF/jsp/products.jsp";
    public static final String FORWARD_ORDERS = "/WEB-INF/jsp/user/order-history.jsp";
    public static final String FORWARD_CHANGE_PRODUCT_FORM = "/WEB-INF/jsp/pharmacist/change-product-form.jsp";
    public static final String FORWARD_GETTING_REQUESTS = "/WEB-INF/jsp/doctor/getting-requests.jsp";
    public static final String FORWARD_EXTENDING_REQUESTS = "/WEB-INF/jsp/doctor/extending-requests.jsp";
    public static final String FORWARD_503_ERROR = "/WEB-INF/jsp/503-error.jsp";

    public static final String REDIRECT_HOME_PAGE = "/home-page";
    public static final String REDIRECT_BASKET = "/basket";
    public static final String REDIRECT_ADD_PRODUCT = "/pharmacist/add-product-form";
    public static final String REDIRECT_SHOW_PRODUCT = "/pharmacist/show-product";
    public static final String REDIRECT_GETTING_REQUESTS = "/doctor/getting-requests?currentPage=1&itemsPerPage=3";
    public static final String REDIRECT_EXTENDING_REQUESTS = "/doctor/extending-requests?currentPage=1&itemsPerPage=3";
    public static final String REDIRECT_503_ERROR = "/503-error";
}
