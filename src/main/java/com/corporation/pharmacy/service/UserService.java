package com.corporation.pharmacy.service;

import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;

/**
 * The Interface UserService.
 * 
 * Works with user operations
 */
public interface UserService {

    /**
     * Registrates specified user.
     *
     * @param user
     *            the user
     * @return the id of registrated user
     * @throws ValidationException
     *             the exception during validation of user parameters
     * @throws ServiceException
     *             the service exception
     */
    Integer registrate(User user) throws ValidationException, ServiceException;

    /**
     * Returns the {@link User} who appropriates specified {@code login or email}
     * and {@code password}. Returns {@code null} if such user is not found.
     *
     * @param loginOrEmail
     *            the login or email
     * @param password
     *            the password
     * @return the user who appropriates specified {@code login or email} and
     *         {@code password}. Returns {@code null} if such user is not found.
     * @throws ValidationException
     *             the exception during validation login or email and password
     * @throws ServiceException
     *             the service exception
     */
    User logIn(String loginOrEmail, String password) throws ValidationException, ServiceException;

    /**
     * Checks if the user with specified {@code idUser} has filled full info about
     * yourself.
     *
     * @param idUser
     *            the id of user
     * @return {@code true}, if user has filled full info
     * @throws ServiceException
     *             the service exception
     */
    boolean isUserFillFullInfo(Integer idUser) throws ServiceException;

    /**
     * Sets the user full info.
     *
     * @param user
     *            the user object containing full info about user (in addition to
     *            existing already login, email and password)
     * @throws ValidationException
     *             the exception during validation of user parameters
     * @throws ServiceException
     *             the service exception
     */
    void setUserFullInfo(User user) throws ValidationException, ServiceException;

    /**
     * Checks if the specified user (by {@code idUser}) got prescriptions for all
     * products in his basket.
     * 
     * @return {@code true} if user got prescriptions for all products in his basket
     * 
     * @throws ServiceException
     *             the service exception
     */
    boolean isUserGotAllPrescriptions(Integer idUser) throws ServiceException;

}
