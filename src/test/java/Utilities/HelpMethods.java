package Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static DataForTesting.Action.GET_NUMBERS_COUNT;
import static DataForTesting.Constans.*;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class HelpMethods {
    public static boolean checkKeysMatchPattern(Response response) throws Exception {
        String jsonResponse = response.asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        boolean allKeysMatchPattern = true;
        var iterator = rootNode.fieldNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.matches("^[a-zA-Z]{2,3}_[01]$")) {
                allKeysMatchPattern = false;
                System.out.println("Key does not match pattern: " + key);
                break;
            }
        }
        return allKeysMatchPattern;
    }

    public static boolean checkValuesNonNegative(Response response) throws Exception {
        String jsonResponse = response.asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        boolean allValuesNonNegative = true;
        var fields = rootNode.fields();
        while (fields.hasNext()) {
            var entry = fields.next();
            JsonNode valueNode = entry.getValue();
            if (valueNode.isNumber()) {
                if (valueNode.asInt() < 0) {
                    allValuesNonNegative = false;
                    System.out.println("Found value less than 0: " + valueNode.asInt() + " for key " + entry.getKey());
                    break;
                }
            }
        }
        return allValuesNonNegative;
    }


    public static void performTestForCountry(Set<String> operators, String country) throws Exception {
        for (String operator : operators) {
            Response response = sendRequest(country, operator);
            validateResponse(response);
        }
    }

    private static Response sendRequest(String country, String operator) {
        return RestAssured.given()
                .queryParam("api_key", API_KEY_TEST)
                .queryParam("action", GET_NUMBERS_COUNT)
                .queryParam("country", country)
                .queryParam("operator", operator)
                .when()
                .get(ENDPOINT)
                .then()
                .extract()
                .response();
    }

    private static void validateResponse(Response response) throws Exception {
        assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
        assertEquals("text/html; charset=UTF-8", response.getContentType());
        assertTrue(checkValuesNonNegative(response));
        assertTrue(checkKeysMatchPattern(response));
    }
}




