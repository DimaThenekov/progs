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
import java.util.LinkedList;

public class GUIUserEditor extends JFrame {
	private LangManager langManager;
	private LinkedList<JTextField> llFieldsUserRole = new LinkedList<>();
	private LinkedList<JTextField> llFieldsRole = new LinkedList<>();
	private LinkedList<JCheckBox> llCheckBoxRoles = new LinkedList<>();
	String[] allFuncs = {};

	public GUIUserEditor(Consumer<String> callback, LangManager langManager, String user_role, String role_func) {
		super(" ");
		System.out.print(role_func);
		this.langManager = langManager;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		getRootPane().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		var arr = user_role.split("\n");
		
		JPanel panel = new JPanel(new GridLayout(arr.length-1, 2, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		for (var i=1; i<arr.length; i++) {
			JLabel label = new JLabel(arr[i].split(":")[0].trim());
			JTextField textField = new JTextField(arr[i].split(":")[1].trim());
			
			panel.add(label);
			panel.add(textField);
			textField.putClientProperty("user", arr[i].split(":")[0].trim());
			llFieldsUserRole.add(textField);
		}
		
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panel);
		add(new JLabel(" "));
		add(new JLabel(" "));
		
		allFuncs = role_func.split("\n")[0].split(":")[1].trim().split(",");
		arr = role_func.split("\n");
		
		JPanel panel2 = new JPanel(new GridLayout(arr.length-1+3, allFuncs.length+1, 5, 5));
		panel2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		for (var j=1; j<arr.length+3; j++) {
			if (j>=arr.length) {
				var e = new JTextField();
				panel2.add(e);
				llFieldsRole.add(e);
			} else if (j!=1) {
				var e=new JTextField(arr[j].split(":")[0]);
				panel2.add(e);
				llFieldsRole.add(e);
				
			} else 
				panel2.add(new JLabel("\\", SwingConstants.CENTER));
			for (var i=0; i<allFuncs.length; i++) {
				if (j>=arr.length) {
					var e = new JCheckBox();
					e.setHorizontalAlignment(SwingConstants.CENTER);
					panel2.add(e);
					llCheckBoxRoles.add(e);
				} else if (j==1)
					panel2.add(new JLabel(allFuncs[i], SwingConstants.CENTER));
				else {
					var e = new JCheckBox();
					e.setHorizontalAlignment(SwingConstants.CENTER);
					if (arr[j].split(":").length>1)
						for (var h:arr[j].split(":")[1].split(","))
							if (allFuncs[i].equals(h))
								e.setSelected(true);
					panel2.add(e);
					llCheckBoxRoles.add(e);
				}
				//JTextField textField1 = new JTextField(arr[i].split(":")[1].trim());
				
				//panel2.add(label1);
				//panel2.add(textField1);
			}
		}
		panel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(panel2);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> {
			callback.accept(Form2Script());
		});
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(saveButton);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		//JOptionPane.showMessageDialog(this, );
		//JOptionPane.showMessageDialog(this, role_func);
	}
	
				//user_add_functionality role:func
				//user_remove_functionality role:func
	private String Form2Script() {
		var s="";
		for (var e:llFieldsUserRole)
			s+="user_set_role "+e.getClientProperty("user")+":"+e.getText().trim()+"\n";
		var i=0;
		for (var e:llFieldsRole)
			if (!e.getText().trim().equals("")) {
				LinkedList<String> remove_funcs = new LinkedList<>();
				LinkedList<String> add_funcs = new LinkedList<>();
				for (var k=0;k<allFuncs.length;k++)
					if (llCheckBoxRoles.get(i++).isSelected())
						add_funcs.add(allFuncs[k]);
					else
						remove_funcs.add(allFuncs[k]);
				if (add_funcs.size()>0) s+="user_add_functionality "+e.getText().trim()+":"+String.join(",", add_funcs.toArray(new String[]{}))+"\n";
				if (remove_funcs.size()>0) s+="user_remove_functionality "+e.getText().trim()+":"+String.join(",", remove_funcs.toArray(new String[]{}))+"\n";
			} else i+=allFuncs.length;
			//else
				//user_add_functionality
		return s;
	}
}