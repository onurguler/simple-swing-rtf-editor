/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import odevcommand.editor.Editor;

/**
 *
 * @author onurosmanguler
 */
public class BoldTextCommand implements Command {

    private Editor editor;
    private int selectionStart;
    private int selectionEnd;
    private String selectedText;
    
    public BoldTextCommand(Editor editor) {
        this.editor = editor;
    }
    
    @Override
    public void execute() {
        int selectionStart = editor.getTextPane().getSelectionStart();
        int selectionEnd = editor.getTextPane().getSelectionEnd();
        String selectedText = editor.getTextPane().getSelectedText();
                
        if (selectionStart != selectionEnd) {
            StyledDocument document = editor.getTextPane().getStyledDocument();
             
            Element element = document.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();
            
            MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
            StyleConstants.setBold(asNew, !StyleConstants.isBold(as));
            
            document.setCharacterAttributes(selectionStart, selectedText.length(), asNew, true);
        }
        
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
        this.selectedText = selectedText;
    }

    @Override
    public void undo() {
        if (selectionStart != selectionEnd) {
            StyledDocument document = editor.getTextPane().getStyledDocument();
            
            Element element = document.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();
            
            MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
            StyleConstants.setBold(asNew, !StyleConstants.isBold(as));
            
            document.setCharacterAttributes(selectionStart, selectedText.length(), asNew, true);
        }
    }
    
}
