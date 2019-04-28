package neonracer.gui.widget.grid;

import neonracer.gui.util.PropertyList;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;

public class Grid extends Container {

    private GridLayoutEngine layoutEngine;

    private PropertyList<ColumnDefinition> columnDefinitions = new PropertyList<>(ColumnDefinition.class);

    private PropertyList<RowDefinition> rowDefinitions = new PropertyList<>(RowDefinition.class);

    public Grid() {
        this.layoutEngine = new GridLayoutEngine(this);
    }

    @Override
    public void performLayout() {
        layoutEngine.calculate();
        for (Widget widget : getChildren()) {
            layoutEngine.layoutChild(widget);

            if (widget instanceof Container)
                ((Container) widget).performLayout();
        }
    }

    public PropertyList<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public PropertyList<RowDefinition> getRowDefinitions() {
        return rowDefinitions;
    }

}
