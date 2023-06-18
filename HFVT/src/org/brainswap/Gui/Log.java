package org.brainswap.Gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.brainswap.GP.VModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.Life;
import org.epochx.stats.StatField;
import org.epochx.stats.Stats;

import javax.swing.JTextArea;

import java.awt.Window.Type;

import javax.swing.JScrollBar;

import java.awt.Scrollbar;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Log extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	 private static VModel model;
	private static  boolean flag;
	private static GPCreator gPcreatorSession;
	private Thread thread;
	/**
	 * Launch the application.
	 */
	public static  void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Log frame = new Log();
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
	public Log() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new MyWindowListener());
		setType(Type.POPUP);
		setBounds(100, 100, 811, 322);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		 textArea = new JTextArea();
		 textArea.setEditable(false);
		contentPane.add(textArea, BorderLayout.CENTER);
		JScrollPane scrollingArea = new JScrollPane(textArea);
		contentPane.add(scrollingArea,BorderLayout.CENTER);
		this.runModel();
		
	}
	
	public synchronized void exitThread(){
	
		
		try {
			synchronized(model){
			model.wait(ABORT);
			}
			Life.get().clearListeners();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	public void runModel() {
		
		
        
		if(model != null){
			
			final PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
			
			
        Life.get().addGenerationListener(new GenerationAdapter(){
        	
			    public void onGenerationEnd() {
			    	
			    	Stats.get().printToStream(printStream,StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN,  StatField.GEN_FITTEST_PROGRAM);
			    	
			 
			    }
			});
			
        Thread thread = new Thread(){
		    public void run(){
		    	GPCreator.btnGenerate.setEnabled(false);
		    	model.run();
		    	
		    	HFVTGUI.setResultingGP(model);
		    	HFVTGUI.setBestCand((GPCandidateProgram) Stats.get().getStat(StatField.RUN_FITTEST_PROGRAM));
		    	HFVTGUI.btnGpCreator.setEnabled(true);
		    	GPCreator.btnGenerate.setEnabled(true);
		    }
		  };
	
		  thread.start();
		
			
		}else
			System.out.println("Set model first");
	}

	public void setModel(boolean b, VModel model) {
		Log.model= model;
		Log.flag= b;
	}
	
	
	class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;
	     
	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }
	     
	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	        textArea.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	}
	
	class MyWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			exitThread();
			GPCreator.btnGenerate.setEnabled(true);
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		}
	
}
