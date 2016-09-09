package challenge.app.com.photoalbum.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import challenge.app.com.photoalbum.R;
import challenge.app.com.photoalbum.model.Photo;


/**
 * Activity to display photo Details
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class DetailActivity extends Activity {
    ImageView imgPhoto;
    TextView lblTitle;
    TextView lblAlbumId;
    TextView lblPhotoId;
    TextView lblUrl;
    TextView lblThumbnailUrl;
    Photo photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_details);
        initUi();
        // get selected photo from intent.
        Intent intent = getIntent();
        photo = (Photo) intent.getSerializableExtra(PhotoAlbumActivity.PHOTO_DETAILS);
        // Display details
        initData();
    }

    private void initUi() {
        imgPhoto = (ImageView) findViewById(R.id.photo);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblAlbumId = (TextView) findViewById(R.id.lblAlbumId_details);
        lblPhotoId = (TextView) findViewById(R.id.lblPhotoId_details);
        lblUrl = (TextView) findViewById(R.id.lblUrl_details);
        lblThumbnailUrl = (TextView) findViewById(R.id.lblThumbnailUrl_details);
    }

    private void initData() {
        // Load Image Url
        Glide.with(this).load(photo.getUrl()).thumbnail(0.1f).into(imgPhoto);
        lblTitle.setText(photo.getTitle());
        lblAlbumId.setText(Integer.toString(photo.getAlbumId()));
        lblPhotoId.setText(Integer.toString(photo.getId()));
        lblUrl.setText(photo.getUrl());
        lblThumbnailUrl.setText(photo.getThumbnailUrl());

    }
}
