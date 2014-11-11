package gui;

import java.awt.BorderLayout;
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
import java.awt.SystemColor;
import java.io.IOException;

import javaMail.IMAPClient;

public class Mainframe extends JFrame {

	private JPanel contentPane;
	private IMAPClient client;

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
	 * Create the frame.
	 */
	public Mainframe() {
		try {
			client = new IMAPClient();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setBackground(Color.LIGHT_GRAY);
		setFont(new Font("Arial", Font.PLAIN, 12));
		setForeground(Color.LIGHT_GRAY);
		setTitle("SSC JavaMail");
		setBounds(100, 100, 291, 170);
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
		
		JButton btnViewBySubject = new JButton("View By Subject");
		btnViewBySubject.setBounds(10, 11, 128, 23);
		contentPane.add(btnViewBySubject);
		
		JButton btnRecentEmails = new JButton("Recent Emails");
		btnRecentEmails.setBounds(10, 93, 128, 23);
		contentPane.add(btnRecentEmails);
		
		JButton btnSendEmail = new JButton("Send Email");
		btnSendEmail.setBounds(170, 51, 105, 23);
		contentPane.add(btnSendEmail);
		
		JButton btnSeenEmails = new JButton("Seen Emails");
		btnSeenEmails.setBounds(170, 11, 105, 23);
		contentPane.add(btnSeenEmails);
		
		JButton btnSpamFilter = new JButton("Spam Filter");
		btnSpamFilter.setBounds(10, 51, 128, 23);
		contentPane.add(btnSpamFilter);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitApp();
			}
		});
		btnClose.setBounds(186, 104, 89, 23);
		contentPane.add(btnClose);
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
			System.exit(0);
		}
		// Don't quit
	}
	
}
