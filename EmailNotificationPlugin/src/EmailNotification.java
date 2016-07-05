import com.treestar.lib.core.WorkspacePluginInterface;
import com.treestar.lib.xml.SElement;

public class EmailNotification implements WorkspacePluginInterface
{
	@Override
    public void save(SElement workspaceElement) {
   	 	@SuppressWarnings("unused")
		SettingsGUI gui = new SettingsGUI();
    }
   
	@Override
	public void endSession() {
	}
	@Override
	public SElement getElement() {
		SElement result = new SElement("SettingsGUI");
		return result;
	}
	@Override
	public String getServerUrl() {
		return "http://localhost:8080/EmailNotification";
	}
	@Override
	public String getVersion() {
		return "1.0";
	}
	@Override
	public boolean openWorkspace(SElement workspaceElement) {

		return true;
	}
}

