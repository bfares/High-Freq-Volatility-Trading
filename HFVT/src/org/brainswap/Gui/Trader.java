package org.brainswap.Gui;


import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import org.brainswap.main.Extract;
import org.brainswap.trader.BackTester;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class Trader extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField iCapital;
	private JTextField iMinwin;
	private JTextField cost;
	private JTextField theta;
	private JButton btnBrowse;
	private JLabel lblSrc;
	private JFileChooser fc;
	private Extract traderExtractSession;
	private int lag=5;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Trader frame = new Trader();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Trader() {
		setTitle("Trader");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 667, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File(".\\backTester"));
		
		iCapital = new JTextField();
		iCapital.setText("100000");
		iCapital.setColumns(10);
		
		JLabel lblInitialCapital = new JLabel("Contract Value");
		
		iMinwin = new JTextField();
		iMinwin.setText("0");
		iMinwin.setColumns(10);
		
		JLabel lblMinWin = new JLabel("Min Profit");
		
		cost = new JTextField();
		cost.setText("0");
		cost.setColumns(10);
		
		theta = new JTextField();
		theta.setText("10");
		theta.setColumns(10);
		
		JLabel lblAvgTransactionCosts = new JLabel("Avg Transaction Costs");
		
		JLabel lblTheta = new JLabel("Initial Theta");
		
		JButton btnBacktest = new JButton("BackTest");
		
		btnBacktest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(traderExtractSession != null){
					Thread thread = new Thread(){
					    public void run(){
					    	traderExtractSession.startExtract("BT");
							
							BackTester backtester= new BackTester(traderExtractSession.getDataMap(),traderExtractSession.getDate(),lag,Integer.parseInt(cost.getText()),Double.parseDouble(theta.getText()),Integer.parseInt(iMinwin.getText()),Double.parseDouble(iCapital.getText()));
							backtester.process();
							backtester.writeoutput();
					    }
					  };
				
					  thread.start();
					
				
				}
				else{
					JOptionPane.showMessageDialog(null, "Choose the set of data first.","No Data Set",JOptionPane.ERROR_MESSAGE);
				}
					
				
				 }
			}
		);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
		

			public void actionPerformed(ActionEvent e) {
				 int returnVal = fc.showOpenDialog(Trader.this);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		                if(file != null){
		               traderExtractSession= new Extract(file);
		                }
		                
		               lblSrc.setText(file.getPath());
		            } else {
		                
		            }
				
			}
		});
		
		lblSrc = new JLabel("");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnBacktest)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblInitialCapital)
								.addComponent(lblMinWin))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(iCapital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(iMinwin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(43)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblAvgTransactionCosts)
								.addComponent(lblTheta))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(theta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(cost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnBrowse)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblSrc)))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBrowse)
						.addComponent(lblSrc))
					.addGap(24)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(iCapital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblInitialCapital)
						.addComponent(cost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAvgTransactionCosts))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(iMinwin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMinWin)
						.addComponent(theta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTheta))
					.addPreferredGap(ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
					.addComponent(btnBacktest)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
