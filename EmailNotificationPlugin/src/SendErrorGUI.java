import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/**
 * The SendErrorGUI class handles exceptions thrown by the
 * javax.mail API when an email fails to send. This class allows
 * the user to decide how to handle the exception. The GUI is
 * only available in headful mode.
 *
 * @author	Stephanie Derosier
 * @version	1.0
 */
public class SendErrorGUI {

	private JDialog dialog;
	private JTextArea errDescrip;
	private JButton btnResend;
	private JButton btnNewButton;

	/**
	 * Creates an instance of the Error GUI when sending of email
	 * notification results in an exception.
	 * <p>
	 * The Error dialog box give the user the option to resend the
	 * email or cancel the send email request.
	 *
	 * @param	err					Error message from thrown exception
	 * @param	emailNotification	Workspace plugin instance
	 */
	public SendErrorGUI(String err, final EmailNotification emailNotification)
	{ 			
		//GUI window
		dialog = new JDialog(null, Dialog.DEFAULT_MODALITY_TYPE);
		dialog.getContentPane().setBackground(UIManager.getColor("Button.background"));
		dialog.setTitle("Send Error");
		dialog.setSize(485, 321);
		dialog.getContentPane().setLayout(null);
		
		//notification of error and description of exception
		JLabel lblUnableToSend = new JLabel("Unable to send email");
		lblUnableToSend.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblUnableToSend.setBounds(64, 30, 219, 20);
		dialog.getContentPane().add(lblUnableToSend);
		
		errDescrip = new JTextArea();
		errDescrip.setBackground(UIManager.getColor("Button.background"));
		errDescrip.setBounds(39, 60, 409, 149);
		dialog.getContentPane().add(errDescrip);
		errDescrip.setColumns(10);
		errDescrip.setText(err);
		errDescrip.setLineWrap(true);
		errDescrip.setWrapStyleWord(true);
		
		//button to try resending email notification
		btnResend = new JButton("Resend");
		btnResend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emailNotification.sendEmail();
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnResend.setBounds(218, 225, 115, 29);
		dialog.getContentPane().add(btnResend);
		
		//button to cancel send request and exit plugin
		btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnNewButton.setBounds(343, 225, 115, 29);
		dialog.getContentPane().add(btnNewButton);
		
		dialog.setVisible(true);
	}
}
