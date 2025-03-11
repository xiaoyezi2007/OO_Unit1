import java.util.ArrayList;

public interface Factor {
    void addFunc(ArrayList<DefinedFunction> functions);

    void print();

    Expr simplify();

    String getFactorType();

    Factor clone();

    Factor replace(ArrayList<Var> var, ArrayList<Factor> factor);

    boolean equal(Factor factor);// must in the same Type

    boolean minusEqual(Factor factor);

    boolean isMulti(Factor factor);

    void multi(Factor factor);

    public void addSign(int sign);

    public Expr dao();
}
