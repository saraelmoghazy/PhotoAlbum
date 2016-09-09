package challenge.app.com.photoalbum.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import challenge.app.com.photoalbum.R;
import challenge.app.com.photoalbum.model.HeaderItem;
import challenge.app.com.photoalbum.model.ListItem;
import challenge.app.com.photoalbum.model.Photo;
import challenge.app.com.photoalbum.service.DataSourceService;
import challenge.app.com.photoalbum.service.RetrofitErrorHandler;
import challenge.app.com.photoalbum.ui.adapter.PhotoAdapter;
import challenge.app.com.photoalbum.ui.listener.RecyclerItemClickListener;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Activity to display photo list , Filter Photo by title and allow user to choose one to view it's details
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class PhotoAlbumActivity extends AppCompatActivity {
    private static final String TAG = PhotoAlbumActivity.class.getSimpleName();
    private String ROOT_URL = "http://jsonplaceholder.typicode.com";
    public static final int ROW_ITEMS_NUMBER = 6;
    public static final String PHOTO_DETAILS = "photoDetails";
    PhotoAdapter pAdapter;
    RecyclerView mRecyclerView;
    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUi();
        getPhotoAlbum();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    private void initUi() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);

        // filter photos by title , get user input when user submit and pass query to adapter
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pAdapter.filter(query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                return false;
            }
        });


        // reset to full list.
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                pAdapter.reset();
            }
        });
    }


    /**
     * fetch photos from sever using Retrofit API (Async call - run in separated thread to avoid 'NetworkOnMainThreadException')
     */
    private void getPhotoAlbum() {

        // Show progress bar while loading
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).setErrorHandler(new RetrofitErrorHandler(this))
                .build();

        DataSourceService api = adapter.create(DataSourceService.class);
        api.getPhoto(new Callback<List<Photo>>() {
            @Override
            public void success(final List<Photo> photos, Response response) {
                TreeMap<Integer, List<Photo>> photoMap = new TreeMap<Integer, List<Photo>>();
                for (int i = 0; i < photos.size(); i++) {
                    if (!photoMap.containsKey(photos.get(i).getAlbumId())) {
                        List<Photo> ph = new ArrayList<>();
                        ph.add(photos.get(i));
                        photoMap.put(photos.get(i).getAlbumId(), ph);

                    } else {
                        List<Photo> mapPhotos = photoMap.get(photos.get(i).getAlbumId());
                        mapPhotos.add(photos.get(i));

                    }
                }

                fillPhotoAdapter(photoMap);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());

                RetrofitErrorHandler.handleWebServiceError(PhotoAlbumActivity.this, error);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    /**
     * fill Adapter with photos list result  , attach Adapter to Recycler View and handle item click.
     *
     * @param photosMap
     */
    private void fillPhotoAdapter(TreeMap<Integer, List<Photo>> photosMap) {

        List<ListItem> mItems = new ArrayList<>();

        for (Integer albumId : photosMap.keySet()) {
            HeaderItem header = new HeaderItem();
            header.setAlbumId(albumId);
            mItems.add(header);
            for (Photo photo : photosMap.get(albumId)) {
                mItems.add(photo);
            }


            pAdapter = new PhotoAdapter(PhotoAlbumActivity.this, mItems);
            mRecyclerView.setAdapter(pAdapter);
            //  set 6 photos per row if List item type --> header , else fill row with header.
            GridLayoutManager layoutManager = new GridLayoutManager(this, ROW_ITEMS_NUMBER);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mRecyclerView.getAdapter().getItemViewType(position) == ListItem.TYPE_HEADER)
                        // return the number of columns so the group header takes a whole row
                        return ROW_ITEMS_NUMBER;
                    // normal child item takes up 1 cell
                    return 1;
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PhotoAlbumActivity.this,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (pAdapter.getItemViewType(position) == ListItem.TYPE_HEADER) return;

                            Photo photo = pAdapter.getItem(position);
                            Intent intent = new Intent(PhotoAlbumActivity.this, DetailActivity.class);
                            intent.putExtra(PHOTO_DETAILS, photo);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    PhotoAlbumActivity.this,
                                    // To show animation while transfer from activity to another ,
                                    // For each shared element, add to this method a new Pair item,
                                    // which contains the reference of the view we are transitioning *from*,
                                    // and the value of the transitionName attribute
                                    new Pair<View, String>(view.findViewById(R.id.photoItem),
                                            getString(R.string.transition_name_photo))
                            );
                            ActivityCompat.startActivity(PhotoAlbumActivity.this, intent, options.toBundle());
                        }
                    }));
        }
    }
}
