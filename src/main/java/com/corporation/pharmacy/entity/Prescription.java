package com.corporation.pharmacy.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Prescription implements Serializable {

    private static final long serialVersionUID = - 5197389797565702424L;

    private Date dateStart;
    private Date dateEnd;
    private Status extendingStatus;
    private Status gettingStatus;
    private Timestamp requestDate;

    public Prescription() {
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Status getExtendingStatus() {
        return extendingStatus;
    }

    public void setExtendingStatus(Status extendingStatus) {
        this.extendingStatus = extendingStatus;
    }

    public Status getGettingStatus() {
        return gettingStatus;
    }

    public void setGettingStatus(Status gettingStatus) {
        this.gettingStatus = gettingStatus;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
        result = prime * result + ((dateStart == null) ? 0 : dateStart.hashCode());
        result = prime * result + ((extendingStatus == null) ? 0 : extendingStatus.hashCode());
        result = prime * result + ((gettingStatus == null) ? 0 : gettingStatus.hashCode());
        result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
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
        Prescription other = (Prescription) obj;
        if (dateEnd == null) {
            if (other.dateEnd != null)
                return false;
        } else if ( ! dateEnd.equals(other.dateEnd))
            return false;
        if (dateStart == null) {
            if (other.dateStart != null)
                return false;
        } else if ( ! dateStart.equals(other.dateStart))
            return false;
        if (extendingStatus != other.extendingStatus)
            return false;
        if (gettingStatus != other.gettingStatus)
            return false;
        if (requestDate == null) {
            if (other.requestDate != null)
                return false;
        } else if ( ! requestDate.equals(other.requestDate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Prescription [dateStart=" + dateStart + ", dateEnd=" + dateEnd + ", extendingStatus=" + extendingStatus
                + ", gettingStatus=" + gettingStatus + ", requestDate=" + requestDate + "]";
    }
}
