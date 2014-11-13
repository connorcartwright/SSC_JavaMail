package gui;

import java.awt.Component;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javaMail.EmailClient;

@SuppressWarnings("serial")
public class Filter extends JFrame {

	private JPanel contentPane;
	private JButton btnAddFilter;
	private JButton btnCancel;
	private JTextArea keywordInput;
	private JLabel lblFlag;
	private JTextField flagInput;
	private Component errorFrame; // used when producing the error message

	/**
	 * Creates a new Filter object which is used to produce a GUI window which allows the user
	 * to specify a custom filter which will allow them to filter the emails in their inbox
	 * based on keywords/phrases, so that they will be flagged up.
	 * 
	 * @param client - the EmailClient object that will allow the user to set flags.
	 */
	public Filter(final EmailClient client) {
		setTitle("Filter Emails");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 394, 309);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblKeywordphrase = new JLabel("Keywords & Phrases (please separate by semi-colons):");
		lblKeywordphrase.setBounds(10, 36, 368, 14);
		contentPane.add(lblKeywordphrase);
		
		keywordInput = new JTextArea();
		keywordInput.setBackground(new Color(230, 230, 250));
		keywordInput.setBounds(10, 62, 368, 174);
		contentPane.add(keywordInput);
		
		lblFlag = new JLabel("Flag:");
		lblFlag.setBounds(10, 11, 46, 14);
		contentPane.add(lblFlag);
		
		flagInput = new JTextField();
		flagInput.setBounds(52, 8, 326, 20);
		contentPane.add(flagInput);
		flagInput.setColumns(10);
		
		btnAddFilter = new JButton("Add Filter");
		btnAddFilter.setBounds(289, 247, 89, 23);
		contentPane.add(btnAddFilter);
		btnAddFilter.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 	if(flagInput.getText().length() < 1) {
				 		JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
								"The Flag input box must not be empty!",
								"No flag specified.", JOptionPane.ERROR_MESSAGE);	
				 	}
				 	else if (keywordInput.getText().length() < 1) {
				 		JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
								"The Keyword input box must not be empty!",
								"No Keywords/Phrases specified.", JOptionPane.ERROR_MESSAGE);	
				 	}
				 	else {
				 		try {
							client.addCustomFlags(flagInput.getText(), keywordInput.getText());
							 JOptionPane.showMessageDialog(errorFrame, // then show an informative message
										"Custom flag and keywords registered successfully!",
										"Flag Set", JOptionPane.INFORMATION_MESSAGE);	
							 dispose();
						} catch (MessagingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				 	}
				 }
			 });
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(10, 247, 77, 23);
		contentPane.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 dispose();
				 }
			 });
	}
}
