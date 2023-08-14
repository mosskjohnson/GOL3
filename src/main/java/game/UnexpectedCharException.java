package game;

public class UnexpectedCharException extends Exception {

    public UnexpectedCharException(String errorMessage) {
        super(errorMessage);
    }

    public UnexpectedCharException(char unexpectedChar) {
        super("the char: " + unexpectedChar + " was not expected");
    }

    public UnexpectedCharException(char expectedChar, char unexpectedChar) {
        super("expected: "  + expectedChar + " and found: " + unexpectedChar);
    }
}