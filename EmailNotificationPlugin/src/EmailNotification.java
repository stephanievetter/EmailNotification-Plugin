import java.io.File;
import java.util.ArrayList;

//import com.treestar.flowjo.engine.EngineManager;
import com.treestar.lib.PluginHelper;
import com.treestar.lib.application.ApplicationInfo;
import com.treestar.lib.core.WorkspacePluginInterface;
import com.treestar.lib.fjml.types.FileTypes;
import com.treestar.lib.prefs.HomeEnv;
import com.treestar.lib.xml.SElement;
import com.treestar.lib.xml.XMLUtil;

/**
 * The EmailNotification class implements the WorkspacePluginInterface
 * and handles retrieving and saving plugin preferences to an XML file
 * and creating a SendEmail object to send email notifications. This class
 * also handles both headless and headful modes.
 *
 * @author	Stephanie Derosier
 * @version	1.0
 */
public class EmailNotification implements WorkspacePluginInterface
{	
	private SettingsGUI gui;		//instance of SettingsGUI (headful mode only)
	private SElement workspace;		//SElement workspace object
	private SElement emailSettings;	//SElement that contains plugin settings
	private String sender_name;
	private String sender_email;
	private String sender_password;
	private String personal_message = "";
	private ArrayList<Recipient> recip_list = new ArrayList<Recipient>();
	private String xml_filename = "EmailNotificationPrefs";	//name of prefs file for plugin

	/**
	 * Method called when FlowJo workspace is saved. 
	 * <p>
	 * If headful mode, the SettingsGUI is instantiated and appears to 
	 * modify email notification settings and indicate next action 
	 * (send email, save settings, do nothing).
	 * <p>
	 * If headless mode, the SettingsGUI isn't instantiated. Emails
	 * are sent using settings from plugin preferences file. 
	 * 
	 * @param	workspaceElement	SElement workspace object
	 */
	@Override
	public void save(SElement workspaceElement) {
		workspace = workspaceElement;
		readSettings();	//read prefs settings

		//check if running in headless or headful mode
		if(ApplicationInfo.isHeadless())
		{
			sendEmail();
		}
		else
		{
			gui = new SettingsGUI(this);	
			initializeGUI();
			gui.ShowGUI();
		}
	}
	/**
	 * Method writes the plugin preferences to an SElement. 
	 * 
	 * @return  				SElement object containing plugin settings
	 * @exception	Exception	Throw exception if error encrypting password
	 */
	@Override
	public SElement getElement() {
		emailSettings = new SElement("EmailNotification");

		//if headful mode
		if(gui != null)
		{
			//write sender information to prefs file
			SElement temp = new SElement("Sender");
			temp.setString("SenderName", gui.getSender_name());
			temp.setString("SenderEmail", gui.getSender_email());

			//encrypt password before writing to prefs fiile
			PasswordSecurity encrypt = new PasswordSecurity();
			String encryptedPassword = null;
			try {
				encryptedPassword = encrypt.encrypt(gui.getSender_password());
			}catch(Exception err) {}
			temp.setString("SenderPassword", encryptedPassword);

			emailSettings.addContent(temp);

			//write recipient list to prefs file
			temp = new SElement("RecipientsList");
			temp.setString("ListCount", String.valueOf(recip_list.size()));
			emailSettings.addContent(temp);
			int counter = 0;
			for(Recipient recip : recip_list)
			{
				temp = new SElement("Recipient" + String.valueOf(counter));
				temp.setString("RecipName", recip.getToName());
				temp.setString("RecipEmail", recip.getToEmailAddress());
				emailSettings.addContent(temp);
				counter++;
			}

			//write personal email message to prefs file
			temp = new SElement("PersonalMessage");
			temp.setString("PersonalMessage", gui.getMessagebox());
			emailSettings.addContent(temp);
		}
		return emailSettings;
	}
	/**
	 * Write SElement that is plugin preferences to an XML file.
	 */	
	public void writeSettings() {
		//create file to save plugin pref settings
		File prefsFile = new File(HomeEnv.getInstance().getUserPrefsFolder(), xml_filename + FileTypes.XML_SUFFIX);
		
		//File prefsFile = new File(EngineManager.getInstance().getPrefsFile().getParentFile(), xml_filename + FileTypes.XML_SUFFIX);
		//System.out.println(prefsFile.getAbsolutePath());
		
		//write plugin preferences SElement to XML file
		SElement out = getElement();
		new XMLUtil().write(out, prefsFile);

		//read updates settings from prefs file
		readSettings();
	}
	/**
	 * Read email notification preferences from prefs.xml file.
	 * <p> 
	 * If the pref file exists in the specified location, settings will be read
	 * and stored in class data members.
	 * If the prefs file doesn't exist in the specified location, no action taken.
	 * 
	 * @exception	Exception	Throws exception if error decrypting password
	 */
	public void readSettings()
	{
		//look for prefs.xml file in prefs folder
		File prefsFile = new File(HomeEnv.getInstance().getUserPrefsFolder(), xml_filename + FileTypes.XML_SUFFIX);

		//File prefsFile = new File(EngineManager.getInstance().getPrefsFile().getParentFile(), xml_filename + FileTypes.XML_SUFFIX);
		//System.out.println(prefsFile.getAbsolutePath());
		
		//read the prefs file if it exists
		if(prefsFile.exists())
		{
			SElement emailSettings = new XMLUtil().fileToElement(prefsFile);
			PasswordSecurity decrypt = new PasswordSecurity();

			//get sender information from prefs file
			SElement temp = emailSettings.getChild("Sender");
			sender_name = temp.getString("SenderName");
			sender_email = temp.getString("SenderEmail");
			//decrypt password
			sender_password = temp.getString("SenderPassword");
			try {
				sender_password = decrypt.decrypt(sender_password);
			}catch(Exception err){}

			//clear recipient list and read recipient list from file
			recip_list.clear();
			temp = emailSettings.getChild("RecipientsList");
			int count = 0;
			try {
				count = Integer.parseInt(temp.getString("ListCount"));
			}catch(NumberFormatException err){}
			for(int i = 0; i < count; i++)
			{
				temp = emailSettings.getChild("Recipient" + String.valueOf(i));
				if(temp != null)
				{	
					recip_list.add(new Recipient(temp.getString("RecipName"),
							temp.getString("RecipEmail")));
				}
			}	

			//read personal message from file
			temp = emailSettings.getChild("PersonalMessage");
			personal_message = temp.getString("PersonalMessage");
		}
	}
	/**
	 * Set fields in SettingsGUI with EmailNotification data members
	 * in headful mode.
	 */
	public void initializeGUI()
	{
		gui.setSender_name(sender_name);
		gui.setSender_email(sender_email);
		gui.setSender_password(sender_password);
		gui.setMessagebox(personal_message);
	}
	/**
	 * Get name of workspace.
	 * 
	 * @return	Return name of workspace as String
	 */	
	public String getWorkspaceName()
	{
		return PluginHelper.getWorkspaceName(workspace);
	}
	/**
	 * Add a Recipient object to recipient list
	 * 
	 * @param	recip	Recipient object containing name 
	 * 					and email address of new recipient
	 */	
	public void addRecipient(Recipient recip)
	{
		recip_list.add(recip);
	}
	/**
	 * Remove a Recipient object from recipient list.
	 * Checks for name and email address of recipient to remove 
	 * from list.
	 * 
	 * @param	recip	Recipient object containing name and 
	 * 					email address of recipient to remove
	 */	
	public void removeRecipient(Recipient recip)
	{
		boolean found = false;

		for(int i = 0; i < recip_list.size(); i++)
		{
			if(recip_list.get(i).getToEmailAddress().equals(recip.getToEmailAddress()) && 
					recip_list.get(i).getToName().equals(recip.getToName()) && found == false)
			{
				recip_list.remove(i);
				found = true;
			}
		}
	}
	/**
	 * Get list of email notification recipients.
	 * 
	 * @return	List of recipients as ArrayList of Recipient objects
	 */	
	public ArrayList<Recipient> getRecipientList()
	{
		return recip_list;
	}
	/**
	 * Create an instance of the SendEmail class and call method to send email
	 * notification.
	 * <p>
	 * SendEmail instance only created if there is at least one recipient in list,
	 * and if there is a sender email and password.
	 */	
	public void sendEmail()
	{
		//call method to send email if at least one recipient, and have 
		//sender email and password
		if(recip_list.size() > 0 && sender_email.length() > 0 && sender_password.length() > 0)
		{
			SendEmail send = new SendEmail(this, sender_email, sender_password, 
					recip_list, personal_message);
			send.Send();
		}
	}
	/**
	 * Return ServerUrl for Email Notification Plugin.
	 * 
	 * @return	Server url of plugin as String
	 */
	@Override
	public String getServerUrl() {
		return "http://localhost:8080/EmailNotification";
	}
	/**
	 * Return version of Email Notification Plugin.
	 * 
	 * @return	Version of plugin as String
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}
	/**
	 * Method called when workspace is opened.
	 * 
	 * @param	workspaceElement	Workspace SElement object		
	 * @return						Boolean value indicating if plugin should be add
	 * 								to the workspace for subsequent operations
	 */	
	@Override
	public boolean openWorkspace(SElement workspaceElement) {		
		return true;
	}
	/**
	 * Method called when FlowJo session ends
	 */	
	@Override
	public void endSession() {
	}
}