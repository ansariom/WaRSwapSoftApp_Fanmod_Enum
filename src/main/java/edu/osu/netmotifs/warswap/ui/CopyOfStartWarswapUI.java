package edu.osu.netmotifs.warswap.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import edu.osu.netmotifs.warswap.JWarswapMultiThread;
import edu.osu.netmotifs.warswap.common.CONF;

public class CopyOfStartWarswapUI extends JPanel implements ActionListener,
		PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 75888941049784369L;
	private static final String ASCI_FORMAT = "TEXT";
	private static final String CSV_FORMAT = "CSV";
	private JFrame frmWarswap;
	private JProgressBar progressBar;
	private JButton startButton;
	private JButton loadHtmButton;
	private JTextArea taskOutput;
	private JTextField inGraphPathText;
	private JTextField outGraphPathText;
	private Task task;
	private Font bodyFont = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
	private JLabel timeLabel;
	private JRadioButton asciBtn;
	private JRadioButton csvBtn;
	private String outFormat = ASCI_FORMAT;
	private JComboBox<String> msizeCombo;
	private JTextField randText;
	private long startTime;

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */

		@Override
		public Void doInBackground() {
			int progress = 0;
			setProgress(0);
			// JWarswapMultiThread jWarswapMultiThread = new
			// JWarswapMultiThread("data/small20.igraph.edges.txt",
			// "data/small20.igraph.vertices.txt", "data", "small20");
			JWarswapMultiThread jWarswapMultiThread = null;
			try {
				jWarswapMultiThread = new JWarswapMultiThread("data/ath80.fanmod",
						 "data/ath80.igraph.vertices.txt", "data", "ath80");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nOfRandNets = Integer.valueOf(randText.getText());
			 jWarswapMultiThread.setNoOfIterations(nOfRandNets);
			taskOutput.setText("Randomization & Enumeration ...\n");
			startTime = System.currentTimeMillis();
			 CONF.pool.submit(jWarswapMultiThread);
//			 jWarswapMultiThread.startRunning();
			while (progress < 100) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignore) {
					ignore.printStackTrace();
				}
				timeLabel.setText("Time Elapsed: " + getTimeElapsed());
				progress = ((jWarswapMultiThread.getFinishedJobs() * 100)/nOfRandNets);
				setProgress(Math.min(progress, 100));
			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			startButton.setEnabled(true);
			loadHtmButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			taskOutput.append("Done!\n");
			// poolExecutor.shutdown();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CopyOfStartWarswapUI window = new CopyOfStartWarswapUI();
					window.frmWarswap.setVisible(true);
					window.frmWarswap.addWindowListener(new WindowListener() {

						@Override
						public void windowOpened(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowIconified(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeiconified(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowDeactivated(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowClosing(WindowEvent e) {
							if (JOptionPane.showConfirmDialog(e.getComponent(), 
						            "Are you sure to close this window?", "Really Closing?", 
						            JOptionPane.YES_NO_OPTION,
						            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
						            System.exit(0);
						        }
							// CONF.poolExecutor.shutdown();
						}

						@Override
						public void windowClosed(WindowEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void windowActivated(WindowEvent e) {
							// TODO Auto-generated method stub

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getTimeElapsed() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000;

		String seconds = Integer.toString((int) (elapsedTime % 60));
		String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
		String hours = Integer.toString((int) (elapsedTime / 3600));

		if (seconds.length() < 2)
			seconds = "0" + seconds;

		if (minutes.length() < 2)
			minutes = "0" + minutes;

		if (hours.length() < 2)
			hours = "0" + hours;

		return hours + ":" + minutes + ":" + seconds;
	}

	/**
	 * Create the application.
	 */
	public CopyOfStartWarswapUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWarswap = new JFrame();
		frmWarswap.setTitle("WaRSwap");
		frmWarswap.setBounds(200, 200, 650, 450);
		frmWarswap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		frmWarswap.getContentPane().add(mainPanel, BorderLayout.NORTH);

		JPanel mainPanelTop = new JPanel(new BorderLayout());
		mainPanelTop.add(createConfigPanel(), BorderLayout.NORTH);

		JPanel gPanel = new JPanel(new GridLayout(1, 0));
		gPanel.add(createInputPanel());
		gPanel.add(createOutputPanel());

		mainPanelTop.add(gPanel, BorderLayout.CENTER);
		mainPanelTop.add(createRunAlgPanel(), BorderLayout.SOUTH);
		
		mainPanel.add(mainPanelTop, BorderLayout.NORTH);
		mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		JButton helpButton = new JButton("Help");
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(helpButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(2,0)));
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(3,0)));
		return buttonPanel;
	}

	private JPanel createInputPanel() {
		JPanel inPanel = new JPanel(new BorderLayout());
		inPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		inPanel.setBorder(BorderFactory.createTitledBorder("INPUT GRAPH"));

		JPanel innerGPanel = new JPanel(new BorderLayout());
		inPanel.add(innerGPanel, BorderLayout.NORTH);

		JLabel ingLabel = new JLabel("Input graph ");
		ingLabel.setFont(bodyFont);
		innerGPanel.add(ingLabel, BorderLayout.WEST);

		inGraphPathText = new JTextField(20);
		innerGPanel.add(inGraphPathText, BorderLayout.CENTER);
		JButton inBrwsBtn = new JButton(" Browse ");
		innerGPanel.add(inBrwsBtn, BorderLayout.EAST);
		inBrwsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(CopyOfStartWarswapUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					inGraphPathText.setText(file.getAbsolutePath());
				}
			}
		});

		return inPanel;
	}

	private JPanel createOutputPanel() {
		JPanel outPanel = new JPanel(new GridBagLayout());
		outPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		outPanel.setBorder(BorderFactory.createTitledBorder("OUTPUT"));
		GridBagConstraints c = new GridBagConstraints();

		JPanel innerGPanel = new JPanel(new BorderLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 0);
		outPanel.add(innerGPanel, c);

		JLabel outgLabel = new JLabel("Output ");
		outgLabel.setFont(bodyFont);
		innerGPanel.add(outgLabel, BorderLayout.WEST);

		outGraphPathText = new JTextField(20);
		innerGPanel.add(outGraphPathText, BorderLayout.CENTER);
		JButton outBrwsBtn = new JButton(" Browse ");
		innerGPanel.add(outBrwsBtn, BorderLayout.EAST);
		outBrwsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(CopyOfStartWarswapUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					outGraphPathText.setText(file.getAbsolutePath());
				}

			}
		});

		JPanel fformatPanel = new JPanel(new GridLayout(0, 1));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 0);
		outPanel.add(fformatPanel, c);

		asciBtn = new JRadioButton("ASCII-Text");
		asciBtn.setFont(bodyFont);
		fformatPanel.add(asciBtn);
		asciBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outFormat = ASCI_FORMAT;
			}
		});

		csvBtn = new JRadioButton("CSV");
		csvBtn.setFont(bodyFont);
		fformatPanel.add(csvBtn);
		csvBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outFormat = CSV_FORMAT;
			}
		});

		ButtonGroup outBtnGroup = new ButtonGroup();
		outBtnGroup.add(asciBtn);
		outBtnGroup.add(csvBtn);

		return outPanel;
	}

	private JPanel createConfigPanel() {
		JPanel algPanel = new JPanel(new GridBagLayout());
		algPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		algPanel.setBorder(BorderFactory.createTitledBorder("CONFIG"));
		GridBagConstraints c = new GridBagConstraints();

		JPanel msizePanel = new JPanel(new BorderLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 2, 10, 0);
		algPanel.add(msizePanel, c);

		JLabel msizeLabel = new JLabel("Subgraph size ");
		msizeLabel.setFont(bodyFont);
		msizePanel.add(msizeLabel, BorderLayout.WEST);
		msizeCombo = new JComboBox<String>(new String[] { "2", "3", "4" });
		msizePanel.add(msizeCombo);

		JPanel randNetPanel = new JPanel(new BorderLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 50, 0, 2);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.1;
		c.anchor = GridBagConstraints.CENTER;
		JLabel randLabel = new JLabel("Number of random networks ");
		randLabel.setFont(bodyFont);
		randText = new JTextField(5);
		randText.setText("2500");
		randNetPanel.add(randLabel, BorderLayout.WEST);
		randNetPanel.add(randText, BorderLayout.CENTER);
		algPanel.add(randNetPanel, c);
		algPanel.add(randNetPanel, c);
		return algPanel;
	}

	private JPanel createRunAlgPanel() {
		JPanel runPanel = new JPanel(new BorderLayout());

		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);
		startButton.setBounds(startButton.getBounds().x,
				startButton.getBounds().y, 90, 23);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.05;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 2, 10, 0);
		panel.add(startButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 2, 10, 0);
		panel.add(progressBar, c);

		timeLabel = new JLabel("");
		timeLabel.setFont(bodyFont);
		timeLabel.setForeground(Color.BLACK);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.45;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 10, 0);
		panel.add(timeLabel, c);

		loadHtmButton = new JButton("Export HTML");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.05;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 2, 10, 0);
		panel.add(loadHtmButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 2, 10, 0);
		panel.add(new JScrollPane(taskOutput), c);

		runPanel.add(panel, BorderLayout.PAGE_START);
		runPanel.setBorder(BorderFactory.createTitledBorder("RUN ALGORITHM"));

		return runPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("start")) {
			startButton.setEnabled(false);
			loadHtmButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// Instances of javax.swing.SwingWorker are not reusuable, so
			// we create new instances as needed.
			task = new Task();
			task.addPropertyChangeListener(this);
			task.execute();

		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			// taskOutput.append(String.format("...\n",
			// task.getProgress()));
		}
	}

}
