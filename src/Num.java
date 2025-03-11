import java.math.BigInteger;
import java.util.ArrayList;

public class Num implements Factor {
    private BigInteger value;
    private ArrayList<DefinedFunction> functions = new ArrayList<>();

    public Num(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public void addSign(int sign) {
        value = value.multiply(BigInteger.valueOf(sign));
    }

    @Override
    public Expr dao() {
        return (new Num(BigInteger.ZERO)).simplify();
    }

    public void add(Num num1) {
        value = value.add(num1.value);
    }

    public void sub(Num num1) {
        value = value.subtract(num1.value);
    }

    public void multi(Num num1) {
        value = value.multiply(num1.value);
    }

    @Override
    public void multi(Factor factor) {
        value = value.multiply(((Num) factor).value);
    }

    public boolean lessThan(Num num1) {
        return value.subtract(num1.value).compareTo(BigInteger.ZERO) < 0;
    }

    public boolean sameSign(Num num1) {
        return (this.isPositive() && num1.isPositive()) || (this.isNegative() && num1.isNegative());
    }

    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    public boolean isNegative() {
        return value.compareTo(BigInteger.ZERO) < 0;
    }

    public boolean isPositive() {
        return value.compareTo(BigInteger.ZERO) > 0;
    }

    public boolean absoluteIsOne() {
        return value.equals(BigInteger.ONE) || value.equals(BigInteger.valueOf(-1));
    }

    public boolean isOne() {
        return value.equals(BigInteger.ONE);
    }

    public boolean isMinusOne() {
        return value.equals(BigInteger.valueOf(-1));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void addFunc(ArrayList<DefinedFunction> functions) {
        this.functions = functions;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    public void print(Boolean withSign) {
        if (withSign && value.compareTo(BigInteger.ZERO) > 0) {
            System.out.print("+");
        }
        System.out.print(value);
    }

    @Override
    public Expr simplify() {
        Expr ans = new Expr(functions);
        ans.addFunction(functions);
        Term term = new Term(functions);
        term.addFactor(this.clone());
        ans.addTerm(term);
        return ans;
    }

    @Override
    public String getFactorType() {
        return "Num";
    }

    @Override
    public Factor clone() {
        return new Num(this.value);
    }

    @Override
    public Factor replace(ArrayList<Var> var, ArrayList<Factor> factor) {
        return this.clone();
    }

    @Override
    public boolean equal(Factor factor) {
        Num num = (Num) factor;
        return value.equals(num.value);
    }

    @Override
    public boolean minusEqual(Factor factor) {
        return false;
    }

    @Override
    public boolean isMulti(Factor factor) {
        return true;
    }

}
