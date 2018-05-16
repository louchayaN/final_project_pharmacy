package com.corporation.pharmacy.entity;

import java.io.Serializable;

public class BasketItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idUser;
    private Integer idProduct;
    private Integer quantity;

    public BasketItem() {
    }

    public BasketItem(Integer idUser, Integer idProduct, Integer quantity) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idProduct == null) ? 0 : idProduct.hashCode());
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
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
        BasketItem other = (BasketItem) obj;
        if (idProduct == null) {
            if (other.idProduct != null)
                return false;
        } else if (! idProduct.equals(other.idProduct))
            return false;
        if (idUser == null) {
            if (other.idUser != null)
                return false;
        } else if (! idUser.equals(other.idUser))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (! quantity.equals(other.quantity))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BasketItem [idUser=" + idUser + ", idProduct=" + idProduct + ", quantity=" + quantity + "]";
    }
}
