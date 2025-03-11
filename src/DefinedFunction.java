import java.util.ArrayList;
import java.util.HashMap;

public class DefinedFunction {
    private HashMap<Integer, Expr> exprs = new HashMap<>();
    private ArrayList<Num> nums = new ArrayList<>();
    private ArrayList<Var> vars = new ArrayList<>();
    private ArrayList<FunctionFactor> funcFactors = new ArrayList<>();
    private Var funcName;
    private ArrayList<Expr> init = new ArrayList<>();

    public void addFuncFactor(FunctionFactor funcFactor) {
        funcFactors.add(funcFactor);
    }

    public void addFuncName(Var funcName) {
        this.funcName = funcName;
    }

    public void addVar(Var var) {
        vars.add(var);
    }

    public void addNum(Num num) {
        nums.add(num);
    }

    public void addExpr(Integer n, Expr expr) {
        exprs.put(n,expr);
    }

    public Var getFuncName() {
        return funcName;
    }

    public void initial() {
        if (!funcName.equals(new Var("f"))) {
            init.add(exprs.get(0).simplify());
        }
        else {
            for (int i = 0; i <= 5; i++) {
                if (i < 2) {
                    init.add(exprs.get(i).simplify());
                }
                else {
                    Expr ans = new Expr(new ArrayList<>());
                    ArrayList<Factor> factors = funcFactors.get(0).getFactors();
                    Expr expr = init.get(i - 1);
                    expr = expr.replace(vars, factors).simplify();
                    expr = expr.multiExpr(nums.get(0).simplify());
                    ans = ans.plusExpr(expr);
                    expr = init.get(i - 2);
                    factors = funcFactors.get(1).getFactors();
                    expr = expr.replace(vars, factors).simplify();
                    expr = expr.multiExpr(nums.get(1).simplify());
                    ans = ans.plusExpr(expr);
                    if (exprs.containsKey(3)) {
                        ans = ans.plusExpr(exprs.get(3).simplify());
                    }
                    init.add(ans);
                }
            }
        }
    }

    public Expr calExpr(int sig, ArrayList<Factor> factors) {
        Expr expr = init.get(sig);
        expr = expr.replace(vars, factors);
        return expr.simplify();
    }
}
