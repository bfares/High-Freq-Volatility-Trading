package org.brainswap.Gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import javax.swing.*;

public class HFVT extends JComponent {

	
	public void start(){
	JFrame main= createFrame("Main");	
	
	
	JPanel btn_Panel= new JPanel();
	
	
	btn_Panel.setLayout(new GridBagLayout());
	
	JButton gP_Btn = new JButton("GP Creator");
	JButton evaluator_Btn = new JButton("GP Evaluator");
	JButton trader_Btn = new JButton("Trader");
	 GridBagConstraints gbc = new GridBagConstraints();
	 
	
	btn_Panel.add(gP_Btn,gbc);
	
	btn_Panel.add(evaluator_Btn,gbc);
//	btn_Panel.add(trader_Btn);
	
	main.add(btn_Panel);
	
	
	gP_Btn.setVisible(true);
	evaluator_Btn.setVisible(true);
	trader_Btn.setVisible(true);
	
	}
	
	
	public JFrame createFrame(String title){
		JFrame frame= new JFrame("Main");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
        
        frame.setVisible(true);
		return frame;
	}
}
