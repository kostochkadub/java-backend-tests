package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;
import ru.geekbrains.kosto.pojo.PojoGetImageResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.geekbrains.kosto.Endpoints.*;

public class UpdateImageInformationTest extends BaseTest {

    @BeforeEach
    void setUp() {
        uploadImageAndGetJsonWithImageHashAndImageDeleteHash();
        checkBeforeUpdateImageInformation();
    }

    @DisplayName("Изменение информации от Authed пользвателя")
    @Test
    void updateImageInformationAuthed() {
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .multiPart("title", "Heart")
                .multiPart("description", "This is an image of a heart outline.")
                .expect()
                .body("data", is(true))
                .when()
                .post(IMAGE_IMAGEHASH_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @DisplayName("Изменение информации от un-Authed пользвателя")
    @Test
    void updateImageInformationUnAuthed() {
        given()
                .spec(reqSpecForAuthorizationWithClientId)
                .multiPart("title", "Heart")
                .multiPart("description", "This is an image of a heart outline.")
                .expect()
                .body("data", is(true))
                .when()
                .post(IMAGE_DELETEHASH_REQUEST, imageDeleteHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @AfterEach
    void tearDown() {
        checkUpdateImageInformation();
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .delete(DELETE_IMAGE_USERNAME_AND_DELETEHASH_REQUEST, username, imageDeleteHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    void checkBeforeUpdateImageInformation() {
        PojoGetImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .get(IMAGE_IMAGEHASH_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoGetImageResponse.class);

        assertThat(response.getData().getTitle(), is(nullValue()));
        assertThat(response.getData().getDescription(), is(nullValue()));
    }

    void checkUpdateImageInformation() {
        PojoGetImageResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .get(IMAGE_IMAGEHASH_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(PojoGetImageResponse.class);

        assertThat(response.getData().getTitle(), is("Heart"));
        assertThat(response.getData().getDescription(), is("This is an image of a heart outline."));
    }

}
