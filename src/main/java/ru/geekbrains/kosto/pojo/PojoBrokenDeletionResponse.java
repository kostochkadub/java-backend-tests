package ru.geekbrains.kosto.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class PojoBrokenDeletionResponse extends CommonResponse<PojoBrokenDeletionResponse.ImageData> {
    @Data
    public static class ImageData {
        @JsonProperty("error")
        private String error;
        @JsonProperty("request")
        private String request;
        @JsonProperty("method")
        private String method;
    }
}
