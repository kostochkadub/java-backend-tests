package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;
import ru.geekbrains.kosto.pojo.CommonResponse;
import ru.geekbrains.kosto.pojo.PojoGetImageResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.geekbrains.kosto.Endpoints.*;

public class FavoriteAnImageTest extends BaseTest{

    @BeforeAll
    static void setUp() {
        uploadImageAndGetJsonWithImageHashAndImageDeleteHash();
        checkFavoritedIsUnfavorited();
    }

    @Test
    void updateFavorite() {
        CommonResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .post(POST_IMAGE_IMAGEHASH_FAVORITE_REQUEST, imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(CommonResponse.class);

        assertThat(response.getData(), is("favorited"));
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getStatus(), is(200));
    }

    @AfterAll
    static void checkAndTearDown() {
        checkFavoritedIsFavorited();
        given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .delete(DELETE_IMAGE_USERNAME_AND_DELETEHASH_REQUEST, username, imageDeleteHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    static void checkFavoritedIsUnfavorited() {
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

        assertThat(response.getData().getFavorite(), is(false));
    }

    static void checkFavoritedIsFavorited() {
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

        assertThat(response.getData().getFavorite(), is(true));
    }


}
