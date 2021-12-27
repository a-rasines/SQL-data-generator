package pack;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pack.Condition.CheckType;
import pack.DataColumn.DataType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;



public class Window extends JFrame{
	private static final long serialVersionUID = -9132954402982379720L;
	private static boolean abort = false; //Si hay algun error al obtener los datos aborta el proceso
	public static Window instance;
	public static void main(String[] args) {
		instance = new Window();
	}
	
	
	private ActionListener numberTextField = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField f = (JTextField)e.getSource();
			try {
				Integer.parseInt(f.getText());
			}catch(NumberFormatException e1) {
				f.setText("1");
			}
		}
		
	};
	public static DefaultListModel<DataColumn> columns = new DefaultListModel<>();
	public Window() {
		setLayout(new BorderLayout());
		
		JList<DataColumn> columnList = new JList<>(columns);
		JScrollPane columnPane = new JScrollPane(columnList);
		
		columnPane.setBorder(BorderFactory.createTitledBorder("Columnas"));
		
		JButton borrar = new JButton("DEL");
		JButton upwards = new JButton("↑");
		JButton downwards = new JButton("↓");
		JButton clear = new JButton("CLR");
		JButton save = new JButton("SAV");
		JButton see = new JButton("SEE");
		JPanel panelDrcha = new JPanel(new BorderLayout());
		JPanel panelDrchaInf = new JPanel(new GridLayout(2, 3));
		downwards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataColumn value = columnList.getSelectedValue();
				int pos =columnList.getSelectedIndex();
				if(pos != columns.size()-1 && pos != -1) {
					columns.remove(pos);
					columns.add(pos+1, value);
				}
			}
		});
		upwards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataColumn value = columnList.getSelectedValue();
				int pos =columnList.getSelectedIndex();
				if(pos >=1) {
					columns.remove(pos);
					columns.add(pos-1, value);
				}
			}
		});
		borrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1)
					columns.remove(columnList.getSelectedIndex());
			}
		});
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				columns.clear();
				
			}
			
		});
		see.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SavedWindow();
			}
			
		});
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					SavedWindow.columns.addElement(new SavedColumn(columnList.getSelectedValue()));
				}
							
			}
			
		});
		
		panelDrchaInf.add(upwards);
		panelDrchaInf.add(borrar);
		panelDrchaInf.add(downwards);
		panelDrchaInf.add(save);
		panelDrchaInf.add(clear);
		panelDrchaInf.add(see);
		
		panelDrcha.add(panelDrchaInf, BorderLayout.SOUTH);
		panelDrcha.add(columnPane, BorderLayout.CENTER);
		
		JComboBox<DataType> typeCombo = new JComboBox<>(DataType.values());
		JTextField param1 = new JTextField("1", 4);
		JTextField param2 = new JTextField("0", 4);
		JButton add = new JButton("ADD");
		JTextArea output = new JTextArea();
		param1.addActionListener(numberTextField);
		param2.addActionListener(numberTextField);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataType t = ((DataType)typeCombo.getSelectedItem());
				if(t.getParams() == 0)
					columns.addElement(new DataColumn(t, new int[] {}));
				else if (t.getParams() == 1)
					columns.addElement(new DataColumn(t, new int[] {Integer.parseInt(param1.getText())}));
				else if (t.getParams() == 2)
					columns.addElement(new DataColumn(t, new int[] {Integer.parseInt(param1.getText()), Integer.parseInt(param2.getText())}));
			}
			
		});
		
		JComboBox<CheckType> checkCombo = new JComboBox<>(CheckType.values());
		JTextField checkParam = new JTextField(8);
		checkParam.setBorder(BorderFactory.createTitledBorder(((CheckType) checkCombo.getSelectedItem()).getLabel()));
		JButton addCheck = new JButton("ADD");
		JButton remCheck = new JButton("REM");
		checkCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkParam.setBorder(BorderFactory.createTitledBorder(((CheckType) checkCombo.getSelectedItem()).getLabel()));
			}
		});
		addCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					DataColumn dc = columnList.getSelectedValue();
					dc.addCondition((CheckType)checkCombo.getSelectedItem(),new Condition((CheckType)checkCombo.getSelectedItem(), checkParam.getText()));
				}
			}	
		});
		remCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(columnList.getSelectedIndex() != -1) {
					DataColumn dc = columnList.getSelectedValue();
					dc.addCondition((CheckType)checkCombo.getSelectedItem(), null);
				}
				
			}
			
		});
		
		JTextField tableName = new JTextField(16);
		JTextField genQ = new JTextField(4);
		JButton generate = new JButton("GEN");
		genQ.addActionListener(numberTextField);
		tableName.setBorder(BorderFactory.createTitledBorder("Nombre de tabla"));
		generate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abort = false;
				int imax = Integer.parseInt(genQ.getText());
				String table = tableName.getText();
				String end = "";
				for(int i = 0; i<imax; i++) {
					if(abort)break;
					List<String> newValue = new ArrayList<>();
					for(Object o: columns.toArray()) {
						DataColumn dc = (DataColumn)o;
						newValue.add(dc.randomValue());
					}
					end += "INSERT INTO "+table+" VALUES "+String.join(" , ", newValue)+";\n";
				}
				output.setText(end);
			}
		});
		
		JScrollPane outputPane = new JScrollPane(output);
		outputPane.setBorder(BorderFactory.createTitledBorder("Output"));
		JPanel panelCenter = new JPanel(new BorderLayout());
		JPanel panelNorth = new JPanel(new GridLayout(3, 1));
		JPanel panelAdd = new JPanel(new FlowLayout());
		JPanel panelCheck = new JPanel(new FlowLayout());
		JPanel panelGenerate = new JPanel(new FlowLayout());
		
		panelAdd.setBorder(BorderFactory.createTitledBorder("Añadir Columna"));
		panelCheck.setBorder(BorderFactory.createTitledBorder("Añadir condición a columna seleccionada"));
		panelGenerate.setBorder(BorderFactory.createTitledBorder("Generación"));
		
		panelAdd.add(typeCombo);
		panelAdd.add(param1);
		panelAdd.add(param2);
		panelAdd.add(add);
		
		panelCheck.add(checkCombo);
		panelCheck.add(checkParam);
		panelCheck.add(addCheck);
		panelCheck.add(remCheck);
		
		panelGenerate.add(tableName);
		panelGenerate.add(genQ);
		panelGenerate.add(generate);
		
		panelNorth.add(panelAdd);
		panelNorth.add(panelCheck);
		panelNorth.add(panelGenerate);
		
		panelCenter.add(panelNorth, BorderLayout.NORTH);
		panelCenter.add(outputPane, BorderLayout.CENTER);
		
		add(panelCenter, BorderLayout.CENTER);
		add(panelDrcha, BorderLayout.WEST);
		setSize(650,400);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setTitle("SQL data generator");
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
				
			}
			
		});
		
	}
	public static void abort(String reason) {
		JOptionPane.showMessageDialog(null, reason);
		Window.abort = true;
	}

}
