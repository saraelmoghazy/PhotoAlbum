package challenge.app.com.photoalbum.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import challenge.app.com.photoalbum.R;
import challenge.app.com.photoalbum.model.HeaderItem;
import challenge.app.com.photoalbum.model.ListItem;
import challenge.app.com.photoalbum.model.Photo;

/**
 * Photo List Adapter , Load listItems by Glide API (Loading & caching)  , Categorized by Album ID and filter listItems by title.
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class PhotoAdapter extends RecyclerView.Adapter {
    private static final String TAG = PhotoAdapter.class.getSimpleName();
    Context context;
    List<ListItem> listItems;
    List<ListItem> filterListItem = null;


    public PhotoAdapter(Context context, List<ListItem> _listItems) {
        this.context = context;
        this.filterListItem = _listItems;
        this.listItems = new ArrayList<>();
        listItems.addAll(filterListItem);
    }


    @Override
    public int getItemViewType(int position) {
        return filterListItem.get(position).getType();
    }

    /**
     * Create Item view from from XML file depending on Item type (Header/Photo)
     *
     * @param parent
     * @param viewType
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            RecyclerView.ViewHolder viewHolder;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            viewHolder = new HeaderHolder(v);
            return viewHolder;

        } else {
            RecyclerView.ViewHolder viewHolder;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            viewHolder = new PhotoHolder(v);
            return viewHolder;
        }


    }


    /**
     * Called per each ItemList item to fill view either with Header / Photo
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        // if the item type header then use  HeaderHolder and fill with Album Id
        if (type == ListItem.TYPE_HEADER) {
            HeaderItem header = (HeaderItem) filterListItem.get(position);
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.header.setText(header.getAlbumId() + "");
        } else {

            // if the item type Photo then use PhotoHolder and fill with photo
            Photo event = (Photo) filterListItem.get(position);
            PhotoHolder photoHolder = (PhotoHolder) holder;
            Log.i(TAG, "" + position);
            // Load Thumbnail Url
            Glide.with(context).load((event).getThumbnailUrl())
                    .thumbnail(0.5f)
                    .override(200, 100)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((photoHolder).photoItem);
        }
    }


    /**
     * get photo list size
     */
    @Override
    public int getItemCount() {
        return filterListItem.size();
    }


    public static class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView photoItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            photoItem = (ImageView) itemView.findViewById(R.id.photoItem);
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
        }
    }


    /**
     * Filter photo list by title
     *
     * @param query
     */
    public void filter(String query) {
        filterListItem.clear();
        //  if empty query return full list
        if (query.length() == 0) {
            filterListItem.addAll(listItems);
        } else {
            for (ListItem photo : listItems) {
                // add header
                if (photo instanceof HeaderItem) {
                    filterListItem.add(photo);
                }
                // filter listItems
                else if (photo instanceof Photo) {
                    if (((Photo) photo).getTitle().toLowerCase()
                            .contains(query)) {
                        filterListItem.add(photo);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void reset() {
        filterListItem = new ArrayList<ListItem>();
        filterListItem.addAll(listItems);
        notifyDataSetChanged();
    }

    public Photo getItem(int position) {
        return (Photo) filterListItem.get(position);
    }
}
