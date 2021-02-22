package ru.geekbrains.kosto;

public class Endpoints {
    public static final String GET_ACCOUNT_REQUEST = "/account/{username}";
    public static final String POST_IMAGE_REQUEST = "/image";
    public static final String GET_IMAGE_ID_REQUEST = "/image/{id}";
    public static final String IMAGE_IMAGEHASH_REQUEST = "/image/{imageHash}";
    public static final String DELETE_IMAGE_USERNAME_AND_DELETEHASH_REQUEST = "/account/{username}/image/{deleteHash}";
    public static final String IMAGE_DELETEHASH_REQUEST = "/image/{deleteHash}";
    public static final String DELETE_IMAGE_IMAGEDELETEHASH_REQUEST = "/image/{imageDeleteHash}";
    public static final String POST_IMAGE_IMAGEHASH_FAVORITE_REQUEST ="/image/{imageHash}/favorite";


}
