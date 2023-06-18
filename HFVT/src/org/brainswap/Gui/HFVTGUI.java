package org.brainswap.Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import org.brainswap.GP.VModel;
import org.epochx.gp.representation.GPCandidateProgram;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HFVTGUI {
	private static VModel resultingGP;
	private static GPCandidateProgram bestCand;
	private JFrame frmHfvt;
	public static JButton btnGpCreator;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HFVTGUI window = new HFVTGUI();
					window.frmHfvt.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HFVTGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHfvt = new JFrame();
		frmHfvt.setTitle("HFVT");
		frmHfvt.setBounds(100, 100, 356, 300);
		frmHfvt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmHfvt.getContentPane().add(panel, BorderLayout.NORTH);
		
		 btnGpCreator = new JButton("GP Evaluator");
		 btnGpCreator.setEnabled(false);
		 
		btnGpCreator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GPEvaluator evaluator= new GPEvaluator();
				GPEvaluator.main(null);
			}
		});
		
		JButton btnGpEvaluator = new JButton("GP Creator");
		btnGpEvaluator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GPCreator creator= new GPCreator();
				GPCreator.main(null);
			}
		});
		
		JButton btnTrader = new JButton("Trader");
		btnTrader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Trader trader= new Trader();
			Trader.main(null);
			
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(129, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnTrader, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnGpCreator, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnGpEvaluator, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(124))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(79)
					.addComponent(btnGpEvaluator)
					.addGap(18)
					.addComponent(btnGpCreator)
					.addGap(18)
					.addComponent(btnTrader)
					.addContainerGap(85, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}

	public static VModel getResultingGP() {
		return resultingGP;
	}

	public static void setResultingGP(VModel resultingGP) {
		HFVTGUI.resultingGP = resultingGP;
	}

	public static GPCandidateProgram getBestCand() {
		return bestCand;
	}

	public static void setBestCand(GPCandidateProgram bestCand) {
		HFVTGUI.bestCand = bestCand;
	}
	
	
}
