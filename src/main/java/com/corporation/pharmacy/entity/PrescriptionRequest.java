package com.corporation.pharmacy.entity;

import java.io.Serializable;

public class PrescriptionRequest implements Serializable {

    private static final long serialVersionUID = 5494862919332514111L;

    private User user;
    private Product product;
    private Prescription prescription;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        result = prime * result + ((prescription == null) ? 0 : prescription.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        PrescriptionRequest other = (PrescriptionRequest) obj;
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
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (! user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrescriptionRequest [user=" + user + ", product=" + product + ", prescription=" + prescription + "]";
    }

}
