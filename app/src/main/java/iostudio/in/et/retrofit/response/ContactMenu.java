package iostudio.in.et.retrofit.response;

public class ContactMenu {
    private int icon;
    private String title;
    private String action;

    public ContactMenu() {
    }

    public ContactMenu(int icon, String title, String action) {
        this.icon = icon;
        this.title = title;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
