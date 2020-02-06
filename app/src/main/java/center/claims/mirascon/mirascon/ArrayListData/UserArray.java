package center.claims.mirascon.mirascon.ArrayListData;

import java.util.ArrayList;
import java.util.List;

public class UserArray {
    ArrayList<String> emailArray = new ArrayList<>();
    public UserArray() {
            emailArray.add(0, "e@mail.de");
        }

    public List<String> getEmailArray() {
        return emailArray;
    }

    public void setEmailArray(ArrayList<String> emailArray) {
        this.emailArray = emailArray;
    }
}
