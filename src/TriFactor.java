import java.math.BigInteger;
import java.util.ArrayList;

public class TriFactor implements Factor {
    private int kind = 0;
    private Expr expr = null;
    private Power power = null;
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public TriFactor(String name, Expr expr, Power power,
        ArrayList<DefinedFunction> functions) {
        if (name.equals("sin")) {
            this.kind = 0;
        }
        else {
            this.kind = 1;
        }
        this.expr = expr;
        this.power = power;
        this.functions = functions;
    }

    public int equalWithoutPower(TriFactor triFactor) {
        if (kind != triFactor.kind) {
            return 0;
        }
        else if (kind == 1) {
            if (expr.equals(triFactor.expr) || expr.minusEqual(triFactor.expr)) {
                return 1;
            }
            return 0;
        }
        else {
            if (expr.minusEqual(triFactor.expr)) {
                return -1;
            }
            else if (expr.equals(triFactor.expr)) {
                return 1;
            }
            return 0;
        }
    }

    public boolean addAsOne(TriFactor triFactor) {
        return kind != triFactor.kind
                && (expr.equals(triFactor.expr)
                || expr.minusEqual(triFactor.expr))
                && power.isSquare() && triFactor.power.isSquare();
    }

    public TriFactor shared(TriFactor triFactor) {
        if (power.lessThan(triFactor.power)) {
            TriFactor ans = (TriFactor) triFactor.clone();
            ans.setPower(power);
            return ans;
        }
        TriFactor ans = (TriFactor) this.clone();
        ans.setPower(triFactor.power);
        return ans;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public boolean isPowerEven() {
        return power.isEven();
    }

    public TriFactor div(TriFactor triFactor) {
        TriFactor triFactor1 = (TriFactor) this.clone();
        triFactor1.power.sub(triFactor.power);
        return triFactor1;
    }

    public boolean powerEqual(TriFactor triFactor) {
        return power.equals(triFactor.power);
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        expr.addFunction(functions);
    }

    @Override
    public void print() {
        if (kind == 0) {
            System.out.print("sin(");
        }
        else {
            System.out.print("cos(");
        }
        if (expr.isFactor()) {
            expr.print();
        }
        else {
            System.out.print("(");
            expr.print();
            System.out.print(")");
        }
        System.out.print(")");
        power.print();
    }

    public boolean isMinusFactor() {
        return expr.isMinusFactor();
    }

    public boolean isSin() {
        return kind == 0;
    }

    @Override
    public Expr simplify() {
        Expr ans = new Expr(functions);
        ans.addFunction(functions);
        Term term = new Term(functions);
        if (power.isZero()) {
            term.addFactor(new Num(BigInteger.ONE));
        }
        else if (expr.isZero()) {
            term.addFactor(new Num(BigInteger.valueOf(kind)));
        }
        else {
            term.addFactor(this.clone());
        }
        ans.addTerm(term);
        return ans;
    }

    @Override
    public String getFactorType() {
        return "TriFactor";
    }

    @Override
    public Factor clone() {
        if (kind == 0) {
            return new TriFactor("sin", this.expr.clone(), power.clone(), functions);
        }
        else {
            return new TriFactor("cos", this.expr.clone(), power.clone(), functions);
        }
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        if (kind == 0) {
            return new TriFactor("sin", this.expr.replace(var, factor).simplify()
                    , power.clone(), functions);
        }
        else {
            return new TriFactor("cos", this.expr.replace(var, factor).simplify()
                    , power.clone(), functions);
        }
    }

    @Override
    public boolean equal(Factor factor) {
        TriFactor triFactor = (TriFactor) factor;
        if (kind == 1 && triFactor.kind == 1) {
            return power.equals(triFactor.power)
                    && (expr.equals(triFactor.expr) || expr.minusEqual(triFactor.expr));
        }
        else if (kind == 0 && triFactor.kind == 0) {
            return power.equals(triFactor.power)
                    && (expr.equals(triFactor.expr)
                    || (power.isEven() && expr.minusEqual(triFactor.expr)));
        }
        return false;
    }

    @Override
    public boolean minusEqual(Factor factor) {
        TriFactor triFactor = (TriFactor) factor;
        return kind == 0 && triFactor.kind == 0
                && power.equals(triFactor.power)
                && expr.minusEqual(triFactor.expr) && !power.isEven();
    }

    @Override
    public boolean isMulti(Factor factor) {
        TriFactor triFactor = (TriFactor) factor;
        return kind == triFactor.kind && expr.equals(triFactor.expr);
    }

    @Override
    public void multi(Factor factor) {
        power.add(((TriFactor) factor).power);
    }

    @Override
    public void addSign(int sign) {
        expr.addSign(sign);
    }

    @Override
    public Expr dao() {
        Expr expr1 = (new Num(power.getPower())).simplify();
        expr1 = expr1.multiExpr(expr.dao());
        expr1 = expr1.multiExpr(new TriFactor(kindName(), this.expr.clone()
                , power.minusOne(), functions).simplify());
        if (kind == 0) {
            expr1 = expr1.multiExpr((new TriFactor("cos", this.expr.clone(),
                new Power(BigInteger.ONE), functions)).simplify());
        }
        else {
            expr1 = expr1.multiExpr((new TriFactor("sin", this.expr.clone(),
                new Power(BigInteger.ONE), functions)).simplify());
            expr1 = expr1.multiExpr(new Num(BigInteger.valueOf(-1)).simplify());
        }
        return expr1;
    }

    public String kindName() {
        if (kind == 0) {
            return "sin";
        }
        else {
            return "cos";
        }
    }
}
