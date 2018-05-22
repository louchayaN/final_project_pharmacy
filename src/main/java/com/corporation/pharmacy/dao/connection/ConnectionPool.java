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

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

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

    public void closeDBResources(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }

    public void closeDBResources(Connection connection, Statement statement) throws SQLException {
        closeDBResources(connection, statement, null);
    }

    public void closeDBResources(Statement... statements) throws SQLException {
        for (Statement statement : statements) {
            closeStatement(statement);
        }
    }

    public void closeDBResources(ResultSet resultSet, Statement... statements) throws SQLException {
        closeResultSet(resultSet);
        for (Statement statement : statements) {
            closeStatement(statement);
        }
    }

    private void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

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
