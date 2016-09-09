package challenge.app.com.photoalbum.model;

/**
 * List Item
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_PHOTO = 1;

    abstract public int getType();
}