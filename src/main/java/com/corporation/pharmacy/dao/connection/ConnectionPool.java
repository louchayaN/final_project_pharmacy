package com.corporation.pharmacy.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The class for instantiation of connection pool.
 */
public final class ConnectionPool {

    /** The queue for keeping the connections */
    private BlockingQueue<Connection> connectionQueue;

    /** Defines data base and connection pool configurations */
    private String driverName;
    private String dbUrl;
    private String user;
    private String password;
    private int poolSize;

    /** The single instance of the connection pool */
    private static final ConnectionPool INSTANCE = new ConnectionPool();

    /**
     * Prevents from instantiation of a new connection pool.
     */
    private ConnectionPool() {
    }

    /**
     * Returns the single instance of Connection Pool.
     */
    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the queue that keeps the connections.
     */
    public BlockingQueue<Connection> getConnectionQueue() {
        return connectionQueue;
    }

    /**
     * Initializes the Connection Pool. Gets data base and connection pool
     * configurations from the properties file (path to this file specified by
     * {@code propertiesFilePath} param). Creates a new queue of connections of
     * defined size and fills it with pooled connections to the defined data base
     * (see {@link PooledConnection}
     *
     * @param propertiesFilePath
     *            the path to properties file with data base and connection pool
     *            configurations
     * @throws ConnectionPoolException
     *             exception during connection pool initialization
     */
    public void initialize(String propertiesFilePath) throws ConnectionPoolException {
        initDBParametres(propertiesFilePath);

        try {
            Class.forName(driverName);
            connectionQueue = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(dbUrl, user, password);
                connectionQueue.add(new PooledConnection(connection));
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionPoolException("Exception during connection pool initialization.", e);
        }
    }

    /**
     * Initializes ConnectionPool fields with data base and connection pool
     * configurations
     *
     * @param propertiesFilePath
     *            the path to properties file with data base and connection pool
     *            configurations
     */
    private void initDBParametres(String propertiesFilePath) {
        ResourceBundle resource = ResourceBundle.getBundle(propertiesFilePath);
        driverName = resource.getString(DBParameter.DB_DRIVER);
        dbUrl = resource.getString(DBParameter.DB_URL);
        user = resource.getString(DBParameter.DB_USER);
        password = resource.getString(DBParameter.DB_PASSWORD);
        poolSize = Integer.parseInt(resource.getString(DBParameter.DB_POOL_SIZE));
        if (poolSize < 0) {
            throw new RuntimeException(
                    "Pool size incorrectly specified in property file, the number of connections should be positive digit.");
        }
    }

    /**
     * Returns the connection which is {@link PooledConnection} from the connection
     * pool.
     *
     * @return the connection which is {@link PooledConnection}
     * @throws ConnectionPoolException
     *             if it is not possible to take exception from the connection pool
     */
    public Connection getConnection() throws ConnectionPoolException {
        Connection connection = null;
        try {
            connection = connectionQueue.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Exception during getting connection from connection pool.", e);
        }
        return connection;
    }

    /**
     * Close data base resources (Connection, Statement and ResultSet)
     *
     * @param connection
     *            the connection
     * @param statement
     *            the statement
     * @param resultSet
     *            the result set
     * @throws SQLException
     *             if a database access error occurs
     */
    public void closeDBResources(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }

    /**
     * Close data base resources (Connection, Statement and ResultSet)
     *
     * @param connection
     *            the connection
     * @param statement
     *            the statement
     * @throws SQLException
     *             if a database access error occurs
     */
    public void closeDBResources(Connection connection, Statement statement) throws SQLException {
        closeDBResources(connection, statement, null);
    }

    /**
     * Close data base resources
     *
     * @param statements
     *            the statements
     * @throws SQLException
     *             if a database access error occurs
     */
    public void closeDBResources(Statement... statements) throws SQLException {
        for (Statement statement : statements) {
            closeStatement(statement);
        }
    }

    /**
     * Close data base resources
     *
     * @param resultSet
     *            the result set
     * @param statements
     *            the statements
     * @throws SQLException
     *             if a database access error occurs
     */
    public void closeDBResources(ResultSet resultSet, Statement... statements) throws SQLException {
        closeResultSet(resultSet);
        for (Statement statement : statements) {
            closeStatement(statement);
        }
    }

    /**
     * Close the connection if not null.
     *
     * @param connection
     *            the connection
     * @throws SQLException
     *             if a database access error occurs
     */
    private void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Close statement if not null.
     *
     * @param statement
     *            the statement
     * @throws SQLException
     *             if a database access error occurs
     */
    private void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * Close ResultSet if not null.
     *
     * @param resultSet
     *            the ResultSet
     * @throws SQLException
     *             if a database access error occurs
     */
    private void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * Destroys the connection pool (really closes all connections - returns all
     * connections to the data base).
     *
     * @throws ConnectionPoolException
     *             if exception during closing the connections occurred
     */
    public void destroy() throws ConnectionPoolException {
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection connection = connectionQueue.take();
                if ( ! connection.getAutoCommit()) {
                    connection.commit();
                }
                ((PooledConnection) connection).reallyClose();
            } catch (SQLException | InterruptedException e) {
                throw new ConnectionPoolException("Exception during closing the connection pool.", e);
            }
        }
    }

}
