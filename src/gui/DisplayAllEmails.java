package gui;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javaMail.EmailClient;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class DisplayAllEmails extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Creates a new DisplayAllEmails object which is used to produce a GUI window
	 * which will allow the user to view all of their emails in an easy to view window.
	 * The user will be able to scroll through their emails, as well as double click in 
	 * order to see an emails content.
	 * 
	 * @param client - the email client used that produces the messages to display.
	 * @throws MessagingException
	 */
	public DisplayAllEmails(final EmailClient client) throws MessagingException {
		Message[] emails = client.getMail();
		String[] col =  {"Subject", "From", "Flag" }; // setting the column titles
		DefaultTableModel defTable = new DefaultTableModel(col, 0); // pass in the column titles, and set the default row count to 0
		
		setTitle("Emails");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 944, 478);
		setResizable(false); // making it so that the frame cannot be resized, making the preferred size the fixed size.
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable(defTable);
		table.setBounds(10, 11, 414, 240);
		
		for(Message m : emails) {
			Object[] o = { m.getSubject(), InternetAddress.toString(m.getFrom()), client.normaliseFlags(m) };
			defTable.insertRow(table.getRowCount(), o);
		}
		table.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				JTable source = (JTable) e.getSource();
				Point point = e.getPoint();
				int rowNo = source.rowAtPoint(point);
				if (e.getClickCount() == 2) {
					Object body = null;
					try {
						body = client.readBody(client.getMail()[rowNo]);
						client.getMail()[rowNo].setFlag(Flags.Flag.SEEN, true);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
					DisplaySingleEmail showEmail = new DisplaySingleEmail(client, ""+ table.getModel().getValueAt(rowNo, 1), ""+ table.getModel().getValueAt(rowNo, 0), ""+body);
					showEmail.setVisible(true);
					dispose();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setViewportBorder(new LineBorder(SystemColor.textHighlight));
		scrollPane.setBounds(10, 51, 908, 378);
		contentPane.add(scrollPane);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(839, 11, 79, 29);
		contentPane.add(btnClose);
		
		btnClose.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent ae) {
				 dispose();
				 }
			 });
	}
}
