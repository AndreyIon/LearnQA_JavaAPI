import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest {

  @Test
  public void testAuthUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");

    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

    Map<String, String> cookies = responseGetAuth.getCookies();
    Headers headers = responseGetAuth.getHeaders();
    int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

    assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
    assertTrue(cookies.containsKey("auth_sid"), "Response doesn't have 'auth_sid' cookies");
    assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response doesn't have 'x-csrf-token' headers");
    assertTrue(responseGetAuth.jsonPath().getInt("user_id") > 0, "User id should be greater than 0");

    JsonPath responseCheckAuth = RestAssured
            .given()
            .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
            .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
            .get("https://playground.learnqa.ru/api/user/auth")
            .jsonPath();

      int userIdOnCheck = responseCheckAuth.getInt("user_id");
      assertTrue(userIdOnCheck > 0, "Unexpected user_id " + userIdOnCheck);

      assertEquals(
              userIdOnAuth,
              userIdOnCheck,
              "user id from auth request is not equal to user_id from check request"
      );

  }
}

/* Версия 2
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest {

  @Test
  public void testAuthUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com"); // Исправлено имя домена
    authData.put("password", "1234");

    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

    // Добавлена проверка на наличие user_id
    assertTrue(responseGetAuth.jsonPath().get("user_id") != null, "user_id should not be null");

    Map<String, String> cookies = responseGetAuth.getCookies();
    Headers headers = responseGetAuth.getHeaders();
    int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

    assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
    assertTrue(cookies.containsKey("auth_sid"), "Response doesn't have 'auth_sid' cookies");
    assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response doesn't have 'x-csrf-token' headers");
    assertTrue(userIdOnAuth > 0, "User id should be greater than 0");

    JsonPath responseCheckAuth = RestAssured
            .given()
            .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
            .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
            .get("https://playground.learnqa.ru/api/user/auth")
            .jsonPath();

    int userIdOnCheck = responseCheckAuth.getInt("user_id");

    // Логирование ответа (если нужно для отладки)
    System.out.println("Check Auth Response: " + responseCheckAuth.prettyPrint());

    assertTrue(userIdOnCheck > 0, "Unexpected user_id " + userIdOnCheck);

    assertEquals(
            userIdOnAuth,
            userIdOnCheck,
            "user id from auth request is not equal to user_id from check request"
    );
  }
}
 */
