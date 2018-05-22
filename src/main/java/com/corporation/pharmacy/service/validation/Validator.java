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

    public static boolean isLoginDataValid(String loginOrEmail, String password) {
        if (GenericValidator.isBlankOrNull(loginOrEmail)) {
            return false;
        }
        if (GenericValidator.isBlankOrNull(password)) {
            return false;
        }
        return true;
    }

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
