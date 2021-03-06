package neonracer.gui.widget.grid;

import neonracer.gui.util.PropertyList;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import org.joml.Vector2i;

class GridLayoutEngine {

    private static final String NAMESPACE = "Grid";

    private Grid grid;

    private int[] calculatedColumns;

    private int[] calculatedRows;

    /**
     * Used by the grid to instantiate the layout engine
     *
     * @param grid The owning grid
     */
    GridLayoutEngine(Grid grid) {
        this.grid = grid;
    }

    /**
     * Calculates all rows and columns to actual pixel values
     */
    void calculate() {
        this.calculatedColumns = calculateColumns();
        this.calculatedRows = calculateRows();
    }

    /**
     * Applies the grid layout to one specific child
     *
     * @param widget The child to be layouted
     */
    void layoutChild(Widget widget) {
        int row = widget.getForeignParameters().getIntOrDefault(NAMESPACE, "Row", 0);
        int col = widget.getForeignParameters().getIntOrDefault(NAMESPACE, "Column", 0);
        int rowSpan = widget.getForeignParameters().getIntOrDefault(NAMESPACE, "RowSpan", 1);
        int colSpan = widget.getForeignParameters().getIntOrDefault(NAMESPACE, "ColSpan", 1);

        Vector2i cellPos = calcCellPos(col, row).add(widget.getMargin().getLeft(), widget.getMargin().getTop());
        Size cellSize = calcCellSize(row, col, rowSpan, colSpan);
        Size targetSize = new Size(cellSize.getWidth() - widget.getMargin().getWidth(), cellSize.getHeight() - widget.getMargin().getHeight());
        Size widgetSize = widget.measure();

        switch (widget.getHorizontalAlignment()) {
            case Fill:
                widget.setX(cellPos.x);
                widget.setWidth(targetSize.getWidth());
                break;
            case Start:
                widget.setX(cellPos.x);
                widget.setWidth((widgetSize.getWidth() / widgetSize.getHeight()) * targetSize.getHeight());
                break;
            case Center:
                if (widget.getWidth() == 0)
                    widget.setWidth((widgetSize.getWidth() / widgetSize.getHeight()) * targetSize.getHeight());
                widget.setX((int) (cellPos.x + targetSize.getWidth() / 2f - widget.getWidth() / 2f));
                break;
            case End:
                widget.setWidth((widgetSize.getWidth() / widgetSize.getHeight()) * targetSize.getHeight());
                widget.setX(cellPos.x - widget.getWidth());
                break;
        }


        switch (widget.getVerticalAlignment()) {
            case Fill:
                widget.setY(cellPos.y);
                widget.setHeight(targetSize.getHeight());
                break;
            case Start: // TODO
                widget.setY(cellPos.y);
                break;
            case Center:
                widget.setY((int) (cellPos.y + cellSize.getHeight() / 2f - widget.getHeight() / 2f));
                break;
            case End:
        }
    }

    /**
     * Resolves the RowDefinitions which contain "Auto" and "*" to
     * actual pixel values
     */
    private int[] calculateRows() {
        PropertyList<RowDefinition> rowDefinitions = grid.getRowDefinitions();
        int[] calculatedRows = new int[rowDefinitions.getList().size()];

        // Store these values for calculating rows marked "FILL_REMAINING"
        int remainingHeight = grid.getHeight();
        int remainingRows = calculatedRows.length;

        for (int i = 0; i < rowDefinitions.getList().size(); i++) {
            RowDefinition definition = rowDefinitions.get(i);

            if (definition == RowDefinition.AUTO) {
                // Find highest child in this row
                int height = 0;
                for (Widget widget : grid.getChildren()) {
                    int row = widget.getForeignParameters().getInt(NAMESPACE, "Row");
                    if (row == i) {
                        Size size = widget.measure();
                        if (size.getHeight() + widget.getMargin().getHeight() > height)
                            height = size.getHeight() + widget.getMargin().getHeight();
                    }
                }

                // Make the row as high as the highest child
                calculatedRows[i] = height;
                remainingHeight -= height;
                remainingRows--;
            } else if (definition != RowDefinition.FILL_REMAINING) {
                // Absolute pixel values are just copied
                RowDefinition def = rowDefinitions.get(i);
                calculatedRows[i] = def.getMode() == BaseDefinition.Mode.Absolute ? def.getValue() : (int) (def.getValue() / 100.0f * grid.getHeight());
                remainingHeight -= calculatedRows[i];
                remainingRows--;
            }
        }

        // Split up the remaining space between all rows marked FILL_REMAINING
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
        PropertyList<ColumnDefinition> columnDefinitions = grid.getColumnDefinitions();
        int[] calculatedColumns = new int[columnDefinitions.getList().size()];

        // Store these values for calculating "FILL_REMAINING" columns
        int remainingWidth = grid.getWidth();
        int remainingCols = calculatedColumns.length;

        for (int i = 0; i < columnDefinitions.getList().size(); i++) {
            ColumnDefinition definition = columnDefinitions.get(i);

            if (definition == ColumnDefinition.AUTO) {
                // Find widest child in this column
                int width = 0;
                for (Widget widget : grid.getChildren()) {
                    int col = widget.getForeignParameters().getInt(NAMESPACE, "Column");
                    if (col == i) {
                        Size size = widget.measure();
                        if (size.getWidth() + widget.getMargin().getWidth() > width)
                            width = size.getWidth() + widget.getMargin().getWidth();
                    }
                }

                // Make the column as wide as the widest child
                calculatedColumns[i] = width;
                remainingWidth -= width;
                remainingCols--;
            } else if (definition != ColumnDefinition.FILL_REMAINING) {
                // Absolute pixel values are just copied
                ColumnDefinition def = columnDefinitions.get(i);
                calculatedColumns[i] = def.getMode() == BaseDefinition.Mode.Absolute ? def.getValue() : (int) (def.getValue() / 100.0f * grid.getWidth());
                remainingWidth -= calculatedColumns[i];
                remainingCols--;
            }
        }

        // Split up the remaining space between all columns marked FILL_REMAINING
        int fillRemainingHeight = (int) ((double) remainingWidth / remainingCols);
        for (int i = 0; i < columnDefinitions.getList().size(); i++)
            if (columnDefinitions.get(i) == ColumnDefinition.FILL_REMAINING)
                calculatedColumns[i] = fillRemainingHeight;
        return calculatedColumns;
    }

    /**
     * Calculates the size of an (extended) cell one widget sits in
     *
     * @param row     The row of the widget
     * @param col     The col of the widget
     * @param rowSpan How many rows the widget extends down
     * @param colSpan How many cols the widget extends to the right
     * @return The size the cell
     */
    private Size calcCellSize(int row, int col, int rowSpan, int colSpan) {
        int width = 0;
        for (int i = col; i < col + colSpan; i++)
            width += calculatedColumns[i];

        int height = 0;
        for (int i = row; i < row + rowSpan; i++)
            height += calculatedRows[i];

        return new Size(width, height);
    }

    /**
     * Calculates the position of the top left corner of the given cell
     *
     * @param column The column of the cell
     * @param row    The row of the celll
     * @return The position
     */
    private Vector2i calcCellPos(int column, int row) {
        int x = 0;
        for (int i = 0; i < column; i++) x += calculatedColumns[i];

        int y = 0;
        for (int i = 0; i < row; i++) y += calculatedRows[i];

        return new Vector2i(grid.getX() + x, grid.getY() + y);
    }

}
