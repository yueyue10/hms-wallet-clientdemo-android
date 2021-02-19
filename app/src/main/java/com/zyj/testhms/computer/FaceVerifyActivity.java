package com.zyj.testhms.computer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.faceverify.MLFaceTemplateResult;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzer;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerFactory;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerSetting;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationResult;
import com.zyj.testhms.R;
import com.zyj.testhms.util.BitmapUtils;

import org.w3c.dom.Text;

import java.util.List;

public class FaceVerifyActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_TEMPLATEPIC = 1000;
    private static final int REQUEST_COMPARE_PIC = 1100;
    private static final int FACEMAX = 3;
    MLFaceVerificationAnalyzer analyzer;
    private Bitmap templateBitmap;
    private Bitmap compareBitmap;
    private ImageView templatePreview;
    private ImageView compareImageView;
    private TextView create_model_result;
    private TextView compare_face_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);
        initAnalyzer();
        findViewById(R.id.select_picture).setOnClickListener(this);
        findViewById(R.id.create_model).setOnClickListener(this);
        findViewById(R.id.select_compare_image).setOnClickListener(this);
        findViewById(R.id.compare_face).setOnClickListener(this);
        templatePreview = findViewById(R.id.temp_Preview);
        compareImageView = findViewById(R.id.compare_face_image);
        create_model_result = findViewById(R.id.create_model_result);
        compare_face_result = findViewById(R.id.compare_face_result);
    }

    private void initAnalyzer() {
        MLFaceVerificationAnalyzerSetting.Factory factory = new MLFaceVerificationAnalyzerSetting.Factory().setMaxFaceDetected(FACEMAX);
        MLFaceVerificationAnalyzerSetting setting = factory.create();
        analyzer = MLFaceVerificationAnalyzerFactory
                .getInstance()
                .getFaceVerificationAnalyzer(setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_picture:
                BitmapUtils.recycleBitmap(templateBitmap);
                selectLocalImage(REQUEST_CHOOSE_TEMPLATEPIC);
                break;
            case R.id.create_model:
                createModel();
                break;
            case R.id.select_compare_image:
                BitmapUtils.recycleBitmap(compareBitmap);
                selectLocalImage(REQUEST_COMPARE_PIC);
                break;
            case R.id.compare_face:
                compareImage();
                break;
        }
    }

    private void compareImage() {
        // 通过bitmap创建MLFrame
        MLFrame compareFrame = MLFrame.fromBitmap(compareBitmap);
        Task<List<MLFaceVerificationResult>> task = analyzer.asyncAnalyseFrame(compareFrame);
        task.addOnSuccessListener(new OnSuccessListener<List<MLFaceVerificationResult>>() {
            @Override
            public void onSuccess(List<MLFaceVerificationResult> results) {
                // 检测成功
                System.out.println("compareImage-onSuccess" + JSON.toJSONString(results));
                compare_face_result.setText(JSON.toJSONString(results));
                if (analyzer != null) analyzer.stop();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 检测失败
                System.out.println("compareImage-onFailure" + e.toString());
                compare_face_result.setText(JSON.toJSONString(e.toString()));
                if (analyzer != null) analyzer.stop();
            }
        });
    }

    private void createModel() {
        // 通过bitmap创建MLFrame
        MLFrame templateFrame = MLFrame.fromBitmap(templateBitmap);
        List<MLFaceTemplateResult> results = analyzer.setTemplateFace(templateFrame);
        System.out.println("createModel-results" + results.toString());
        create_model_result.setText(JSON.toJSONString(results));
        for (int i = 0; i < results.size(); i++) {
            // 处理模板图片识别结果
        }
    }

    private void selectLocalImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, requestCode);
    }

    private Bitmap loadPic(Uri picUri, ImageView view) {
        Bitmap pic = null;
        pic = BitmapUtils.loadFromPath(this, picUri, ((View) view.getParent()).getWidth(),
                ((View) view.getParent()).getHeight()).copy(Bitmap.Config.ARGB_8888, true);
        if (pic == null) {
            Toast.makeText(getApplicationContext(), "请选择图片", Toast.LENGTH_SHORT).show();
        }
        view.setImageBitmap(pic);
        return pic;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CHOOSE_TEMPLATEPIC) && (resultCode == Activity.RESULT_OK)) {
            // In this case, imageUri is returned by the chooser, save it.
            if (data == null) {
                Toast.makeText(getApplicationContext(), "请选择图片！", Toast.LENGTH_SHORT).show();
                return;
            }
            templateBitmap = loadPic(data.getData(), templatePreview);
        } else if (requestCode == REQUEST_COMPARE_PIC && resultCode == Activity.RESULT_OK) {
            compareBitmap = loadPic(data.getData(), compareImageView);
        }
    }
}
