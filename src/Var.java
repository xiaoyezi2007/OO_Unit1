public class Var {
    private final String name;

    public Var(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Var var) {
        return this.name.equals(var.getName());
    }

    public Var clone() {
        return new Var(this.name);
    }

    public void print() {
        System.out.print(name);
    }
}
