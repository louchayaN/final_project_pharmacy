package com.corporation.pharmacy.service;

import com.corporation.pharmacy.service.impl.BasketServiceImpl;
import com.corporation.pharmacy.service.impl.OrderServiceImpl;
import com.corporation.pharmacy.service.impl.PrescriptionServiceImpl;
import com.corporation.pharmacy.service.impl.ProductServiceImpl;
import com.corporation.pharmacy.service.impl.UserServiceImpl;

public class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();
    private static final UserService userService = new UserServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final BasketService basketService = new BasketServiceImpl();
    private static final PrescriptionService prescriptionService = new PrescriptionServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public BasketService getBasketService() {
        return basketService;
    }

    public PrescriptionService getPrescriptionService() {
        return prescriptionService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

}
