package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;

public class FavoriteAnImageTest extends BaseTest{

    @BeforeAll
    static void setUp() {
        uploadImageAndGetJson();
        imageHash = from(json).get("data.id");
        imageDeleteHash = from(json).get("data.deletehash");
        checkFavoritedIsUnfavorited();
    }

    @Test
    void updateFavorite() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data", is("favorited"))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @AfterAll
    static void checkAndTearDown() {
        checkFavoritedIsFavorited();
        given()
                .headers("Authorization", token)
                .when()
                .delete("account/{username}/image/{imageDeleteHash}", username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    static void checkFavoritedIsUnfavorited() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data.favorite", is(false))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    static void checkFavoritedIsFavorited() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("data.favorite", is(true))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }


}
