package com.corporation.pharmacy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

    private static final long serialVersionUID = 7660351695015637134L;

    private Integer idProduct;
    private String name;
    private String nonPatentName;
    private String producer;
    private String form;
    private String instructionFileName;
    private Boolean needPrescription;
    private Integer quantity;
    private BigDecimal price;
    private LocaleType locale;

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNonPatentName() {
        return nonPatentName;
    }

    public void setNonPatentName(String nonPatentName) {
        this.nonPatentName = nonPatentName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
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

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }

    public LocaleType getLocale() {
        return locale;
    }

    public void setLocale(LocaleType locale) {
        this.locale = locale;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((form == null) ? 0 : form.hashCode());
        result = prime * result + ((idProduct == null) ? 0 : idProduct.hashCode());
        result = prime * result + ((instructionFileName == null) ? 0 : instructionFileName.hashCode());
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((needPrescription == null) ? 0 : needPrescription.hashCode());
        result = prime * result + ((nonPatentName == null) ? 0 : nonPatentName.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((producer == null) ? 0 : producer.hashCode());
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
        Product other = (Product) obj;
        if (form == null) {
            if (other.form != null)
                return false;
        } else if ( ! form.equals(other.form))
            return false;
        if (idProduct == null) {
            if (other.idProduct != null)
                return false;
        } else if ( ! idProduct.equals(other.idProduct))
            return false;
        if (instructionFileName == null) {
            if (other.instructionFileName != null)
                return false;
        } else if ( ! instructionFileName.equals(other.instructionFileName))
            return false;
        if (locale != other.locale)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if ( ! name.equals(other.name))
            return false;
        if (needPrescription == null) {
            if (other.needPrescription != null)
                return false;
        } else if ( ! needPrescription.equals(other.needPrescription))
            return false;
        if (nonPatentName == null) {
            if (other.nonPatentName != null)
                return false;
        } else if ( ! nonPatentName.equals(other.nonPatentName))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if ( ! price.equals(other.price))
            return false;
        if (producer == null) {
            if (other.producer != null)
                return false;
        } else if ( ! producer.equals(other.producer))
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
        return "Product [idProduct=" + idProduct + ", name=" + name + ", nonPatentName=" + nonPatentName + ", producer="
                + producer + ", form=" + form + ", instructionFileName=" + instructionFileName + ", needPrescription="
                + needPrescription + ", quantity=" + quantity + ", price=" + price + ", locale=" + locale + "]";
    }

}
