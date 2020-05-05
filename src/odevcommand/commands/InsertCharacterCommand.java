/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import odevcommand.editor.Editor;

/**
 *
 * @author onurosmanguler
 */
public class InsertCharacterCommand implements Command {

    private Editor editor;
    
    public InsertCharacterCommand(Editor editor) {
        this.editor = editor;
    }
      
    @Override
    public void execute() {
        // do nothing only add the command to stack
    }

    @Override
    public void undo() {
        StyledDocument document = editor.getTextPane().getStyledDocument();;
        if (document.getLength() > 0) {
            try {
                document.remove(document.getLength() - 1, 1);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
       
    }
    
}
