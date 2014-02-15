package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ErrorDialog extends JFrame {
	private static final long serialVersionUID = 8485264323107024074L;
	private Font font = new Font("Microsoft Yahei", 0, 18);
	public ErrorDialog(String message) {  
		// TODO Auto-generated constructor stub  
		setResizable(false);
		final Container container = getContentPane();  
		container.setLayout(new BorderLayout());  
		JLabel label = new JLabel(message);
		container.add(label);
		//label.setBounds(0, 0, 200, 150);
		label.setFont(font);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		setTitle("´íÎó");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setSize(350, 130);
		setLocationRelativeTo(null);
		
		this.getRootPane().registerKeyboardAction(
	            new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent arg0) {
	                    ErrorDialog.this.dispose();
	                }
	            },
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
	}  
}
