package com.corporation.pharmacy.service.impl;

import com.corporation.pharmacy.dao.BasketDAO;
import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.UserDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.service.UserService;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.exception.ValidationException;
import com.corporation.pharmacy.service.util.HashUtil;
import com.corporation.pharmacy.service.validation.Validator;

/**
 * The Class BasketServiceImpl is a class for working with user operations.
 */
public class UserServiceImpl implements UserService {

    /**
     * The single instance of non transactional DAOManager witch is a factory of DAO
     * that is used in non transactional operations.
     */
    private static DAOManager daoManager = DAOFactory.getFactory().getNonTransactionalDAOManager();

    /**
     * The single instances of DAOs for non transactional operations.
     */
    private static UserDAO userDAO = daoManager.getUserDAO();
    private static final BasketDAO basketDAO = daoManager.getBasketDAO();

    /**
     * Registrates specified user. Password before sending to DAO level is hashed
     * according specified algorithms.
     *
     * @param user
     *            the user
     * @return the id of registrated user
     * @throws ValidationException
     *             the exception during validation of user parameters or if such
     *             email or login is already exists.
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public Integer registrate(User user) throws ValidationException, ServiceException {
        if ( ! Validator.isRegistrationDataValid(user)) {
            throw new ValidationException("Sent data isn't valid.");
        }

        try {
            if (userDAO.isEmailOrLoginExists(user)) {
                throw new ValidationException("Email or login are not unique.");
            }
            String hashedPassword = HashUtil.toHash(user.getPassword());
            user.setPassword(hashedPassword);
            Integer idUser = userDAO.addUser(user);
            return idUser;
        } catch (DaoException e) {
            throw new ServiceException("Exception during user registration.", e);
        }
    }

    /**
     * Returns the {@link User} who appropriates specified {@code login or email}
     * and {@code password}. Returns {@code null} if such user is not found.
     * Password before sending to DAO level is hashed according specified
     * algorithms.
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
    @Override
    public User logIn(String loginOrEmail, String password) throws ValidationException, ServiceException {
        if ( ! Validator.isLoginDataValid(loginOrEmail, password)) {
            throw new ValidationException("Sent data isn't valid.");
        }

        try {
            String hashedPassword = HashUtil.toHash(password);
            return userDAO.getUser(loginOrEmail, hashedPassword);
        } catch (DaoException e) {
            throw new ServiceException("Exception during user signing in.", e);
        }
    }

    /**
     * Checks if the user with specified {@code idUser} has filled full info about
     * yourself by checking of filling the name. If the name is known returns
     * {@code true}, if not {@code false}.
     *
     * @param idUser
     *            the id of user
     * @return {@code true}, if user has filled full info
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public boolean isUserFillFullInfo(Integer idUser) throws ServiceException {
        try {
            String name = userDAO.getUserName(idUser);
            return name == null ? false : true;
        } catch (DaoException e) {
            throw new ServiceException("Exception during checking filling name by user as criteria of filling full info.", e);
        }
    }

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
    @Override
    public void setUserFullInfo(User user) throws ValidationException, ServiceException {
        if ( ! Validator.isUserDataValid(user)) {
            throw new ValidationException("Sent data isn't valid.");
        }

        try {
            userDAO.setUserFullInfo(user);
        } catch (DaoException e) {
            throw new ServiceException("Exception during setting full info about user.", e);
        }
    }

    /**
     * Checks if the specified user (by {@code idUser}) got prescriptions for all
     * products in his basket.
     * 
     * @return {@code true} if user got prescriptions for all products in his basket
     * 
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public boolean isUserGotAllPrescriptions(Integer idUser) throws ServiceException {
        try {
            int basketItemsThatNeedPrescription = basketDAO.getBasketItemsCountThatNeedPrescription(idUser);
            return basketItemsThatNeedPrescription == 0 ? true : false;
        } catch (DaoException e) {
            throw new ServiceException("Exception during checking of getting by the user all nessessary prescriptions.", e);
        }
    }

}
