package io.github.juliog516;

import io.github.juliog516.JMockaroo.Helpers.JMockarooException;
import io.github.juliog516.JMockaroo.MockarooClient;
import io.github.juliog516.Models.MockedClass;
import junit.framework.TestCase;

public class MockarooClientTest extends TestCase {

    private final MockarooClient mockarooClient = new MockarooClient("yourApiKey");



    public void testGetMocks() throws  JMockarooException {
        var result = mockarooClient.getMocks(MockedClass.class, 20);

        assertEquals(20, result.size());
    }

    public void testGetSingle() throws JMockarooException {
        var one = mockarooClient.getSingleMock(MockedClass.class);

        assertNotSame("" , one.Name);
    }






}