package ru.geekbrains.kosto;

public enum Images {
    IMAGE_10MB("src/test/resources/repository/10mb.png"),
    IMAGE_MORE_10MB("src/test/resources/repository/more_10.png"),
    IMAGE_SMALL_SIZE("src/test/resources/repository/less_1_kb.png"),
    IMAGE_LESS_10MB("src/test/resources/repository/less_10.png"),
    TEXT_FILE("src/test/resources/repository/test.txt");

    public final String path;

    Images(String path) {
        this.path = path;
    }
}
