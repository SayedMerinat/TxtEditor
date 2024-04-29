package txt_editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TextEditor extends JFrame implements ActionListener {
	
	FileReader text;
	
	TextArea textArea = new TextArea();
	
	JMenuBar menuBar;
	
	JMenu fileMenu;
	JMenu editMenu;
	JMenu fontMenu;
	JMenu helpMenu;
	
	JMenuItem loadItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	JMenuItem encryptItem;
	JMenuItem decryptItem;
	JMenuItem font;
	JMenuItem fontSize;
	
	JFileChooser fileChooser = new JFileChooser();
	
	public static boolean isBasicLatinAlphabet(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
	public static String encrypt(String plaintext, String key) {
        StringBuilder ciphertext = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < plaintext.length(); i++) {
            char plainChar = plaintext.charAt(i);
            if (isBasicLatinAlphabet(plainChar)) {
                char keyChar = key.charAt(j % keyLength);
                int shift = (Character.toUpperCase(plainChar) - 'A' + Character.toUpperCase(keyChar) - 'A') % 26;
                char encryptedChar = (char) (Character.isUpperCase(plainChar) ? ('A' + shift) : ('a' + shift));
                ciphertext.append(encryptedChar);
                j++;
            } else {
                ciphertext.append(plainChar);
            }
        }
        return ciphertext.toString();
        }
	
	public static String decrypt(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < ciphertext.length(); i++) {
            char encryptedChar = ciphertext.charAt(i);
            if (isBasicLatinAlphabet(encryptedChar)) {
                char keyChar = key.charAt(j % keyLength);
                int shift = (Character.toUpperCase(encryptedChar) - 'A' - (Character.toUpperCase(keyChar) - 'A') + 26) % 26;
                char decryptedChar = (char) (Character.isUpperCase(encryptedChar) ? ('A' + shift) : ('a' + shift));
                plaintext.append(decryptedChar);
                j++;
            } else {
                plaintext.append(encryptedChar);
            }
        }
        return plaintext.toString();
    }
	
	TextEditor() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		menuBar = new JMenuBar();
		
		textArea.setSize(475, 420);
		textArea.setLocation(10,10);
		textArea.setBackground(Color.lightGray);
		textArea.setForeground(Color.black);
		textArea.setFont(new Font("Calibri", Font.BOLD, 11));
		
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		fontMenu = new JMenu("Font");
		helpMenu = new JMenu("Help");
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(fontMenu);
		menuBar.add(helpMenu);
		
		loadItem = new JMenuItem("Load");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");
		encryptItem = new JMenuItem("Encrypt");
		decryptItem = new JMenuItem("Decrypt");
		font = new JMenuItem("Calibri");
		fontSize = new JMenuItem("11");
		
		loadItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		encryptItem.addActionListener(this);
		decryptItem.addActionListener(this);
		font.addActionListener(this);
		fontSize.addActionListener(this);
		
		fileMenu.setMnemonic(KeyEvent.VK_F); // ALT + f for file
		editMenu.setMnemonic(KeyEvent.VK_E); // ALT + e for file
		helpMenu.setMnemonic(KeyEvent.VK_H); // ALT + h for file
		loadItem.setMnemonic(KeyEvent.VK_L); // l for load
		saveItem.setMnemonic(KeyEvent.VK_S); // s for save
		exitItem.setMnemonic(KeyEvent.VK_E); // e for exit
		encryptItem.setMnemonic(KeyEvent.VK_E); // e for encrypt
		decryptItem.setMnemonic(KeyEvent.VK_D); // d for decrypt
		
		fileMenu.add(loadItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		
		editMenu.add(encryptItem);
		editMenu.add(decryptItem);
		
		fontMenu.add(font);
		fontMenu.add(fontSize);

		this.setJMenuBar(menuBar);
		this.add(textArea);
		this.pack();
		this.setSize(500,500);
		this.setResizable(false);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==loadItem) {
			int response = fileChooser.showOpenDialog(null);
			if(response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

	            // Load file asynchronously
	            new Thread(() -> {
	                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                    StringBuilder stringBuilder = new StringBuilder();
	                    String line;

	                    while ((line = reader.readLine()) != null) {
	                        stringBuilder.append(line);
	                        stringBuilder.append(System.lineSeparator());
	                    }

	                    String fileContent = stringBuilder.toString();

	                    // Update GUI on the Event Dispatch Thread
	                    SwingUtilities.invokeLater(() -> textArea.setText(fileContent));
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "An Error Occured.");
	                } 
	            }).start();
			}
		}		
		if(e.getSource()==saveItem) {
			int response1 = fileChooser.showSaveDialog(null);
			if(response1 == JFileChooser.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "Operation Cancelled.");
			}
			else {
				//save
				File selectedFile = fileChooser.getSelectedFile();
	            try {
	                // Create a FileWriter to write to the file
	                FileWriter writer = new FileWriter(selectedFile);

	                // Write the content of the JTextArea to the file
	                writer.write(textArea.getText());

	                // Flush and close the writer to ensure all data is written
	                writer.flush();
	                writer.close();

	                // Notify the user that the file has been saved
	                JOptionPane.showMessageDialog(null, "File saved successfully!");
	            } catch (IOException wr) {
	                wr.printStackTrace();
	                JOptionPane.showMessageDialog(null, "Error saving file: " + wr.getMessage());
	            }
			}
		}
		if(e.getSource()==exitItem) {
			System.exit(0);
		}
		if(e.getSource()==encryptItem) {
			String key = JOptionPane.showInputDialog("Please enter the key you want to use.");
			if (key == null) {
				//JOptionPane.showMessageDialog(null, "Operation Cancelled.");
			}
			else {
				//encrypt
				String encryptedText = encrypt(textArea.getText(), key);
				textArea.setText(encryptedText);
			}				
		}
		if(e.getSource()==decryptItem) {
			String key = JOptionPane.showInputDialog("Please enter the key you want to use.");
			if (key == null) {
				//JOptionPane.showMessageDialog(null, "Operation Cancelled.");
			}
			else {
				//decrypt
				String decryptedText = decrypt(textArea.getText(), key);
				textArea.setText(decryptedText);
				}
			}
		}
	}

