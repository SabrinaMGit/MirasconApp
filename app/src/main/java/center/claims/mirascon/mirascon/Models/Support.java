package center.claims.mirascon.mirascon.Models;

public class Support {

    private String Title;
    private int Thumbnail;

    public Support(String Title, int Thumbnail) {
        this.Title = Title;
        this.Thumbnail = Thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
