package pack;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SavedWindow extends JFrame{
	private static final long serialVersionUID = 6567129659028635808L;
	public static DefaultListModel<SavedColumn> columns = new DefaultListModel<>();
	
	public SavedWindow() {
		JList<SavedColumn> columnList = new JList<>(columns);
		JScrollPane columnPane = new JScrollPane(columnList);
		JTextField nam = new JTextField();
		JButton set = new JButton("SET");
		JButton add = new JButton("ADD");
		JButton rem = new JButton("REM");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					columnList.getSelectedValue().addToUse();
				}
				
			}
		});
		set.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					columnList.getSelectedValue().changeName(nam.getText());
				}
			}
			
		});
		rem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					columns.remove(columnList.getSelectedIndex());
				}
				
			}
		});
		setLayout(new BorderLayout());
		JPanel buttons = new JPanel(new GridLayout(1,2));
		buttons.add(add);
		buttons.add(rem);
		add(columnPane, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		setVisible(true);
		setMinimumSize(new Dimension(200,300));
		setLocationRelativeTo(Window.instance);
	}
}
