package com.corporation.pharmacy.service.validation;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.BigDecimalValidator;

import com.corporation.pharmacy.entity.LocalProductInfo;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.entity.dto.LocalizedProduct;

/**
 * Defines static methods for data validation.
 */
public class Validator {

    /**
     * Checks if the registration data about specified User is valid.
     *
     * @param user
     *            the user
     * @return true, if is registration data valid
     */
    public static boolean isRegistrationDataValid(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        String password = user.getPassword();

        if ( ! GenericValidator.isEmail(email)) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(login)) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(password) || ! GenericValidator.minLength(password, 6)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the login or email and password are valid.
     *
     * @param loginOrEmail
     *            the login or email
     * @param password
     *            the password
     * @return true, if the login or email and password are valid
     */
    public static boolean isLoginDataValid(String loginOrEmail, String password) {
        if (GenericValidator.isBlankOrNull(loginOrEmail)) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(password)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the data about specified LocalizedProduct is valid.
     *
     * @param localizedProduct
     *            the localized product
     * @return true, if is product data valid
     */
    public static boolean isProductDataValid(LocalizedProduct localizedProduct) {
        if (localizedProduct.getNeedPrescription() == null) {
            return false;
        }
        if ( ! GenericValidator.minValue(localizedProduct.getQuantity(), 0)) {
            return false;
        }
        if ( ! BigDecimalValidator.getInstance().minValue(localizedProduct.getPrice(), 0)) {
            return false;
        }

        for (LocalProductInfo productInfo : localizedProduct.getProductInfoForDifferentLocales()) {
            if (GenericValidator.isBlankOrNull(productInfo.getName())) {
                return false;
            }
            if (GenericValidator.isBlankOrNull(productInfo.getNonPatentName())) {
                return false;
            }
            if (GenericValidator.isBlankOrNull(productInfo.getProducer())) {
                return false;
            }
            if (GenericValidator.isBlankOrNull(productInfo.getForm())) {
                return false;
            }

            if (GenericValidator.isBlankOrNull(productInfo.getInstructionFileName())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if is user data valid.
     *
     * @param user
     *            the user
     * @return true, if is user data valid
     */
    public static boolean isUserDataValid(User user) {
        if (GenericValidator.isBlankOrNull(user.getName())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(user.getMiddleName())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(user.getSurname())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(user.getAdress())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(user.getPassport())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(user.getTelephone())) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is product modification data valid.
     *
     * @param product
     *            the product
     * @return true, if is product modification data valid
     */
    public static boolean isProductModificationDataValid(Product product) {
        if (GenericValidator.isBlankOrNull(product.getName())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(product.getNonPatentName())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(product.getProducer())) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(product.getForm())) {
            return false;
        }

        if (GenericValidator.isBlankOrNull(product.getInstructionFileName())) {
            return false;
        }

        if (product.getNeedPrescription() == null) {
            return false;
        }
        if ( ! GenericValidator.minValue(product.getQuantity(), 0)) {
            return false;
        }
        if ( ! BigDecimalValidator.getInstance().minValue(product.getPrice(), 0)) {
            return false;
        }

        return true;
    }

}
