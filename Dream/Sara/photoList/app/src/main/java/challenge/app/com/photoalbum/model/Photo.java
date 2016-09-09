package challenge.app.com.photoalbum.model;

import java.io.Serializable;

/**
 * Photo Model
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class Photo extends ListItem implements Serializable {

    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;


    public Photo() {
    }

    public Photo(int _albumId, int _id, String _title, String _url, String _thumbnailUrl) {
        this.setAlbumId(_albumId);
        this.setId(_id);
        this.setTitle(_title);
        this.setUrl(_url);
        this.setThumbnailUrl(_thumbnailUrl);
    }


    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_PHOTO;
    }
}
