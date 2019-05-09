package neonracer.server;

import neonracer.network.proto.Race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

class RaceManager {

    private static final int LOBBY_TIMEOUT = 10000;
    private static final int START_TIMEOUT = 10000;
    private static final int LAP_COUNT = 4;

    private final GameServer parent;
    private final List<RemoteClient> participants;
    private final List<Entity> entities;

    private boolean open = true;

    RaceManager(GameServer parent) {
        this.parent = parent;
        participants = new CopyOnWriteArrayList<>();
        entities = new CopyOnWriteArrayList<>();
        new Thread(this::notifyPrepare).start();
        // TODO kick all clients after a specific race timeout
    }

    private void notifyPrepare() {
        Race.Prepare message = Race.Prepare.newBuilder().setRemainingMilliseconds(LOBBY_TIMEOUT).build();
        parent.sendExcept(message, null);
        System.out.println("New race in preparation. Lobby will close in " + LOBBY_TIMEOUT + "ms...");
        try {
            Thread.sleep(LOBBY_TIMEOUT);
        } catch (InterruptedException e) {
            return;
        }
        notifyStart();
    }

    private void notifyStart() {
        Race.Start template = Race.Start.newBuilder().setElapsedMilliseconds(-START_TIMEOUT).setLapCount(LAP_COUNT).build();
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            positions.add(i);
        }
        Collections.shuffle(positions, new Random());
        for (int i = 0; i < participants.size(); i++) {
            Race.Start message = Race.Start.newBuilder(template).setPosition(positions.get(i)).build();
            participants.get(i).trySend(message);
        }
        parent.sendExcept(template, null);
        open = false;
        System.out.println("Lobby closed. Race begins in " + START_TIMEOUT + "ms...");
    }

    List<RemoteClient> getParticipants() {
        return participants;
    }

    List<Entity> getEntities() {
        return entities;
    }

    boolean isOpen() {
        return open;
    }
}
