package com.xcyo.live.activity.user_edit_icon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.activity.user_edit.EditPresenter;
import com.xcyo.live.utils.ServerEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by TDJ on 2016/6/20.
 */
public class EditIconPresent extends BaseActivityPresenter<EditIconActivity, EditIconRecord> {

    private String token = "7aKNfyHtqIFAKqq20RSEnrSPKcFAPRB_qNuS9AK_:6EYu_Kvz4TKaKg7vwC4aZ1BTPK8=:eyJwZXJzaXN0ZW50T3BzIjoiYXZ0aHVtYlwvZmx2XC92YlwvMS4yNW07YXZ0aHVtYlwvbTN1OHxzYXZlYXNcLyIsInBlcnNpc3RlbnROb3RpZnlVcmwiOiJodHRwOlwvXC93d3cueGN5by5jb21cL3VwbG9hZFwvbm90aWZ5LXVybCIsInBlcnNpc3RlbnRQaXBlbGluZSI6InZpZGVvMSIsInNjb3BlIjoieGN5by1wdWJsaWMiLCJkZWFkbGluZSI6MTQ2NjY2NzEwMX0=";

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_IMAGE_QN_TOKEN.equals(evt)){

        }else if(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_ICON.equals(evt)){
            ToastUtil.toastTip(mActivity, "图片上传成功");
            mRecord = (EditIconRecord)data.record;
            if(mRecord.getUser() != null){
                Intent mIntent = new Intent();
                mIntent.putExtra("r_icon", mRecord.getUser().avatar);
                mActivity.setResult(EditPresenter.R_ICON_CODE, mIntent);
            }
            mActivity.finish();
        }
    }

    @Override
    protected void onServerFailedCallback(String evt, ServerBinderData data) {
        super.onServerFailedCallback(evt, data);
        if(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_ICON.equals(evt) || ServerEvents.CALL_SERVER_METHOD_IMAGE_QN_TOKEN.equals(evt)){
            ToastUtil.toastTip(mActivity, "上传图片失败");
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if ("close".equals(action)) {
            getActivity().finish();
        }else if("photo".equals(action)){
            openPhoto();
        }else if("camera".equals(action)){
            openCamera();
        }
    }

    private void openPhoto(){
        Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(mIntent, mActivity.R_ICON_P);
    }

    private void openCamera(){
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mActivity.PATH_CAMERA)));
        mActivity.startActivityForResult(mIntent, mActivity.R_ICON_C);
    }

    protected void cropImage(Uri uri){
        Intent mIntent = new Intent("com.android.camera.action.CROP");
        mIntent.setDataAndType(uri, "image/*");
        mIntent.putExtra("crop", "true");
        mIntent.putExtra("aspectX", 1);// 裁剪框比例
        mIntent.putExtra("aspectY", 1);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mActivity.PATH_CROP)));//图像输出
        mIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mIntent.putExtra("noFaceDetection", true);
        mIntent.putExtra("return-data", false);//回调方法data.getExtras().getParcelable("data")返回数据为空
        mActivity.startActivityForResult(mIntent, mActivity.R_CROP);
    }

    protected void getImageToken(){
        callServer(ServerEvents.CALL_SERVER_METHOD_IMAGE_QN_TOKEN, new PostParamHandler());
    }

    protected void upLoadUsrIcon(@NonNull String key){
        callServer(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_ICON, new PostParamHandler("avatar", key));
    }

    protected void toByteArray(@NonNull File file){
        if(file == null)
            return ;
        FileInputStream fips = null;
        ByteArrayOutputStream fops = null;
        try {
            fips = new FileInputStream(file);
            fops = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int index = -1;
            while((index = fips.read(b)) != -1){
                fops.write(b);
            }
            upLoadFile2QL(fops.toByteArray(), token);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1){
            e1.printStackTrace();
        } finally {
            try {
                if(fips != null){
                    fips.close();
                }
                if(fops != null){
                    fops.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void upLoadFile2QL(byte[] fileContent, String token){
        if(fileContent != null){
            com.qiniu.android.storage.Configuration config = new com.qiniu.android.storage.Configuration.Builder()
                    .connectTimeout(5) // 链接超时。默认 10秒
                    .responseTimeout(12) // 服务器响应超时。默认 60秒
                    .build();
            // 创建一个 uploadManager 对象
            com.qiniu.android.storage.UploadManager uploadManager = new com.qiniu.android.storage.UploadManager(config);
            uploadManager.put(fileContent, null, token,
                    new com.qiniu.android.storage.UpCompletionHandler() {
                        @Override
                        public void complete(String key, com.qiniu.android.http.ResponseInfo info, JSONObject res) {
                            LogUtil.e("TAG", key + "->" + res.toString());
                            try {
                                if(res != null && res.has("key")){
                                    upLoadUsrIcon(res.getString("key"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);
        }
    }
}
