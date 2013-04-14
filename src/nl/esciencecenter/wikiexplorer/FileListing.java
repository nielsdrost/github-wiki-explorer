package nl.esciencecenter.wikiexplorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class FileListing extends JFrame {

    private JPanel contentPane;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileListing frame = new FileListing();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /** 
     * Displays Strings as JLabels 
     */  
    private class LabelRenderer extends DefaultTableCellRenderer {  
  
        /** 
         * @see java.io.Serializable 
         */  
        private static final long serialVersionUID = 2l;  
  
        /* 
         * (non-Javadoc) 
         *  
         * @see 
         * javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent 
         * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int) 
         */  
        public Component getTableCellRendererComponent(JTable table,  
                Object value, boolean isSelected, boolean hasFocus, int row,  
                int column) {  
            JLabel label = new JLabel("the text");  
            try {  
                // BTW: Ensure you are allowed to use the icons -  
                // This page seems to allow their use;  
                // http://www.iconarchive.com/show/hat-icons-by-rob-sanders/Hat-cowboy-black-icon.html  
                label.setIcon(new ImageIcon(new URL(  
                        "http://www.iconarchive.com/icons/rob-sanders/hat/32/Hat-cowboy-black-icon.png")));  
            } catch (MalformedURLException mfe) {  
            }  
            return label;  
        }  
    }  

    /**
     * Create the frame.
     */
    public FileListing() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //IGNORE
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 636, 595);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.CENTER);
        
        JTree tree = new JTree();
        splitPane.setLeftComponent(tree);
        
        JScrollPane scrollPane = new JScrollPane();
        splitPane.setRightComponent(scrollPane);
        
        JLabel someFile = new JLabel("File");
        
        table = new JTable();
        table.setGridColor(Color.LIGHT_GRAY);

        table.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
            },
            new String[] {
                "New column", "New column", "New column", "New column", "New column", "New column", "New column"
            }
        ) {
            Class[] columnTypes = new Class[] {
                JLabel.class, Long.class, Boolean.class, Object.class, Object.class, Object.class, Object.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        scrollPane.setViewportView(table);
        table.setDefaultRenderer(JLabel.class, new  LabelRenderer());
        table.getModel().setValueAt(someFile,  0,  0);
        table.setRowHeight(30);
    }
    public JTable getTable() {
        return table;
    }
}
