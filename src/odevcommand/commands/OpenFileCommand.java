/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import odevcommand.editor.Editor;

/**
 *
 * @author onurosmanguler
 */
public class OpenFileCommand implements Command {

    private Editor editor;
    private File backup;
    
    public OpenFileCommand(Editor editor) {
        this.editor = editor;
    }
    
    private void backup() {
        backup = editor.getOpenedFile();
    }
    
    @Override
    public void execute() {
        backup();
        
        try {
            // Select only rtf or text file
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "RTF or TXT only", 
                    "rtf", 
                    "txt"
            );
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Select a RTF or TXT file to open");
            
            int returnVal = chooser.showOpenDialog(null);
            
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            
            File openedFile = chooser.getSelectedFile();
            if (openedFile == null) {
                return;
            }

            if (!openedFile.exists()) {
                JOptionPane.showMessageDialog(
                        null, 
                        "Failed to open file, file does not exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            String fileName = openedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            // Check file extension is supported
            if (!fileExtension.equalsIgnoreCase(".txt") && !fileExtension.equalsIgnoreCase(".rtf")) {
                return;
            }
             
            editor.setOpenedFile(openedFile);
            editor.renderDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        editor.setOpenedFile(backup);
        editor.renderDocument();
    }

}
