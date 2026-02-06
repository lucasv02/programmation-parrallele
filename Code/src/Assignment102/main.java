package Assignment102;

public class main {

    public static void main(String[] args) {
        PiMonteCarlo PiVal = new PiMonteCarlo(100000);
        long startTime = System.nanoTime();
        double value = PiVal.getPi();
        long stopTime = System.nanoTime();
        System.out.println("Approx value:" + value);
        System.out.println("Difference to exact value of pi: " + (value - Math.PI));
        System.out.println("Error: " + (value - Math.PI) / Math.PI);
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Time Duration: " + (stopTime - startTime) + "ms");
    }
}
