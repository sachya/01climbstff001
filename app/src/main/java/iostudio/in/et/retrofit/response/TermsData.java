package iostudio.in.et.retrofit.response;

import java.util.ArrayList;

public class TermsData {

    private String privacy;
    private String title;
    private String description;
    private ArrayList<String> points;

    private ArrayList<TermsData> terms;

    public ArrayList<TermsData> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<TermsData> terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public ArrayList<String> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
