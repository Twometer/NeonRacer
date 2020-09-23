package neonracer.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.model.track.Material;
import neonracer.model.track.Track;
import neonracer.util.Log;

import java.io.IOException;

public class AssetProvider {

    private static final String PATH_MATERIALS = "materials.yml";
    private static final String PATH_TRACKS = "tracks.yml";
    private static final String PATH_CARS = "cars.yml";

    private Material[] materials;

    private Track[] tracks;

    private Car[] cars;

    public void load(GameContext context) throws IOException {
        Log.i("Loading resources...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        materials = read(mapper, PATH_MATERIALS, Material[].class);
        tracks = read(mapper, PATH_TRACKS, Track[].class);
        cars = read(mapper, PATH_CARS, Car[].class);

        Log.i("Initializing resources...");
        initialize(context, materials);
        initialize(context, tracks);
        initialize(context, cars);

        Log.i("Resource loading completed");
    }

    private <T> T read(ObjectMapper mapper, String file, Class<T> clazz) throws IOException {
        String contents = ResourceLoader.loadString(String.format("data/%s", file));
        return mapper.readValue(contents, clazz);
    }

    private void initialize(GameContext context, IAsset[] data) {
        for (IAsset dataObj : data)
            dataObj.initialize(context);
    }

    public Material getMaterial(String materialId) {
        for (Material material : materials)
            if (material.getId().equals(materialId))
                return material;
        throw new IllegalArgumentException(String.format("Unknown material %s", materialId));
    }

    public Car getCar(String carId) {
        for (Car car : cars)
            if (car.getId().equals(carId))
                return car;
        throw new IllegalArgumentException(String.format("Unknown car %s", carId));
    }

    public Track getTrack(String trackId) {
        for (Track track : tracks)
            if (track.getId().equals(trackId))
                return track;
        throw new IllegalArgumentException(String.format("Unknown track %s", trackId));
    }

    public Material[] getMaterials() {
        return materials;
    }

    public Track[] getTracks() {
        return tracks;
    }

    public Car[] getCars() {
        return cars;
    }
}
