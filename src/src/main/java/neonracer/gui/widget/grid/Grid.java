package neonracer.gui.widget.grid;

import neonracer.gui.util.PropertyList;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import org.joml.Vector2f;

public class Grid extends Container {

    private static final String NAMESPACE = "Grid";

    private PropertyList<ColumnDefinition> columnDefinitions = new PropertyList<>(ColumnDefinition.class);

    private PropertyList<RowDefinition> rowDefinitions = new PropertyList<>(RowDefinition.class);

    @Override
    public void performLayout() {
        int[] calculatedColumns = calculateColumns();
        int[] calculatedRows = calculateRows();
        for (Widget widget : getChildren()) {
            int row = widget.getForeignParameters().getInt(NAMESPACE, "Row");
            int col = widget.getForeignParameters().getInt(NAMESPACE, "Column");
            Vector2f cellPos = getCellPos(calculatedColumns, calculatedRows, col, row);
            widget.setX((int) cellPos.x());
            widget.setY((int) cellPos.y());
            widget.setWidth(calculatedColumns[col]);
            widget.setHeight(calculatedRows[row]);

            if (widget instanceof Container)
                ((Container) widget).performLayout();
        }
    }

    // TODO: 1. Merge calculateColumns() and calculateRows()
    //       2. Introduce new foreign parameter "ScaleMode" or something like that to make it possible for widgets to not
    //          fill an entire cell but be centered or things like that.

    /**
     * Resolves the RowDefinitions which contain "Auto" and "*" to
     * actual pixel values
     */
    private int[] calculateRows() {
        int[] calculatedRows = new int[rowDefinitions.getList().size()];
        int remainingHeight = getHeight();
        int remainingRows = calculatedRows.length;
        for (int i = 0; i < rowDefinitions.getList().size(); i++) {
            RowDefinition definition = rowDefinitions.get(i);
            if (definition == RowDefinition.AUTO) {
                int height = 0;
                for (Widget widget : getChildren()) {
                    int row = widget.getForeignParameters().getInt(NAMESPACE, "Row");
                    if (row == i) {
                        Size size = widget.measure();
                        if (size.getHeight() > height) height = size.getHeight();
                    }
                }
                calculatedRows[i] = height;
                remainingHeight -= height;
                remainingRows--;
            } else if (definition != RowDefinition.FILL_REMAINING) {
                calculatedRows[i] = rowDefinitions.get(i).getValue();
                remainingHeight -= calculatedRows[i];
                remainingRows--;
            }
        }
        // Divide the remaining space between all rows marked FILL_REMAINING
        int fillRemainingHeight = (int) ((double) remainingHeight / remainingRows);
        for (int i = 0; i < rowDefinitions.getList().size(); i++)
            if (rowDefinitions.get(i) == RowDefinition.FILL_REMAINING)
                calculatedRows[i] = fillRemainingHeight;
        return calculatedRows;
    }

    /**
     * Resolves the ColumnDefinitions which contain "Auto" and "*" to
     * actual pixel values
     */
    private int[] calculateColumns() {
        int[] calculatedColumns = new int[columnDefinitions.getList().size()];
        int remainingWidth = getWidth();
        int remainingCols = calculatedColumns.length;
        for (int i = 0; i < columnDefinitions.getList().size(); i++) {
            ColumnDefinition definition = columnDefinitions.get(i);
            if (definition == ColumnDefinition.AUTO) {
                int width = 0;
                for (Widget widget : getChildren()) {
                    int col = widget.getForeignParameters().getInt(NAMESPACE, "Column");
                    if (col == i) {
                        Size size = widget.measure();
                        if (size.getHeight() > width) width = size.getHeight();
                    }
                }
                calculatedColumns[i] = width;
                remainingWidth -= width;
                remainingCols--;
            } else if (definition != ColumnDefinition.FILL_REMAINING) {
                calculatedColumns[i] = columnDefinitions.get(i).getValue();
                remainingWidth -= calculatedColumns[i];
                remainingCols--;
            }
        }
        // Divide the remaining space between all columns marked FILL_REMAINING
        int fillRemainingHeight = (int) ((double) remainingWidth / remainingCols);
        for (int i = 0; i < columnDefinitions.getList().size(); i++)
            if (columnDefinitions.get(i) == ColumnDefinition.FILL_REMAINING)
                calculatedColumns[i] = fillRemainingHeight;
        return calculatedColumns;
    }

    private Vector2f getCellPos(int[] columns, int[] rows, int column, int row) {
        int x = 0;
        for (int i = 0; i < column; i++) x += columns[i];

        int y = 0;
        for (int i = 0; i < row; i++) y += rows[i];

        return new Vector2f(x, y);
    }

    public PropertyList<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public PropertyList<RowDefinition> getRowDefinitions() {
        return rowDefinitions;
    }

}
