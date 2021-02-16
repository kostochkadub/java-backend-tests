package ru.geekbrains.kosto.img;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseTest {

    static Properties prop = new Properties();
    static String token;
    static String username;
    static String clientId;
    protected static Map<String, String> headers = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        headers.put("Authorization", token);

        RestAssured.baseURI = prop.getProperty("base.url");
        username = prop.getProperty("username");
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static void loadProperties() {
        try(InputStream inputStream = new FileInputStream("src/test/resources/application.properties")){
            prop.load(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
