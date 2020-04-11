package neonracer.gui.screen;

import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;
import neonracer.gui.events.Event;
import neonracer.gui.events.TickEvent;
import neonracer.gui.widget.ImageButton;
import neonracer.gui.widget.Label;
import neonracer.gui.widget.ProgressBar;
import neonracer.model.entity.EntityCar;
import neonracer.network.proto.Entity;
import neonracer.network.proto.Race;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.RenderContext;
import neonracer.render.engine.Spline2D;
import neonracer.util.Log;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joml.Vector2f;

@LayoutFile("guis/cars.xml")
public class CarSelectorScreen extends Screen {

    private int position;

    private long totalWaitMs = 0;

    private long startMs = 0;

    private static final String[] AVAILABLE_CARS = new String[]{
            "kart",
            "sportscar",
            "rocketcar"
    };
    private String selectedCar = "";

    @BindWidget("lbState")
    private Label lbState;
    @BindWidget("kart")
    private ImageButton kart;

    @BindWidget("sportscar")
    private ImageButton sportscar;

    @BindWidget("rocketcar")
    private ImageButton rocketcar;

    @BindWidget("lbDescription")
    private Label lbDescription;

    @BindWidget("pbState")
    private ProgressBar pbState;

    @EventHandler("kart")
    public void onSelectKart(ClickEvent event) {
        clearSelection();
        kart.setSelected(true);
        selectedCar = "kart";
        updateDesc();
    }

    @EventHandler("sportscar")
    public void onSelectSportscar(ClickEvent event) {
        clearSelection();
        sportscar.setSelected(true);
        selectedCar = "sportscar";
        updateDesc();
    }

    @EventHandler("rocketcar")
    public void onSelectRocketcar(ClickEvent event) {
        clearSelection();
        rocketcar.setSelected(true);
        selectedCar = "rocketcar";
        updateDesc();
    }

    private void clearSelection() {
        kart.setSelected(false);
        sportscar.setSelected(false);
        rocketcar.setSelected(false);
    }

    private void updateDesc() {
        String desc = context.getDataManager().getCar(selectedCar).getDescription();
        if (desc != null)
            lbDescription.setText(desc);
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        EventBus.getDefault().register(this);

        if (context.isDebugMode()) startDebug();
    }

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        if (event instanceof TickEvent) {
            if (startMs == 0) {
                lbState.setText("Warte auf Spieler...");
                pbState.setValue(100);
            } else {
                long remaining = startMs - System.currentTimeMillis();
                if (remaining <= 0) {
                    parent.close(this);
                    EventBus.getDefault().unregister(this);
                    startRace();
                } else {
                    lbState.setText("Noch " + (remaining / 1000) + " Sekunden");
                    pbState.setValue((int) (((float) remaining / totalWaitMs) * 100));
                }
            }
        }
    }

    private void startRace() {
        if (selectedCar.isEmpty()) selectedCar = AVAILABLE_CARS[(int) (Math.random() * AVAILABLE_CARS.length)];

        EntityCar playerEntity = new EntityCar(context.getClient().newEntityId(), 0.0f, 0.0f, 0.0f, context.getDataManager().getCar(selectedCar));

        float t = 0.05f * position;
        Log.i(String.format("Placing car at {pos=%d, t=%s}", position, t));
        placeCar(playerEntity, t);

        playerEntity.setPhysics(CarPhysicsFactory.createDriveable(context, playerEntity));

        context.getGameState().setPlayerEntity(playerEntity);
        context.getGameState().addEntity(playerEntity);

        context.getClient().send(Entity.Create.newBuilder()
                .setType(playerEntity.getCar().getId())
                .setEntityId(playerEntity.getEntityId())
                .setX(playerEntity.getPosition().x)
                .setY(playerEntity.getPosition().y)
                .setRotation(playerEntity.getRotation()).build());
    }

    private void placeCar(EntityCar car, float t) {
        Spline2D spline = context.getGameState().getCurrentTrack().getTrackDef().getSpline2D();

        Vector2f position = spline.interpolate(t);
        car.setPosition(position.add(0, car.getHeight() / 2));

        /*Map<String, String> map = new HashMap<>();
        map.put("color_texture", "textures/kart_2.png");
        float rotation = getTrackRotation(spline, t);
        EntityStatic entityStatic = new EntityStatic("static", position.x, position.y, rotation, map);
        context.getGameState().addEntity(entityStatic);*/

        //car.setRotation(rotation);
    }

    private float getTrackRotation(Spline2D spline, float t) {
        Vector2f normal = spline.getNormal(t);
        return (float) Math.atan2(normal.y, normal.x);
    }

    @Subscribe
    public void onStart(Race.Start start) {
        if (start.getElapsedMilliseconds() < 0) {
            totalWaitMs = -start.getElapsedMilliseconds();
            startMs = System.currentTimeMillis() + totalWaitMs;
            this.position = start.getPosition();
        }
    }

    private void startDebug() {
        totalWaitMs = 5000;
        startMs = System.currentTimeMillis() + totalWaitMs;
        this.position = 1;
    }

}
