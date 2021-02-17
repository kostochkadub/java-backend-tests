package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;

public class DeleteImagePositiveTest extends BaseTest{

    @BeforeEach
    void setUp() {
        uploadImageAndGetJson();
        imageHash = from(json).get("data.id");
        imageDeleteHash = from(json).get("data.deletehash");
        checkGetImageOk();
    }

    @Test
    void imageDeletionAuthed() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("image/{imageHash}",  imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void imageDeletionUnAuthed() {
        given()
                .headers("Authorization", clientId)
                .when()
                .delete("image/{imageDeleteHash}", imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @AfterEach
    void checkGetDeleteImage() {
        given()
                .headers("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    void checkGetImageOk() {
        given()
                .headers("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
