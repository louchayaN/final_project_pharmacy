package com.corporation.pharmacy.entity.dto;

import java.io.Serializable;

import com.corporation.pharmacy.entity.Prescription;
import com.corporation.pharmacy.entity.Product;

public class BasketTO implements Serializable {

    private static final long serialVersionUID = 9162723760117553482L;

    private Product product;
    private Integer orderedQuantity;
    private Prescription prescription;

    public BasketTO() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderedQuantity == null) ? 0 : orderedQuantity.hashCode());
        result = prime * result + ((prescription == null) ? 0 : prescription.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
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
        BasketTO other = (BasketTO) obj;
        if (orderedQuantity == null) {
            if (other.orderedQuantity != null)
                return false;
        } else if (! orderedQuantity.equals(other.orderedQuantity))
            return false;
        if (prescription == null) {
            if (other.prescription != null)
                return false;
        } else if (! prescription.equals(other.prescription))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (! product.equals(other.product))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BasketView [product=" + product + ", orderedQuantity=" + orderedQuantity + ", prescription=" + prescription + "]";
    }
}
