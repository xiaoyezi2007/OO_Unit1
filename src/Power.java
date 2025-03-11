import java.math.BigInteger;

public class Power {
    private BigInteger power;

    public Power(BigInteger power) {
        this.power = power;
    }

    public BigInteger getPower() {
        return power;
    }

    public boolean lessThan(Power power) {
        return this.power.compareTo(power.getPower()) <= 0;
    }

    public boolean isZero() {
        return this.power.equals(BigInteger.ZERO);
    }

    public boolean isEven() {
        return power.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO);
    }

    public boolean isSquare() {
        return this.power.equals(BigInteger.valueOf(2));
    }

    public Power minusOne() {
        return new Power(power.subtract(BigInteger.ONE));
    }

    public int min(Power power1) {
        BigInteger p1 = power1.getPower();
        if (power.compareTo(p1) < 0) {
            return power.intValue();
        }
        else {
            return p1.intValue();
        }
    }

    public boolean equals(Power power1) {
        return this.power.equals(power1.power);
    }

    public void add(Power power1) {
        power = power.add(power1.power);
    }

    public void sub(Power power1) {
        power = power.subtract(power1.power);
    }

    public Power clone() {
        return new Power(this.power);
    }

    public void print() {
        if (power.equals(BigInteger.ONE)) {
            return;
        }
        System.out.print("^" + power);
    }
}
