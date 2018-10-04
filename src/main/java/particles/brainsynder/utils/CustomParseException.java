package particles.brainsynder.utils;

public class CustomParseException extends Exception {
    public CustomParseException(String s) {
        super("Can't be used ["+s+"]");
    }
}
