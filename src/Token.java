public class Token {
    public enum Type {
        ADD, MUL, LPAREN, RPAREN, NUM, VAR, DAO,
        MINUS, POWER, COMMA, LCURLY, RCURLY, FUNC, EQUAL, RECURVAR, SIN, COS
    }

    private final Type type;
    private final String content;

    public Token(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Boolean isSign() {
        if (type == Type.ADD || type == Type.MINUS) {
            return true;
        }
        else {
            return false;
        }
    }
}