package neonracer.gui.util;

import java.util.ArrayList;
import java.util.List;

public class PropertyList<T> {

    private Class<T> clazz;
    private List<T> list;

    public PropertyList(Class<T> clazz) {
        this.clazz = clazz;
        this.list = new ArrayList<>();
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public List<T> getList() {
        return list;
    }

    public T get(int idx) {
        return list.get(idx);
    }

}
