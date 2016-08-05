package com.xcyo.live.activity.user_edit_icon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.utils.Constants;
import com.xutils.x;

import java.io.File;

/**
 * Created by TDJ on 2016/6/20.
 */
public class EditIconActivity extends BaseActivity<EditIconPresent> {

    protected static final int R_ICON_P = 101;
    protected static final int R_ICON_C = 102;
    protected static final int R_CROP = 99;

    protected static final String PATH_CAMERA = Constants.SD_CACHE+"temp.jpg";
    protected static final String PATH_CROP = Constants.SD_CACHE+"crop.jpg";

    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_icon_edit);

        close = findViewById(R.id.icon_close);

        img = (ImageView)findViewById(R.id.icon_img);
        photo = (TextView)findViewById(R.id.icon_photo);
        camera = (TextView)findViewById(R.id.icon_camera);

        ViewGroup img_parent = (ViewGroup)img.getParent();
        if(img_parent.getVisibility() == View.VISIBLE){
            ViewGroup.LayoutParams vgp = img_parent.getLayoutParams();
            if(vgp != null){
                vgp.height = getResources().getDisplayMetrics().widthPixels;
            }
        }

        if(UserModel.getInstance().getRecord() != null){
            x.image().bind(img, "http://file.xcyo.com/"+UserModel.getInstance().getRecord().getUser().avatar);
        }
    }

    private View close;

    private ImageView img;
    private TextView photo;
    private TextView camera;

    @Override
    protected void initEvents() {
        addOnClickListener(close, "close");
        addOnClickListener(photo, "photo");
        addOnClickListener(camera, "camera");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case R_ICON_P:
                if(data == null){
                    return ;
                }
                Uri uri = data.getData();
                if(uri != null){
                    presenter().cropImage(uri);
                }
                break;
            case R_ICON_C:
                presenter().cropImage(Uri.fromFile(new File(PATH_CAMERA)));
                break;
            case R_CROP:
                if(data == null)
                    return ;
                setIcon(BitmapFactory.decodeFile(PATH_CROP));
                break;
        }
    }

    private void setIcon(Bitmap mBitmap){
        if(mBitmap != null){
            img.setImageBitmap(mBitmap);
            presenter().toByteArray(new File(PATH_CROP));
//            presenter().getImageToken();
        }
    }

}
