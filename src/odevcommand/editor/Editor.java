/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odevcommand.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import odevcommand.commands.BoldTextCommand;
import odevcommand.commands.Command;
import odevcommand.commands.CommandHistory;
import odevcommand.commands.InsertCharacterCommand;
import odevcommand.commands.ItalicTextCommand;
import odevcommand.commands.NewFileCommand;
import odevcommand.commands.OpenFileCommand;
import odevcommand.commands.SaveAsCommand;
import odevcommand.commands.SaveFileCommand;

/**
 *
 * @author onurosmanguler
 */
public class Editor {
    
    private final String TITLE = "OGTextEdit - RTF Text Editor";
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    
    private JFrame frame;
    
    private JTextPane textPane;
    
    private final CommandHistory history;
    
    private File openedFile;
    
    public Editor() {
        history = new CommandHistory();
    }            
    
    public void init() {
        openedFile = null;
        
        Editor editor = this;
        
        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                executeCommand(new InsertCharacterCommand(editor));
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JButton boldButton = new JButton("Bold");
        boldButton.setFont(new Font(boldButton.getFont().getName(), Font.BOLD, boldButton.getFont().getSize()));
        boldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new BoldTextCommand(editor));
            }
            
        });
        
        JButton italicButton = new JButton("Italic");
        italicButton.setFont(new Font(italicButton.getFont().getName(), Font.ITALIC, italicButton.getFont().getSize()));
        italicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new ItalicTextCommand(editor));
            }
        });
        
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        
        JPanel commandsPanel = new JPanel();
        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.X_AXIS));
        commandsPanel.add(boldButton);
        commandsPanel.add(italicButton); 
        commandsPanel.add(Box.createHorizontalGlue());
        commandsPanel.add(undoButton);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(5, 5));
        contentPanel.add(commandsPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JMenuItem openFileMenuItem = new JMenuItem("Open File");
        openFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new OpenFileCommand(editor));
            }
        });
        
        JMenuItem newFileMenuItem = new JMenuItem("New File");
        newFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new NewFileCommand(editor));
            }
        });
        
        JMenuItem saveFileMenuItem = new JMenuItem("Save File");
        saveFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (openedFile == null) {
                    executeCommand(new SaveAsCommand(editor));
                } else {
                    executeCommand(new SaveFileCommand(editor));
                }
            }
        });
        
        JMenuItem shareMenuItem = new JMenuItem("Share");
        // facebook kullanmıyorum o yüzden bu özelliği eklemedim
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openFileMenuItem);
        fileMenu.add(newFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(shareMenuItem);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        
        frame = new JFrame();
        updateTitle();
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setJMenuBar(menuBar);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }
    
    private void undo() {
        // Unlimited undo
        if (history.isEmpty()) {
            return;
        }
        
        Command command = history.pop();
        
        if (command != null ) {
            command.undo();
        }
    }

    public void renderDocument() {
        if (openedFile == null) {
            textPane.setText("");
            updateTitle();
            return;
        }
        
        try {
            Scanner reader = new Scanner(openedFile);
            StringBuilder contents = new StringBuilder();
            
            while (reader.hasNextLine()) {
                contents.append(reader.nextLine()).append("\n");
            }
            
            reader.close();
               
            updateTitle();
            updateContentType();
            textPane.setText(contents.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateContentType() {
        if (openedFile == null) {
            textPane.setContentType("text/plain");
            return;
        }
        
        if (!openedFile.exists()) {
            textPane.setContentType("text/plain");
            return;
        }
        
        String fileName = openedFile.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        
        if (fileExtension.equalsIgnoreCase(".txt")) {
            textPane.setContentType("text/plain");
        } else if (fileExtension.equalsIgnoreCase(".rtf")) {
            textPane.setContentType("text/rtf");
        }
    }
    
    private void updateTitle() {
        if (openedFile == null) {
            frame.setTitle(TITLE + " (Untitled.rtf)");
            return;
        }
        
        frame.setTitle(TITLE + " (" + openedFile.getName() + ")");
    }
      
    public JTextPane getTextPane() {
        return textPane;
    }
    
    public File getOpenedFile() {
        return openedFile;
    }

    public void setOpenedFile(File openedFile) {
        this.openedFile = openedFile;
    }
    
}
