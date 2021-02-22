package ru.geekbrains.kosto.img;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import ru.geekbrains.kosto.pojo.CommonResponse;
import ru.geekbrains.kosto.pojo.PojoPostUploadImageResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static ru.geekbrains.kosto.Endpoints.POST_IMAGE_REQUEST;

public abstract class BaseTest {

    static Properties prop = new Properties();
    static String token;
    static String username;
    static String clientId;
    static String json;
    static String imageHash;
    static String imageDeleteHash;

    protected static Map<String, String> headers = new HashMap<>();

    static ResponseSpecification responseSpecification = null;
    static ResponseSpecification badResponseSpecification = null;
    static RequestSpecification reqSpecForAuthorizationWithToken;
    static RequestSpecification reqSpecForAuthorizationWithClientId;

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        headers.put("Authorization", token);

        RestAssured.baseURI = prop.getProperty("base.url");
        username = prop.getProperty("username");
        clientId = prop.getProperty("clientId");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        reqSpecForAuthorizationWithToken = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.ANY)
                .build();

        reqSpecForAuthorizationWithClientId = new RequestSpecBuilder()
                .addHeader("Authorization", clientId)
                .setAccept(ContentType.ANY)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(20000L))
                .build();

        badResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectStatusLine("HTTP/1.1 400 Bad Request")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(20000L))
                .build();

    }

    private static void loadProperties() {
        try(InputStream inputStream = new FileInputStream("src/test/resources/application.properties")){
            prop.load(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected byte[] getFileContentInBase64(String pathname) {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(pathname);
        byte[] fileContent = new byte[0];
        try {
            fileContent =   FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    static String uploadImageAndGetJsonWithImageHashAndImageDeleteHash() {
        json = given()
                .spec(reqSpecForAuthorizationWithToken)
                .body("https://i.imgur.com/n744BL9.png")
                .when()
                .post(POST_IMAGE_REQUEST)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        imageHash = from(json).get("data.id");
        imageDeleteHash = from(json).get("data.deletehash");

        return json;
    }
}
