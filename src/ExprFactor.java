import java.math.BigInteger;
import java.util.ArrayList;

@SuppressWarnings("checkstyle:Indentation")
public class ExprFactor implements Factor {
    private Expr expr;
    private Power power;
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public ExprFactor(Expr expr, Power power, ArrayList<DefinedFunction> functions) {
        this.expr = expr;
        this.power = power;
        this.functions = functions;
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        expr.addFunction(functions);
    }

    @Override
    public void print() {
        System.out.print("Wrong!!!!!");
    }

    @Override
    public Expr simplify() {
        Expr ans = new Expr(functions);
        ans.addFunction(functions);
        if (power.getPower().equals(BigInteger.valueOf(0))) {
            Term term = new Term(functions);
            term.addFactor(new Num(BigInteger.valueOf(1)));
            ans.addTerm(term);
            return ans;
        } else {
            ans = expr.simplify();
            for (int i = 1; BigInteger.valueOf(i).compareTo(power.getPower()) < 0; i++) {
                ans = ans.multiExpr(expr.simplify());
            }
            return ans;
        }
    }

    @Override
    public String getFactorType() {
        return "ExprFactor";
    }

    @Override
    public Factor clone() {
        return new ExprFactor(expr.clone(), power.clone(), functions);
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        return new ExprFactor(this.expr.replace(var, factor), this.power.clone(), functions);
    }

    @Override
    public boolean equal(Factor factor) {
        ExprFactor factor1 = (ExprFactor) factor;
        return this.simplify().equals(factor1.simplify());
    }

    @Override
    public boolean minusEqual(Factor factor) {
        return false;
    }

    @Override
    public boolean isMulti(Factor factor) {
        return false;
    }

    @Override
    public void multi(Factor factor) {

    }

    @Override
    public void addSign(int sign) {

    }

    @Override
    public Expr dao() {
        return this.simplify().dao();
    }
}
