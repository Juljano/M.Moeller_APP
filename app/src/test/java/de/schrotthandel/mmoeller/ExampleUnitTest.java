package de.schrotthandel.mmoeller;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void getAllInformation(){
        CustomerData customerData = CustomerData.getInstance();
        assertNotNull(customerData);

        customerData.setAddress("street");
        customerData.setName("Name");
        customerData.setCityAndPLZ("Minden");
        customerData.setDate("12.1.2024");
        customerData.setTaxNumber("Steuern");
        customerData.setReceiverName("Firma");
        customerData.setiBan("Iban");
        customerData.setBankTransfer(true);
        customerData.setCashPayment(true);
        customerData.setPrivate(true);
        customerData.setBusiness(true);


        assertEquals("street",customerData.getAddress());
        assertEquals("Name",customerData.getName());
        assertEquals("Minden",customerData.getCityAndPLZ());
        assertEquals("12.1.2024",customerData.getDate());
        assertEquals("Steuern",customerData.getTaxNumber());
        assertEquals("Iban",customerData.getiBan());
        assertEquals("Firma",customerData.getReceiverName());
        assertEquals(true,customerData.getBankTransfer());
        assertEquals(true, customerData.getCashPayment());
        assertEquals(true, customerData.getPrivate());
        assertEquals(true, customerData.getBusiness());

    }
}