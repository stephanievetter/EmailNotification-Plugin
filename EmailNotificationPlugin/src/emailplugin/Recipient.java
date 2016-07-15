package emailplugin;

/**
 * The Recipient class constructs a Recipient object that contains the 
 * name and email address of an email recipient.
 *
 * @author	Stephanie Derosier
 * @version	1.0
 */
public class Recipient {

	private String toEmailAddress;
	private String toName;

	/**
	 * Creates a recipient object with name and email address
	 * 
	 * @param	name	Name of recipient
	 * @param	email	Email address of recipient
	 */
	public Recipient(String name, String email)
	{
		toName = name;
		toEmailAddress = email;
	}
	/**
	 * Gets recipient's name
	 *
	 * @return	Name of recipient as String
	 */
	public String getToName() {
		return toName;
	}
	/**
	 * Gets recipient's email address
	 *
	 * @return	Email address of recipient as String
	 */	
	public String getToEmailAddress() {
		return toEmailAddress;
	}
}
