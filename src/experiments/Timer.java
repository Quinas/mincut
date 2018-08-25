package experiments;

public class Timer {
    long startTime;

    Timer() {
        startTime = System.nanoTime();
    }

    double ellapsed() {
        long endTime = System.nanoTime();
        long durationNano = (endTime - startTime);
        return (double) durationNano * 1e-9;
    }
}