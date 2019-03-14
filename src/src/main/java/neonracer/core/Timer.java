package neonracer.core;

public class Timer {

    private static final long NS_PER_SECOND = 10000000L;
    private static final long MAX_NS_PER_UPDATE = 10000000L;
    private float ticksPerSecond;
    private long lastTime;
    private int ticks;
    private float partialTicks;
    private float timeScale;
    private float passedTime;

    Timer(float ticksPerSecond) {
        this.timeScale = 1.0f;
        this.passedTime = 0.0f;
        this.ticksPerSecond = ticksPerSecond;
        this.lastTime = System.nanoTime();
    }

    public int getTicks() {
        return ticks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void onFrame() {
        long now = System.nanoTime();
        long passedNs = now - this.lastTime;
        this.lastTime = now;
        if (passedNs < 0L) passedNs = 0L;
        if (passedNs > MAX_NS_PER_UPDATE) passedNs = MAX_NS_PER_UPDATE;
        this.passedTime += passedNs * this.timeScale * this.ticksPerSecond / NS_PER_SECOND;
        this.ticks = (int) this.passedTime;
        if (this.ticks > 100) this.ticks = 100;
        this.passedTime -= this.ticks;
        this.partialTicks = this.passedTime;
    }

}
