package com.corporation.pharmacy.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.corporation.pharmacy.entity.LocalProductInfo;

public class LocalizedProduct implements Serializable {

    private static final long serialVersionUID = 2710086484750313236L;

    private Integer idProduct;
    private Boolean needPrescription;
    private Integer quantity;
    private BigDecimal price;
    List<LocalProductInfo> productInfoForDifferentLocales;

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Boolean getNeedPrescription() {
        return needPrescription;
    }

    public void setNeedPrescription(Boolean needPrescription) {
        this.needPrescription = needPrescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<LocalProductInfo> getProductInfoForDifferentLocales() {
        return productInfoForDifferentLocales;
    }

    public void setProductInfoForDifferentLocales(List<LocalProductInfo> productInfoForDifferentLocales) {
        this.productInfoForDifferentLocales = productInfoForDifferentLocales;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idProduct == null) ? 0 : idProduct.hashCode());
        result = prime * result + ((needPrescription == null) ? 0 : needPrescription.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((productInfoForDifferentLocales == null) ? 0 : productInfoForDifferentLocales.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
        LocalizedProduct other = (LocalizedProduct) obj;
        if (idProduct == null) {
            if (other.idProduct != null)
                return false;
        } else if ( ! idProduct.equals(other.idProduct))
            return false;
        if (needPrescription == null) {
            if (other.needPrescription != null)
                return false;
        } else if ( ! needPrescription.equals(other.needPrescription))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if ( ! price.equals(other.price))
            return false;
        if (productInfoForDifferentLocales == null) {
            if (other.productInfoForDifferentLocales != null)
                return false;
        } else if ( ! productInfoForDifferentLocales.equals(other.productInfoForDifferentLocales))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if ( ! quantity.equals(other.quantity))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LocalizedProduct [idProduct=" + idProduct + ", needPrescription=" + needPrescription + ", quantity=" + quantity
                + ", price=" + price + ", productInfoForDifferentLocales=" + productInfoForDifferentLocales + "]";
    }

}
