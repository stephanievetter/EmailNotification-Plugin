import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;

/**
 * The AddRemoveGUI class is a graphic user interface that allows
 * the user to modify the email notification recipients list. The
 * GUI is only avaliable in headful mode.
 *
 * @author	Stephanie Derosier
 * @version	1.0
 */
public class AddRemoveGUI {
	private JDialog dialog;
	private JTextField recip_name;
	private JTextField recip_email;
	private JTable recipList;
	private DefaultTableModel defTable;
	private JScrollPane scrollPane;
	private JTextArea nameErr;	//invalid recip name error message
	private JTextArea emailErr;	//invalid recip email error message
	private EmailNotification emailNotification; //workspace plugin instance

	/**
	 * Creates an instance of the Add/Remove GUI to add and/or
	 * remove recipients from the email notification recipient list
	 * <p>
	 * The constructor calls the method to display the GUI
	 *
	 * @param  	emailNot	workspace plugin object
	 */
	public AddRemoveGUI(final EmailNotification emailNot)
	{
		emailNotification = emailNot;
		scrollPane = new JScrollPane();
		recipList = new JTable();
		defTable = new DefaultTableModel();

		showAddRemoveGUI();
	}
	/**
	 * Creates GUI components (labels, textboxes, buttons) and calls 
	 * methods necessary to build and display Add/Remove GUI.
	 */
	public void showAddRemoveGUI()
	{ 			
		//GUI window
		dialog = new JDialog(null, Dialog.DEFAULT_MODALITY_TYPE);
		dialog.setTitle("Add/Remove");
		dialog.getContentPane().setLayout(null);
		dialog.setSize(415, 470);

		//Button to close window
		JButton btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnNewButton.setBounds(264, 381, 115, 29);
		dialog.getContentPane().add(btnNewButton);

		displayRecips();
		addRecip();
		removeRecip();

		//Error message if email address not in correct format
		emailErr = new JTextArea();
		emailErr.setText("Must enter valid address\r\n   ex: johnsmith@example.com");
		emailErr.setForeground(Color.RED);
		emailErr.setEditable(false);
		emailErr.setBackground(SystemColor.menu);
		emailErr.setBounds(133, 109, 246, 40);
		dialog.getContentPane().add(emailErr);
		emailErr.setVisible(false);

		//Error message if recip name not entered
		nameErr = new JTextArea();
		nameErr.setText("Must enter name");
		nameErr.setForeground(Color.RED);
		nameErr.setEditable(false);
		nameErr.setBackground(SystemColor.menu);
		nameErr.setBounds(133, 60, 139, 22);
		dialog.getContentPane().add(nameErr);
		nameErr.setVisible(false);

		dialog.setVisible(true);
	}
	/**
	 * Display email notification recipients in an immutable table with ability
	 * to select one entry at a time
	 */
	public void displayRecips()
	{
		//get list of recipients from plugin instance
		ArrayList<Recipient> recips = emailNotification.getRecipientList();

		//display list of recipients on email notification list in table
		scrollPane.setBounds(15, 199, 363, 173);
		dialog.getContentPane().add(scrollPane);
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
		recipList.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		recipList.getTableHeader().setBackground(Color.PINK);
		recipList.getTableHeader().setAlignmentX(JLabel.CENTER);
		recipList.setModel(defTable);
		recipList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//remove all table entries and display updated list of recipients
		recipList.removeAll();
		for(Recipient recip : recips)
		{	
			defTable.addRow(new Object[] {recip.getToName(), recip.getToEmailAddress()});
		}
	}
	/**
	 * Add a new recipient to the email recipient list. A name and valid
	 * email address must be entered to add recipient to list. The method
	 * doesn't check for duplicate entries.
	 */
	public void addRecip()
	{
		//section to add recipient to notification list
		JLabel lblAddRecipient = new JLabel("Add Recipient");
		lblAddRecipient.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblAddRecipient.setBounds(15, -11, 121, 42);
		dialog.getContentPane().add(lblAddRecipient);

		//section to enter new recipient's name
		JLabel lblRecipientName = new JLabel("Recipient Name:");
		lblRecipientName.setBounds(15, 38, 121, 20);
		dialog.getContentPane().add(lblRecipientName);

		recip_name = new JTextField();
		recip_name.setBounds(133, 35, 246, 26);
		dialog.getContentPane().add(recip_name);
		recip_name.setColumns(10);

		//section to enter new recipient's email
		JLabel lblRecipientEmail = new JLabel("Recipient Email:");
		lblRecipientEmail.setBounds(15, 85, 121, 20);
		dialog.getContentPane().add(lblRecipientEmail);

		recip_email = new JTextField();
		recip_email.setBounds(133, 82, 246, 26);
		dialog.getContentPane().add(recip_email);
		recip_email.setColumns(10);

		//button to add recipient to list
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if name not entered, display name error message
				if(recip_name.getText().length() == 0)
					nameErr.setVisible(true);
				else
					nameErr.setVisible(false);

				//regular expression to check for valid email format
				String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";		
				//if email address not correct format, display email error message
				if(!recip_email.getText().matches(EMAIL_REGEX))
					emailErr.setVisible(true);
				else 
					emailErr.setVisible(false);

				//if both name and email errors are invisible, add recipient to list
				if(!nameErr.isVisible() && !emailErr.isVisible())
				{
					Recipient recip = new Recipient(recip_name.getText(), recip_email.getText());
					emailNotification.addRecipient(recip);
					recip_name.setText("");
					recip_email.setText("");
					displayRecips();
				}
			}
		});
		btnAdd.setBounds(264, 149, 115, 29);
		dialog.getContentPane().add(btnAdd);
	}
	/**
	 * Remove a selected recipient from email recipient list. Only one 
	 * recipient may be selected and removed at a time. 
	 */
	public void removeRecip()
	{
		//Section to remove a recipient from notification list
		JLabel lblRemoveRecipient = new JLabel("Remove Recipient");
		lblRemoveRecipient.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRemoveRecipient.setBounds(15, 176, 161, 20);
		dialog.getContentPane().add(lblRemoveRecipient);

		//Button to remove selected recipient from list
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int deleteIndex;
				//if there are recipients in the table and row selected
				if(recipList.getRowCount() > 0 && (deleteIndex = recipList.getSelectedRow()) >= 0)
				{
					emailNotification.removeRecipient(new Recipient
							(recipList.getValueAt(deleteIndex, 0).toString(), 
									recipList.getValueAt(deleteIndex, 1).toString()));

					displayRecips();
				}
			}
		});
		btnRemove.setBounds(134, 381, 115, 29);
		dialog.getContentPane().add(btnRemove);
	}
}
