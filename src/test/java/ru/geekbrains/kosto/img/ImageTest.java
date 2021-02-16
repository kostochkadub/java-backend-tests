package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.geekbrains.kosto.img.BaseTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageTest extends BaseTest {

    String encodedImage;
    String uploadedImageHashCode;

    @Test
    void uploadFileTest() {
        uploadedImageHashCode = given()
                .headers("Authorization", token)
                .body("https://i.imgur.com/n744BL9.png")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("account/{username}/image/{deleteHash}", username, uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(200);
    }


}
