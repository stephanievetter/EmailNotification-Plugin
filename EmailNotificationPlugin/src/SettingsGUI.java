import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SettingsGUI {
	private JFrame frame;
	private JTextField sender_name;
	private JTextField sender_email;
	private JPasswordField sender_password;
	private JTextField recip_name;
	private JTextField recip_email;
//	private String senderName;
//	private String senderEmail;
//	private String senderPassword;
//	private String recipName;
//	private String recipEmail;

	public SettingsGUI()
	{ 
		frame = new JFrame("Email Notification Settings");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(456, 374);
		frame.getContentPane().setLayout(null);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				senderName = sender_name.getText();
//				senderEmail = sender_email.getText();
//				senderPassword = String.valueOf(sender_password.getPassword());
//				recipName = recip_name.getText();
//				recipEmail = recip_email.getText();

				SendEmail send = new SendEmail(sender_email.getText(), String.valueOf(sender_password.getPassword()), recip_email.getText());
				send.Send();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnSend.setBounds(172, 278, 115, 29);
		frame.getContentPane().add(btnSend);
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.setBounds(302, 278, 115, 29);
		frame.getContentPane().add(btnCancel);
		
		JLabel lblSenderName = new JLabel("Sender Name:");
		lblSenderName.setBounds(55, 27, 126, 20);
		frame.getContentPane().add(lblSenderName);

		JLabel lblSenderEmail = new JLabel("Sender Email:");
		lblSenderEmail.setBounds(55, 68, 115, 20);
		frame.getContentPane().add(lblSenderEmail);

		JLabel lblSenderPassword = new JLabel("Sender Password:");
		lblSenderPassword.setBounds(33, 116, 137, 20);
		frame.getContentPane().add(lblSenderPassword);

		JLabel lblRecipientName = new JLabel("Recipient Name:");
		lblRecipientName.setBounds(43, 160, 126, 20);
		frame.getContentPane().add(lblRecipientName);

		JLabel lblRecipientEmail = new JLabel("Recipient Email:");
		lblRecipientEmail.setBounds(44, 215, 126, 20);
		frame.getContentPane().add(lblRecipientEmail);

		sender_name = new JTextField();
		sender_name.setBounds(162, 24, 257, 26);
		frame.getContentPane().add(sender_name);
		sender_name.setColumns(10);

		sender_email = new JTextField();
		sender_email.setBounds(162, 65, 257, 26);
		frame.getContentPane().add(sender_email);
		sender_email.setColumns(10);

		sender_password = new JPasswordField();
		sender_password.setBounds(162, 113, 257, 26);
		frame.getContentPane().add(sender_password);
		sender_password.setColumns(10);
		
		recip_name = new JTextField();
		recip_name.setBounds(162, 157, 257, 26);
		frame.getContentPane().add(recip_name);
		recip_name.setColumns(10);

		recip_email = new JTextField();
		recip_email.setBounds(162, 212, 247, 26);
		frame.getContentPane().add(recip_email);
		recip_email.setColumns(10);
		
		frame.setVisible(true);
	}
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		SettingsGUI gui = new SettingsGUI();
	}
}