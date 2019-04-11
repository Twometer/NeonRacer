package neonracer.core;

public class Timer {

    private final int ticksPerSecond;

    private final int tickTime;

    private int ticks;

    private float elaspedTime;

    private long lastTick = System.currentTimeMillis();

    Timer(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.tickTime = 1000 / ticksPerSecond;
    }

    public void update() {
        long now = System.currentTimeMillis();
        ticks = (int) ((now - lastTick) / tickTime);
        elaspedTime = ((now - lastTick) / (float) tickTime) - ticks;
        if (ticks > 0)
            lastTick = now;
    }

    public int getTicks() {
        return ticks;
    }

    public float getElapsedTime() {
        return elaspedTime;
    }

    public int getTicksPerSecond() {
        return ticksPerSecond;
    }
}
