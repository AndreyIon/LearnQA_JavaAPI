import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.naming.Name;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {

  @ParameterizedTest
  @ValueSource(strings = {"", "John", "Pete"})
  public void testHelloMethodWithoutName(String name){
    Map<String, String> quereParams = new HashMap<>();

    if (name.length() > 0){
      quereParams.put("name", name);
    }

    JsonPath response = RestAssured
            .given()
            .queryParams(quereParams)
            .get("https://playground.learnqa.ru/api/hello")
            .jsonPath();
    String answer = response.getString("answer");
    String expectedName = (name.length() > 0) ? name : "someone";
    assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
  }
}
