package org.ssu.belous;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class TestAuth {
    @Test
    public void test_1(){


        RestAssured.given().header("Content-type", "application/json").baseUri("http://localhost:8020/api/auth/registration").post().then().statusCode(200).log().all();

        RestAssured.given().header("Content-type", "application/json").baseUri("http://localhost:8020/api/auth/login").post().then().statusCode(200).log().all();
    }
}
