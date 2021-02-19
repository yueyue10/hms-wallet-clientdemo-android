/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.zyj.testhms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.zyj.testhms.apptest.PushGetTokenActivity;
import com.zyj.testhms.computer.FaceVerifyActivity;
import com.zyj.testhms.wallet.MainIndexActivity;
import com.zyj.testhms.wallet.WalletPassCnActivity;

import java.util.ArrayList;
import java.util.List;

public class PageHomeActivity extends Activity {

    private static final String TAG = "PageHomeActivity";
    private static final int PERMISSION_REQUESTS = 1;
    boolean mIsSupportedBade = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setBadgeNum(10);
        if (!this.allPermissionsGranted()) {
            this.getRuntimePermissions();
        }
    }

    public void hwPassBtn1(View view) {
        Intent intent = new Intent(this, WalletPassCnActivity.class);
        startActivity(intent);
    }

    public void hwPassBtn2(View view) {
        Intent intent = new Intent(this, MainIndexActivity.class);
        startActivity(intent);
    }

    public void hwPassBtn3(View view) {
        Intent intent = new Intent(this, PushGetTokenActivity.class);
        startActivity(intent);
    }

    public void hwPassBtn4(View view) {
        Intent intent = new Intent(this, FaceVerifyActivity.class);
        startActivity(intent);
    }

    /**
     * set badge number
     */
    public void setBadgeNum(int num) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.zyj.testhms"); // com.test.badge is your package name
            bunlde.putString("class", "com.zyj.testhms.PageHomeActivity"); // com.test. badge.MainActivity is your apk main activity
            bunlde.putInt("badgenumber", num);
            this.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            mIsSupportedBade = false;
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : this.getRequiredPermissions()) {
            if (!PageHomeActivity.isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : this.getRequiredPermissions()) {
            if (!PageHomeActivity.isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }
        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, allNeededPermissions.toArray(new String[0]), PageHomeActivity.PERMISSION_REQUESTS);
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
