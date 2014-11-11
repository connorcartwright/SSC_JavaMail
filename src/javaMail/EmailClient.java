package javaMail;

import java.util.Properties;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.sun.mail.imap.IMAPFolder;

public class EmailClient {
	
	private IMAPFolder folder;
	private Store store;
	private String username;
	private String password;
	private String smtphost;
	private Session session;

	public EmailClient() throws MessagingException {
		setUpUser();
	}
	
	public void setUpUser() throws MessagingException 
	{
		folder = null;
		store = null;
		username = "sscjavamail@gmail.com";
		password = "";	        

		// Step 1: Set all Properties
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");
		
		JPasswordField pwd = new JPasswordField(10);  
		int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
		if(action < 0) {
			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
			System.exit(0); 
		}
		else {
			password = new String(pwd.getPassword());  
		}
		
		// Set Property with username and password for authentication  
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		//Step 2: Establish a mail session (java.mail.Session)
		Session session = Session.getDefaultInstance(props);

		// We need to get Store from mail session
		// A store needs to connect to the IMAP server  
		store = session.getStore("imaps");
		store.connect("imap.googlemail.com",username, password);

		// Choose folder, in this case, we chose your inbox
		folder = (IMAPFolder) store.getFolder("inbox"); 
	}

}
