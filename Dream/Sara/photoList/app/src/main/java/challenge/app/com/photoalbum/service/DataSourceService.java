package challenge.app.com.photoalbum.service;

import java.util.List;

import challenge.app.com.photoalbum.model.Photo;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Web services interface
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public interface DataSourceService {

    @GET("/photos")
    public void getPhoto(Callback<List<Photo>> response);
}
