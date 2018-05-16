package com.corporation.pharmacy.entity;

import java.io.Serializable;

import javax.servlet.http.Part;

public class FileInfo implements Serializable {

    private static final long serialVersionUID = - 8608064834618884555L;

    private Part filePart;
    private String directory;
    private String fileName;

    public Part getFilePart() {
        return filePart;
    }

    public void setFilePart(Part filePart) {
        this.filePart = filePart;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directory == null) ? 0 : directory.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((filePart == null) ? 0 : filePart.hashCode());
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
        FileInfo other = (FileInfo) obj;
        if (directory == null) {
            if (other.directory != null)
                return false;
        } else if ( ! directory.equals(other.directory))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if ( ! fileName.equals(other.fileName))
            return false;
        if (filePart == null) {
            if (other.filePart != null)
                return false;
        } else if ( ! filePart.equals(other.filePart))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FileInfo [filePart=" + filePart + ", directory=" + directory + ", fileName=" + fileName + "]";
    }

}
