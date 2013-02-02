package nl.esciencecenter.wikiexplorer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JFileChooser;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JTextArea;

public class GUI {
    
    public static final String REMOTE_REPOSITORY = "https://github.com/NLeSC/eWaterCycle.wiki.git";
    public static final String ATTACHMENT_OVERVIEW_URL = "https://github.com/NLeSC/eWaterCycle/wiki/Attachments";

    private JFrame frmUploadAFile;
    private JPasswordField passwordField;
    private JTextField usernamefield;
    private JTextField selectedFileField;
    private final Action chooseFileAction = new ChooseFileAction();
    private JButton btnUpload;

    private File file = null;
    private final Action uploadAction = new UploadAction();
    private JTextArea textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI window = new GUI();
                    window.frmUploadAFile.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmUploadAFile = new JFrame();
        frmUploadAFile.setResizable(false);
        frmUploadAFile.setTitle("Upload a file to the Wiki");
        frmUploadAFile.setBounds(100, 100, 489, 285);
        frmUploadAFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frmUploadAFile.getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblUsername = new JLabel("Username");

        usernamefield = new JTextField();
        usernamefield.setColumns(10);

        JLabel lblPassword = new JLabel("Password");

        passwordField = new JPasswordField();

        JLabel lblFile = new JLabel("File");

        selectedFileField = new JTextField();
        selectedFileField.setEditable(false);
        selectedFileField.setColumns(10);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("72px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("119px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("119px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("max(54dlu;default)"),},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("max(27dlu;default)"),}));
        panel.add(lblUsername, "2, 2, left, center");
        panel.add(usernamefield, "4, 2, 5, 1, fill, top");
        panel.add(lblPassword, "2, 4, right, center");
        panel.add(passwordField, "4, 4, 5, 1, fill, top");
        panel.add(lblFile, "2, 6, right, center");
        panel.add(selectedFileField, "4, 6, 3, 1, fill, top");

        JButton btnChooseFile = new JButton("Choose File");
        btnChooseFile.setAction(chooseFileAction);
        panel.add(btnChooseFile, "8, 6");

        btnUpload = new JButton("Upload");
        btnUpload.setAction(uploadAction);
        panel.add(btnUpload, "4, 10");

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(btnCancel, "6, 10");
        
        textField = new JTextArea("Attchments will be available at the wiki at:\n" + ATTACHMENT_OVERVIEW_URL 
                + "\nand can be linked using [[attachments/FILENAME]]");
        textField.setRows(2);
        textField.setEditable(false);
        panel.add(textField, "4, 14, 5, 1, fill, fill");
        textField.setColumns(10);
    }

    private class ChooseFileAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public ChooseFileAction() {
            putValue(NAME, "Choose File");
            putValue(SHORT_DESCRIPTION, "Choose a file to upload");
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showDialog(null, "Upload");

            if (result == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                selectedFileField.setText(file.getName());
                System.out.println(file);
            }
        }
    }

    private class UploadAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public UploadAction() {
            putValue(NAME, "Upload");
            putValue(SHORT_DESCRIPTION, "Upload the selected file to the wiki");
        }

        public void actionPerformed(ActionEvent e) {
            
            new UploadDialog(usernamefield.getText(), passwordField.getPassword(), file);
          
        }
    }

    
}
