package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class UpdateImageInformationTest extends BaseTest {

    @BeforeEach
    void setUp() {
        uploadImageAndGetJson();
        imageHash = from(json).get("data.id");
        imageDeleteHash = from(json).get("data.deletehash");

        checkBeforeUpdateImageInformation();
    }

    @DisplayName("Изменение информации от Authed пользвателя")
    @Test
    void updateImageInformationAuthed() {
        given()
                .headers("Authorization", token)
                .multiPart("title", "Heart")
                .multiPart("description", "This is an image of a heart outline.")
                .expect()
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Изменение информации от un-Authed пользвателя")
    @Test
    void updateImageInformationUnAuthed() {
        given()
                .headers("Authorization", clientId)
                .multiPart("title", "Heart")
                .multiPart("description", "This is an image of a heart outline.")
                .expect()
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @AfterEach
    void tearDown() {
        checkUpdateImageInformation();
        given()
                .headers("Authorization", token)
                .when()
                .delete("account/{username}/image/{deleteHash}", username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    void checkBeforeUpdateImageInformation() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data.title", is(nullValue()))
                .body("data.description", is(nullValue()))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    void checkUpdateImageInformation() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data.title", is("Heart"))
                .body("data.description", is("This is an image of a heart outline."))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
