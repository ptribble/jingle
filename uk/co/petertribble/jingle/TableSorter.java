/**
 * This is the TableSorter.java file from the Swing Tutorial.
 *
 * The following changes from the original were made by Peter Tribble:
 *  The package statement has been added
 *  The sorting cycle order has been reversed
 *  The icon is a solid triangle
 * Tom Erickson cleaned it up and generified it, and fine-tuned the sorting
 * order.
 */

package uk.co.petertribble.jingle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * TableSorter is a decorator for TableModels; adding sorting
 * functionality to a supplied TableModel. TableSorter does
 * not store or copy the data in its TableModel; instead it maintains
 * a map from the row indexes of the view to the row indexes of the
 * model. As requests are made of the sorter (like getValueAt(row, col))
 * they are passed to the underlying model after the row numbers
 * have been translated via the internal mapping array. This way,
 * the TableSorter appears to hold another copy of the table
 * with the rows in a different order.
 * <p>
 * TableSorter registers itself as a listener to the underlying model,
 * just as the JTable itself would. Events received from the model
 * are examined, sometimes manipulated (typically widened), and then
 * passed on to the TableSorter's listeners (typically the JTable).
 * If a change to the model has invalidated the order of TableSorter's
 * rows, a note of this is made and the sorter will resort the
 * rows the next time a value is requested.
 * <p>
 * When the tableHeader property is set, either by using the
 * setTableHeader() method or the two argument constructor, the
 * table header may be used as a complete UI for TableSorter.
 * The default renderer of the tableHeader is decorated with a renderer
 * that indicates the sorting status of each column. In addition,
 * a mouse listener is installed with the following behavior:
 * <ul>
 * <li>
 * Mouse-click: Clears the sorting status of all other columns
 * and advances the sorting status of that column through three
 * values: {NOT_SORTED, ASCENDING, DESCENDING} (then back to
 * NOT_SORTED again).
 * <li>
 * SHIFT-mouse-click: Clears the sorting status of all other columns
 * and cycles the sorting status of the column through the same
 * three values, in the opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 * <li>
 * CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except
 * that the changes to the column do not cancel the statuses of columns
 * that are already sorting - giving a way to initiate a compound
 * sort.
 * </ul>
 * <p>
 * This is a long overdue rewrite of a class of the same name that
 * first appeared in the swing table demos in 1997.
 *
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @version 2.0 02/27/04
 */

public final class TableSorter extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private transient TableModel tableModel;

    /**
     * Indicates a sort in descending order.
     */
    public static final int DESCENDING = -1;
    /**
     * Indicates the table is not sorted.
     */
    public static final int NOT_SORTED = 0;
    /**
     * Indicates a sort in ascending order.
     */
    public static final int ASCENDING = 1;

    private static final Directive EMPTY_DIRECTIVE
	= new Directive(-1, NOT_SORTED);

    /**
     * A comparator that uses the natural ordering of its objects for
     * comparison.
     */
    public static final Comparator COMPARABLE_COMPARATOR = new Comparator() {
	@SuppressWarnings("unchecked")
        @Override
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };
    /**
     * A comparator that uses the String version of its objects for
     * comparison.
     */
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {
	@Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    private transient Row[] viewToModel;
    private transient int[] modelToView;

    /**
     * The table header.
     */
    private JTableHeader tableHeader;
    private transient MouseListener mouseListener;
    private transient TableModelListener tableModelListener;
    private transient Map<Class, Comparator> columnComparators
	= new HashMap<>();
    private transient List<Directive> sortingColumns = new ArrayList<>();

    /**
     * Create a new TableSorter.
     */
    public TableSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    /**
     * Create a new TableSorter.
     *
     * @param tableModel the Model to be sorted
     */
    public TableSorter(TableModel tableModel) {
        this();
        setTableModel(tableModel);
    }

    /**
     * Create a new TableSorter.
     *
     * @param tableHeader the JTableHeader applied to this Model
     * @param tableModel the Model to be sorted
     */
    public TableSorter(TableModel tableModel, JTableHeader tableHeader) {
        this();
        setTableHeader(tableHeader);
        setTableModel(tableModel);
    }

    private void clearSortingState() {
        viewToModel = null;
        modelToView = null;
    }

    /**
     * Get the TableModel associated with this TableSorter.
     *
     * @return the TableModel associated with this TableSorter
     */
    public TableModel getTableModel() {
        return tableModel;
    }

    /**
     * Set the TableModel associated with this TableSorter.
     *
     * @param tableModel the TableModel to be associated with this TableSorter
     */
    public void setTableModel(TableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(tableModelListener);
        }

        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(tableModelListener);
        }

        clearSortingState();
        fireTableStructureChanged();
    }

    /**
     * Get the JTableHeader associated with this TableSorter.
     *
     * @return the JTableHeader associated with this TableSorter
     */
    public JTableHeader getTableHeader() {
        return tableHeader;
    }

    /**
     * Set the JTableHeader associated with this TableSorter.
     *
     * @param tableHeader the JTableHeader to be associated with this
     * TableSorter
     */
    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(mouseListener);
            TableCellRenderer defaultRenderer =
		this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer((
		    (SortableHeaderRenderer)
			defaultRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = tableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(mouseListener);
            this.tableHeader.setDefaultRenderer(
                    new SortableHeaderRenderer(
			this.tableHeader.getDefaultRenderer()));
        }
    }

    /**
     * Get whether the table is currently being sorted.
     *
     * @return true if the table is being sorted, specifically if the number
     * columns being sorted is non-zero
     */
    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }

    private Directive getDirective(int column) {
        for (Directive d : sortingColumns) {
            if (d.column == column) {
                return d;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    /**
     * Get the current sorting status of a given column.
     *
     * @param column the column to return the sorting status of
     *
     * @return the sorting status of the requested column
     */
    public int getSortingStatus(int column) {
        return getDirective(column).direction;
    }

    private void sortingStatusChanged() {
        clearSortingState();
        fireTableDataChanged();
        if (tableHeader != null) {
            tableHeader.repaint();
        }
    }

    /**
     * Set the current sorting status of a given column.
     *
     * @param column the column whose sorting status should be set
     * @param status the new value of the given column's sorting status
     */
    public void setSortingStatus(int column, int status) {
        Directive d = getDirective(column);
        if (d != EMPTY_DIRECTIVE) {
	    if (status == NOT_SORTED) {
		sortingColumns.remove(d);
	    } else {
		// toggle sort order in place
		d.direction = status;
	    }
        } else if (status != NOT_SORTED) {
            sortingColumns.add(new Directive(column, status));
        }
        sortingStatusChanged();
    }

    private Icon getHeaderRendererIcon(int column, int size) {
        Directive d = getDirective(column);
        if (d == EMPTY_DIRECTIVE) {
            return null;
        }
        return new Arrow(d.direction == ASCENDING, size,
		sortingColumns.indexOf(d));
    }

    private void cancelSorting() {
        sortingColumns.clear();
        sortingStatusChanged();
    }

    /**
     * Set the Comparator for a given class.
     *
     * @param type the Class the comparator should apply to
     * @param comparator the Comparator to apply
     */
    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        } else {
            columnComparators.put(type, comparator);
        }
    }

    private Comparator getComparator(int column) {
        Class columnType = tableModel.getColumnClass(column);
        Comparator comparator = columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMPARATOR;
        }
        return LEXICAL_COMPARATOR;
    }

    private Row[] getViewToModel() {
        if (viewToModel == null) {
            int tableModelRowCount = tableModel.getRowCount();
            viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; row++) {
                viewToModel[row] = new Row(row);
            }

            if (isSorting()) {
                Arrays.sort(viewToModel);
            }
        }
        return viewToModel;
    }

    /**
     * Map the index in the view to the model.
     *
     * @param viewIndex the index in the view
     *
     * @return the index in the underlying model
     */
    public int modelIndex(int viewIndex) {
        return getViewToModel()[viewIndex].modelIndex;
    }

    private int[] getModelToView() {
        if (modelToView == null) {
            int n = getViewToModel().length;
            modelToView = new int[n];
            for (int i = 0; i < n; i++) {
                modelToView[modelIndex(i)] = i;
            }
        }
        return modelToView;
    }

    // TableModel interface methods

    @Override
    public int getRowCount() {
        return (tableModel == null) ? 0 : tableModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return (tableModel == null) ? 0 : tableModel.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return tableModel.getColumnName(column);
    }

    @Override
    public Class getColumnClass(int column) {
        return tableModel.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return tableModel.isCellEditable(modelIndex(row), column);
    }

    @Override
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(modelIndex(row), column);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        tableModel.setValueAt(aValue, modelIndex(row), column);
    }

    // Helper classes

    private class Row implements Comparable<Row> {
        private int modelIndex;

        Row(int index) {
            this.modelIndex = index;
        }

	@SuppressWarnings("unchecked")
	@Override
        public int compareTo(Row o) {
            int row1 = modelIndex;
            int row2 = o.modelIndex;

            for (Directive directive : sortingColumns) {
                int column = directive.column;
                Object o1 = tableModel.getValueAt(row1, column);
                Object o2 = tableModel.getValueAt(row2, column);

                int comparison = 0;
                // Define null less than everything, except null.
                if (o1 == null && o2 == null) {
                    comparison = 0;
                } else if (o1 == null) {
                    comparison = -1;
                } else if (o2 == null) {
                    comparison = 1;
                } else {
                    comparison = getComparator(column).compare(o1, o2);
                }
                if (comparison != 0) {
                    return directive.direction
			== DESCENDING ? -comparison : comparison;
                }
            }
            return 0;
        }
    }

    private final class TableModelHandler implements TableModelListener {
	@Override
        public void tableChanged(TableModelEvent e) {
            // If we're not sorting by anything, just pass the event along.
            if (!isSorting()) {
                clearSortingState();
                fireTableChanged(e);
                return;
            }

            // If the table structure has changed, cancel the sorting; the
            // sorting columns may have been either moved or deleted from
            // the model.
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                cancelSorting();
                fireTableChanged(e);
                return;
            }

            // We can map a cell event through to the view without widening
            // when the following conditions apply:
            //
            // a) all the changes are on one row
	    // (e.getFirstRow() == e.getLastRow()) and,
            // b) all the changes are in one column
	    // (column != TableModelEvent.ALL_COLUMNS) and,
            // c) we are not sorting on that column
	    // (getSortingStatus(column) == NOT_SORTED) and,
            // d) a reverse lookup will not trigger a sort (modelToView != null)
            //
            // Note: INSERT and DELETE events fail this test as they have
	    // column == ALL_COLUMNS.
            //
            // The last check, for (modelToView != null) is to see if
	    // modelToView is already allocated. If we don't do this check;
	    // sorting can become
            // a performance bottleneck for applications where cells
            // change rapidly in different parts of the table. If cells
            // change alternately in the sorting column and then outside of
            // it this class can end up re-sorting on alternate cell updates -
            // which can be a performance problem for large tables. The last
            // clause avoids this problem.
            int column = e.getColumn();
            if (e.getFirstRow() == e.getLastRow()
                    && column != TableModelEvent.ALL_COLUMNS
                    && getSortingStatus(column) == NOT_SORTED
                    && modelToView != null) {
                int viewIndex = getModelToView()[e.getFirstRow()];
                fireTableChanged(new TableModelEvent(TableSorter.this,
                                                     viewIndex, viewIndex,
                                                     column, e.getType()));
                return;
            }

            // Something happened to the data that may invalidate the row order.
            clearSortingState();
            fireTableDataChanged();
        }
    }

    private final class MouseHandler extends MouseAdapter {
	@Override
        public void mouseClicked(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
                int status = getSortingStatus(column);
                if (!e.isControlDown()) {
		    // If the table was sorted by multiple columns, and
		    // this columns is not the primary sort, just start
		    // over (don't cycle through sort).
		    if (sortingColumns.size() > 1) {
			Directive d = getDirective(column);
			if (sortingColumns.indexOf(d) != 0) {
			    status = NOT_SORTED;
			}
		    }
                    cancelSorting();
                }

		JTable table = h.getTable();
		TableModel tableModel = table.getModel();
		Class columnType = tableModel.getColumnClass(column);
		if (columnType != null
			&& Number.class.isAssignableFrom(columnType)) {
		    // Time should also be descending, but a raw Date is
		    // not usually loaded into a table as-is. Whatever
		    // class formats time values for display should
		    // implement Comparable, and this method should
		    // initially sort it descending.
		    switch (status) {
			case NOT_SORTED:
			    status = DESCENDING;
			    break;
			case DESCENDING:
			    status = ASCENDING;
			    break;
			case ASCENDING:
			    status = NOT_SORTED;
			    break;
			default:
			    throw new IllegalArgumentException(
				    "unexpected sort: " + status);
		    }
		} else {
		    switch (status) {
			case NOT_SORTED:
			    status = ASCENDING;
			    break;
			case DESCENDING:
			    status = NOT_SORTED;
			    break;
			case ASCENDING:
			    status = DESCENDING;
			    break;
			default:
			    throw new IllegalArgumentException(
				    "unexpected sort: " + status);
		    }
		}

                setSortingStatus(column, status);
            }
        }
    }

    private static class Arrow implements Icon {
        private boolean ascending;
        private int size;
        private int priority;

        Arrow(boolean ascending, int size, int priority) {
            this.ascending = ascending;
            this.size = size;
            this.priority = priority;
        }

	@Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
	    // PTRIBBLE - solid black triangle rather than shaded
            Color color = Color.BLACK;

	    Polygon pol = new Polygon();
            // In a compound sort, make each successive triangle 20%
            // smaller than the previous one.
            int dx = (int) (size / 2 * Math.pow(0.8, priority));
	    dx = ascending ? dx + 1 : dx;
            int dy = ascending ? dx : -dx;
            // Align icon (roughly) with font baseline.
            y = y + 5 * size / 6 + (ascending ? -dy : 0);

	    pol.addPoint(x, y);
	    pol.addPoint(x + (dx / 2), y + dy);
	    pol.addPoint(x - (dx / 2), y + dy);

            g.setColor(color);
	    g.fillPolygon(pol);
        }

	@Override
        public int getIconWidth() {
            return size;
        }

	@Override
        public int getIconHeight() {
            return size;
        }
    }

    private class SortableHeaderRenderer implements TableCellRenderer {
        private TableCellRenderer tableCellRenderer;

        SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

	@Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn,
						l.getFont().getSize()));
            }
            return c;
        }
    }

    private static class Directive {
        private int column;
        private int direction;

        Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
}
