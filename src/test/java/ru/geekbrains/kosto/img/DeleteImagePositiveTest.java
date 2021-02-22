package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static ru.geekbrains.kosto.Endpoints.*;

public class DeleteImagePositiveTest extends BaseTest{

    @BeforeEach
    void setUp() {
        uploadImageAndGetJsonWithImageHashAndImageDeleteHash();
        checkGetImageOk();
    }

    @Test
    void imageDeletionAuthed() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .delete(IMAGE_DELETEHASH_REQUEST,  imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void imageDeletionUnAuthed() {
        given()
                .spec(reqSpecForAuthorizationWithClientId)
                .when()
                .delete(DELETE_IMAGE_IMAGEDELETEHASH_REQUEST, imageDeleteHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @AfterEach
    void checkGetDeleteImage() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .get(IMAGE_IMAGEHASH_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    void checkGetImageOk() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .get(IMAGE_IMAGEHASH_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

}
