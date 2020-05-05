/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import java.io.File;
import odevcommand.editor.Editor;

/**
 *
 * @author onurosmanguler
 */
public class NewFileCommand implements Command {
    
    private Editor editor;
    
    private File backup;
    
    public NewFileCommand(Editor editor) {
        this.editor = editor;
    }
    
    private void backup() {
        backup = editor.getOpenedFile();
    }

    @Override
    public void execute() {
        backup();
        
        editor.setOpenedFile(null);
        editor.renderDocument();
    }

    @Override
    public void undo() {
        editor.setOpenedFile(backup);
        editor.renderDocument();
    }    
    
}
