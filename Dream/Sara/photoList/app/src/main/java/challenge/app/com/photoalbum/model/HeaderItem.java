package challenge.app.com.photoalbum.model;

/**
 * Header Item
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class HeaderItem extends ListItem {

    private int albumId;


    @Override
    public int getType() {
        return ListItem.TYPE_HEADER;
    }


    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
