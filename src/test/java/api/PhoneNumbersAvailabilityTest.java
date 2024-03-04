package api;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static DataForTesting.Constans.*;
import static DataForTesting.CountryCode.*;
import static DataForTesting.Operators.*;
import static Utilities.HelpMethods.*;


public class PhoneNumbersAvailabilityTest {
    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void checkNumberOfAvailableForRussiaAllOperators() throws Exception {
        performTestForCountry(OPERATORS_RUSSIA, RUSSIA);
    }

    @Test
    public void checkNumberOfAvailableForUkraineAllOperators() throws Exception {
        performTestForCountry(OPERATORS_UKRAINE, UKRAINE);
    }

    @Test
    public void checkNumberOfAvailableForKazakhstanAllOperators() throws Exception {
        performTestForCountry(OPERATORS_KAZAKHSTAN, KAZAKHSTAN);
    }


}
