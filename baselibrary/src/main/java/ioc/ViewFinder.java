package ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by pc on 2018/4/25.
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    ViewFinder(View source) {
        this.mView = source;
    }


    public View findViewById(int id){
        if(mActivity != null){
            return  mActivity.findViewById(id);
        } else if (mView != null){
            return  mView.findViewById(id);
        } else {
            return  null;
        }
    }
}
