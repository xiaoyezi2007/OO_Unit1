import java.util.ArrayList;

public class Lexer {
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int index = 0;
    private int pos = 0;

    public Lexer(String input) {
        while (pos < input.length()) {
            if (input.charAt(pos) == 's') {   // use s to recognize sin !!!!!
                tokens.add(new Token(Token.Type.SIN, "sin"));
                pos += 3;
            } else if (input.charAt(pos) == 'd') {
                tokens.add(new Token(Token.Type.DAO, "dx"));
                pos += 2;
            } else if (input.charAt(pos) == 'c') {
                tokens.add(new Token(Token.Type.COS, "cos"));
                pos += 3;
            } else if (input.charAt(pos) == ',') {
                tokens.add(new Token(Token.Type.COMMA, ","));
                pos++;
            } else if (input.charAt(pos) == 'n') {
                tokens.add(new Token(Token.Type.RECURVAR, "n"));
                pos++;
            } else if (input.charAt(pos) == '{') {
                tokens.add(new Token(Token.Type.LCURLY, "{"));
                pos++;
            } else if (input.charAt(pos) == '}') {
                tokens.add(new Token(Token.Type.RCURLY, "}"));
                pos++;
            } else if (input.charAt(pos) == 'f' || input.charAt(pos) == 'g' || input.charAt(pos) == 'h') {
                tokens.add(new Token(Token.Type.FUNC, Character.toString(input.charAt(pos))));
                pos++;
            } else if (input.charAt(pos) == '=') {
                tokens.add(new Token(Token.Type.EQUAL, "="));
                pos++;
            } else if (input.charAt(pos) == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "("));
                pos++;
            } else if (input.charAt(pos) == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")"));
                pos++;
            } else if (input.charAt(pos) == '+') {
                tokens.add(new Token(Token.Type.ADD, "+"));
                pos++;
            } else if (input.charAt(pos) == '-') {
                tokens.add(new Token(Token.Type.MINUS, "-"));
                pos++;
            } else {
                checkStyle(input);
            }
        }
    }

    public Token getCurToken() {
        return tokens.get(index);
    }

    public void nextToken() {
        index++;
    }

    public boolean isEnd() {
        return index >= tokens.size();
    }

    public void checkStyle(String input) {
        if (input.charAt(pos) == '*') {
            tokens.add(new Token(Token.Type.MUL, "*"));
            pos++;
        } else if (input.charAt(pos) == 'x' || input.charAt(pos) == 'y') {
            tokens.add(new Token(Token.Type.VAR, Character.toString(input.charAt(pos))));
            pos++;
        } else if (input.charAt(pos) == ' ' || input.charAt(pos) == '\t') {
            pos++;
        } else if (input.charAt(pos) == '^') {
            tokens.add(new Token(Token.Type.POWER, "^"));
            pos++;
        } else {
            char now = input.charAt(pos);
            StringBuilder sb = new StringBuilder();
            while (now >= '0' && now <= '9') {
                sb.append(now);
                pos++;
                if (pos >= input.length()) {
                    break;
                }
                now = input.charAt(pos);
            }
            tokens.add(new Token(Token.Type.NUM, sb.toString()));
        }
    }
}
