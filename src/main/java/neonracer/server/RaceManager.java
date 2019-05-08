package neonracer.server;

import neonracer.network.proto.Race;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class RaceManager {

    private static final int LOBBY_TIMEOUT = 10000;
    private static final int START_TIMEOUT = 10000;
    private static final int LAP_COUNT = 4;

    private final GameServer parent;
    private final List<RemoteClient> participants;
    private final List<Entity> entities;

    private boolean open;

    RaceManager(GameServer parent) {
        this.parent = parent;
        participants = Collections.synchronizedList(new LinkedList<>());
        entities = Collections.synchronizedList(new LinkedList<>());
        // TODO call prepare and start with timeouts
        // TODO kick all clients after a specific race timeout
    }

    private void notifyPrepare() {
        Race.Prepare message = Race.Prepare.newBuilder().setRemainingMilliseconds(LOBBY_TIMEOUT).build();
        parent.sendExcept(message, null);

    }

    private void notifyStart() {
        Race.Start message = Race.Start.newBuilder().setElapsedMilliseconds(-START_TIMEOUT).setLapCount(LAP_COUNT).build();
        parent.sendExcept(message, null);
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
