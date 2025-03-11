import java.util.ArrayList;

public class DaoFactor implements Factor {
    private ArrayList<DefinedFunction> functions;
    private Expr expr;

    public DaoFactor(Expr expr, ArrayList<DefinedFunction> functions) {
        this.expr = expr;
        this.functions = functions;
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
    }

    @Override
    public void print() {

    }

    @Override
    public Expr simplify() {
        return expr.dao();
    }

    @Override
    public String getFactorType() {
        return "DaoFactor";
    }

    @Override
    public Factor clone() {
        return new DaoFactor(expr.clone(), functions);
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        return new DaoFactor(expr.replace(var, factor), functions);
    }

    @Override
    public boolean equal(Factor factor) {
        if (factor.getFactorType().equals("DaoFactor")) {
            return expr.equals(((DaoFactor) factor).expr);
        }
        return false;
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
        expr.addSign(sign);
    }

    @Override
    public Expr dao() {
        return this.simplify().dao();
    }
}
