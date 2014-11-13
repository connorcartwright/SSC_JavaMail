package javaMail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.io.File;
import java.io.IOException;

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
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
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
	private HashMap<Flags, String> extraFlags = new HashMap<Flags, String>();

	/**
	 * Creates a new EmailClient object which is capable of connecting to an email (setUpUser),
	 * sending an email (sendEmail), getting all of the email from the email (getMail), ending
	 * the connection (endConnection), normalising flags, reading the content of an email and 
	 * adding custom flags.
	 * 
	 * @throws MessagingException
	 */
	public EmailClient() throws MessagingException {
		setUpUser();
	}

	/**
	 * This method sets up a new user using the email that has been hardcoded, and asks the user 
	 * to input the password for this email.
	 * @throws MessagingException
	 */
	public void setUpUser() throws MessagingException {
		folder = null;
		store = null;
		username = "sscjavamail@gmail.com";
		password = "";
		smtphost = "smtp.gmail.com";

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
		session = Session.getDefaultInstance(props);

		// We need to get Store from mail session
		// A store needs to connect to the IMAP server
		store = session.getStore("imaps");
		store.connect("imap.googlemail.com", username, password);

		// Choose folder, in this case, we chose your inbox
		folder = (IMAPFolder) store.getFolder("inbox");
	}

	/**
	 * This method is used to send an email using the parameters and the methods; it allows the user
	 * to include attachments.
	 * 
	 * @param receiver - the person we want to send the email to
	 * @param cc - anyone we want to cc the email to
	 * @param bcc - anyone we want to bcc the email to
	 * @param subject - the subject of the email
	 * @param content - the content of the email
	 * @param attachments - the attachments (if there are any)
	 */
	public void sendEmail(String receiver, String cc, String bcc, String subject, String content, ArrayList<File> attachments) {
		try {
			// Step 3: Create a message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiver));
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc));
			message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc));
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
					MimeBodyPart messageBodyA = new MimeBodyPart();
					messageBodyA.setDataHandler(new DataHandler(sauce));
					messageBodyA.setFileName(filename);
					multipart.addBodyPart(messageBodyA);
				}
			}
			message.setContent(multipart);
			message.saveChanges();

			// Step 4: Send the message by javax.mail.Transport .
			Transport tr = session.getTransport("smtp"); // Get Transport object from session
			tr.connect(smtphost, username, password); // We need to connect
			tr.sendMessage(message, message.getAllRecipients()); // Send message

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method returns all of the Messages inside the mailbox of the email we are connected
	 * to.
	 * @return An Array of Messages from the email we are connected to.
	 * @throws MessagingException
	 */
	public Message[] getMail() throws MessagingException {
		if (!folder.isOpen()) {
			folder.open(Folder.READ_WRITE);
		}
		Message[] mailbox = new Message[folder.getMessageCount()];
		try 
		{
			for (int j = 0; j < folder.getMessageCount(); j++) { 
				mailbox[j] = folder.getMessage(j + 1);
			}
		} 
		catch (NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		return mailbox;
	}
	
	/**
	 * Ends the connection to the email.
	 * @throws MessagingException
	 */
	public void endConnection() throws MessagingException {
		if (folder != null && folder.isOpen()) { folder.close(true); }
		if (store != null) { store.close(); }
	}
	
	/**
	 * Sets all the flags ready for display in the GUI.
	 * @param m - the message in question that we want to set the readable flag for.
	 * @return - the string of the readable flag
	 * @throws MessagingException
	 */
	public String normaliseFlags(Message m) throws MessagingException {
		for(HashMap.Entry<Flags, String> entry : extraFlags.entrySet()) {
			if(m.getFlags().contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		
		if(m.getFlags().contains(Flags.Flag.ANSWERED)) {
			return "Answered";
		}
		else if(m.getFlags().contains(Flags.Flag.SEEN)) {
			return "Seen";
		}
		else if(m.getFlags().contains(Flags.Flag.RECENT)) {
			return "Recent";
		}
		else if(m.getFlags().contains(Flags.Flag.DELETED)) {
			return "Deleted";
		}
		else if(m.getFlags().contains(Flags.Flag.DRAFT)) {
			return "Draft";
		}
		else if(m.getFlags().contains(Flags.Flag.USER)) {
			return "User";
		}
		else if(m.getFlags().contains(Flags.Flag.FLAGGED)) {
			return "Flagged";
		}
		else 
		{
			return "Unread";
		}
		
	}

	/**
	 * This method reads the body of an email and returns it in String form; this is used
	 * for the DisplaySingleEmail class so that the user can view emails including their content.
	 * 
	 * @param m - the message we want to read the content of
	 * @return A string value of the content of the email
	 * @throws IOException
	 * @throws MessagingException
	 */
	public String readBody(Part m) throws IOException, MessagingException {
		if(m.getContentType().contains("TEXT/PLAIN")) 
		{
			return m.getContent().toString();
		}
		else if (m.getContent() instanceof Multipart)
		{
			// How to get parts from multiple body parts of MIME message
			Multipart multipart = (Multipart) m.getContent();
			for (int x = 0; x < multipart.getCount(); x++) {
				BodyPart bodyPart = multipart.getBodyPart(x);
				// If the part is a plan text message, then print it out.
				if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
				{
					return bodyPart.getContent().toString();
				}

			}
		}
		return "";
	}

	/**
	 * This method allows us to add in custom user-defined flags into our email, so that the GUI
	 * can display them.
	 * 
	 * @param flag - the custom flag that we want to add
	 * @param keywords - the keywords that we want to be associated with the new custom flag
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void addCustomFlags(String flag, String keywords) throws MessagingException, IOException {
		String[] keywordsArray = keywords.replaceAll("\\s+","").split(";");
		Flags f = new Flags(flag);
		extraFlags.put(f, flag);
		
		for(int i = 0; i < getMail().length; i++) {
			for(int j = 0; j < keywordsArray.length; j++) {
				if(getMail()[i].getSubject().contains(keywordsArray[j])) {
					getMail()[i].setFlags(f, true);
					normaliseFlags(getMail()[i]);
				}
				else if(readBody(getMail()[i]).contains(keywordsArray[j])) {
					getMail()[i].setFlags(f, true);
					normaliseFlags(getMail()[i]);
				}
			}
		}
	}
}
