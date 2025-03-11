import java.util.ArrayList;
import java.util.Scanner;

public class Input {
    private final Expr expr;
    private ArrayList<DefinedFunction> funcs = new ArrayList<>();

    public Input() {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < num; i++) {
            String string = scanner.nextLine();
            Lexer lexer = new Lexer(string);
            Parser parser = new Parser(lexer);
            DefinedFunction function = parser.parseSimFunc(funcs);
            function.initial();
            funcs.add(function);
        }
        num = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < num; i++) {
            String string = scanner.nextLine();
            String string1 = scanner.nextLine();
            String string2 = scanner.nextLine();
            ArrayList<Lexer> lexers = new ArrayList<>();
            Lexer lexer = new Lexer(string);
            lexers.add(lexer);
            lexers.add(new Lexer(string1));
            lexers.add(new Lexer(string2));
            Parser parser = new Parser(lexer);
            parser.addLexer(lexers);
            DefinedFunction function = parser.parseFunc(funcs);
            function.initial();
            funcs.add(function);
        }
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        expr = parser.parseExpr(funcs);
    }

    public Expr target() {
        return expr;
    }
}
