import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();
    private ArrayList<DefinedFunction> functions = new ArrayList<>();
    private int sign = 1;

    public Term(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public void addSign(int sig) {
        sign *= sig;
    }

    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        for (Factor factor : factors) {
            factor.addFunc(functions);
        }
    }

    public void addSignIn() {
        Num num = this.findNum();
        num.addSign(sign);
        sign = 1;
    }

    public Term multi(Term term1) {
        Term tmp = null;
        tmp = this.clone();
        Term tmp1 = null;
        tmp1 = term1.clone();
        Num num = this.findNum();
        factors.remove(num);
        Num num1 = term1.findNum();
        term1.factors.remove(num1);
        num.multi(num1);
        Term ans = new Term(functions);
        ans.addFactor(num);
        for (Factor factor : factors) {
            Factor use = null;
            for (Factor factor1 : term1.factors) {
                if (factor.getFactorType().equals(factor1.getFactorType())
                    && factor.isMulti(factor1)) {
                    use = factor1;
                    factor.multi(factor1);
                    ans.addFactor(factor);
                }
            }
            if (use == null) {
                ans.addFactor(factor);
            }
            else {
                term1.factors.remove(use);
            }
        }
        for (Factor factor : term1.factors) {
            ans.addFactor(factor);
        }
        factors.clear();
        factors.addAll(tmp.factors);
        this.sign = tmp.sign;
        term1.factors.clear();
        term1.factors.addAll(tmp1.factors);
        term1.sign = tmp1.sign;
        return ans;
    }

    public Expr Simplify() {
        Expr exprSim = null;
        for (Factor factor : factors) {
            factor.addFunc(functions);
            if (exprSim == null) {
                exprSim = factor.simplify();
            }
            else {
                exprSim = exprSim.multiExpr(factor.simplify());
            }
        }
        return exprSim;
    }

    public Num findNum() {
        Num num = new Num(BigInteger.valueOf(1));
        int flag = 0;
        int sig = 1;
        for (Factor factor : factors) {
            if (factor.getFactorType().equals("Num")) {
                num = (Num) factor;
                flag = 1;
            }
            else if (factor.getFactorType().equals("TriFactor")
                && ((TriFactor) factor).isMinusFactor()) {
                factor.addSign(-1);
                if (((TriFactor) factor).isSin() && !((TriFactor) factor).isPowerEven()) {
                    sig *= -1;
                }
            }
        }
        num.addSign(sig);
        if (flag == 0) {
            this.addFactor(num);
        }
        return num;
    }

    public Term clone() {
        Term term = new Term(functions);
        for (Factor factor : factors) {
            term.addFactor(factor.clone());
        }
        term.sign = this.sign;
        return term;
    }

    public boolean print(boolean withSign) {
        int flag = 0;
        if (sign < 0) {
            this.findNum().addSign(sign);
            sign = 1;
        }
        if (this.findNum().absoluteIsOne() && factors.size() > 1) {
            if (this.findNum().isNegative()) {
                System.out.print("-");
            }
            else if (withSign) {
                System.out.print("+");
            }
        }
        else if (this.findNum().isZero()) {
            return false;
        }
        else {
            this.findNum().print(withSign);
            flag = 1;
        }
        for (Factor factor : factors) {
            if (!factor.getFactorType().equals("Num")) {
                if (flag == 1) {
                    System.out.print("*");
                }
                else {
                    flag = 1;
                }
                factor.print();
            }
        }
        return true;
    }

    public Term replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        Term term = new Term(functions);
        for (Factor factor1 : factors) {
            Factor factor2 = factor1.replace(var, factor);
            term.addFactor(factor2);
        }
        return term;
    }

    public boolean isFactor() {
        return factors.size() == 1 || (factors.size() == 2 && this.findNum().isOne());
    }

    public boolean isMinusFactor() {
        return (factors.size() == 1 && this.findNum().isNegative())
                || (factors.size() == 2 && this.findNum().isMinusOne());
    }

    public int solvePowerFactor(ArrayList<Factor> term1Dif,
        Factor factor, ArrayList<Factor> shared) {
        Factor use = null;
        for (Factor factor1 : term1Dif) {
            if (factor1.getFactorType().equals("PowerFactor") && factor.equal(factor1)) {
                shared.add(factor);
                use = factor1;
                break;
            }
        }
        if (use == null) {
            return 0;
        }
        else {
            term1Dif.remove(use);
        }
        return 1;
    }

    public ArrayList<Integer> solveTriFactor(Factor factor,
        ArrayList<Factor> term1Dif, ArrayList<Factor> shared,
        ArrayList<Factor> termDif, int sig, int sig1) {
        TriFactor triFactor = (TriFactor) factor;
        int si1 = sig1;
        int si = sig;
        Factor use = null;
        Factor ne = null;
        for (Factor factor1 : term1Dif) {
            if (!factor1.getFactorType().equals("TriFactor")) {
                continue;
            }
            TriFactor triFactor1 = (TriFactor) factor1;
            int k = triFactor.equalWithoutPower(triFactor1);
            if (k != 0) {
                TriFactor sharedTry = triFactor.shared(triFactor1);
                shared.add(sharedTry);
                use = factor1;
                if (!triFactor.powerEqual(sharedTry)) {
                    termDif.add(triFactor.div(sharedTry));
                    if (!sharedTry.isPowerEven()) {
                        si1 *= k;
                    }
                }
                else if (!triFactor1.powerEqual(sharedTry)) {
                    ne = triFactor1.div(sharedTry);
                    if (!sharedTry.isPowerEven()) {
                        si *= k;
                    }
                }
                else {
                    if (!sharedTry.isPowerEven()) {
                        si *= k;
                    }
                }
                break;
            }
        }
        if (use != null) {
            term1Dif.remove(use);
        }
        else {
            termDif.add(factor);
        }
        if (ne != null) {
            term1Dif.add(ne);
        }
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(si);
        ans.add(si1);
        return ans;
    }

    public ArrayList<Term> integrate(Num co, Term term1, Num co1) {
        ArrayList<Factor> termDif = new ArrayList<>();
        ArrayList<Factor> term1Dif = new ArrayList<>();
        ArrayList<Factor> shared = new ArrayList<>();
        int sig = 1;
        int sig1 = 1;
        if (term1.factors.size() == 1 && factors.size() > 1) {
            return null;
        }
        for (Factor factor : term1.factors) {
            if (!factor.getFactorType().equals("Num")) {
                term1Dif.add(factor);
            }
        }
        for (Factor factor : factors) {
            if (termDif.size() > 1) {
                return null;
            }
            if (factor.getFactorType().equals("Num")) {
                continue;
            }
            else if (factor.getFactorType().equals("PowerFactor")) {
                if (solvePowerFactor(term1Dif, factor, shared) == 0) {
                    return null;
                }
            }
            else {
                ArrayList<Integer> tmp =
                    solveTriFactor(factor, term1Dif, shared, termDif, sig, sig1);
                sig = tmp.get(0);
                sig1 = tmp.get(1);
            }
        }
        return integrateAns(termDif, term1Dif, shared, co, co1, sig, sig1);
    }

    public ArrayList<Term> solveEmpty(ArrayList<Factor> shared
        , Num co, Num co1, int sig, int sig1) {
        final ArrayList<Term> terms = new ArrayList<>();
        Term ans = new Term(functions);
        for (Factor factor : shared) {
            ans.addFactor(factor);
        }
        co.addSign(sig);
        co1.addSign(sig1);
        co.add(co1);
        ans.addFactor(co);
        terms.add(ans);
        return terms;
    }

    public ArrayList<Term> solveAddAsOne(Num co, Num co1,Term ans, Term ans1,
        ArrayList<Factor> termDif, ArrayList<Factor> term1Dif) {
        ArrayList<Term> terms = new ArrayList<>();
        if (co.isPositive()) {
            if (co.lessThan(co1)) {
                ans.addFactor(co);
                co1.sub(co);
                ans1.addFactor(co1);
                ans1.addFactor(term1Dif.get(0));
            }
            else {
                ans.addFactor(co1);
                co.sub(co1);
                ans1.addFactor(co);
                ans1.addFactor(termDif.get(0));
            }
        }
        else {
            if (!co.lessThan(co1)) {
                ans.addFactor(co);
                co1.sub(co);
                ans1.addFactor(co1);
                ans1.addFactor(term1Dif.get(0));
            }
            else {
                ans.addFactor(co1);
                co.sub(co1);
                ans1.addFactor(co);
                ans1.addFactor(termDif.get(0));
            }
        }
        terms.add(ans);
        terms.add(ans1);
        return terms;
    }

    public ArrayList<Term> integrateAns(ArrayList<Factor> termDif,
        ArrayList<Factor> term1Dif, ArrayList<Factor> shared, Num co, Num co1, int sig, int sig1) {
        for (Factor factor : term1Dif) {
            if (factor.getFactorType().equals("PowerFactor")) {
                return null;
            }
        }
        for (Factor factor : termDif) {
            if (factor.getFactorType().equals("PowerFactor")) {
                return null;
            }
        }
        if (termDif.size() == term1Dif.size() && termDif.isEmpty()) {
            return solveEmpty(shared, co, co1, sig, sig1);
        }
        else if (term1Dif.size() == termDif.size() && termDif.size() == 1) {
            final TriFactor triFactor = (TriFactor) termDif.get(0);
            final TriFactor triFactor1 = (TriFactor) term1Dif.get(0);
            co.addSign(sig);
            co1.addSign(sig1);
            Term ans = new Term(functions);
            Term ans1 = new Term(functions);
            for (Factor factor : shared) {
                ans.addFactor(factor.clone());
                ans1.addFactor(factor.clone());
            }
            if (co.sameSign(co1) && triFactor.addAsOne(triFactor1)) {
                return solveAddAsOne(co, co1, ans, ans1, termDif, term1Dif);
            }
            else {
                return null;
            }
        }
        return null;
    }

    public Expr dao() {
        Expr ans = new Expr(functions);
        for (int i = 0; i < factors.size(); i++) {
            Expr expr = factors.get(i).dao();
            for (int j = 0; j < factors.size(); j++) {
                if (j != i) {
                    expr = expr.multiExpr(factors.get(j).simplify());
                }
            }
            ans = ans.plusExpr(expr);
        }
        return ans;
    }
}
