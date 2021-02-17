package ru.geekbrains.kosto.img;

import io.restassured.RestAssured;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {

    static Properties prop = new Properties();
    static String token;
    static String username;
    static String clientId;
    static String json;
    static String imageHash;
    static String imageDeleteHash;

    protected static Map<String, String> headers = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        headers.put("Authorization", token);

        RestAssured.baseURI = prop.getProperty("base.url");
        username = prop.getProperty("username");
        clientId = prop.getProperty("clientId");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
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

    static String uploadImageAndGetJson() {
        json = given()
                .headers("Authorization", token)
                .body("https://i.imgur.com/n744BL9.png")
                .when()
                .post("/image")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        return json;
    }
}
