package com.corporation.pharmacy.entity;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = - 476932035938893724L;

    private Integer idUser;
    private String email;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String middleName;
    private String adress;
    private String passport;
    private String telephone;
    private Role role;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return name + " " + middleName + " " + surname;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adress == null) ? 0 : adress.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((passport == null) ? 0 : passport.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
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
        User other = (User) obj;
        if (adress == null) {
            if (other.adress != null)
                return false;
        } else if (! adress.equals(other.adress))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (! email.equals(other.email))
            return false;
        if (idUser == null) {
            if (other.idUser != null)
                return false;
        } else if (! idUser.equals(other.idUser))
            return false;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (! login.equals(other.login))
            return false;
        if (middleName == null) {
            if (other.middleName != null)
                return false;
        } else if (! middleName.equals(other.middleName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (! name.equals(other.name))
            return false;
        if (passport == null) {
            if (other.passport != null)
                return false;
        } else if (! passport.equals(other.passport))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (! password.equals(other.password))
            return false;
        if (role != other.role)
            return false;
        if (surname == null) {
            if (other.surname != null)
                return false;
        } else if (! surname.equals(other.surname))
            return false;
        if (telephone == null) {
            if (other.telephone != null)
                return false;
        } else if (! telephone.equals(other.telephone))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [idUser=" + idUser + ", email=" + email + ", login=" + login + ", password=" + password + ", name=" + name
                + ", surname=" + surname + ", middleName=" + middleName + ", adress=" + adress + ", passport=" + passport
                + ", telephone=" + telephone + ", role=" + role + "]";
    }

}
