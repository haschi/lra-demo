package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@QuarkusTest
public class LraTest {

    @Test
    public void canExecuteLongRunningAction() {
        var context = given()
                .when()
                .put("/first")
                .then()
                .statusCode(200)
                        .extract().body().asString();

        given()
                .header(LRA_HTTP_CONTEXT_HEADER, context)
                .when()
                .put("/second")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldCompensateFirst() {
        var context = given()
                .when()
                .put("/first")
                .then()
                .statusCode(200)
                .extract().body().asString();

        given()
                .header(LRA_HTTP_CONTEXT_HEADER, context)
                .when()
                .put("/second?error=true")
                .then()
                .statusCode(400);
    }
}
