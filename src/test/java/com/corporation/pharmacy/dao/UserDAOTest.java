package com.corporation.pharmacy.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.User;
import com.corporation.pharmacy.service.util.HashUtil;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class UserDAOTest {

    private static final UserDAO userDAO = DAOFactory.getFactory().getNonTransactionalDAOManager().getUserDAO();

    private static final String GET_USER_EMAIL = "SELECT `email` FROM users WHERE `id_user` = ?";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestUtil.initializeConnectionPool();
        TestUtil.initializeDB();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ConnectionPool.getInstance().destroy();
    }

    @DataProvider
    public static Object[][] provideLoginEmailData() {
        return new Object[][] { { "petr", "petr@mail.ru", false }, { "user", "user@mail.ru", true },
                { "user", "petr@mail.ru", true }, { "petr", "user@mail.ru", true }, };
    }

    @Test
    @UseDataProvider("provideLoginEmailData")
    public void testIsEmailOrLoginExists(String login, String email, boolean expected) throws DaoException {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);

        boolean emailOrLoginExists = userDAO.isEmailOrLoginExists(user);

        assertEquals(expected, emailOrLoginExists);
    }

    @Test
    public void testAddUser() throws DaoException, ConnectionPoolException, SQLException {
        User user = new User();
        user.setLogin("hello");
        user.setEmail("hello@mail.ru");
        user.setPassword("hello1234");

        Integer idUser = userDAO.addUser(user);
        String gottenEmail = getUserEmail(idUser);

        assertEquals(user.getEmail(), gottenEmail);
    }

    private String getUserEmail(Integer idUser) throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(GET_USER_EMAIL);
            statement.setInt(1, idUser);
            rs = statement.executeQuery();
            rs.next();
            String email = rs.getString(1);
            return email;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @Test
    public void testSetUserFullInfo() throws DaoException {
        User user = new User();
        user.setIdUser(1);
        user.setName("ivan");
        user.setMiddleName("ivanovich");
        user.setSurname("ivanov");
        user.setAdress("Minsk");
        user.setPassport("MP5678009");
        user.setTelephone("888-888-888");

        userDAO.setUserFullInfo(user);
        String gottenUserName = userDAO.getUserName(1);

        assertEquals("ivan", gottenUserName);
    }

    @DataProvider
    public static Object[][] provideUsersAuthorizationData() {
        return new Object[][] { { "user", "user", true }, { "user@mail.ru", "user", true },
                { "user@mail.ru", "password1234", false }, };
    }

    @Test
    @UseDataProvider("provideUsersAuthorizationData")
    public void testGetUser(String loginOrEmail, String password, boolean expected) throws DaoException {
        String hashedPassword = HashUtil.toHash(password);

        User user = userDAO.getUser(loginOrEmail, hashedPassword);
        boolean isUserFound = (user != null);

        assertEquals(expected, isUserFound);
    }

    @Test
    public void testGetUserName() throws DaoException {
        Integer idUser = 4;
        String expectedUserName = "Petr";

        String gottenUserName = userDAO.getUserName(idUser);

        assertEquals(expectedUserName, gottenUserName);
    }

}
