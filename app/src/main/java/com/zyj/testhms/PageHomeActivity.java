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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.zyj.testhms.apptest.PushGetTokenActivity;
import com.zyj.testhms.wallet.MainIndexActivity;
import com.zyj.testhms.wallet.WalletPassCnActivity;

public class PageHomeActivity extends Activity {
    boolean mIsSupportedBade = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setBadgeNum(10);
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
}
