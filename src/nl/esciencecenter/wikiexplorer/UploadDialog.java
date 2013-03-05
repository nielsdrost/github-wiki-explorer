package nl.esciencecenter.wikiexplorer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class UploadDialog extends JDialog implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();

    private final JTextArea textArea;
    private final JProgressBar progressBar;

    private final String project;
    private final String username;
    private final char[] password;
    private final File file;

    private final Uploader uploader;

    // /**
    // * Launch the application.
    // */
    // public static void main(String[] args) {
    // try {
    // UploadDialog dialog = new UploadDialog();
    // dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    // dialog.setVisible(true);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public UploadDialog(String project, String username, char[] password, File file) {
        this.project = project;
        this.username = username;
        this.password = password;
        this.file = file;

        setTitle("Uploading file");
        setBounds(100, 100, 531, 359);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("221px:grow"), }, new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, }));
        {
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add(scrollPane, "1, 2, fill, fill");
            {
                textArea = new JTextArea();
                textArea.setRows(10);
                textArea.setEditable(false);
                scrollPane.setViewportView(textArea);
            }
        }
        {
            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            contentPanel.add(progressBar, "1, 4");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }

        setVisible(true);
        uploader = new Uploader();

        uploader.addPropertyChangeListener(this);

        uploader.execute();
    }

    private void copy(File src, File dst) throws IOException {
        byte[] buffer = new byte[1000];

        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);

        while (true) {
            int read = in.read(buffer);

            if (read == -1) {
                out.flush();
                in.close();
                out.close();
                return;
            }

            out.write(buffer, 0, read);
        }
    }

    private class Uploader extends SwingWorker<Void, String> {

        public Void doInBackground() throws Exception {

            File localRepo = new File(System.getProperty("java.io.tmpdir") + File.separator + "wiki-uploader-" + project.replaceAll("/", "-") + "-"
                    + System.getProperty("user.name"));

            if (username == null || username.isEmpty()) {
                throw new Exception("username not specified");
            }

            if (file == null || file.getPath() == null || file.getPath().isEmpty()) {
                throw new Exception("no file specified to upload");
            }

            publish("uploading " + file);
            publish("target repository: " + GUI.getRemoteRepository(project));

            CredentialsProvider.setDefault(new UsernamePasswordCredentialsProvider(username, password));

            Git git;
            if (localRepo.exists()) {
                publish("opening existing repository at " + localRepo);
                git = Git.open(localRepo);
                publish("pulling updates to " + localRepo);
                git.pull().call();
            } else {
                publish("cloning repository at " + localRepo);
                git = Git.cloneRepository().setURI(GUI.getRemoteRepository(project)).setDirectory(localRepo).call();
            }

            File attachmentDir = new File(localRepo, "attachments");

            if (!attachmentDir.exists()) {
                publish("creating attachment dir " + attachmentDir);
                attachmentDir.mkdir();
            }

            File targetFile = new File(attachmentDir, file.getName());

            if (targetFile.exists()) {
                int result = JOptionPane.showConfirmDialog(contentPanel, "File " + file.getName()
                        + " already exists in repository. Overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    return null;
                }
            }

            publish("copying file to " + targetFile);

            copy(file, targetFile);

            publish("adding file to repository");

            git.add().addFilepattern("attachments/" + file.getName()).call();

            publish("creating attachment overview page");

            File attachmentOverview = new File(localRepo, "Attachments.md");
            FileWriter writer = new FileWriter(attachmentOverview);

            writer.write("| File Name | Link | Preview |\n");
            writer.write("| --------- | ---- | ------- |\n");

            File[] files = attachmentDir.listFiles();
            Arrays.sort(files);
            
            for (File attachment : files) {
                String name = attachment.getName();
                writer.write("| attachments/" + name + " | [[" + name + "| attachments/" + name
                        + "]] | [[ attachments/" + name + "|height=100px]] |\n");
            }
            writer.flush();
            writer.close();

            git.add().addFilepattern("Attachments.md").call();

            publish("committing to local repository");

            git.commit().setMessage("adding new file " + file.getName()).call();

            publish("pushing to remote repository");

            git.push().call();

            publish("done!");

            return null;
        }

        @Override
        protected void process(List<String> messages) {
            for (String message : messages) {
                textArea.append(message + "\n");
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
            try {
                uploader.get();
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                JOptionPane.showMessageDialog(contentPanel,
                        "Done uploading file. File available in the wiki as:\n[[attachments/" + file.getName() + "]]",
                        "Uploading done",

                        JOptionPane.PLAIN_MESSAGE);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(contentPanel, "Error while uploading file: " + exception.getMessage(),
                        "Upload Error", JOptionPane.ERROR_MESSAGE);
            }

            setVisible(false);
            dispose();
        }

    }

}
