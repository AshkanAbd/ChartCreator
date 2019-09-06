package ir.ashkanabd;

public class Main {
    public static void main(String[] args) throws Exception {
        int min = 12;
        int max = 20;
        ChartGenerator chartGenerator = new ChartGenerator(min, max);
        chartGenerator.includes("graphic", "jabr khati", "compiler","algorithm");
        chartGenerator.generate();
        chartGenerator.sortResult();
        chartGenerator.showResult();
    }
}