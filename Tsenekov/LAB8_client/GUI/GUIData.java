package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import managers.LangManager;
import java.util.Arrays;
import java.util.HashMap;
import general.*;
import utility.Runner;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;

public class GUIData extends JFrame {
	private JLabel userLabel;
	private JLabel filterLabel;
	private JLabel langLabel;

	private JTabbedPane tabbedPane;

	private JPanel tablePanel;
	private JPanel bottomPanel;
	private JPanel imagePanel;
	private JTable dataTable;
	private JButton editButton;
	private JButton userButton;
	private JButton exitBtn;
	private LangManager langManager;
	
	private JComboBox condFieldBox, condBox;
	private JTextField condField;

	private Runner runner;

	private GUIUtilRender guiUtilRender = new GUIUtilRender();
	private HashMap<Integer, GUIEditor> editors = new HashMap<>();
	private HashMap<String, JButton> commandButtons = new HashMap<>();

	Object[][] sourseData = {};//{ {1, "user1", "Toothless", 20.0, 123d, 312d}, {2, "user1", "Saphira", 42.123, -123d, 312d}, {3, "user2", "Falkor", 100.312, 123d, -312d} };
	Object[][] visibleData;
	
	String[] columnNames = {"id", "userID", "Name", "Age", "XCoordinate", "YCoordinate", "creationDate", "Color", "DragonType", "Character", "PersonName", "PersonBirthday", "PersonWeight", "PersonPassportID", "PersonEyeColor"};
	String[] columnTypes = {"I", "S", "S", "F", "F", "F", "D", "S", "S", "S", "S", "D", "I", "S", "S"};


	public GUIData(Runner runner, LangManager langManager) {
		this.langManager = langManager;
		this.runner = runner;
		
		setTitle("DF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// Create the labels for user login and role
		userLabel = new JLabel("U: ");
		var filterAndLangPanel = new JPanel(new BorderLayout());
		filterAndLangPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		var filterPanel = new JPanel();
		filterLabel = new JLabel("F: ");
		condFieldBox = new JComboBox(new String[]{"", "123", "123", "123"});
		condFieldBox.setSelectedIndex(0);
		condBox = new JComboBox(new String[]{"", "<", "=", ">"});
		condBox.setSelectedIndex(0);
		condField = new JTextField();
		
		
		condFieldBox.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { if (condFieldBox.getSelectedItem()!=null) filterTable(); }});
		condBox.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { filterTable(); }});
		condField.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { filterTable(); }});
		
		filterPanel.add(filterLabel);
		filterPanel.add(condFieldBox);
		filterPanel.add(condBox);
		filterPanel.add(condField);
		
		filterAndLangPanel.add(filterPanel, BorderLayout.WEST);
		
		
		var langPanel = new JPanel();
		langLabel = new JLabel("L: ");
		JComboBox langBox = new JComboBox(langManager.getLangs());
		langBox.setSelectedIndex(langManager.getIndex());
		langBox.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			langManager.setLang((String)langBox.getSelectedItem());
			filterTable();
			updateLabels();
		}});
		langPanel.add(langLabel);
		langPanel.add(langBox);
		filterAndLangPanel.add(langPanel, BorderLayout.EAST);

		// Create the table panel
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		// Set up the table
		dataTable = new JTable(new Object[][]{}, columnNames);
		dataTable.setModel(new DefaultTableModel(new Object[][]{}, columnNames) { public boolean isCellEditable(int row, int column) { return false; } });
		dataTable.setAutoCreateRowSorter(true);
		dataTable.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int column = dataTable.columnAtPoint(e.getPoint());
				dataTable.getRowSorter().toggleSortOrder(column);
			}
		});
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		//dataTable.setPreferredScrollableViewportSize(new Dimension(dataTable.getPreferredSize().width, 2 * dataTable.getRowHeight()));
		JScrollPane scrollPane = new JScrollPane(dataTable);//dataTable);
		tablePanel.add(scrollPane);//, BorderLayout.CENTER);
		//scrollPane.setPreferredSize(new Dimension(Math.max(10,tablePanel.getHeight()-10), Math.max(10,tablePanel.getWidth()-10)));

		// Create the image panel
		imagePanel = new JPanel(new BorderLayout()){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (guiUtilRender.render(g, imagePanel.getHeight(), imagePanel.getWidth()))
					repaint();
			}
		};

		var ipml = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { guiUtilRender.startMove(e.getPoint().x, e.getPoint().y); imagePanel.repaint(); }
			@Override
			public void mouseDragged(MouseEvent e) { guiUtilRender.move(e.getPoint().x, e.getPoint().y); imagePanel.repaint(); }
			@Override
			public void mouseExited(MouseEvent e) { guiUtilRender.endMove(e.getPoint().x, e.getPoint().y); imagePanel.repaint(); }
			@Override
			public void mouseReleased(MouseEvent e) { guiUtilRender.endMove(e.getPoint().x, e.getPoint().y); imagePanel.repaint(); }
		};
		imagePanel.addMouseListener(ipml);
		imagePanel.addMouseMotionListener(ipml);

		// Create the tabbed pane and add the panels to it
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("T", tablePanel);
		tabbedPane.addTab("I", imagePanel);

		// Create the buttons
		editButton = new JButton("E");
		userButton = new JButton("U");

		editButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			editD();
		}});
		
		userButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			userEditor();
		}});

		// Add everything to the frame
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		filterAndLangPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(userLabel);
		
		exitBtn = new JButton("Exit");
		exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			onExit();
		}});
		topPanel.add(exitBtn);
		
		topPanel.add(filterAndLangPanel);
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		bottomPanel = new JPanel();
		bottomPanel.setLayout (new WrapLayout());
		commandButtons.put("user_set_role", userButton);
		bottomPanel.add(userButton);
		commandButtons.put("update", editButton);
		bottomPanel.add(editButton);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		// Set the frame size and make it visible
		//setSize(500, 500);
		pack();
		
		addComponentListener(new ComponentAdapter () { public void componentResized(ComponentEvent e) {
			scrollPane.setPreferredSize(new Dimension(Math.max(10,tablePanel.getHeight()/2-10), Math.max(10,tablePanel.getWidth()/2-10)));
			repaint();
		}});
		
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		new Timer().schedule( new TimerTask() {public void run() { fillTable(); }}, 2000L ,1000L);
		fillTable();
		updateLabels();
		setVisible(true);
	}

	private void onExit() {
		new GUILogin(langManager, runner);
		dispose();
	}

	private void fillTable() {
		try {
			// load from server and set sourseData
			// 1, "user1", "Toothless", 20.0, 123d, 312d
			LinkedList<LinkedList<Object>> list = (LinkedList<LinkedList<Object>>) runner.launchCommand("show").returnObject;
			sourseData = list.stream().map(x->{
				var d = (Dragon) x.get(0);
				
				return new Object[]{d.getId().intValue(), String.valueOf(x.get(1)), d.getName(), new Double(d.getAge()), new Double(d.getCoordinates().getX()), new Double(d.getCoordinates().getY()), d.getCreationDate(), d.getColor().toString(), d.getType().toString(), d.getCharacter()==null?null:d.getCharacter().toString(), d.getKiller()==null?null:d.getKiller().getName(), d.getKiller()==null?null:d.getKiller().getBirthday(), d.getKiller()==null?null:d.getKiller().getWeight(), d.getKiller()==null?null:d.getKiller().getPassportID(), d.getKiller()==null?null:d.getKiller().getEyeColor().toString()};
			}).toArray(Object[][]::new);;
			getContentPane().setVisible(true);
			
			updateButtons();
			filterTable();
		} catch (ClassCastException e) { getContentPane().setVisible(false); }
	}

	private void filterTable() { // sourseData -> visibleData
		
		if (((String)condFieldBox.getSelectedItem()).equals("") || ((String)condBox.getSelectedItem()).equals(""))
			visibleData = sourseData;
		else
			visibleData = Arrays.stream(sourseData).filter(x->{
				for (var i=0;i<x.length;i++)
					if (langManager.get("TABLE_"+columnNames[i]) == (String)condFieldBox.getSelectedItem()) {
						var cnd = (String)condBox.getSelectedItem();
						if (cnd.equals("<") && String.valueOf(x[i]).compareTo(condField.getText())<0) return true;
						if (cnd.equals("=") && String.valueOf(x[i]).compareTo(condField.getText())==0) return true;
						if (cnd.equals(">") && String.valueOf(x[i]).compareTo(condField.getText())>0) return true;
					}
				return false;
			}).toArray(Object[][]::new);
		formatTable();
		updateTable();
	}

	private void formatTable() {
		//{300, 300, 30, "nameUser", 1}
		guiUtilRender.setDs(Arrays.stream(visibleData).map(x->new Object[]{((Long)Math.round((Double)x[4])).intValue(),((Long)Math.round((Double)x[5])).intValue(),((Long)Math.round((Double)x[3])).intValue(),x[1],(Integer)x[0]}).toArray(Object[][]::new));
		imagePanel.repaint();
		visibleData = Arrays.stream(visibleData).map(x->{
			String[] nx = new String[x.length];
			for (var i=0;i<x.length;i++) {
				nx[i] = "null";
				if (x[i]==null) continue;
				nx[i] = String.valueOf(x[i]);
				if (columnTypes[i].equals("I")) nx[i]=String.valueOf(x[i]);
				if (columnTypes[i].equals("D")) nx[i]=langManager.formatDate((LocalDateTime)x[i]);
				if (columnTypes[i].equals("F")) nx[i]=langManager.formatFloat((Double)x[i]);
			}
			return nx;
		}).toArray(Object[][]::new);
	}

	private void updateTable() {
		while (dataTable.getRowCount() > visibleData.length) ((DefaultTableModel)dataTable.getModel()).removeRow(0);
		while (dataTable.getRowCount() < visibleData.length) ((DefaultTableModel)dataTable.getModel()).addRow(columnNames);
		
		for (int row = 0; row < dataTable.getRowCount(); row++) {
			for (int column = 0; column < dataTable.getColumnCount(); column++) {
				((DefaultTableModel)dataTable.getModel()).setValueAt(visibleData[row][column], row, column);
			}
		}
	}

	private void updateLabels() {
		setTitle(langManager.get("DataForm"));
		
		userLabel.setText(langManager.get("Username")+": "+runner.getLogin());
		
		filterLabel.setText(langManager.get("Filter")+": ");
		langLabel.setText(langManager.get("Language")+": ");
		
		tabbedPane.setTitleAt(0, langManager.get("Table"));
		tabbedPane.setTitleAt(1, langManager.get("Image"));
		
		editButton.setText(langManager.get("Edit"));
		userButton.setText(langManager.get("UserRole"));
		exitBtn.setText(langManager.get("Exit"));
		
		condFieldBox.removeAllItems();
		condFieldBox.addItem("");
		for(String s:columnNames){
			condFieldBox.addItem(langManager.get("TABLE_"+s));
		}
		
		for (int i = 0; i < dataTable.getColumnCount(); i++) {
			dataTable.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(langManager.get("TABLE_"+columnNames[i]));
		}
		
		for (var e:editors.values())
			if (e.isVisible()){
				e.updateLabels();
				e.repaint();
			}
		
		repaint();
	}

	private void editD() {
		var selectId = getSelectedId();
		
		if (editors.get(selectId)==null || !editors.get(selectId).isVisible()) {
			LinkedList<LinkedList<Object>> list = (LinkedList<LinkedList<Object>>) runner.launchCommand("show").returnObject;
			final int selId = selectId;
			list.forEach((e) -> {
				if (((Dragon)e.get(0)).getId()==selId)
					editors.put(selId,new GUIEditor(d->{
						var s = (String) runner.launchCommand("update", String.valueOf(selId), d).returnObject;
						if (s!=null && !s.equals("OK"))
							JOptionPane.showMessageDialog(this, s);
						}, langManager, (Dragon)e.get(0)));
			});
		} else {
			editors.get(selectId).toFront();
			editors.get(selectId).repaint();
		}
		//JOptionPane.showMessageDialog(this, "id #"+String.valueOf(selectId));
		
	}

	private int getSelectedId() {
		var selectId = -1;
		if (tabbedPane.getSelectedIndex()==0) {
			if (dataTable.getSelectedRow()>=0) selectId = Integer.parseInt((String)visibleData[dataTable.getSelectedRow()][0]);
		} else
			selectId = guiUtilRender.getSelected();
		
		if (selectId<0) {
			JOptionPane.showMessageDialog(this, langManager.get("NeedSelect"));
			return -1;
		}
		return selectId;
		// JOptionPane.showMessageDialog(this, "#"+String.valueOf(selectId)+" removed");
	}

	private GUIUserEditor guiUserEditor = null;
	private void userEditor() {
		if (guiUserEditor == null || !guiUserEditor.isVisible()) {
			guiUserEditor = new GUIUserEditor(script->{for (var e:script.trim().split("\n")) runner.launchCommand(e.split(" ")[0],e.split(" ")[1]);},langManager, (String) runner.launchCommand("user_set_role",":").returnObject, (String) runner.launchCommand("user_add_functionality",":").returnObject);
			updateButtons();
		} else {
			guiUserEditor.toFront();
			guiUserEditor.repaint();
		}
	}

	private void updateButtons() {
		var usedButtons = new LinkedList<JButton>();
		for (var cmd:runner.getCommands().keySet()){
			var text=new String(cmd);
			if (text.charAt(0) == '$') {
				text = text.substring(1);
			}
			if (text.equals("show")) continue;
			if (text.equals("save")) continue;
			if (text.equals("update")) { commandButtons.get("update").setVisible(true); usedButtons.add(commandButtons.get("update")); continue; }
			if (text.equals("user_set_role")) { commandButtons.get("user_set_role").setVisible(true); usedButtons.add(commandButtons.get("user_set_role")); continue; }
			/*
				execute_script <file_name>
				user_add_functionality role:func
				user_remove_functionality role:func
				user_set_role login:role
				history
				info
				show
				add {element}
				clear
				max_by_character
				print_unique_age
				remove_any_by_character {character}
				remove_at index
				remove_by_id ID
				remove_last
				reorder
				update ID {element}
			*/
			
			if (!runner.getCommands().get(cmd).getName().trim().contains(" ")) { // команда без аргументов
				if (commandButtons.get(cmd)==null) {
					var cmdBtn = new JButton(text);
					cmdBtn.putClientProperty("cmd", text);
					cmdBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
						var s = (String) runner.launchCommand((String) (((JButton)e.getSource()).getClientProperty("cmd")),"",null).returnObject;
						if (s.length() > 500) s = s.substring(0, 497) + "...";
						if (s!=null && !s.equals("OK"))
							JOptionPane.showMessageDialog(null, s);
					}});
					commandButtons.put(cmd, cmdBtn);
					bottomPanel.add(cmdBtn);
				}
				commandButtons.get(cmd).setText(langManager.get(text));
				commandButtons.get(cmd).setVisible(true);
				usedButtons.add(commandButtons.get(cmd));
			} else if (runner.getCommands().get(cmd).getName().endsWith(" ID")) { // команда c id
				if (commandButtons.get(cmd)==null) {
					var cmdBtn = new JButton(text);
					cmdBtn.putClientProperty("cmd", text);
					cmdBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
						var selectId = getSelectedId();
						if (selectId>0) {
							var s = (String) runner.launchCommand((String) (((JButton)e.getSource()).getClientProperty("cmd")),String.valueOf(selectId),null).returnObject;
							if (s.length() > 500) s = s.substring(0, 497) + "...";
							if (s!=null && !s.equals("OK"))
								JOptionPane.showMessageDialog(null, s);
						}
					}});
					commandButtons.put(cmd, cmdBtn);
					bottomPanel.add(cmdBtn);
				}
				commandButtons.get(cmd).setText(langManager.get(text));
				commandButtons.get(cmd).setVisible(true);
				usedButtons.add(commandButtons.get(cmd));
			} else if (runner.getCommands().get(cmd).getName().endsWith(" {element}")) { // команда c {element}
				if (commandButtons.get(cmd)==null) {
					var cmdBtn = new JButton(text);
					cmdBtn.putClientProperty("cmd", text);
					cmdBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
						new GUIEditor(d->{
							var s = (String) runner.launchCommand((String) (((JButton)e.getSource()).getClientProperty("cmd")), "", d).returnObject;
							if (s.length() > 500) s = s.substring(0, 497) + "...";
							if (s!=null && !s.equals("OK"))
								JOptionPane.showMessageDialog(null, s);
							}, langManager, null);
					}});
					commandButtons.put(cmd, cmdBtn);
					bottomPanel.add(cmdBtn);
				}
				commandButtons.get(cmd).setText(langManager.get(text));
				commandButtons.get(cmd).setVisible(true);
				usedButtons.add(commandButtons.get(cmd));
			} else if (runner.getCommands().get(cmd).getName().endsWith(" index")||runner.getCommands().get(cmd).getName().endsWith(" <file_name>")) {
				if (commandButtons.get(cmd)==null) {
					var cmdBtn = new JButton(text);
					cmdBtn.putClientProperty("cmd", text);
					cmdBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
							var arg=(String)JOptionPane.showInputDialog(null, "Complete the index", "Complete the index", JOptionPane.PLAIN_MESSAGE, null, null, "0");
							if (arg == null) return;
							var s = (String) runner.launchCommand((String) (((JButton)e.getSource()).getClientProperty("cmd")), arg).returnObject;
							if (s.length() > 500) s = s.substring(0, 497) + "...";
							if (s!=null && !s.equals("OK"))
								JOptionPane.showMessageDialog(null, s);
					}});
					commandButtons.put(cmd, cmdBtn);
					bottomPanel.add(cmdBtn);
				}
				commandButtons.get(cmd).setText(langManager.get(text));
				commandButtons.get(cmd).setVisible(true);
				usedButtons.add(commandButtons.get(cmd));
			} else if (runner.getCommands().get(cmd).getName().endsWith(" {character}")) {
				if (commandButtons.get(cmd)==null) {
					var cmdBtn = new JButton(text);
					cmdBtn.putClientProperty("cmd", text);
					cmdBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
							var arg = (DragonCharacter)JOptionPane.showInputDialog(null, "Complete the character", "Complete the character", JOptionPane.PLAIN_MESSAGE, null, DragonCharacter.values(), DragonCharacter.values()[0]);
							if (arg == null) return;
							var s = (String) runner.launchCommand((String) (((JButton)e.getSource()).getClientProperty("cmd")), "", arg).returnObject;
							if (s.length() > 500) s = s.substring(0, 497) + "...";
							if (s!=null && !s.equals("OK"))
								JOptionPane.showMessageDialog(null, s);
					}});
					commandButtons.put(cmd, cmdBtn);
					bottomPanel.add(cmdBtn);
				}
				commandButtons.get(cmd).setText(langManager.get(text));
				commandButtons.get(cmd).setVisible(true);
				usedButtons.add(commandButtons.get(cmd));
			} else
				System.out.println(text);
		}
		for (var e: commandButtons.values())
			if (!usedButtons.contains(e))
			e.setVisible(false);
	}

	private String[] append(String s, String[] arr) { String[] newArr = new String[arr.length + 1]; newArr[0] = s; System.arraycopy(arr, 0, newArr, 1, arr.length); return newArr; }
}