package Models;

import javax.swing.table.DefaultTableModel;

public class TableModel {
    public static DefaultTableModel getTableModel(){

        DefaultTableModel model = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };
        return model;
    }

}
