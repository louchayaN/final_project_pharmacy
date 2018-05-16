package com.corporation.pharmacy.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.PrescriptionRequest;
import com.corporation.pharmacy.entity.PrescriptionRequestType;
import com.corporation.pharmacy.entity.Status;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class PrescriptionDAOTest {

    private static final PrescriptionDAO prescriptionDAO = DAOFactory.getFactory().getNonTransactionalDAOManager()
            .getPrescriptionDAO();

    private static final String GET_GETTING_REQUEST_STATUS = "SELECT `getting_request_status` FROM prescriptions WHERE `id_user` = ? AND `id_product` = ?";
    private static final String GET_EXTENDING_REQUEST_STATUS = "SELECT `extending_request_status` FROM prescriptions WHERE `id_user` = ? AND `id_product` = ?";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestUtil.initializeConnectionPool();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ConnectionPool.getInstance().destroy();
    }

    @Before
    public void setUp() throws Exception {
        TestUtil.initializeDB();
    }

    @DataProvider
    public static Object[][] providePrescriptionRequests() {
        return new Object[][] { { 3, 5, PrescriptionRequestType.GETTING, Status.WAITING },
                { 3, 2, PrescriptionRequestType.EXTENDING, Status.WAITING } };
    }

    @Test
    @UseDataProvider("providePrescriptionRequests")
    public void testAddPrescriptionRequst(Integer idUser, Integer idProduct, PrescriptionRequestType requestType,
            Status expectedRequestStatus) throws DaoException, ConnectionPoolException, SQLException {

        prescriptionDAO.addPrescriptionRequest(idUser, idProduct, requestType);
        Status gottenRequestStatus = getRequestStatus(idUser, idProduct, requestType);

        assertEquals(expectedRequestStatus, gottenRequestStatus);
    }

    private Status getRequestStatus(Integer idUser, Integer idProduct, PrescriptionRequestType requestType)
            throws ConnectionPoolException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            if (PrescriptionRequestType.GETTING == requestType) {
                statement = connection.prepareStatement(GET_GETTING_REQUEST_STATUS);
            } else {
                statement = connection.prepareStatement(GET_EXTENDING_REQUEST_STATUS);
            }
            statement.setInt(1, idUser);
            statement.setInt(2, idProduct);
            rs = statement.executeQuery();
            rs.next();
            Status requestStatus = Status.valueOf(rs.getString(1));
            return requestStatus;
        } finally {
            ConnectionPool.getInstance().closeDBResources(connection, statement, rs);
        }
    }

    @DataProvider
    public static Object[][] provideDataForRequestsView() {

        return new Object[][] { { LocaleType.RU_BY, 1, 2, PrescriptionRequestType.GETTING, new ArrayList<>(Arrays.asList(2, 1)) },
                { LocaleType.EN_US, 1, 2, PrescriptionRequestType.EXTENDING, new ArrayList<>(Arrays.asList(7, 6)) } };
    }

    @Test
    @UseDataProvider("provideDataForRequestsView")
    public void testGetPrescriptionRequests(LocaleType locale, int currentPage, int itemsPerPage,
            PrescriptionRequestType requestType, List<Integer> expectedIdProduct) throws DaoException {

        List<PrescriptionRequest> requests = prescriptionDAO.getPrescriptionRequests(locale, currentPage, itemsPerPage,
                requestType);
        List<Integer> gottenProductsId = getProductsId(requests);

        assertTrue(expectedIdProduct.containsAll(gottenProductsId));
    }

    private List<Integer> getProductsId(List<PrescriptionRequest> requests) {
        List<Integer> productsId = new ArrayList<>();
        for (PrescriptionRequest request : requests) {
            productsId.add(request.getProduct().getIdProduct());
        }
        return productsId;
    }

    @DataProvider
    public static Object[][] providePrescriptionTotalCount() {
        return new Object[][] { { PrescriptionRequestType.GETTING, 3 }, { PrescriptionRequestType.EXTENDING, 2 } };
    }

    @Test
    @UseDataProvider("providePrescriptionTotalCount")
    public void testGetPrescriptionTotalCount(PrescriptionRequestType requestType, int expectedTotalCount) throws DaoException {

        int gottenPrescriptionTotalCount = prescriptionDAO.getPrescriptionTotalCount(requestType);

        assertEquals(expectedTotalCount, gottenPrescriptionTotalCount);
    }

    @DataProvider
    public static Object[][] providePrescriptionRequestsForSatisfying() {
        return new Object[][] { { 2, 1, PrescriptionRequestType.GETTING, Status.SATISFY },
                { 1, 7, PrescriptionRequestType.EXTENDING, Status.SATISFY } };
    }

    @Test
    @UseDataProvider("providePrescriptionRequestsForSatisfying")
    public void testSatisfyPrescriptionRequest(Integer idUser, Integer idProduct, PrescriptionRequestType requestType,
            Status expectedRequestStatus) throws DaoException, ConnectionPoolException, SQLException {

        prescriptionDAO.satisfyPrescriptionRequest(idUser, idProduct, requestType);
        Status gottenRequestStatus = getRequestStatus(idUser, idProduct, requestType);

        assertEquals(expectedRequestStatus, gottenRequestStatus);
    }

}
