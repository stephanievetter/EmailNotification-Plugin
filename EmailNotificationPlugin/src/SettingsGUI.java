import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.SystemColor;
import java.awt.Font;

/**
 * The SettingsGUI class allows the user to modify the plugin
 * preference settings. The GUI is only available in headful mode.
 *
 * @author	Stephanie Derosier
 * @version	1.0
 */
public class SettingsGUI {
	private JDialog dialog;
	private JTextField sender_name = new JTextField();
	private JTextField sender_email = new JTextField();
	private JPasswordField sender_password = new JPasswordField();
	private JTextArea messageBox = new JTextArea();
	private JCheckBox cc_checkbox = new JCheckBox("Cc to sender");
	private JTable recipList;
	private JScrollPane scrollPane;
	private DefaultTableModel defTable;
	private JTextArea nameErr;					 //invalid name error message
	private JTextArea emailErr;					 //invalid email error message
	private JTextArea passwordErr;				 //invalid password error message
	private String emailExt = "@gmail.com";		 //supported sender email extension
	private EmailNotification emailNotification; //workspace plugin instance

	/**
	 * Creates an instance of the Email Notification settings GUI that is used
	 * to set/change sender's information. The Settings GUI is only used in 
	 * headful mode.
	 *
	 * @param  	emailNot	Workspace plugin object
	 */
	public SettingsGUI(final EmailNotification emailNot){
		emailNotification = emailNot;
		recipList = new JTable();
		scrollPane = new JScrollPane();
	}	
	/**
	 * Creates GUI components (labels, textboxes, buttons) and calls 
	 * methods necessary to build and display Settings GUI.
	 */
	public void showGUI(){ 			
		//GUI window
		dialog = new JDialog(null, Dialog.DEFAULT_MODALITY_TYPE);
		dialog.setTitle("Email Notification Settings");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(456, 640);
		dialog.getContentPane().setLayout(null);
		
		//optional personal message
		JLabel lblOptionalMessage = new JLabel("Optional: Personal Message");
		lblOptionalMessage.setBounds(15, 444, 208, 20);
		dialog.getContentPane().add(lblOptionalMessage);
		
		messageBox.setWrapStyleWord(true);
		JScrollPane scrollP = new JScrollPane();
		scrollP.setBounds(15, 464, 386, 66);
		dialog.getContentPane().add(scrollP);
		scrollP.setViewportView(messageBox);
		
		//button to save settings to prefs.xml file and send email notifications
		JButton btnSend = new JButton("Save/Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if sender valid, call method to send email, then close window
				if(validateSender())
				{
					emailNotification.writeSettings();
					emailNotification.sendEmail();
					dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		btnSend.setBounds(92, 539, 100, 29);
		dialog.getContentPane().add(btnSend);

		//button to exit GUI without saving to prefs.xml file or sending email notifications
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.setBounds(303, 539, 100, 29);
		dialog.getContentPane().add(btnCancel);

		//button to save settings to prefs.xml file without sending email
		JButton btnSave = new JButton("Save Only");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if sender valid, save settings to prefs file, then close window 
				if(validateSender())
				{
					emailNotification.writeSettings();
					dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		btnSave.setBounds(197, 539, 100, 29);
		dialog.getContentPane().add(btnSave);

		//button to pop up GUI to add and/or remove recipients from list
		JButton btnAddremove = new JButton("Add/Remove");
		btnAddremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddRemoveGUI(emailNotification);

				displayRecipients();
			}
		});
		btnAddremove.setBounds(277, 405, 126, 29);
		dialog.getContentPane().add(btnAddremove);

		sender();
		displayRecipients();
		
		//error message for empty sender name field
		nameErr = new JTextArea();
		nameErr.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		nameErr.setBackground(UIManager.getColor("Button.background"));
		nameErr.setEditable(false);
		nameErr.setText("Must enter name");
		nameErr.setForeground(Color.RED);
		nameErr.setBounds(146, 51, 139, 22);
		dialog.getContentPane().add(nameErr);
		nameErr.setVisible(false);
		
		//error message for incorrectly formatted email address
		emailErr = new JTextArea();
		emailErr.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		emailErr.setBackground(UIManager.getColor("Button.background"));
		emailErr.setEditable(false);
		emailErr.setText("Invalid Email");
		emailErr.setForeground(Color.RED);
		emailErr.setBounds(146, 99, 115, 22);
		dialog.getContentPane().add(emailErr);
		emailErr.setVisible(false);

		//error message for incorrect password (may not be able to use this)
		passwordErr = new JTextArea();
		passwordErr.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		passwordErr.setText("Invalid Password");
		passwordErr.setForeground(Color.RED);
		passwordErr.setEditable(false);
		passwordErr.setBackground(SystemColor.menu);
		passwordErr.setBounds(146, 147, 139, 20);
		dialog.getContentPane().add(passwordErr);
		passwordErr.setVisible(false);
		
		dialog.setVisible(true);
	}
	/**
	 * Creates components of Settings GUI for sender section
	 */
	public void sender()
	{
		//section to enter sender's name
		JLabel lblSenderName = new JLabel("Sender Name:");
		lblSenderName.setBounds(38, 27, 126, 20);
		dialog.getContentPane().add(lblSenderName);

		sender_name.setBounds(146, 24, 257, 26);
		dialog.getContentPane().add(sender_name);
		sender_name.setColumns(10);
		sender_name.setText(getSender_name());

		//section to enter sender's email
		JLabel lblSenderEmail = new JLabel("Sender Email:");
		lblSenderEmail.setBounds(38, 79, 115, 20);
		dialog.getContentPane().add(lblSenderEmail);

		sender_email.setBounds(146, 76, 153, 26);
		dialog.getContentPane().add(sender_email);
		sender_email.setColumns(10);
		sender_email.setText(getSender_email().replaceAll("@gmail.com", ""));
		
		//email extension label supported by plugin
		JLabel lblgmailcom = new JLabel("@gmail.com");
		lblgmailcom.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblgmailcom.setBounds(303, 66, 115, 37);
		dialog.getContentPane().add(lblgmailcom);

		//section to enter sender's password
		JLabel lblSenderPassword = new JLabel("Sender Password:");
		lblSenderPassword.setBounds(15, 124, 137, 20);
		dialog.getContentPane().add(lblSenderPassword);

		sender_password.setBounds(146, 121, 257, 26);
		dialog.getContentPane().add(sender_password);
		sender_password.setColumns(10);
		sender_password.setText(getSender_password());

		//check box to add sender to email list
		cc_checkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add sender to recipient list if valid and checked
				if(cc_checkbox.isSelected() && validateSender())
				{
					emailNotification.addRecipient(new Recipient(getSender_name(), 
							getSender_email()));
					
					displayRecipients();
				}
				//remove sender from recipient list
				//if sender removed from Add/Remove gui, checkbox does not auto-update
				else if(!cc_checkbox.isSelected())	 
				{
					emailNotification.removeRecipient(new Recipient(getSender_name(), 
							getSender_email()));
					
					displayRecipients();
				}
			}});
		cc_checkbox.setBounds(146, 171, 139, 29);
		dialog.getContentPane().add(cc_checkbox);
	}
	/**
	 * Display email notification recipients in an immutable table
	 */
	public void displayRecipients()
	{
		//get list from plugin instance
		ArrayList<Recipient> recips = emailNotification.getRecipientList();
			
		//display list of recipients on email notification list in table
		JLabel lblRecipients = new JLabel("Recipients");
		lblRecipients.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRecipients.setBounds(32, 204, 100, 20);
		dialog.getContentPane().add(lblRecipients);

		scrollPane.setBounds(32, 227, 371, 173);
		dialog.getContentPane().add(scrollPane);
	
		recipList.setRowSelectionAllowed(false);
		scrollPane.setViewportView(recipList);
		
		String columns [] = {"Name", "Email"};
		defTable = new DefaultTableModel(0,0){
			private static final long serialVersionUID = 1L;	//default id

		//make cells immutable
		@Override
		    public boolean isCellEditable(int i, int i1) {
		        return false; 
		    }
		};
		defTable.setColumnIdentifiers(columns);
		recipList.setModel(defTable);
		recipList.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		recipList.getTableHeader().setBackground(Color.PINK);
		
		//remove all entries from table and update with current list
		recipList.removeAll();
		for(Recipient recip : recips)
		{	
			defTable.addRow(new Object[] {recip.getToName(), recip.getToEmailAddress()});
		}
	}
	/**
	 * Checks for valid sender information and returns whether information is 
	 * valid or invalid
	 * <p>
	 * Valid sender information includes a name, properly formatted email address,
	 * and a password. The method doesn't check for valid email address and password
	 * combination.
	 *
	 * @return  boolean value indicating if sender information is valid
	 */
	public boolean validateSender()
	{
		//regular expression to check for valid email address
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";		
		
		boolean valid = true;	//return if sender information is valid
		
		if(sender_name.getText().length() == 0)
			nameErr.setVisible(true);
		else
			nameErr.setVisible(false);
		
		if(!(sender_email.getText() + emailExt).matches(EMAIL_REGEX))
			emailErr.setVisible(true);
		else
			emailErr.setVisible(false);
		
		if(String.valueOf(sender_password.getPassword()).length() == 0)
			passwordErr.setVisible(true);
		else
			passwordErr.setVisible(false);
		
		//if any error messages are visible, sender info invalid
		if(emailErr.isVisible() || passwordErr.isVisible() || nameErr.isVisible())
			valid = false;
		
		return valid;
	}
	/**
	 * Gets value of sender's name.
	 *
	 * @return  Name of sender as a String 
	 */
	public String getSender_name() {
		return sender_name.getText();
	}
	/**
	 * Sets String value of sender's name.
	 *
	 * @param  	name	String that is name of sender
	 */
	public void setSender_name(String name) {
		this.sender_name.setText(name);
	}
	/**
	 * Gets value of sender's email address with extension.
	 *
	 * @return  Email address of sender as a String with
	 * 			concatenated email extension 
	 */
	public String getSender_email() {
		return sender_email.getText() + emailExt;
	}
	/**
	 * Sets String value of sender's email address.
	 *
	 * @param  	email	String that is email address of sender
	 */
	public void setSender_email(String email) {
		this.sender_email.setText(email); 
	}
	/**
	 * Gets value of sender's password.
	 *
	 * @return  Password of sender as a String 
	 */
	public String getSender_password() {
		return String.valueOf(sender_password.getPassword());
	}
	/**
	 * Sets String value of sender's password.
	 *
	 * @param  	password	String that is password of sender
	 */
	public void setSender_password(String password) {
		this.sender_password.setText(password);
	}
	/**
	 * Gets contents of personal message.
	 *
	 * @return  Personal email message as a String 
	 */
	public String getMessagebox() {
		return messageBox.getText();
	}
	/**
	 * Sets String value of optional personal message.
	 *
	 * @param  	message	String that is personal message in email
	 */
	public void setMessagebox(String message)
	{
		this.messageBox.setText(message);
	}
}