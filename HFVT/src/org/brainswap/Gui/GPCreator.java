package org.brainswap.Gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import org.brainswap.GP.VModel;
import org.brainswap.main.Extract;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.tools.random.MersenneTwisterFast;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class GPCreator extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean[] terminalSet= {true,true,true,true,true,true,true,true};
	private boolean[] fctSet= {true,true,true,true,true,true,true,true,true,true,true,true};
	private JSpinner step;
	private JSpinner repProb;
	private JSpinner mutProb;
	private JSpinner crossProb;
	private JSpinner genNb;
	private JSpinner propSize;
	public static JButton  btnGenerate;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GPCreator frame = new GPCreator();
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
	public GPCreator() {
		setTitle("GP Creator");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 530, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		 btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(){
				    public void run(){
				    	startGP();
				    }
				  };
			
				  thread.start();
				
			
			}

			private void startGP() {
				Extract session= new Extract();
				session.startExtract("GP");
				MersenneTwisterFast rnd = new MersenneTwisterFast();
				int lag= (int) step.getValue();
				int nbGen= (int) genNb.getValue() ;
				int popSize= (int) propSize.getValue();
				double crossPrb= (double) crossProb.getValue();
				double mutPrb= (double) mutProb.getValue();
				double repPrb = (double) repProb.getValue();
				
				
				
				
				VModel model = new VModel(rnd,500,session.getDataMap(),session.getDate(),lag,fctSet,terminalSet);
				 model.setPopulationSize(popSize);
				 model.setNoGenerations(nbGen);
				 FitnessProportionateSelector fitnessP= new FitnessProportionateSelector(model,true);
				 model.setProgramSelector(fitnessP);
				 model.setCrossoverProbability(crossPrb);
				 model.setMutationProbability(mutPrb);
				 model.setReproductionProbability(repPrb);
				 model.setTerminationFitness(0.00001);
				 model.setInitialiser(new RampedHalfAndHalfInitialiser(model));
				 
				 Log logger= new Log();
				 logger.setModel(true, model);
				 logger.main(null);
				
				 
				
				
			}
		});
		
		JPanel panel_2 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnGenerate))
					.addPreferredGap(ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
					.addGap(42))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(46)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
								.addComponent(btnGenerate)
								.addGap(83))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))))
		);
		
		step = new JSpinner();
		step.setModel(new SpinnerNumberModel(5, 1, 48, 1));
		
		JLabel lblStepsToForecast = new JLabel("Steps to Forecast");
		
		propSize = new JSpinner();
		propSize.setModel(new SpinnerNumberModel(new Integer(1000), new Integer(50), null, new Integer(1)));
		
		JLabel lblPopulationSize = new JLabel("Population Size");
		
		JLabel lblNbOfGeneration = new JLabel("Nb of Generations");
		
		genNb = new JSpinner();
		genNb.setModel(new SpinnerNumberModel(new Integer(500), new Integer(50), null, new Integer(1)));
		
		JLabel lblCrossoverProb = new JLabel("Crossover Prob");
		
		crossProb = new JSpinner();
		crossProb.setModel(new SpinnerNumberModel(0.85, 0.0, 1.0, 0.05));
		
		JLabel lblMutationProb = new JLabel("Mutation Prob");
		
		 mutProb = new JSpinner();
		mutProb.setModel(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05));
		
		JLabel lblReproductionProb = new JLabel("Reproduction Prob");
		
		 repProb = new JSpinner();
		repProb.setModel(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.05));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(lblStepsToForecast)
						.addComponent(step, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNbOfGeneration)
						.addComponent(lblMutationProb)
						.addComponent(lblReproductionProb)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(propSize, Alignment.LEADING)
							.addComponent(lblPopulationSize, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(genNb, Alignment.LEADING)
							.addComponent(lblCrossoverProb, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(crossProb, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
						.addComponent(mutProb, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(repProb, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(35, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStepsToForecast)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(step, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPopulationSize)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(propSize, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNbOfGeneration)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(genNb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblCrossoverProb)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(crossProb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblMutationProb)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mutProb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblReproductionProb)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(repProb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		final JCheckBox checkBox = new JCheckBox("+");
		checkBox.setSelected(true);
		
		
		final JCheckBox checkBox_1 = new JCheckBox("-");
		checkBox_1.setSelected(true);
		
		final JCheckBox checkBox_2 = new JCheckBox("*");
		checkBox_2.setSelected(true);
		
		final JCheckBox checkBox_3 = new JCheckBox("/");
		checkBox_3.setSelected(true);
		
		final JCheckBox chckbxSqrt = new JCheckBox("Sqrt");
		chckbxSqrt.setSelected(true);
		
		final JCheckBox chckbxExp = new JCheckBox("Exp");
		chckbxExp.setSelected(true);
		
		final JCheckBox chckbxMax = new JCheckBox("Max");
		chckbxMax.setSelected(true);
		
		final JCheckBox chckbxMin = new JCheckBox("Min");
		chckbxMin.setSelected(true);
		
		final JCheckBox chckbxLn = new JCheckBox("LN");
		chckbxLn.setSelected(true);
		
		final JCheckBox chckbxErc = new JCheckBox("ERC");
		chckbxErc.setSelected(true);
		
		final JCheckBox chckbxDatai = new JCheckBox("Data(i)");
		chckbxDatai.setSelected(true);
		
		JLabel lblFunctionSet = new JLabel("Function Set");
		lblFunctionSet.setFont(new Font("Times New Roman", Font.BOLD, 13));
		
		final JCheckBox chckbxAvg = new JCheckBox("Avg");
		chckbxAvg.setSelected(true);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(checkBox)
								.addComponent(checkBox_2)
								.addComponent(chckbxSqrt)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxExp)))
							.addGap(18)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxMin)
								.addComponent(chckbxLn)
								.addComponent(checkBox_3)
								.addComponent(checkBox_1)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(chckbxMax)
							.addGap(18)
							.addComponent(chckbxAvg))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(chckbxErc))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(chckbxDatai)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(49, Short.MAX_VALUE)
					.addComponent(lblFunctionSet, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblFunctionSet)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(checkBox)
						.addComponent(checkBox_1))
					.addGap(3)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(checkBox_2)
						.addComponent(checkBox_3))
					.addGap(3)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxSqrt)
						.addComponent(chckbxLn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxExp)
						.addComponent(chckbxMin))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(chckbxMax)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxErc)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxDatai))
						.addComponent(chckbxAvg))
					.addGap(16))
		);
		panel_1.setLayout(gl_panel_1);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Implied Vol");
		chckbxNewCheckBox.setSelected(true);
		
		final JCheckBox chckbxRealisedVol = new JCheckBox("Realised Vol");
		chckbxRealisedVol.setSelected(true);
		
		final JCheckBox chckbxHigh = new JCheckBox("High");
		chckbxHigh.setSelected(true);
		
		final JCheckBox chckbxLow = new JCheckBox("Low");
		chckbxLow.setSelected(true);
		
		final JCheckBox chckbxOpen = new JCheckBox("Open");
		chckbxOpen.setSelected(true);
		
		final JCheckBox chckbxClose = new JCheckBox("Close");
		chckbxClose.setSelected(true);
		
		final JCheckBox chckbxIntegratedVol = new JCheckBox("Integrated Vol");
		chckbxIntegratedVol.setSelected(true);
		
		final JCheckBox chckbxReturn = new JCheckBox("Return");
		chckbxReturn.setSelected(true);
		
		JLabel lblTerminalSet = new JLabel("Terminal Set");
		lblTerminalSet.setFont(new Font("Times New Roman", Font.BOLD, 13));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxRealisedVol)
						.addComponent(chckbxHigh)
						.addComponent(chckbxLow)
						.addComponent(chckbxOpen)
						.addComponent(chckbxClose)
						.addComponent(chckbxReturn)
						.addComponent(chckbxIntegratedVol)
						.addComponent(chckbxNewCheckBox)
						.addComponent(lblTerminalSet, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(45, Short.MAX_VALUE)
					.addComponent(lblTerminalSet, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxNewCheckBox)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxRealisedVol)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxHigh)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxLow)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxOpen)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxClose)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxReturn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxIntegratedVol)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
	
	
	class itemStateChange implements ItemListener {
		   
	   
		@Override
		public void itemStateChanged(ItemEvent arg0) {
			 Object source = arg0.getItemSelectable();

			    if (source == checkBox) {
			    	fctSet[0]= !fctSet[0];
			    } else if (source == checkBox_1) {
			        fctSet[1]= !fctSet[1];
			    } else if (source == checkBox_2) {
			    	fctSet[2]= !fctSet[2];
			    } else if (source == checkBox_3) {
			    	fctSet[3]= !fctSet[3];
			    }else if (source == chckbxSqrt) {
			    	fctSet[4]= !fctSet[4];
			    } else if (source == chckbxExp) {
			    	fctSet[5]= !fctSet[5];
			    } else if (source == chckbxMax) {
			    	fctSet[6]= !fctSet[6];
			    }else if (source == chckbxMin) {
			    	fctSet[7]= !fctSet[7];
			    } else if (source == chckbxLn) {
			    	fctSet[8]= !fctSet[8];
			    } else if (source == chckbxErc) {
			    	fctSet[9]= !fctSet[9];
			    } else if (source == chckbxDatai) {
			    	fctSet[10]= !fctSet[10];
			    } else if (source == chckbxAvg) {
			    	fctSet[11]= !fctSet[11];
			    } 
			    
			    else if (source == chckbxNewCheckBox) {
			    	terminalSet[0]= !terminalSet[0];
			    } else if (source == chckbxRealisedVol) {
			    	terminalSet[1]= !terminalSet[1];
			    } else if (source == chckbxHigh) {
			    	terminalSet[2]= !terminalSet[2];
			    } else if (source == chckbxLow) {
			    	terminalSet[3]= !terminalSet[3];
			    }else if (source == chckbxOpen) {
			    	terminalSet[4]= !terminalSet[4];
			    } else if (source == chckbxClose) {
			    	terminalSet[5]= !terminalSet[5];
			    } else if (source == chckbxIntegratedVol) {
			    	terminalSet[6]= !terminalSet[6];
			    }else if (source == chckbxReturn) {
			    	terminalSet[7]= !terminalSet[7];
			    
			    if (arg0.getStateChange() == ItemEvent.DESELECTED){
			    	
			    }
			   
		}
		    
	    
	} 
	
	}
	
	checkBox.addItemListener(new itemStateChange() );
    checkBox_1.addItemListener(new itemStateChange() );
    checkBox_2.addItemListener(new itemStateChange() ); 
    checkBox_3.addItemListener(new itemStateChange() );
    chckbxSqrt.addItemListener(new itemStateChange() );
    chckbxExp.addItemListener(new itemStateChange() );
    chckbxMax.addItemListener(new itemStateChange() );
    chckbxMin.addItemListener(new itemStateChange() );
    chckbxLn.addItemListener(new itemStateChange() );
    chckbxErc.addItemListener(new itemStateChange() );
    chckbxDatai.addItemListener(new itemStateChange() );
    chckbxNewCheckBox .addItemListener(new itemStateChange() );
	chckbxRealisedVol.addItemListener(new itemStateChange() );
	chckbxHigh .addItemListener(new itemStateChange() );
	chckbxLow .addItemListener(new itemStateChange() );
	chckbxOpen .addItemListener(new itemStateChange() ); 
	chckbxClose .addItemListener(new itemStateChange() ) ;
	chckbxIntegratedVol .addItemListener(new itemStateChange() ) ;
	chckbxReturn .addItemListener(new itemStateChange() );
	chckbxAvg.addItemListener(new itemStateChange() );
	}
}
