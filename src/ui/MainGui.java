package ui;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;

import util.ApiCaller;
import util.IO;
import util.SqlUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFormattedTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import entity.Person;

public class MainGui {

	private static final String LAST_USED_FOLDER = "WIKITIMEMACHINE_LAST_USED_FOLDER";
	private JFrame frmWikitimemachineCrawlerV;
	private String path;
	private List<String> category;
	protected String category2;
	private JButton btnSaveAs;
	private JFormattedTextField formattedTextField;
	private JTextArea formattedTextField_1;
	private JButton btnNewButton;
	private JLabel lblCategory;
	private JRadioButton rdbtnCrawl;
	private JPanel panel;
	private ButtonGroup bg;
	private JRadioButton rdbtnReadDates;
	private JMenuBar menuBar;
	private JMenu mnHelp;
	private JMenu mnFile;
	private JPanel panel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					MainGui window = new MainGui();
					window.frmWikitimemachineCrawlerV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public MainGui() throws IOException {
		initialize();
		addMenu();
		addActionListeners();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWikitimemachineCrawlerV = new JFrame();
		frmWikitimemachineCrawlerV.setTitle("WikiTimeMachine Crawler v 0.5");
		frmWikitimemachineCrawlerV.setBounds(100, 100, 450, 300);
		frmWikitimemachineCrawlerV
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel_1 = new JPanel();
		panel_1.setBounds(8, 5, 264, 33);
		frmWikitimemachineCrawlerV.getContentPane().add(panel_1);
		btnSaveAs = new JButton("Save as...");
		panel_1.add(btnSaveAs);
		formattedTextField = new JFormattedTextField();
		formattedTextField.setColumns(20);
		panel_1.add(formattedTextField);
		

		bg = new ButtonGroup();

		panel = new JPanel();
		panel.setBounds(277, 5, 149, 33);
		frmWikitimemachineCrawlerV.getContentPane().add(panel);

		rdbtnCrawl = new JRadioButton("Crawl");

		rdbtnCrawl.setSelected(true);
		panel.add(rdbtnCrawl);
		bg.add(rdbtnCrawl);

		rdbtnReadDates = new JRadioButton("Read Dates");
		panel.add(rdbtnReadDates);
		bg.add(rdbtnReadDates);

		btnNewButton = new JButton("Run");

		btnNewButton.setBounds(8, 206, 418, 23);
		frmWikitimemachineCrawlerV.getContentPane().add(btnNewButton);

		formattedTextField_1 = new JTextArea();
		formattedTextField_1.setRows(5);
		formattedTextField_1.setBounds(101, 42, 171, 94);
		frmWikitimemachineCrawlerV.getContentPane().add(formattedTextField_1);

		lblCategory = new JLabel("Categories");
		lblCategory.setBounds(18, 45, 79, 14);
		frmWikitimemachineCrawlerV.getContentPane().add(lblCategory);

	}

	private void addMenu() {
		menuBar = new JMenuBar();
		frmWikitimemachineCrawlerV.setJMenuBar(menuBar);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		frmWikitimemachineCrawlerV.getContentPane().setLayout(null);

	}

	public void addActionListeners() {
		rdbtnCrawl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visibility = true;
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					btnSaveAs.setText("Open...");
					visibility = false;
				} else {
					btnSaveAs.setText("Save as...");
					lblCategory.setVisible(true);
					visibility = true;
				}
				formattedTextField_1.setVisible(visibility);
				lblCategory.setVisible(visibility);

			}
		});
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if (path == null) {
					return;
				}
				if (formattedTextField_1.getText() == null) {
					return;
				}
				category = Arrays.asList(formattedTextField_1.getText().split(
						"\n"));

				try {
					runPerformed();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnCrawl.isSelected())
				{
					openOrSave(true);	
				}
				else
				{
					openOrSave(false);	
				}
				
			}
		});
	}

	boolean openOrSave(boolean save) {

		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		JFileChooser chooser;

		if (path == null)
			path = System.getProperty("user.home");
		File file = new File(path.trim());

		chooser = new JFileChooser(prefs.get(LAST_USED_FOLDER,
				new File(".").getAbsolutePath()));
		chooser.setDialogType(save? JFileChooser.SAVE_DIALOG:JFileChooser.OPEN_DIALOG);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JSON FILES", "json");

		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

		chooser.setFileFilter(filter);
		chooser.setDialogTitle(save? "Save as...":"Open...");
		chooser.setVisible(true);

		int result = chooser.showSaveDialog(frmWikitimemachineCrawlerV);

		if (result == JFileChooser.APPROVE_OPTION) {

			path = chooser.getSelectedFile().toString();
			file = new File(path);
			if (!filter.accept(file)) {
				path += ".json";
			}
			prefs.put(LAST_USED_FOLDER, file.getParent());
			chooser.setVisible(false);
			formattedTextField.setText(path);
			return true;
		}
		chooser.setVisible(false);
		return false;
	}

	public void runPerformed() throws ClassNotFoundException, SQLException {
		if(rdbtnCrawl.isSelected())
		{
			ApiCaller api;
			try {
				api = new ApiCaller(path, category);
				api.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			IO io = new IO();
			List<Person> pList = null;
			try {
				pList= io.readFromJsonFile(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SqlUtil sq = new SqlUtil();
			sq.storePersons(pList);
		}
		

	}
}
