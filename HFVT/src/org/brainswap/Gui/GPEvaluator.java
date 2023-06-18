package org.brainswap.Gui;


import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.brainswap.main.Extract;
import org.brainswap.main.Output;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;

import java.awt.Font;

public class GPEvaluator extends JFrame {
	
	private Extract evalSession;
	private JPanel contentPane;
	JFileChooser fc;
	private JLabel lblBrowse;
	private Output results;
	JSpinner lag;
	private JTextField mSEField;
	private JTextField maxErr;
	private JTextField minErr;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GPEvaluator frame = new GPEvaluator();
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
	public GPEvaluator() {
		setTitle("GP Evaluator");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 571, 296);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File(".\\data"));
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 523, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JButton btnEvaluate = new JButton("Evaluate");
		btnEvaluate.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				if (evalSession!=null){
				 results= new Output("EurDataResults"+ "",evalSession.getDate(),evalSession.getDataMap(),HFVTGUI.getResultingGP(),HFVTGUI.getBestCand(), 500,500,(int)lag.getValue());
				results.getValues();
				 mSEField.setText( String.format("%.5f", results.getStat().getMean()));
				 maxErr.setText(String.format("%.5f", Math.sqrt(results.getStat().getMax())));
				 minErr.setText(String.format("%.5f", Math.sqrt(results.getStat().getMin())));
				}
				else{
					JOptionPane.showMessageDialog(null, "Choose the set of data first.","No Data Set",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				
		            int returnVal = fc.showOpenDialog(GPEvaluator.this);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		               evalSession= new Extract(file);
		                evalSession.startExtract("GPE");
		                lblBrowse.setText(file.getPath());
		            } else {
		                
		            }
				
			}
		});
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 int returnVal = fc.showSaveDialog(GPEvaluator.this);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		                if(results !=null){
		                	
		               results.setFileName(file.getPath());
		                try {
							results.writeOutput();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		            } else {

		            }

		        
				
			}
		});
		
		 lblBrowse = new JLabel("No File Loaded");
		
		 lag = new JSpinner();
		lag.setModel(new SpinnerNumberModel(5, 1, 48, 1));
		
		mSEField = new JTextField();
		mSEField.setEditable(false);
		mSEField.setColumns(10);
		
		JLabel lblMse = new JLabel("MSE :");
		lblMse.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		
		JLabel lblMaxErr = new JLabel("Max Err :");
		lblMaxErr.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		
		maxErr = new JTextField();
		maxErr.setEditable(false);
		maxErr.setColumns(10);
		
		minErr = new JTextField();
		minErr.setEditable(false);
		minErr.setColumns(10);
		
		JLabel lblMinErr = new JLabel("Min Err :");
		lblMinErr.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		
		JLabel lblStepsToForecast = new JLabel("Steps To Forecast");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnSave, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnBrowse, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnEvaluate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(lblStepsToForecast))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lag, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblBrowse, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
							.addGap(24))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(77)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMaxErr)
								.addComponent(lblMse)
								.addComponent(lblMinErr))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(maxErr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(minErr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(mSEField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(162))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBrowse)
						.addComponent(lblBrowse))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEvaluate)
						.addComponent(lblMse)
						.addComponent(mSEField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMaxErr)
						.addComponent(maxErr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStepsToForecast)
						.addComponent(lag, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMinErr)
						.addComponent(minErr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(92)
					.addComponent(btnSave)
					.addContainerGap(26, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		
	}
}
