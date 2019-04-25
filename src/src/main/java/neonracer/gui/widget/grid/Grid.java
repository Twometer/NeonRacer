package neonracer.gui.widget.grid;

import neonracer.gui.GuiContext;
import neonracer.gui.util.PropertyList;
import neonracer.gui.widget.base.Container;

public class Grid extends Container {

    private PropertyList<ColumnDefinition> columnDefinitions = new PropertyList<>(ColumnDefinition.class);

    private PropertyList<RowDefinition> rowDefinitions = new PropertyList<>(RowDefinition.class);

    @Override
    public void draw(GuiContext guiContext) {

    }

    @Override
    public void performLayout() {

    }

    /**
     * Resolves the RowDefinitions which contain "Auto" and "*" to
     * actual pixel values
     */
    private RowDefinition[] calculateRows() {
        RowDefinition[] calculatedRows = new RowDefinition[rowDefinitions.getList().size()];
        // TODO
        return calculatedRows;
    }

    /**
     * Resolves the ColumnDefinitions which contain "Auto" and "*" to
     * actual pixel values
     */
    private ColumnDefinition[] calculateColumns() {
        ColumnDefinition[] calculatedColumns = new ColumnDefinition[columnDefinitions.getList().size()];
        // TODO
        return calculatedColumns;
    }

    public PropertyList<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public PropertyList<RowDefinition> getRowDefinitions() {
        return rowDefinitions;
    }

}
