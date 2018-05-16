package com.corporation.pharmacy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements Serializable {

    private static final long serialVersionUID = - 6049744062341913734L;

    private Product orderedProduct;
    private Integer orderedQuantity;
    private BigDecimal price;
    private Timestamp date;

    public Order() {
    }

    public Product getOrderedProduct() {
        return orderedProduct;
    }

    public void setOrderedProduct(Product orderedProduct) {
        this.orderedProduct = orderedProduct;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((orderedProduct == null) ? 0 : orderedProduct.hashCode());
        result = prime * result + ((orderedQuantity == null) ? 0 : orderedQuantity.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if ( ! date.equals(other.date))
            return false;
        if (orderedProduct == null) {
            if (other.orderedProduct != null)
                return false;
        } else if ( ! orderedProduct.equals(other.orderedProduct))
            return false;
        if (orderedQuantity == null) {
            if (other.orderedQuantity != null)
                return false;
        } else if ( ! orderedQuantity.equals(other.orderedQuantity))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if ( ! price.equals(other.price))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Order [orderedProduct=" + orderedProduct + ", orderedQuantity=" + orderedQuantity + ", price=" + price + ", date="
                + date + "]";
    }
}
