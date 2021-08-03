package neonracer.core;

public class Timer {

    private final int ticksPerSecond;

    private final int tickTime;

    private int ticks;

    private float elapsedTime;

    private long lastTick = System.currentTimeMillis();

    Timer(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.tickTime = 1000 / ticksPerSecond;
    }

    public void update() {
        long now = System.currentTimeMillis();
        ticks = (int) ((now - lastTick) / tickTime);
        elapsedTime = ((now - lastTick) / (float) tickTime) - ticks;
        if (ticks > 0)
            lastTick = now;
    }

    public void reset() {
        this.elapsedTime = 0.0f;
        this.ticks = 0;
        this.lastTick = System.currentTimeMillis();
    }

    public int getTicks() {
        return ticks;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public int getTicksPerSecond() {
        return ticksPerSecond;
    }
}
