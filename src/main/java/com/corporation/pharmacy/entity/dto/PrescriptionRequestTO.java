package com.corporation.pharmacy.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.corporation.pharmacy.entity.PrescriptionRequest;

public class PrescriptionRequestTO implements Serializable {

    private static final long serialVersionUID = - 426433087412592515L;

    private List<PrescriptionRequest> prescriptionRequests;
    private Integer totalPageCount;

    public List<PrescriptionRequest> getPrescriptionRequests() {
        return prescriptionRequests;
    }

    public void setPrescriptionRequests(List<PrescriptionRequest> prescriptionRequests) {
        this.prescriptionRequests = prescriptionRequests;
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
        result = prime * result + ((prescriptionRequests == null) ? 0 : prescriptionRequests.hashCode());
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
        PrescriptionRequestTO other = (PrescriptionRequestTO) obj;
        if (prescriptionRequests == null) {
            if (other.prescriptionRequests != null)
                return false;
        } else if (! prescriptionRequests.equals(other.prescriptionRequests))
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
        return "PrescriptionRequestView [prescriptionRequests=" + prescriptionRequests + ", totalPageCount=" + totalPageCount
                + "]";
    }
}
