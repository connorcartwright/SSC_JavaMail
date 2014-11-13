package gui;

import java.awt.EventQueue;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javaMail.EmailClient;

@SuppressWarnings("serial")
public class Mainframe extends JFrame {

	private JPanel contentPane;
	private EmailClient client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainframe frame = new Mainframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates a new Mainframe which serves as our home which contains our navigation buttons
	 * which allow us to access all of the functions of the interface. From here we can select
	 * to view all of our emails, to specify custom filters, or to send an email.
	 */
	public Mainframe() {
		try {
			client = new EmailClient();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		
		
		setBackground(Color.LIGHT_GRAY);
		setFont(new Font("Arial", Font.PLAIN, 12));
		setForeground(Color.LIGHT_GRAY);
		setTitle("SSC JavaMail");
		setBounds(100, 100, 210, 246);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // setting the default close operation
		setPreferredSize(new Dimension(300, 290)); // setting the preferred size
		setResizable(false); // making it so that the frame cannot be resized, making the preferred size the fixed size.
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 206, 209));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitApp(); // run the exitApp method if the window 'x' button is pressed
			}
		});
		
		JButton btnSendEmail = new JButton("Send Email"); // creating a new button
		btnSendEmail.setBounds(50, 64, 103, 30); // setting its area
		contentPane.add(btnSendEmail); // adding it to the content pane
		
		btnSendEmail.addActionListener(new ActionListener() { // adding an action listener
			public void actionPerformed(ActionEvent arg0) {
					SendEmail send = new SendEmail(client); // creating a new SendEmail object
					send.setVisible(true); // and making it visible
			}
		});
		
		JButton btnSpamFilter = new JButton("Spam Filter"); // creating a new button
		btnSpamFilter.setBounds(47, 120, 110, 30); // setting its area
		contentPane.add(btnSpamFilter); // adding it to the content pane
		
		btnSpamFilter.addActionListener(new ActionListener() { // adding an action listener
			public void actionPerformed(ActionEvent e) {
				Filter filter = new Filter(client); // creating a new Filter object
				filter.setVisible(true); // and making it visible
			}
		});
		
		JButton btnClose = new JButton("Close"); // creating a new button
		btnClose.setBounds(57, 172, 89, 30); // setting its area
		contentPane.add(btnClose); // adding it to the content pane
		btnClose.addActionListener(new ActionListener() { // adding an action listener
			public void actionPerformed(ActionEvent e) {
				exitApp(); // helper method used to exit the application
			}
		});
		
		JButton btnViewEmails = new JButton("View Emails"); // creating a new button
		btnViewEmails.setBounds(50, 11, 103, 30); // setting its area
		contentPane.add(btnViewEmails); // adding it to the content pane
		btnViewEmails.addActionListener(new ActionListener() { // adding an action listener
			public void actionPerformed(ActionEvent arg0) {
				try {
					DisplayAllEmails emails = new DisplayAllEmails(client); // creating a new DisplayAllEmails object
					emails.setVisible(true); // and making it visible
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Helper method to ensure consistency in leaving application.
	 * With credit to Aston University Computer Science.
	 */
	private void exitApp() {
		// Display confirmation dialog before exiting application
		int response = JOptionPane.showConfirmDialog(contentPane,
				"Do you really want to quit?", "Quit?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				client.endConnection();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		// Don't quit
	}
}
