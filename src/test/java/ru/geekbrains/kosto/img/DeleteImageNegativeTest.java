package ru.geekbrains.kosto.img;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.kosto.pojo.CommonResponse;
import ru.geekbrains.kosto.pojo.PojoBrokenDeletionResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.geekbrains.kosto.Endpoints.POST_IMAGE_REQUEST;

public class DeleteImageNegativeTest extends BaseTest{

    @Test
    void imageDeletionAuthed() {
        PojoBrokenDeletionResponse response = given()
                .spec(reqSpecForAuthorizationWithToken)
                .when()
                .delete(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification)
                .extract()
                .body()
                .as(PojoBrokenDeletionResponse.class);

        assertThat(response.getData().getError(), is("An ID is required."));
    }

    @Test
    void imageDeletionUnAuthed() {
        PojoBrokenDeletionResponse response = given()
                .spec(reqSpecForAuthorizationWithClientId)
                .when()
                .delete(POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(badResponseSpecification)
                .extract()
                .body()
                .as(PojoBrokenDeletionResponse.class);

        assertThat(response.getData().getError(), is("An ID is required."));
    }


}
