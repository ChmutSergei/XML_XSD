package by.chmut.xml.builder;

public class BuilderException extends Exception {

    public BuilderException() {
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(Exception e) {
        super(e);
    }

    public BuilderException(String message, Exception e) {
        super(message, e);
    }
}

