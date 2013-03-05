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
    
    public static String getRemoteRepository(String projectName) {
        return "https://github.com/" + projectName + ".wiki.git";
    }
    
    private JFrame frmUploadAFile;
    private JPasswordField passwordField;
    private JTextField usernamefield;
    private JTextField selectedFileField;
    private final Action chooseFileAction = new ChooseFileAction();
    private JButton btnUpload;

    private File file = null;
    private final Action uploadAction = new UploadAction();
    private JTextArea textField;
    private JLabel lblProject;
    private JTextField projectField;

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
        frmUploadAFile.setBounds(100, 100, 532, 307);
        frmUploadAFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frmUploadAFile.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("80px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("119px:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("119px"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("50dlu"),
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("19px"),
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("max(27dlu;default)"),}));
        
        lblProject = new JLabel("Project");
        panel.add(lblProject, "2, 2, right, default");
                                        
                                        projectField = new JTextField();
                                        projectField.setText("NLeSC/eWaterCycle");
                                        projectField.setColumns(10);
                                        panel.add(projectField, "4, 2, 5, 1, fill, default");
                                        
                                                JLabel lblUsername = new JLabel("Username");
                                                panel.add(lblUsername, "2, 4, right, center");
                                
                                        usernamefield = new JTextField();
                                        usernamefield.setColumns(10);
                                        panel.add(usernamefield, "4, 4, 5, 1, fill, top");
                        
                                JLabel lblPassword = new JLabel("Password");
                                panel.add(lblPassword, "2, 6, right, center");
                        
                                passwordField = new JPasswordField();
                                panel.add(passwordField, "4, 6, 5, 1, fill, top");
        
                JLabel lblFile = new JLabel("File");
                panel.add(lblFile, "2, 8, right, center");
        
                selectedFileField = new JTextField();
                selectedFileField.setEditable(false);
                selectedFileField.setColumns(10);
                panel.add(selectedFileField, "4, 8, 3, 1, fill, top");
        
                JButton btnChooseFile = new JButton("Choose File");
                btnChooseFile.setAction(chooseFileAction);
                panel.add(btnChooseFile, "8, 8");

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
        
        textField = new JTextArea("Attchments will be available at the wiki at:\nhttps://github.com/USER/PROJECT_NAME/wiki/Attachments\ne.g.  https://github.com/NLeSC/some-project/wiki/Attachments\nand can be linked using [[attachments/FILENAME]]");
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
            
            new UploadDialog(projectField.getText(), usernamefield.getText(), passwordField.getPassword(), file);
          
        }
    }

    
}
