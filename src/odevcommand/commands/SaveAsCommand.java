/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import odevcommand.editor.Editor;

/**
 *
 * @author onurosmanguler
 */
public class SaveAsCommand implements Command {
    
    private Editor editor;
    
    public SaveAsCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        File dummyFile = new File("Untitled.rtf");
        JFileChooser chooser = new JFileChooser(dummyFile);
        chooser.setSelectedFile(dummyFile);
        chooser.setMultiSelectionEnabled(false);
        
        // Save only rtf file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("RTF only", "rtf");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int option = chooser.showSaveDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                StyledDocument document = editor.getTextPane().getStyledDocument();
                RTFEditorKit kit = new RTFEditorKit();   
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                kit.write(out, document, document.getStartPosition().getOffset(), document.getLength());
                out.close();

                String rtfContent = out.toString();

                final StringBuffer rtfContentBuffer = new StringBuffer(rtfContent);
                final int endProlog = rtfContentBuffer.indexOf("\n\n");

                rtfContentBuffer.insert(endProlog, "\n\\sl240");
                rtfContentBuffer.insert(endProlog, "\n\\sb0\\sa0");

                rtfContent = rtfContentBuffer.toString();

                final File selectedFile = chooser.getSelectedFile();
                
                // Get file name without file extension
                String fileName = selectedFile.getName();
                int index = fileName.lastIndexOf(".");
                
                if (index > 0) {
                    fileName = fileName.substring(0, index);
                }
                
                // Set file extension to rtf because my text editor supported only rtf format
                final File outFile = new File(selectedFile.getParentFile(), fileName + ".rtf");

                // Save renamed file
                final FileOutputStream fos = new FileOutputStream(outFile);
                fos.write(rtfContent.toString().getBytes());
                fos.close();
                
                // Update editor
                editor.setOpenedFile(outFile);
                editor.renderDocument();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void undo() {
        // Not needed. only pop command stack
    }
    
}
