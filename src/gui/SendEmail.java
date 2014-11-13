package gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javaMail.EmailClient;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class SendEmail extends JFrame {

	private JPanel contentPane;
	private JTextField toInput;
	private JTextField ccInput;
	private JTextField bccInput;
	private Component errorFrame; // used when producing the error message
	private JLabel lblSubject;
	private JTextField subjectInput;
	private JLabel lblBody;
	private JTextField attachmentsInput;
	private ArrayList<File> attachments = new ArrayList<File>();

	/**
	 * Creates a new SendEmail object which is used to produce a GUI window which allows the user
	 * to send a new email to an address of their choosing. The user can select the recipient,
	 * who to cc, who to bcc, the subject, content, and any attachments of their choosing.
	 * 
	 * @param client - the EmailClient object used to send the email.
	 */
	public SendEmail(final EmailClient client) {
		setTitle("Send Email");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 673, 419);
		setResizable(false); // making it so that the frame cannot be resized, making the preferred size the fixed size.
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(10, 11, 46, 14);
		contentPane.add(lblTo);
		
		toInput = new JTextField();
		toInput.setBounds(69, 8, 551, 20);
		contentPane.add(toInput);
		toInput.setColumns(10);
		
		JLabel lblCc = new JLabel("CC:");
		lblCc.setBounds(10, 45, 46, 14);
		contentPane.add(lblCc);
		
		JLabel lblBcc = new JLabel("BCC:");
		lblBcc.setBounds(320, 45, 46, 14);
		contentPane.add(lblBcc);
		
		ccInput = new JTextField();
		ccInput.setBounds(69, 42, 223, 20);
		contentPane.add(ccInput);
		ccInput.setColumns(10);
		
		bccInput = new JTextField();
		bccInput.setBounds(376, 39, 244, 20);
		contentPane.add(bccInput);
		bccInput.setColumns(10);
		
		lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(10, 82, 46, 14);
		contentPane.add(lblSubject);
		
		subjectInput = new JTextField();
		subjectInput.setBounds(69, 79, 223, 20);
		contentPane.add(subjectInput);
		subjectInput.setColumns(10);
		
		lblBody = new JLabel("Body:");
		lblBody.setBounds(10, 123, 46, 14);
		contentPane.add(lblBody);
		
		final JTextArea bodyInput = new JTextArea();
		bodyInput.setBounds(48, 118, 599, 184);
		contentPane.add(bodyInput);
		
		JLabel lblAttachments = new JLabel("Attachments:");
		lblAttachments.setBounds(10, 320, 76, 14);
		contentPane.add(lblAttachments);
		
		attachmentsInput = new JTextField();
		attachmentsInput.setBounds(107, 315, 445, 24);
		contentPane.add(attachmentsInput);
		attachmentsInput.setColumns(10);
		
		JButton btnAddAttachment = new JButton("Add...");
		btnAddAttachment.setBounds(577, 316, 70, 23);
		contentPane.add(btnAddAttachment);
		
		btnAddAttachment.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
			        JFileChooser fileChooser = new JFileChooser();
			        int returnV = fileChooser.showOpenDialog(null);
			        if (returnV == JFileChooser.APPROVE_OPTION) {
			          File selectedFile = fileChooser.getSelectedFile();
			          attachments.add(selectedFile);
			          attachmentsInput.setText(attachmentsInput.getText() + ", " + selectedFile.getName());
			        }
			      }
			    });
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(571, 350, 76, 23);
		contentPane.add(btnSend);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(toInput.getText().length() < 1) {
					JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
						"The To input box must not be empty!",
						"No reciever specified.", JOptionPane.ERROR_MESSAGE);	
				}
				else if(! (toInput.getText().matches("[A-Za-z0-9._%+-][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3}"))) {
					JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
						"The email address of the reciever must be valid.",
						"Invalid reciever specified.", JOptionPane.ERROR_MESSAGE);	
				}
				else if (subjectInput.getText().length() < 1) {
					JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
							"The Subject input box must not be empty!",
							"No Subject specified.", JOptionPane.ERROR_MESSAGE);	
				}
				else if (bodyInput.getText().length() < 1) {
					JOptionPane.showMessageDialog(errorFrame, // then show an informative error message
							"The Body of the email must not be empty!",
							"No body specified.", JOptionPane.ERROR_MESSAGE);	
				}
				else
				{
					 client.sendEmail(toInput.getText(), ccInput.getText(), bccInput.getText(), subjectInput.getText(), bodyInput.getText(), attachments);
					 JOptionPane.showMessageDialog(errorFrame, // then show an informative message
								"Email Sent to '" + toInput.getText() + "' Successfully!",
								"Email Sent", JOptionPane.INFORMATION_MESSAGE);	
					 dispose();
				}
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(10, 350, 76, 23);
		contentPane.add(btnCancel);
		
		btnCancel.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 dispose();
				 }
			 });
	}
}
