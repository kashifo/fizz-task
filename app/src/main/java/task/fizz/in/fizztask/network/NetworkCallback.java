package task.fizz.in.fizztask.network;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Kashif on 5/30/2017.
 */

public abstract class NetworkCallback {

    private Activity activity;
    private Fragment fragment;

    public NetworkCallback(Activity activity) {
        this.activity = activity;
    }

    public NetworkCallback(Fragment fragment) {
        this.fragment = fragment;
    }

    private Activity getActivity() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getActivity();
        }
        throw new RuntimeException("Neither Activity nor fragment is set");
    }

    public void onAPIResponse(boolean err, String str){

    }

}
