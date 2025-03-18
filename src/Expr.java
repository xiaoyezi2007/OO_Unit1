import java.util.ArrayList;

public class Expr {
    private final ArrayList<Term> terms = new ArrayList<>();
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public Expr(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        for (Term term : terms) {
            term.addFunc(functions);
        }
    }

    public void addSign(int sign) {
        for (Term term : terms) {
            term.addSign(sign);
            term.addSignIn();
        }
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public void addFunc(DefinedFunction func) {
        functions.add(func);
    }

    public void addFunction(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
        for (Term term : terms) {
            term.addFunc(functions);
        }
    }

    public void removeTerm(Term term) { terms.remove(term); }

    public Expr simplify() {
        Expr exprSim = new Expr(functions);
        for (Term term : terms) {
            term.addSignIn();
            term.addFunc(functions);
            exprSim = exprSim.plusExpr(term.Simplify());
        }
        return exprSim;
    }

    public Expr plusExpr(Expr expr1) {
        Expr ans = new Expr(functions);
        for (Term term : terms) {
            ans.addTerm(term);
        }
        for (Term term : expr1.terms) {
            ans.addTerm(term);
        }
        return ans.combine();
    }

    public Expr multiExpr(Expr expr1) {
        Expr expr = new Expr(functions);
        for (Term term1 : expr1.terms) {
            for (Term term : terms) {
                expr.addTerm(term.multi(term1));
            }
        }
        return expr.combine();
    }

    public Expr combine() {
        Expr expr = new Expr(functions);
        for (Term termIn : terms) {
            Term use = null;
            for (Term term : expr.terms) {
                ArrayList<Term> newTerms = term.integrate(term.findNum(), termIn, termIn.findNum());
                if (newTerms != null) {
                    use = term;
                    for (Term newTerm : newTerms) {
                        if (!newTerm.findNum().isZero()) {
                            expr.addTerm(newTerm);
                        }
                    }
                    break;
                }
            }
            if (use == null) {
                if (!termIn.findNum().isZero()) {
                    expr.addTerm(termIn);
                }
            }
            else {
                expr.removeTerm(use);
            }
        }
        return expr;
    }

    public void print() {
        int flag = 0;
        Term use = null;
        for (Term ter : terms) {
            if (ter.findNum().isPositive()) {
                use = ter;
                ter.print(false);
                flag = 1;
                break;
            }
        }
        if (use != null) {
            terms.remove(use);  // there change the terms!!!!!
        }
        for (Term term : terms) {
            if (flag == 0) {
                if (term.print(false)) {
                    flag = 1;
                }
            }
            else {
                term.print(true);
            }
        }
        if (flag == 0) {
            System.out.print(0);
        }
    }

    public boolean isZero() {
        Expr ans = this;
        if (ans.terms.isEmpty()) {
            return true;
        }
        if (ans.terms.size() == 1) {
            return terms.get(0).findNum().isZero();
        }
        return false;
    }

    public Expr replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        Expr expr = new Expr(functions);
        for (Term term : terms) {
            Term term1 = term.replace(var, factor);
            expr.addTerm(term1);
        }
        return expr;
    }

    public boolean equals(Expr expr) {
        Expr tmp = new Expr(functions);
        for (Term term : terms) {
            tmp.addTerm(term.clone());
        }
        for (Term term : expr.terms) {
            Term term1 = term.clone();
            term1.addSign(-1);
            term1.addSignIn();
            tmp.addTerm(term1);
        }
        return tmp.combine().isZero();
    }

    public boolean minusEqual(Expr expr) {
        Expr tmp = new Expr(functions);
        for (Term term : terms) {
            tmp.addTerm(term.clone());
        }
        for (Term term : expr.terms) {
            tmp.addTerm(term.clone());
        }
        return tmp.combine().isZero();
    }

    public boolean isFactor() {
        return terms.size() == 1 && terms.get(0).isFactor();
    }

    public boolean isMinusFactor() {
        return terms.size() == 1 && terms.get(0).isMinusFactor();
    }

    @Override
    public Expr clone() {
        Expr expr = new Expr(functions);
        for (Term term : terms) {
            expr.addTerm(term.clone());
        }
        return expr;
    }

    public Expr dao() {
        Expr expr = new Expr(functions);
        for (Term term : terms) {
            expr = expr.plusExpr(term.dao());
        }
        return expr.simplify();
    }
}
