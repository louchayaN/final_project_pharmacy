package com.corporation.pharmacy.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.corporation.pharmacy.entity.Product;

public class ProductsTO implements Serializable {

    private static final long serialVersionUID = - 7720625273032215403L;

    private List<Product> products;
    private Integer totalPageCount;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        result = prime * result + ((totalPageCount == null) ? 0 : totalPageCount.hashCode());
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
        ProductsTO other = (ProductsTO) obj;
        if (products == null) {
            if (other.products != null)
                return false;
        } else if (! products.equals(other.products))
            return false;
        if (totalPageCount == null) {
            if (other.totalPageCount != null)
                return false;
        } else if (! totalPageCount.equals(other.totalPageCount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProductsView [products=" + products + ", totalPageCount=" + totalPageCount + "]";
    }

}
