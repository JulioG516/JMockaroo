package io.github.juliog516;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.github.juliog516.Helpers.JMockarooException;
import io.github.juliog516.Models.MockedClass;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

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