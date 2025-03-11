import java.util.ArrayList;

public class FunctionFactor implements Factor {
    private String num = "";
    private ArrayList<Factor> factors = new ArrayList<>();
    private Var funcName;
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public FunctionFactor(Var funcName, ArrayList<DefinedFunction> functions) {
        this.funcName = funcName;
        this.functions = functions;
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        for (Factor factor : factors) {
            factor.addFunc(functions);
        }
    }

    public void addNum(String num) {
        this.num = num;
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    @Override
    public void print() {

    }

    @Override
    public Expr simplify() {
        for (DefinedFunction function : functions) {
            if (funcName.equals(function.getFuncName())) {
                return function.calExpr(Integer.parseInt(num), factors);
            }
        }
        return null;
    }

    @Override
    public String getFactorType() {
        return "FunctionFactor";
    }

    @Override
    public Factor clone() {
        return null;
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        return null;
    }

    @Override
    public boolean equal(Factor factor) {
        FunctionFactor factor1 = (FunctionFactor) factor;
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
