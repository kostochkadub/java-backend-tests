package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;
import ru.geekbrains.kosto.pojo.CommonResponse;
import ru.geekbrains.kosto.pojo.PojoPostUploadImageResponse;

import java.io.*;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.geekbrains.kosto.Endpoints.DELETE_IMAGE_USERNAME_AND_DELETEHASH_REQUEST;
import static ru.geekbrains.kosto.Endpoints.POST_IMAGE_REQUEST;

public class UploadImagePositiveTest extends BaseTest {

    String encodedImageSizeLess10;
    String encodedImageSizeLess1kb;
    String encodedImageSizeAbout10;
    String uploadedImageHashCode;

    String multiPartImage = "image";

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
        PojoPostUploadImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .body("https://i.imgur.com/n744BL9.png")
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoPostUploadImageResponse.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        uploadedImageHashCode = response.getData().getDeletehash();
    }

    @DisplayName("Загрузка файла размером меньше 10mb base 64")
    @Test
    void uploadFileTestSizeLess10() {
        PojoPostUploadImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart(multiPartImage, encodedImageSizeLess10)
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoPostUploadImageResponse.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getWidth(), equalTo(4329));
        assertThat(response.getData().getHeight(), equalTo(4033));
        assertThat(response.getData().getSize(), equalTo(879072));
        assertThat(response.getData().getSize(), is(notNullValue()));
        uploadedImageHashCode = response.getData().getDeletehash();
    }


    @DisplayName("Загрузка файла размером около 10mb base 64")
    @Test
    void uploadFileTestSizeAbout10() {
        PojoPostUploadImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart(multiPartImage, encodedImageSizeAbout10)
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoPostUploadImageResponse.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getWidth(), equalTo(3116));
        assertThat(response.getData().getHeight(), equalTo(4632));
        assertThat(response.getData().getSize(), equalTo(591466));
        assertThat(response.getData().getSize(), is(notNullValue()));
        uploadedImageHashCode = response.getData().getDeletehash();
    }

    @DisplayName("Загрузка файла размером меньше 1kb base 64")
    @Test
    void uploadFileTestSizeLess1kb() {
        PojoPostUploadImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart(multiPartImage, encodedImageSizeLess1kb)
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoPostUploadImageResponse.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getWidth(), equalTo(2));
        assertThat(response.getData().getHeight(), equalTo(1));
        assertThat(response.getData().getSize(), equalTo(124));
        assertThat(response.getData().getSize(), is(notNullValue()));
        uploadedImageHashCode = response.getData().getDeletehash();
    }

    @DisplayName("Загрузка файла размером около 10mb файлом")
    @Test
    void uploadFileTestSizeAbout10mbFile() {
        PojoPostUploadImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart(multiPartImage, new File("src/test/java/ru/geekbrains/kosto/img/repository/10mb.png"))
                .when()
                .post(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoPostUploadImageResponse.class);

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getSuccess(), equalTo(true));
        assertThat(response.getData().getWidth(), equalTo(3116));
        assertThat(response.getData().getHeight(), equalTo(4632));
        assertThat(response.getData().getSize(), equalTo(591466));
        assertThat(response.getData().getSize(), is(notNullValue()));
        uploadedImageHashCode = response.getData().getDeletehash();
    }


    @AfterEach
    void tearDown() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .delete(DELETE_IMAGE_USERNAME_AND_DELETEHASH_REQUEST, username, uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
