/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.commands;

import java.util.Stack;

/**
 *
 * @author onurosmanguler
 */
public class CommandHistory {
    private final Stack<Command> history;

    public CommandHistory() {
        this.history = new Stack<>();
    }
    
    public void push(Command command) {
        history.push(command);
    }
    
    public Command pop() {
        return history.pop();
    }
    
    public boolean isEmpty() {
        return history.isEmpty();
    }
}
