package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Subjects extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel emailTable;
	private String col[] = {"Name","Address","Phone"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Subjects frame = new Subjects();
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
	public Subjects() {
		String col[] = {"Subject", "Date", "Flag"};
		
		emailTable = new DefaultTableModel(col, 50);
		
		setTitle("Email Subjects");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 759, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnClose.setBounds(655, 461, 78, 23);
		contentPane.add(btnClose);
		
		JButton btnOpenSubjectEmail = new JButton("Open Subject Email");
		btnOpenSubjectEmail.setBounds(10, 461, 158, 23);
		contentPane.add(btnOpenSubjectEmail);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(208, 349, 335, -162);
		contentPane.add(scrollPane_1);
		
		table = new JTable();
		scrollPane_1.setViewportView(table);
	}
}
