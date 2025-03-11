public class MainClass {
    public static void main(String[] args) {
        Input input = new Input();
        Expr expr = input.target();
        expr = expr.simplify();
        expr.print();
    }
}
