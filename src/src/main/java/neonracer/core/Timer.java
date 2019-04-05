package neonracer.core;

public class Timer {

    private final int tickTime;

    private int ticks;

    private float deltaTime;

    private long lastTick = System.currentTimeMillis();

    Timer(int ticksPerSecond) {
        this.tickTime = 1000 / ticksPerSecond;
    }

    public void update() {
        long now = System.currentTimeMillis();
        ticks = (int) ((now - lastTick) / tickTime);
        deltaTime = ((now - lastTick) / (float)tickTime) - ticks;
        if (ticks > 0)
            lastTick = now;
    }

    public int getTicks() {
        return ticks;
    }

    public float getDeltaTime() {
        return deltaTime;
    }
}
