package challenge.app.com.photoalbum.service;

import android.app.Activity;
import android.content.Context;

import challenge.app.com.photoalbum.R;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;


/**
 * Handle Retrofit Errors (Net work connection , No response ..etc)
 *
 * @author Sara Elmoghazy
 * @since 4/22/2016
 */
public class RetrofitErrorHandler implements ErrorHandler {
    public static final String DEFAULT_ERROR_MSG = "Unknown Error";
    private Context context;

    public RetrofitErrorHandler(Activity _context) {
        this.context = _context;
    }


    /**
     * Replace server error cause to meaningful error to user
     *
     * @param cause

     */
    @Override
    public Throwable handleError(RetrofitError cause) {
        String errorDescription = DEFAULT_ERROR_MSG;
        if (context != null) {
            if (cause.isNetworkError()) {
                errorDescription = context.getString(R.string.error_network);
            } else if (cause.getResponse() == null) {
                errorDescription = context.getString(R.string.error_no_response);
            } else {
                errorDescription = context.getString(R.string.error_unknown);
            }
        }
        return new Exception(errorDescription);
    }


    /**
     * Display custom error message to user according to Retrofit error
     *
     * @param activity
     * @param e
     */
    public static void handleWebServiceError(Activity activity, Throwable e) {
        String message = DEFAULT_ERROR_MSG;
        if (activity != null) {
            if (e != null && e.getMessage() != null)
                message = e.getMessage();
        }
        Crouton.makeText(activity, message, Style.ALERT).show();

    }
}