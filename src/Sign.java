public class Sign {
    private int sig;

    public Sign(Token token) {
        if (token.getType() == Token.Type.ADD) {
            this.sig = 1;
        }
        else if (token.getType() == Token.Type.MINUS) {
            this.sig = -1;
        }
    }

    public int getSign() {
        return sig;
    }
}
