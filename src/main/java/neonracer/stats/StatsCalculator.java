package neonracer.stats;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityCar;
import neonracer.model.track.Track;
import neonracer.render.engine.collider.ICollider;
import neonracer.render.engine.collider.TrackColliderResult;

import java.util.*;

public class StatsCalculator {

    private GameContext gameContext;

    public StatsCalculator(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void update() {
        Scoreboard scoreboard = new Scoreboard();
        ICollider<Track, TrackColliderResult> trackCollider = gameContext.getGameState().getCurrentTrack().getCollider();
        for (Entity entity : gameContext.getGameState().getEntities()) {
            if (entity instanceof EntityCar) {
                CarStats stats = ((EntityCar) entity).getCarStats();
                float position = trackCollider.collides(entity.getPosition()).getClosestT();

                if (position < 0.05 && stats.isPassedHalfway()) {
                    stats.setLapsPassed(stats.getLapsPassed() + 1);
                    stats.setPassedHalfway(false);
                }
                if (Math.abs(position - 0.5f) < 0.05 && !stats.isPassedHalfway()) {
                    stats.setPassedHalfway(true);
                }

                scoreboard.put(stats, position);
            }
        }
        scoreboard.apply();
    }

    public static class ScoreboardEntry {

        private float position;

        private CarStats carStats;

        ScoreboardEntry(float position, CarStats carStats) {
            this.position = position;
            this.carStats = carStats;
        }

        public float getPosition() {
            return position;
        }

        public CarStats getCarStats() {
            return carStats;
        }
    }

    private class Scoreboard {

        Map<Integer, List<ScoreboardEntry>> map = new HashMap<>();

        public void put(CarStats carStats, float position) {
            if (map.containsKey(carStats.getLapsPassed()))
                map.get(carStats.getLapsPassed()).add(new ScoreboardEntry(position, carStats));
            else {
                List<ScoreboardEntry> l = new ArrayList<>();
                l.add(new ScoreboardEntry(position, carStats));
                map.put(carStats.getLapsPassed(), l);
            }
        }

        void apply() {
            // Sort cars in each lap by their position on the track
            for (List<ScoreboardEntry> list : map.values())
                list.sort((o1, o2) -> Float.compare(o1.getPosition(), o2.getPosition()));

            // Then, go from the last lap to the first
            List<Integer> keys = new ArrayList<>(map.keySet());
            keys.sort(Comparator.comparingInt(o -> -o));

            // Finally, apply those places to the car stats
            int place = 1;
            for (Integer key : keys) {
                List<ScoreboardEntry> entries = map.get(key);
                for (ScoreboardEntry entry : entries) {
                    entry.carStats.setPlace(place);
                    place++;
                }
            }

        }

    }

}
