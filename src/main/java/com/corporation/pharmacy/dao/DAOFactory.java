package com.corporation.pharmacy.dao;

import com.corporation.pharmacy.dao.exception.UnsupportedStoradgeTypeException;
import com.corporation.pharmacy.dao.mysql.MySqlDAOFactory;

/**
 * An abstract factory for creating transactional and not transactional DAO
 * factories.
 */
public abstract class DAOFactory {

    /**
     * Returns the factory. By default it's MySql factory
     */
    public static DAOFactory getFactory() {
        return getFactory(StoradgeTypes.MySql);
    }

    /**
     * Gets the factory by {@link StoradgeTypes}.
     *
     * @param type
     *            the type of factory
     * @return the factory defined by the {@code type} or throws
     *         {@codeUnsupportedStoradgeTypeException} if factory for this
     *         {@code type} is not defined
     */
    public static DAOFactory getFactory(StoradgeTypes type) {
        switch (type) {
        case MySql:
            return new MySqlDAOFactory();
        default:
            throw new UnsupportedStoradgeTypeException("Storage type wasn't choosed correctly");
        }
    }

    public abstract DAOManager getTransactionalDAOManager();

    public abstract DAOManager getNonTransactionalDAOManager();

}
