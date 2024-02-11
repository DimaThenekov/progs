package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import managers.LangManager;
import java.util.Arrays;
import general.*;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUIEditor extends JFrame {
	private LangManager langManager;
	
	private JTextField nameField;
	private JTextField xField;
	private JTextField yField;
	private JTextField ageField;
	

	private JComboBox colorComboBox;
	private JComboBox typeComboBox;
	private JComboBox characterComboBox;

	private JCheckBox killerCheckBox;

	private JTextField personNameField;
	private JTextField personBirthdayField;
	private JTextField personWeightField;
	private JTextField personPassportIDField;
	private JComboBox personColorComboBox;
	
	private JLabel nameLabel;
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel ageLabel;
	private JLabel colorLabel;
	private JLabel typeLabel;
	private JLabel characterLabel;
	private JLabel personNameLabel;
	private JLabel personBirthdayLabel;
	private JLabel personWeightLabel;
	private JLabel personPassportIDLabel;
	private JLabel personColorLabel;

	public GUIEditor(Consumer<Dragon> callback, LangManager langManager, Dragon d) {
		super("DE");
		this.langManager = langManager;
		if (d == null) d = new Dragon(1l, "", new Coordinates(0, 0D), 1, general.Color.GREEN, DragonType.UNDERGROUND, null, null);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		nameLabel = new JLabel("Name");
		nameField = new JTextField(d.getName(), 20);
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(nameLabel);
		nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(nameField);

		xLabel = new JLabel("X coordinate");
		xField = new JTextField(d.getCoordinates().getX().toString());
		xLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(xLabel);
		xField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(xField);

		yLabel = new JLabel("Y coordinate");
		yField = new JTextField(d.getCoordinates().getY().toString());
		yLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(yLabel);
		yField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(yField);

		ageLabel = new JLabel("Age");
		ageField = new JTextField(d.getAge().toString());
		ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(ageLabel);
		ageField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(ageField);

		colorLabel = new JLabel("Color");
		colorComboBox = new JComboBox(new String[]{"GREEN","RED","BLACK","ORANGE"});
		colorComboBox.setSelectedItem(d.getColor().toString());
		colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(colorLabel);
		colorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(colorComboBox);

		typeLabel = new JLabel("DragonType"); 
		typeComboBox = new JComboBox(new String[]{"UNDERGROUND","AIR","FIRE"});
		typeComboBox.setSelectedItem(d.getType().toString());
		typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(typeLabel);
		typeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(typeComboBox);

		characterLabel = new JLabel("Character");
		characterComboBox = new JComboBox(new String[]{"null","CUNNING","EVIL","GOOD","CHAOTIC"});
		typeComboBox.setSelectedItem(d.getType()==null?"null":d.getType().toString());
		characterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(characterLabel);
		characterComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(characterComboBox);

		killerCheckBox = new JCheckBox("Killer"); 
		var needKiller = d.getKiller()!=null;
		killerCheckBox.setSelected(needKiller);
		killerCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		killerCheckBox.addChangeListener(new ChangeListener() { public void stateChanged(ChangeEvent changeEvent) { updateLabels(); }});
		contentPane.add(killerCheckBox);
		
		personNameLabel = new JLabel("Person.Name");
		personNameField = new JTextField(needKiller?d.getKiller().getName():"");
		personNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personNameLabel);
		personNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personNameField);
		
		personBirthdayLabel = new JLabel("Person.Birthday"+" ("+LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)+" or 2023-03-11)");
		personBirthdayField = new JTextField(needKiller&&d.getKiller().getBirthday()!=null?d.getKiller().getBirthday().toString().split("T")[0]:"");
		personBirthdayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personBirthdayLabel);
		personBirthdayField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personBirthdayField);
		
		personWeightLabel = new JLabel("Person.Weight");
		personWeightField = new JTextField(needKiller&&d.getKiller().getWeight()!=null?d.getKiller().getWeight().toString():"");
		personWeightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personWeightLabel);
		personWeightField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personWeightField);
		
		personPassportIDLabel = new JLabel("Person.PassportID");
		personPassportIDField = new JTextField(needKiller&&d.getKiller().getPassportID()!=null?d.getKiller().getPassportID():"");
		personPassportIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personPassportIDLabel);
		personPassportIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personPassportIDField);
		

		personColorLabel = new JLabel("Person.EyeColor");
		personColorComboBox = new JComboBox(new String[]{"GREEN","RED","BLACK","ORANGE"});
		if (needKiller) typeComboBox.setSelectedItem(d.getKiller().getEyeColor().toString());
		personColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personColorLabel);
		personColorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(personColorComboBox);
		contentPane.add(new JLabel(" "));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> {
			// Create a new Dragon object from the user's input
			if (askDragon()!=null && askDragon().validate()) {
				callback.accept(askDragon());
				dispose();
			} else
				JOptionPane.showMessageDialog(this, langManager.get("SaveError"));
		});
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(saveButton);

		getContentPane().add(contentPane);
		updateLabels();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public Dragon askDragon() {
		/*
			private String name; //Поле не может быть null, Строка не может быть пустой
			private Coordinates coordinates; //Поле не может быть null
			private Integer age; //Значение поля должно быть больше 0, Поле может быть null
			private Color color; //Поле не может быть null
			private DragonType type; //Поле не может быть null
			private DragonCharacter character; //Поле может быть null
			private Person killer; //Поле может быть null
		*/
		boolean isNotValid=false;
		String name = nameField.getText();
		isNotValid|=name.equals("");
		nameField.setBackground(name.equals("")?java.awt.Color.RED:null);
		
		var coordinates = askCoordinates();
		isNotValid|=coordinates==null;
		
		Integer age = -1;
		ageField.setBackground(null);
		if (ageField.getText().equals("")) 
			age = null;
		else
			try {
				age = Integer.parseInt(ageField.getText());
				if (age<=0) throw new NumberFormatException("age<=0");
			} catch (NumberFormatException e) { isNotValid=true; ageField.setBackground(java.awt.Color.RED); }
		
		var color = askColor();
		var type = askDragonType();
		var character = askDragonCharacter();
		Person killer = null;
		if (killerCheckBox.isSelected()) {
			killer = askPerson();
			isNotValid|=killer==null;
		}
		return isNotValid?null:new Dragon(10000l, name, coordinates, age, color, type, character, killer);
	}

	public Coordinates askCoordinates() {
		// private Integer x; //Значение поля должно быть больше -485, Поле не может быть null
		// private Double y; //Максимальное значение поля: 907, Поле не может быть null
		boolean isNotValid=false;
		Integer x = -1;
		xField.setBackground(null);
		try { x = Integer.parseInt(xField.getText()); if (x<=-485) throw new NumberFormatException("x<=-485"); } catch (NumberFormatException e) { isNotValid = true; xField.setBackground(java.awt.Color.RED); }
		
		Double y = -1d;
		yField.setBackground(null);
		try { y = Double.parseDouble(yField.getText().replace(',', '.')); if (y>907) throw new NumberFormatException("y>907"); } catch (NumberFormatException e) { isNotValid = true; yField.setBackground(java.awt.Color.RED); }
		return isNotValid?null:new Coordinates(x, y);
	}

	public general.Color askColor() {
		general.Color r = null;
		try { r = general.Color.valueOf((String) colorComboBox.getSelectedItem()); } catch (NullPointerException | IllegalArgumentException  e) { }
		return r;
	}

	public DragonType askDragonType() {
		DragonType r = null;
		try { r = DragonType.valueOf((String) typeComboBox.getSelectedItem()); } catch (NullPointerException | IllegalArgumentException  e) { }
		return r;
	}

	public DragonCharacter askDragonCharacter() {
		DragonCharacter r = null;
		try { r = DragonCharacter.valueOf((String) characterComboBox.getSelectedItem()); } catch (NullPointerException | IllegalArgumentException  e) { }
		return r;
	}

	public Person askPerson() {
		// private String name; //Поле не может быть null, Строка не может быть пустой
		// private LocalDateTime birthday; //Поле может быть null
		// private Long weight; //Поле может быть null, Значение поля должно быть больше 0
		// private String passportID; //Длина строки должна быть не меньше 9, Поле может быть null
		// private Color eyeColor; //Поле не может быть null
		
		boolean isNotValid=false;
		String name = personNameField.getText();
		isNotValid|=name.equals("");
		personNameField.setBackground(name.equals("")?java.awt.Color.RED:null);
		
		LocalDateTime birthday = null;
		personBirthdayField.setBackground(null);
		
		if (personBirthdayField.getText().equals("")) 
			birthday = null;
		else
			try { birthday = LocalDateTime.parse(personBirthdayField.getText()+"T00:00:00.0000", DateTimeFormatter.ISO_DATE_TIME); } catch (DateTimeParseException e) { isNotValid=true; personBirthdayField.setBackground(java.awt.Color.RED); }
		
		Long weight = -1l;
		personWeightField.setBackground(null);
		if (personWeightField.getText().equals("")) 
			weight = null;
		else
			try {
				weight = Long.parseLong(personWeightField.getText());
				if (weight<=0) throw new NumberFormatException("weight<=0");
			} catch (NumberFormatException e) { isNotValid=true; personWeightField.setBackground(java.awt.Color.RED); }
		
		String passportID = "";
		personPassportIDField.setBackground(null);
		if (personPassportIDField.getText().equals("")) 
			passportID = null;
		else
			passportID = personPassportIDField.getText();
		
		if (passportID!=null && passportID.length()<9) {
			isNotValid=true;
			personPassportIDField.setBackground(java.awt.Color.RED);
		}
		
		general.Color color = null;
		try { color = general.Color.valueOf((String) personColorComboBox.getSelectedItem()); } catch (NullPointerException | IllegalArgumentException  e) { }
		isNotValid|=color==null;
		
		return isNotValid?null:new Person(name, birthday, weight, passportID, color);
	}

	public void updateLabels() {
		setTitle(langManager.get("Dragon Editor"));
		// Name X coordinate Y coordinate Age Color DragonType Character Killer Person.Name Person.Birthday Person.Weight Person.PassportID Person.EyeColor Save
		
		nameLabel.setText(langManager.get("TABLE_Name"));
		xLabel.setText(langManager.get("TABLE_XCoordinate"));
		yLabel.setText(langManager.get("TABLE_YCoordinate"));
		ageLabel.setText(langManager.get("TABLE_Age"));
		colorLabel.setText(langManager.get("TABLE_Color"));
		typeLabel.setText(langManager.get("TABLE_DragonType")); 
		characterLabel.setText(langManager.get("TABLE_Character"));
		personNameLabel.setText(langManager.get("TABLE_PersonName"));
		personBirthdayLabel.setText(langManager.get("TABLE_PersonBirthday")+" (2023-06-23)");
		personWeightLabel.setText(langManager.get("TABLE_PersonWeight"));
		personPassportIDLabel.setText(langManager.get("TABLE_PersonPassportID"));
		personColorLabel.setText(langManager.get("TABLE_PersonEyeColor"));
		
		boolean visibleKillerEdit = killerCheckBox.isSelected();
		personNameLabel.setVisible(visibleKillerEdit);
		personBirthdayLabel.setVisible(visibleKillerEdit);
		personWeightLabel.setVisible(visibleKillerEdit);
		personPassportIDLabel.setVisible(visibleKillerEdit);
		personColorLabel.setVisible(visibleKillerEdit);
		
		personNameField.setVisible(visibleKillerEdit);
		personBirthdayField.setVisible(visibleKillerEdit);
		personWeightField.setVisible(visibleKillerEdit);
		personPassportIDField.setVisible(visibleKillerEdit);
		personColorComboBox.setVisible(visibleKillerEdit);
		pack();
		repaint();
	}
}