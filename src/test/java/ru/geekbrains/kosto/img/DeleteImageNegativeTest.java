package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;

public class DeleteImageNegativeTest extends BaseTest{

    @Test
    void imageDeletionAuthed() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data.error", is("An ID is required."))
                .when()
                .delete("image/")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @Test
    void imageDeletionUnAuthed() {
        given()
                .headers("Authorization", clientId)
                .expect()
                .body("data.error", is("An ID is required."))
                .when()
                .delete("image/")
                .prettyPeek()
                .then()
                .statusCode(400);
    }


}
