package ru.geekbrains.kosto.img;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import ru.geekbrains.kosto.img.BaseTest;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UploadImagePositiveTest extends BaseTest {

    String encodedImageSizeLess10;
    String encodedImageSizeLess1kb;
    String encodedImageSizeAbout10;
    String uploadedImageHashCode;

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContentInBase64("src/test/java/ru/geekbrains/kosto/img/repository/less_10.png");
        encodedImageSizeLess10 = Base64.getEncoder().encodeToString(fileContent);
        fileContent = getFileContentInBase64("src/test/java/ru/geekbrains/kosto/img/repository/10mb.png");
        encodedImageSizeAbout10 = Base64.getEncoder().encodeToString(fileContent);
        fileContent = getFileContentInBase64("src/test/java/ru/geekbrains/kosto/img/repository/less_1_kb.png");
        encodedImageSizeLess1kb = Base64.getEncoder().encodeToString(fileContent);

    }

    @DisplayName("Загрузка картинки размером меньше 10mb через URL")
    @Test
    void uploadFileTestURL() {
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

    @DisplayName("Загрузка файла размером меньше 10mb base 64")
    @Test
    void uploadFileTestSizeLess10() {
        uploadedImageHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", encodedImageSizeLess10)
                .expect()
                .body("success", is(true))
                .body("data.width",is(4329))
                .body("data.height",is(4033))
                .body("data.size",is(879072))
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


    @DisplayName("Загрузка файла размером около 10mb base 64")
    @Test
    void uploadFileTestSizeAbout10() {
        uploadedImageHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", encodedImageSizeAbout10)
                .expect()
                .body("success", is(true))
                .body("data.width",is(3116))
                .body("data.height",is(4632))
                .body("data.size",is(591466))
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

    @DisplayName("Загрузка файла размером меньше 1kb base 64")
    @Test
    void uploadFileTestSizeLess1kb() {
        uploadedImageHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", encodedImageSizeLess1kb)
                .expect()
                .body("success", is(true))
                .body("data.width",is(2))
                .body("data.height",is(1))
                .body("data.size",is(124))
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

    @DisplayName("Загрузка файла размером около 10mb файлом")
    @Test
    void uploadFileTestSizeAbout10mbFile() {
        uploadedImageHashCode = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/java/ru/geekbrains/kosto/img/repository/10mb.png"))
                .expect()
                .body("success", is(true))
                .body("data.width",is(3116))
                .body("data.height",is(4632))
                .body("data.size",is(591466))
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
