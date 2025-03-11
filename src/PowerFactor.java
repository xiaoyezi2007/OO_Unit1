import java.math.BigInteger;
import java.util.ArrayList;

public class PowerFactor implements Factor {
    private Power power;
    private Var var;
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public PowerFactor(Var var, Power power, ArrayList<DefinedFunction> functions) {
        this.power = power;
        this.var = var;
        this.functions = functions;
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
    }

    @Override
    public void print() {
        var.print();
        power.print();
    }

    @Override
    public Expr simplify() {
        Expr ans = new Expr(functions);
        ans.addFunction(functions);
        Term term = new Term(functions);
        if (this.power.isZero()) {
            term.addFactor(new Num(BigInteger.ONE));
        }
        else {
            term.addFactor(this.clone());
        }
        ans.addTerm(term);
        return ans;
    }

    @Override
    public String getFactorType() {
        return "PowerFactor";
    }

    @Override
    public Factor clone() {
        return new PowerFactor(var.clone(), power.clone(), functions);
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        for (int i = 0; i < var.size(); i++) {
            if (this.var.equals(var.get(i))) {
                return new ExprFactor(factor.get(i).simplify(), this.power, functions);
            }
        }
        return this.clone();
    }

    @Override
    public boolean equal(Factor factor) {
        PowerFactor powerFactor = (PowerFactor) factor;
        return var.equals(powerFactor.var) && power.equals(powerFactor.power);
    }

    @Override
    public boolean minusEqual(Factor factor) {
        return false;
    }

    @Override
    public boolean isMulti(Factor factor) {
        return var.equals(((PowerFactor) factor).var);
    }

    @Override
    public void multi(Factor factor) {
        power.add(((PowerFactor) factor).power);
    }

    @Override
    public void addSign(int sign) {

    }

    @Override
    public Expr dao() {
        Expr expr = (new PowerFactor(var, power.minusOne(), functions)).simplify();
        expr = expr.multiExpr((new Num(power.getPower())).simplify());
        return expr;
    }

}
