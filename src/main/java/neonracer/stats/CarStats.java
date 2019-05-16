package neonracer.stats;

public class CarStats {

    private int place;

    private int lapsPassed;

    private boolean passedHalfway = true;

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getLapsPassed() {
        return lapsPassed;
    }

    public void setLapsPassed(int lapsPassed) {
        this.lapsPassed = lapsPassed;
    }

    public boolean isPassedHalfway() {
        return passedHalfway;
    }

    public void setPassedHalfway(boolean passedHalfway) {
        this.passedHalfway = passedHalfway;
    }
}
