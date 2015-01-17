package gr.rambou.myicarus;

/**
 * Created by darknight on 1/17/2015.
 */
public class NavigationRowItem {
    String Title;
    int ImageId;

    public NavigationRowItem(String title, int imageId)
    {
        this.Title= title;
        this.ImageId= imageId;
    }

    public String getTitle(){
        return Title;
    }

    public int getImageId()
    {
        return ImageId;
    }

}
