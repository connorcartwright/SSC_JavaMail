package javaMail;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

	public void setUpUser() throws MessagingException {
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
		int action = JOptionPane.showConfirmDialog(null, pwd, "Enter Password",
				JOptionPane.OK_CANCEL_OPTION);
		if (action < 0) {
			JOptionPane.showMessageDialog(null,
					"Cancel, X or escape key selected");
			System.exit(0);
		} else {
			password = new String(pwd.getPassword());
		}

		// Set Property with username and password for authentication
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		// Step 2: Establish a mail session (java.mail.Session)
		Session session = Session.getDefaultInstance(props);

		// We need to get Store from mail session
		// A store needs to connect to the IMAP server
		store = session.getStore("imaps");
		store.connect("imap.googlemail.com", username, password);

		// Choose folder, in this case, we chose your inbox
		folder = (IMAPFolder) store.getFolder("inbox");
	}

	public void sendEmail(String receiver, String subject, String content,
			ArrayList<File> attachments) {
		try {
			// Step 3: Create a message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(receiver));
			message.setSubject(subject);
			MimeBodyPart messageBody = new MimeBodyPart();
			MimeMultipart multipart = new MimeMultipart();
			messageBody.setText(content);
			multipart.addBodyPart(messageBody);
			if (attachments.size() > 0) 
			{
				for (File f : attachments) {
					String filename = f.getPath();
					DataSource sauce = new FileDataSource(filename);
					messageBody.setDataHandler(new DataHandler(sauce));
					messageBody.setFileName(filename);
					multipart.addBodyPart(messageBody);
				}
			}
			message.setContent(multipart);
			message.saveChanges();

			// Step 4: Send the message by javax.mail.Transport .
			Transport tr = session.getTransport("smtp"); // Get Transport object
															// from session
			tr.connect(smtphost, username, password); // We need to connect
			tr.sendMessage(message, message.getAllRecipients()); // Send message

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public Message[] getMail() throws MessagingException {
		if (!folder.isOpen()) {
			folder.open(Folder.READ_WRITE);
		}
		Message[] mailbox = new Message[folder.getMessageCount()];
		// Get total number of message
		System.out.println("No of Messages : " + folder.getMessageCount());
		// Get total number of unread message
		System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());

		try 
		{
			for (int j = 0; j < folder.getMessageCount(); j++) { 
				mailbox[j] = folder.getMessage(j + 1);
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return mailbox;
	}
	
	public void endConnection() throws MessagingException {
		if (folder != null && folder.isOpen()) { folder.close(true); }
		if (store != null) { store.close(); }
	}
}
