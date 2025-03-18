import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    private ArrayList<Lexer> lexers = new ArrayList<>();
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
    }

    public void addLexer(ArrayList<Lexer> lexers) {
        this.lexers = lexers;
    }

    public DefinedFunction parseSimFunc(ArrayList<DefinedFunction> functions) {
        DefinedFunction func = new DefinedFunction();
        func.addFuncName(new Var(lexer.getCurToken().getContent()));
        lexer.nextToken();
        while (lexer.getCurToken().getType() != Token.Type.RPAREN) {
            lexer.nextToken();
            func.addVar(new Var(lexer.getCurToken().getContent()));
            lexer.nextToken();
        }
        lexer.nextToken();
        lexer.nextToken();
        func.addExpr(0, parseExpr(functions));
        return func;
    }

    public DefinedFunction parseFunc(ArrayList<DefinedFunction> functions) {
        DefinedFunction func = new DefinedFunction();
        int first = 0;
        for (Lexer lexer1: lexers) {
            this.lexer = lexer1;
            func.addFuncName(new Var(lexer.getCurToken().getContent()));
            lexer.nextToken();
            lexer.nextToken();
            final String flag = lexer.getCurToken().getContent();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            if (first == 0) {
                func.addVar(new Var(lexer.getCurToken().getContent()));
            }
            lexer.nextToken();
            while (lexer.getCurToken().getType() != Token.Type.RPAREN) {
                lexer.nextToken();
                if (first == 0) {
                    func.addVar(new Var(lexer.getCurToken().getContent()));
                }
                lexer.nextToken();
            }
            lexer.nextToken();
            lexer.nextToken();
            if (flag.equals("n")) {
                func.addNum(parseNum(functions));
                lexer.nextToken();
                func.addFuncFactor(parseFuncFactor(functions));
                Sign sign = parseSign();
                Num num = parseNum(functions);
                num.addSign(sign.getSign());
                func.addNum(num);
                lexer.nextToken();
                func.addFuncFactor(parseFuncFactor(functions));
                if (!lexer.isEnd()) {
                    lexer.nextToken();
                    func.addExpr(3,parseExpr(functions));
                }
            }
            else {
                func.addExpr(Integer.parseInt(flag), parseExpr(functions));
            }
            first = 1;
        }
        return func;
    }

    public Expr parseExpr(ArrayList<DefinedFunction> functions) {
        Expr expr = new Expr(functions);

        if (lexer.getCurToken().isSign()) {
            Sign sign = parseSign();
            Term term = parseTerm(functions);
            term.addSign(sign.getSign());
            expr.addTerm(term);
        } else {
            Term term = parseTerm(functions);
            expr.addTerm(term);
        }
        while (!lexer.isEnd() && lexer.getCurToken().isSign()) {
            Sign sign = parseSign();
            Term term = parseTerm(functions);
            term.addSign(sign.getSign());
            expr.addTerm(term);
        }
        return expr;
    }

    public Term parseTerm(ArrayList<DefinedFunction> functions) {
        Term term = new Term(functions);
        if (lexer.getCurToken().isSign()) {
            term.addSign(parseSign().getSign());
        }
        term.addFactor(parseFactor(functions)); //<todo5>
        while (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.MUL) {
            lexer.nextToken();
            term.addFactor(parseFactor(functions));
        }
        return term;
    }

    public Factor parseFactor(ArrayList<DefinedFunction> functions) {
        Token token = lexer.getCurToken();
        if (token.getType() == Token.Type.FUNC) {
            return parseFuncFactor(functions);
        }
        if (token.getType() == Token.Type.LPAREN) {
            return parseExprFactor(functions);
        } else if (token.getType() == Token.Type.VAR) {
            return parsePowerFactor(functions);
        } else if (token.getType() == Token.Type.SIN || token.getType() == Token.Type.COS) {
            return parseTriFactor(functions);
        } else if (token.getType() == Token.Type.DAO) {
            return parseDaoFactor(functions);
        } else {
            return parseNum(functions);
        }
    }

    public DaoFactor parseDaoFactor(ArrayList<DefinedFunction> functions) {
        lexer.nextToken();
        lexer.nextToken();
        Expr expr = parseExpr(functions);
        lexer.nextToken();
        return new DaoFactor(expr, functions);
    }

    public TriFactor parseTriFactor(ArrayList<DefinedFunction> functions) {
        final TriFactor triFactor;
        final String Tri = lexer.getCurToken().getContent();
        lexer.nextToken();
        lexer.nextToken();
        Factor factor = parseFactor(functions);
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.POWER) {
            Power power = parsePower();
            triFactor = new TriFactor(Tri, factor.simplify(), power, functions);
        }
        else {
            triFactor = new TriFactor(Tri, factor.simplify(), new Power(BigInteger.ONE), functions);
        }
        return triFactor;
    }

    public FunctionFactor parseFuncFactor(ArrayList<DefinedFunction> functions) {
        final FunctionFactor funcFactor =
            new FunctionFactor(new Var(lexer.getCurToken().getContent()), functions);
        if (lexer.getCurToken().getContent().equals("f")) {
            lexer.nextToken();
            lexer.nextToken();
            String signal = "";
            while (lexer.getCurToken().getType() != Token.Type.RCURLY) {
                signal += lexer.getCurToken().getContent();
                lexer.nextToken();
            }
            funcFactor.addNum(signal);
            lexer.nextToken();
        }
        else {
            lexer.nextToken();
            funcFactor.addNum("0");
        }
        lexer.nextToken();
        funcFactor.addFactor(parseFactor(functions));
        while (lexer.getCurToken().getType() != Token.Type.RPAREN) {
            lexer.nextToken();
            funcFactor.addFactor(parseFactor(functions));
        }
        lexer.nextToken();
        return funcFactor;
    }

    public Num parseNum(ArrayList<DefinedFunction> functions) {
        Num num;
        int sign = 1;
        if (lexer.getCurToken().isSign()) {
            sign = parseSign().getSign();
        }
        Token token = lexer.getCurToken();
        lexer.nextToken();
        num = new Num(new BigInteger(token.getContent()));
        num.addSign(sign);
        return num;
    }

    public PowerFactor parsePowerFactor(ArrayList<DefinedFunction> functions) {
        PowerFactor powerFactor;
        Var var = parseVar();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.POWER) {
            Power power = parsePower();
            powerFactor = new PowerFactor(var, power, functions);
        } else {
            powerFactor = new PowerFactor(var, new Power(new BigInteger("1")), functions);
        }
        return powerFactor;
    }

    public ExprFactor parseExprFactor(ArrayList<DefinedFunction> functions) {
        ExprFactor exprFactor;
        lexer.nextToken();
        Expr expr = parseExpr(functions);
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.POWER) {
            Power power = parsePower();
            exprFactor = new ExprFactor(expr, power, functions);
        } else {
            exprFactor = new ExprFactor(expr, new Power(new BigInteger("1")), functions);
        }
        return exprFactor;
    }

    public Var parseVar() {
        Var var;
        Token token = lexer.getCurToken();
        lexer.nextToken();
        var = new Var(token.getContent());
        return var;
    }

    public Power parsePower() {
        lexer.nextToken();
        if (lexer.getCurToken().isSign()) { // this is true for only '+' !!!!!
            lexer.nextToken();
        }
        Token token = lexer.getCurToken();
        lexer.nextToken();
        Power power;
        power = new Power(new BigInteger(token.getContent()));
        return power;
    }

    public Sign parseSign() {
        Sign sign;
        Token token = lexer.getCurToken();
        lexer.nextToken();
        sign = new Sign(token);
        return sign;
    }

}
