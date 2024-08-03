package io.github.juliog516;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.juliog516.Helpers.JMockarooException;
import io.github.juliog516.Helpers.MockElement;
import io.github.juliog516.Helpers.MockableObject;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;


public class MockarooClient {

    private final static String MockarooApiUrl = "api.mockaroo.com/api/";
    //    private static String MockarooApiUrl = "https://api.mockaroo.com/api/";
    private final static String MockarooSchemaApiUrl = "";

    private final String apiKey;

    public MockarooClient(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Invalid api key, please certify that the key it's correct.");
        }
        this.apiKey = apiKey;
    }


    private URI getUri(int count) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("https")
                .setHost(MockarooApiUrl)
                .setPath("/generate.json")
                .setParameter("key", apiKey)
                .setParameter("count", String.valueOf(count))
                .build();
    }

    private HttpRequest getRequest(ArrayNode jsonModel, int count) throws URISyntaxException {
        URI uri = getUri(count);

        return HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonModel.toString()))
                .build();
    }

    public <T> T getSingleMock(Class<T> clazz) throws JMockarooException {
        var jsonArray = toJsonArray(clazz);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        try {
            request = getRequest(jsonArray, 1);
        } catch (URISyntaxException e) {
            throw new JMockarooException("Failed to create an HttpRequest. " + e.getMessage());
        }

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new JMockarooException("Failed to query the Mockaroo API" + " " + e.getMessage());
        }

        try {
            return deserializeSingle(response.body(), clazz);
        } catch (JsonProcessingException e) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                throw new JMockarooException("Failed to create a new instance of " + clazz.getName() + " " + ex.getMessage());
            }
        }
    }

    private <T> T deserializeSingle(String json, Class<T> clazz ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    public <T> Collection<T> getMocks(Class<T> clazz, int... count) throws  JMockarooException {
        int rCounter = 10;
        Collection<T> mockCollection;


        if (count.length > 0) {
            if (count[0] > 0) {
                rCounter = count[0];
            }
        }

        var jsonArray = toJsonArray(clazz);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = null;
        try {
            request = getRequest(jsonArray, rCounter);
        } catch (URISyntaxException e) {
            throw new JMockarooException("Failed to create an HttpRequest. " + e.getMessage());
        }

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new JMockarooException("Error while getting Mockaroo response." + " " + e.getMessage());
        }

        ArrayNode jsonArrayNode = null;
        try {
            jsonArrayNode = (ArrayNode) new ObjectMapper().readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new JMockarooException("Error while reading Mockaroo Response" + " " + e.getMessage());
        }

        try {
            mockCollection = getCollection(jsonArrayNode, clazz);
        } catch (JsonProcessingException e) {
            throw new JMockarooException("Error while deserializing object" + " " + e.getMessage());
        }

        return mockCollection;
    }


    public ArrayNode toJsonArray(Class clazz) throws JMockarooException {
        if (clazz == null) throw new JMockarooException("Can't parse that null class to Json Array.");


        if (!isAnnotated(clazz)) {
            throw new JMockarooException("The class "
                    + clazz.getSimpleName()
                    + " is not annotated with MockableObject.");
        }


        return createArrayNode(clazz);
    }


    private boolean isAnnotated(Class clazz) {
        return clazz.isAnnotationPresent(MockableObject.class);
    }

    private ArrayNode createArrayNode(Class clazz) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arrayNode = factory.arrayNode();

        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(MockElement.class)) {
                arrayNode.add(createObjectNode(field, factory));
            }
        }

        return arrayNode;
    }

    private ObjectNode createObjectNode(Field field, JsonNodeFactory factory) {
        var annotation = field.getAnnotation(MockElement.class);
        ObjectNode node = factory.objectNode();

        node.put("name", field.getName());
        node.put("percentBlank", annotation.blankPercentage());
        node.put("type", annotation.dataType().toString());

        if (!annotation.formula().isBlank()) {
            node.put("formula", annotation.formula());
        }


        return node;
    }


    public <T> List<T> getCollection(ArrayNode arrayNode, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);

        return mapper.readValue(arrayNode.toString(), type);
    }


}
