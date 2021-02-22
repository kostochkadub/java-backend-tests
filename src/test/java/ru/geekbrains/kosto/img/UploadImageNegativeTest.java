package ru.geekbrains.kosto.img;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.geekbrains.kosto.Images;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static ru.geekbrains.kosto.Endpoints.POST_IMAGE_REQUEST;

public class UploadImageNegativeTest extends BaseTest {

    String encodedImageSizeOver10;
    String uploadedImageHashCode;

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContentInBase64(Images.IMAGE_MORE_10MB.path);
        encodedImageSizeOver10 = Base64.getEncoder().encodeToString(fileContent);
    }

    @DisplayName("Загрузка файла размером больше 10mb base 64")
    @Test
    void uploadFileTestSizeOver10() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart("image", encodedImageSizeOver10)
                .expect()
                .body("success", is(false))
                .body("data.error", is("File is over the size limit"))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification);
    }

    @DisplayName("Загрузка невалидного base64")
    @Test
    void uploadNotValidBase64() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5B546AEAAAAAНЕВАЛИДLAASDFAAAABAAEAAAIBRAA7SDFGбю")
                .expect()
                .body("success", is(false))
                .body("data.error.code", is(1003))
                .body("data.error.message", is("File type invalid (2)"))
                .body("data.error.type", is("ImgurException"))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification);
    }

    @DisplayName("Загрузка картинки размером больше 10mb через URL")
    @Test
    void uploadFileTestSizeOver10URL() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .body("http://img.lenagold.ru/tc/tcvet/roz/roz_tcvet210.png")
                .expect()
                .body("success", is(false))
                .body("data.error", is("File is over the size limit"))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification);
    }


    @DisplayName("Загрузка картинки размером больше 10mb")
    @Test
    void uploadFileTestMoreThen10MB() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart("image", new File(Images.IMAGE_MORE_10MB.path))
                .expect()
                .body("success", is(false))
                .body("data.error", is("File is over the size limit"))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification);
    }

    @DisplayName("Загрузка .txt")
    @Test
    void uploadFileTestTxt() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart("plain", new File(Images.TEXT_FILE.path))
                .expect()
                .body("success", is(false))
                .body("data.error", is("Invalid URL (test)"))//Пытается вытащить URL из файла. Но должно быть что-то типа File type invalid. Но это надо спрашивать у разрабов. Пока такая проверка
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .statusCode(400);
    }



}
