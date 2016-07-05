import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

	private Properties properties;
	private String email_message;
	private String subject;
	private String senderEmail;
	private String senderPassword;
	private String recipEmail;
	

	public SendEmail(String sEmail, String sPassword, String rEmail)
	{
		senderEmail = sEmail;
		senderPassword = sPassword;
		recipEmail = rEmail;

		properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		SetMessage();
	}

	private void SetMessage()
	{
		subject = "Ahhhh!!!!";
		email_message = "You are da bomb!!!!";
	}

	public void Send()
	{
		Session session = Session.getDefaultInstance(properties, 
				new Authenticator() { 
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipEmail));
			message.setSubject(subject);

			Multipart multipart = new MimeMultipart();

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(email_message);
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("\nSend from plugin!");
			multipart.addBodyPart(messageBodyPart);
	
			message.setContent(multipart);
			Transport.send(message);
			
		} catch(MessagingException err)
		{
		}
	}
}

