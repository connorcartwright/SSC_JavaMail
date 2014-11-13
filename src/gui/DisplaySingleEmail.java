package gui;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javaMail.EmailClient;

import javax.swing.JButton;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class DisplaySingleEmail extends JFrame {

	private JPanel contentPane;
	private JTextField fromInput;
	private JTextField subjectInput;
	private EmailClient client;

/**
 * Creates a new DisplaySingleEmail object which is used to produce a GUI window that shows the contents of
 * one email including the sender, the subject and the email body.
 * 
 * @param client - The email client used to pass back to the Display All Emails window
 * @param from - The sender of the email we are looking at
 * @param subject - The subject of the email we are looking at
 * @param body - The content of the email we are looking at
 */
	public DisplaySingleEmail(final EmailClient client, String from, String subject, String body) {
		this.client = client;
		setTitle("Display Single Email");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 474, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFrom = new JLabel("From: ");
		lblFrom.setBounds(10, 11, 46, 14);
		contentPane.add(lblFrom);
		
		fromInput = new JTextField();
		fromInput.setBounds(73, 8, 375, 20);
		contentPane.add(fromInput);
		fromInput.setColumns(10);
		fromInput.setText(from);
		
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(10, 46, 46, 14);
		contentPane.add(lblSubject);
		
		subjectInput = new JTextField();
		subjectInput.setBounds(73, 43, 375, 20);
		contentPane.add(subjectInput);
		subjectInput.setColumns(10);
		subjectInput.setText(subject);
		
		JLabel lblBody = new JLabel("Body:");
		lblBody.setBounds(10, 85, 46, 14);
		contentPane.add(lblBody);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(359, 328, 89, 23);
		contentPane.add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 reopenAllEmails();
				 }
			 });
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(73, 85, 375, 232);
		contentPane.add(scrollPane);
		
		JTextArea bodyInput = new JTextArea();
		bodyInput.setLineWrap(true);
		scrollPane.setViewportView(bodyInput);
		bodyInput.setBackground(new Color(230, 230, 250));
		bodyInput.setText(body);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				reopenAllEmails();
			}
		});
	}
	
	/*
	 * This method is used to close the single email window we are looking at and reopen the window that
	 * show all of the emails.
	 */
	private void reopenAllEmails() {
		try {
			DisplayAllEmails emails = new DisplayAllEmails(client);
			emails.setVisible(true);
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}	
		dispose();
	}
}
