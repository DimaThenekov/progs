package GUI;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import managers.LangManager;
import utility.Runner;

public class GUILogin extends JFrame {

	private JTextField userField;
	private JPasswordField passwordField;
	private JButton loginButton, registerButton;

	private LangManager langManager;
	private Runner runner;
	private JLabel userLabel, passwordLabel, titleLabel;

	public GUILogin(LangManager langManager, Runner runner) {
		this.langManager = langManager;
		this.runner = runner;
		
		setTitle(langManager.get("Login"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// Create main panel with BorderLayout
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40)); // Set left and right border
		
		// Create title label and add to north of main panel
		titleLabel = new JLabel(langManager.get("Login"), SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.add(titleLabel, BorderLayout.NORTH);
		
		// Create center panel with GridLayout and add to center of main panel
		JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		userLabel = new JLabel(langManager.get("Username")+":");
		userField = new JTextField();
		passwordLabel = new JLabel(langManager.get("Password")+":");
		passwordField = new JPasswordField();
		centerPanel.add(userLabel);
		centerPanel.add(userField);
		centerPanel.add(passwordLabel);
		centerPanel.add(passwordField);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		// Create south panel with GridBagLayout and add to south of main panel
		JPanel southPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		loginButton = new JButton(langManager.get("Login"));
		registerButton = new JButton(langManager.get("Register"));
		c.insets = new Insets(5, 0, 2, 2); c.weightx = 0.8; c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 0; c.gridy = 0;
		southPanel.add(loginButton, c);
		c.insets = new Insets(5, 2, 2, 0); c.weightx = 0.2; c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 1; c.gridy = 0;
		southPanel.add(registerButton, c);
		
		JComboBox languageBox = new JComboBox(langManager.getLangs());
		languageBox.setSelectedIndex(langManager.getIndex());
		c.insets = new Insets(2, 0, 2, 0); c.gridwidth = 2; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 0; c.gridy = 2;
		southPanel.add(languageBox, c);
		
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		
		onEdit("","");
		// Add main panel to JFrame
		add(mainPanel);
		pack();
		
		languageBox.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			langManager.setLang((String)languageBox.getSelectedItem());
			updateLabels();
		}});
		
		userField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { warn(); }
			public void removeUpdate(DocumentEvent e) { warn(); }
			public void insertUpdate(DocumentEvent e) { warn(); }
			public void warn() {
				onEdit(userField.getText(),new String(passwordField.getPassword()));
			}
		});
		
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { warn(); }
			public void removeUpdate(DocumentEvent e) { warn(); }
			public void insertUpdate(DocumentEvent e) { warn(); }
			public void warn() {
				onEdit(userField.getText(),new String(passwordField.getPassword()));
			}
		});

		loginButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			loginButton.setEnabled(true);
			onLogin(userField.getText(),new String(passwordField.getPassword()));
		}});
		
		registerButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			registerButton.setEnabled(true);
			onRegister(userField.getText(),new String(passwordField.getPassword()));
		}});
		
		addComponentListener(new ComponentAdapter () { public void componentResized(ComponentEvent e) {
			mainPanel.setBorder(BorderFactory.createEmptyBorder(Math.max(0,getHeight()/2-95), Math.max(0,getWidth()/2-100), Math.max(0,getHeight()/2-95), Math.max(0,getWidth()/2-100)));
			repaint();
		}});
		
		updateLabels();
		setVisible(true);
	}

	private void onLogin(String login, String pass) {
		if (runner.login(login, pass)) {
			new GUIData(runner, langManager);
			dispose();
		} else
			JOptionPane.showMessageDialog(this, "User login not successfully");
	}

	private void onRegister(String login, String pass) {
		if (runner.register(login, pass)) {
			new GUIData(runner, langManager);
			dispose();
		} else
			JOptionPane.showMessageDialog(this, "User registered not successfully");
	}

	private void onEdit(String login, String pass) {
		if (login.equals("") || pass.equals("")) {
			loginButton.setEnabled(false);
			registerButton.setEnabled(false);
		} else {
			loginButton.setEnabled(true);
			registerButton.setEnabled(true);
		}
		repaint();
	}

	public void updateLabels() {
		setTitle(langManager.get("Login"));
		loginButton.setText(langManager.get("Login"));
		registerButton.setText(langManager.get("Register"));
		userLabel.setText(langManager.get("Username")+":");
		passwordLabel.setText(langManager.get("Password")+":");
		titleLabel.setText(langManager.get("Login"));
	}
}