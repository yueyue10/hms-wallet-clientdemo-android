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

package com.zyj.testhms.wallet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zyj.testhms.R;
import com.zyj.testhms.util.TestUtil;
import com.huawei.wallet.hmspass.service.IpassModifyNFCCardImpl;
import com.huawei.wallet.hmspass.service.WalletPassApiResponse;
import com.huawei.wallet.hmspass.service.WalletPassStatus;
import com.hw.passsdk.WalletPassApi;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Demo of a third-party app that demonstrates the ability to add card coupons
 */
public class WalletPassCnActivity extends Activity {

    private final String TAG = "3th_app_MainActivity";

    /**
     * Is it possible to add a button to the card
     */
    private Button canAddPassBtn;

    /**
     * Add card button
     */
    private Button addPassBtn;

    /**
     * Get token
     */
    private Button getTokenBtn;

    /**
     * Get temporary public key
     */
    private Button shortTimePublickeyBtn;

    /**
     * Start writing cards
     */
    private Button whiteCardBtn;

    /**
     * Start reading
     */
    private Button readCardBtn;

    /**
     * Query unique ID
     */
    private Button getPassDeviceIdBtn;

    /**
     * Query key status
     */
    private Button getPassStatusBtn;

    /**
     * Start deleting cards
     */
    private Button deleteCardBtn;

    /**
     * Start quering cards
     */
    private Button queryCardBtn;

    /**
     * Start quering swipe cards
     */
    private Button querySwipeCardBtn;

    /**
     * 认证注册请求
     */
    private Button requestRegisterBtn;

    /**
     * 认证注册确认
     */
    private Button confirmRegisterBtn;

    /**
     * 个人化请求
     */
    private Button requestPersonalizeBtn;

    /**
     * 个人化确认
     */
    private Button confirmPersonalizeBtn;

    /**
     * 请求交易
     */
    private Button requestTransactionBtn;

    /**
     * 确认交易
     */
    private Button confirmTransactionBtn;

    /**
     * Show results
     */
    private TextView resultText;

    /**
     * The object calling PassSdk
     */
    private WalletPassApi mWalletPassApi;

    /**
     * Identifies whether the card is being loaded
     */
    private boolean isAddPassLoading = false;

    /**
     * Take Huitailong as an example
     */
    public final static String WHITECARD_HUTLON_PASSTYPEID = "hwpass.zyj.testhms.pass.ticket";

    public final static String WHITECARD_HUTLON_APPID = "103685579";

    public final static String WHITECARD_HUTLON_PASSID = "EventTicketPass100024";

    public final static String sing =
        "7a68616f7975656a756e";

    private String returnRes;

    private String teeTempAccessSec;

    private String teeTempTransId;

    private Map<String, Object> readNFCKeyMap = new HashMap<String, Object>();

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_pass_cn);

        Log.i(TAG, "3th App MainActivity onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mWalletPassApi = new WalletPassApi(this);

        resultText = (TextView) findViewById(R.id.result);
        canAddPassBtn = (Button) findViewById(R.id.can_add_pass_btn1);
        addPassBtn = (Button) findViewById(R.id.add_pass_btn2);

        getTokenBtn = (Button) findViewById(R.id.get_token_btn3);
        shortTimePublickeyBtn = (Button) findViewById(R.id.get_short_time_publickey_btn4);
        whiteCardBtn = (Button) findViewById(R.id.white_card_btn5);
        readCardBtn = (Button) findViewById(R.id.white_card_btn6);
        getPassDeviceIdBtn = (Button) findViewById(R.id.white_card_btn7);
        getPassStatusBtn = (Button) findViewById(R.id.white_card_btn8);
        deleteCardBtn = (Button) findViewById(R.id.white_card_btn9);
        queryCardBtn = (Button) findViewById(R.id.white_card_btn10);
        querySwipeCardBtn = (Button) findViewById(R.id.white_card_btn11);
        requestRegisterBtn = (Button) findViewById(R.id.white_card_btn12);
        confirmRegisterBtn = (Button) findViewById(R.id.white_card_btn13);
        requestPersonalizeBtn = (Button) findViewById(R.id.white_card_btn14);
        confirmPersonalizeBtn = (Button) findViewById(R.id.white_card_btn15);
        requestTransactionBtn = (Button) findViewById(R.id.white_card_btn16);
        confirmTransactionBtn = (Button) findViewById(R.id.white_card_btn17);
        addPassBtn.setEnabled(false);

        /**
         * Is it possible to add card rolls
         */
        canAddPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testCanAddPass();
                }
            }
        });

        /**
         * Add voucher, including claim card package
         */
        addPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testAddPass();
                }

            }
        });

        /**
         * Check if Pass package already exists
         */
        addPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testAddPass();
                }

            }
        });

        /**
         * Can only be called after the card package is completed (empty issue and write card)
         * One method to write a card by air: apply for verification token
         */
        getTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testGetTokenPass();
                }
            }
        });

        /**
         * Method for writing and issuing cards by air Part 2: Obtaining the encrypted temporary public key
         */
        shortTimePublickeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testGitPublickey();
                }
            }
        });

        /**
         * Method for issuing a card by air
         */
        whiteCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testAddWritePass(createParam1());
                }
            }
        });

        /**
         * Method for issuing and writing cards by air Part 4: Card reading
         */
        readCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testReadNFCCard(createReadParam1());
                }
            }
        });

        /**
         * Method 5 of issuing a card by air: obtaining a unique identifier
         */
        getPassDeviceIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testRequirePassDeviceId();
                }
            }
        });

        /**
         * Method of issuing a card by air Part 6: Obtaining the key status
         */
        getPassStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testQueryPassStatus();
                }
            }
        });

        /**
         * Card writing method 7: delete card
         */
        deleteCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testDeletePass();
                }
            }
        });

        /**
         * Card writing method 7: query card
         */
        queryCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testQueryPass();
                }
            }
        });

        /**
         * Card writing method 7: query card
         */
        querySwipeCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAddPassLoading) {
                    testQuerySwipeCardInfo();
                }
            }
        });

        /**
         * Method of request regist to wallet
         */
        requestRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerRequestBody = "{\"requestBody\":{\"passTypeIdentifier\":\"hwpass.zyj.testhms.pass.ticket\",\"serialNumber\":\"EventTicketPass100003\"}}";
                if (!isAddPassLoading) {
                    testrequestRegister(registerRequestBody);
                }

            }
        });

        /**
         * Method of confirm regist to wallet
         */
        confirmRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerConfirmBody =
                        "{\"requestBody\":{\"certificate\":{\"signature\":\"`\",\"publickKey\":\"\"},\"serialNumber\":\"\",\"passTypeIdentifier\":\"\"}}";

                if (!isAddPassLoading) {
                    testconfirmRegister(registerConfirmBody);
                }
            }
        });

        /**
         * Method of request personalize to wallet
         */
        requestPersonalizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personalizeRequestBody =
                        "{\"requestBody\":{\"passTypeIdentifier\":\"\",\"serialNumber\":\"\",\"token\":\"\"}}";

                Log.i(TAG, "personalizeRequestBody:" + personalizeRequestBody);
                if (!isAddPassLoading) {
                    testrequestPersonalize(personalizeRequestBody);
                }
            }
        });

        /**
         * Method of confirm personalize to wallet
         */
        confirmPersonalizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personalizeConfirmBody =
                        "{\"confirmCode\":\"200\",\"requestBody\":{\"passTypeIdentifier\":\"\",\"serialNumber\":\"A\",\"encryptAppletPersonalizeFields\":\"\",\"encryptSessionKey\":\"\"},\"signature\":\"\"}";
                if (!isAddPassLoading) {
                    testconfirmPersonalize(personalizeConfirmBody);
                }
            }
        });

        /**
         * Method of request transaction to wallet
         */
        requestTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddPassLoading) {
                    testrequestTransaction("{\"requestBody\":{\"passTypeIdentifier\":\"\",\"serialNumber\":\"\"}}");
                }
            }
        });

        /**
         * Method of confirm transaction to wallet
         */
        confirmTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddPassLoading) {
                    String str1 =
                            "{\"requestBody\":{\"passTypeIdentifier\":\"\",\"serialNumber\":\"\",\"transType\":\"car_key_hand_shake_1\",\"transContent\":\"\"}}";
                    String str2 =
                            "{\"requestBody\":{\"passTypeIdentifier\":\"\",\"serialNumber\":\"\",\"transType\":\"car_key_hand_shake_2\",\"transContent\":\"\"}}";
                    testconfirmTransaction(str1);
                }
            }
        });
    }

    /**
     * Test if you can add a card
     */
    private void testCanAddPass() {

        if (TextUtils.isEmpty(WHITECARD_HUTLON_APPID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "appId and passType cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        resultText.setText("Determining whether a card can be added, please wait ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.canAddPass(WHITECARD_HUTLON_APPID, WHITECARD_HUTLON_PASSTYPEID);
                Log.i(TAG, "3th App canAddPass");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                addPassBtn.setEnabled(true);
                                resultText.setText("Test if you can add coupons: Yes");
                            } else {
                                addPassBtn.setEnabled(false);
                                resultText.setText("Test if you can add a card: No\n returnCode = "
                                        + response.getReturnCode() + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Test if you can add a card: No\n response = null");
                        }

                    }
                });
            }
        }).start();
    }

    /**
     * Test Query swipe Card info
     */
    private void testQuerySwipeCardInfo() {
        resultText.setText("query swipe card info, please wait ...");
        isAddPassLoading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject.put("passTypeIdentifier", "hwpass.zyj.testhms.pass.ticket");
                    jsonObject.put("queryType", "SWIPE_LOG");
                    jsonObject2.put("requestBody", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String requestBody = jsonObject2.toString();
                Log.i(TAG, "3th App queryPass requestBody ==== " + requestBody);

                final WalletPassApiResponse response = mWalletPassApi.queryPass(requestBody);
                Log.i(TAG, "3th App queryPass");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Test query Card: Success");
                            } else {
                                resultText.setText("Test query Card: Failed\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Test query Card: Failed\n response = null");
                        }
                        isAddPassLoading = false;
                    }
                });
            }
        }).start();
    }

    /**
     * Test Query Card
     */
    private void testQueryPass() {
        resultText.setText("query card info, please wait ...");
        isAddPassLoading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject.put("passTypeIdentifier", "hwpass.zyj.testhms.pass.ticket");
                    jsonObject.put("queryType", "BIND_READER_ID");
                    jsonObject2.put("requestBody", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String requestBody = jsonObject2.toString();
                Log.i(TAG, "3th App queryPass requestBody ==== " + requestBody);

                final WalletPassApiResponse response = mWalletPassApi.queryPass(requestBody);
                Log.i(TAG, "3th App queryPass");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Test query Card: Success");
                            } else {
                                resultText.setText("Test query Card: Failed\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Test query Card: Failed\n response = null");
                        }
                        isAddPassLoading = false;
                    }
                });
            }
        }).start();
    }

    /**
     * Test Add Card
     */
    private void testAddPass() {
        resultText.setText("Adding coupons, please wait ...");
        isAddPassLoading = true;
        addPassBtn.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Create simulation data
                String base64ZipData = TestUtil.createPassData(WalletPassCnActivity.this);

                Log.i(TAG, "3th App addPass base64ZipData ======== " + base64ZipData);

                final WalletPassApiResponse response = mWalletPassApi.addPass(base64ZipData);
                Log.i(TAG, "3th App addPass");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Test Add Card: Success");
                            } else {
                                resultText.setText("Test Add Card: Failed\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Test Add Card: Failed\n response = null");
                        }

                        isAddPassLoading = false;
                        addPassBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    /**
     * Test Add Card
     */
    private void testQueryPassStatus() {
        resultText.setText("Checking for vouchers, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "appId and passType cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response =
                        mWalletPassApi.queryPassStatus(WHITECARD_HUTLON_PASSTYPEID, WHITECARD_HUTLON_PASSID);
                Log.i(TAG, "3th App queryPassStatus");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            String text = "Check if the ticket exists: Success";
                            if ("0".equals(response.getReturnCode())) {
                                final WalletPassStatus passStatus = response.getPassStatus();
                                if (null != passStatus) {
                                    text = text + ", PassId:" + passStatus.getPassId() + "status:"
                                            + passStatus.getStatus();
                                }

                                resultText.setText(text);
                            } else {
                                resultText.setText("Check if the card exists: Failure\n returnCode = "
                                        + response.getReturnCode() + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Check if the card exists: Failure\n response = null");
                        }

                        isAddPassLoading = false;
                        addPassBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    /**
     * Get token
     */
    private void testGetTokenPass() {

        resultText.setText("Judging token, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "passTypeId Can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.requireAccessToken(WHITECARD_HUTLON_PASSTYPEID);
                Log.i(TAG, "3th App canAddPass");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "requireAccessToken:  " + response.getAccessToken());
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Get token: Yes" + response.getAccessToken());
                            } else {
                                resultText.setText("Get token: no returnCode = " + response.getReturnCode()
                                        + " returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Get token: no response = null");
                        }

                        isAddPassLoading = false;
                    }
                });
            }
        }).start();
    }

    /**
     * Get public key
     */
    private void testGitPublickey() {
        resultText.setText("Getting public key, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "appId and passType cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Create simulation data
                String base64ZipData = TestUtil.createPassData(WalletPassCnActivity.this);

                Log.i(TAG, "3th App addPass base64ZipData ======== " + base64ZipData);

                final WalletPassApiResponse response =
                        mWalletPassApi.requireAccessCardSec(WHITECARD_HUTLON_PASSTYPEID, WHITECARD_HUTLON_PASSID, sing);
                Log.i(TAG, "3th App addPass");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                returnRes = response.getTempAccessSec().trim();
                                teeTempAccessSec = response.getTeeTempAccessSec().trim();
                                Log.i(TAG, "teeTempAccessSec:" + teeTempAccessSec);
                                teeTempTransId = response.getTeeTempTransId().trim();
                                Log.i(TAG, "teeTempTransId:" + teeTempTransId);
                                Log.i(TAG, "returnRes:" + returnRes);
                                resultText.setText(
                                        "Getting public key: Success" + returnRes + "   size: " + returnRes.length());

                                // testAddWritePass();

                            } else {
                                resultText.setText("Getting public key: failed returnCode = " + response.getReturnCode()
                                        + " returnRes = " + response.getReturnRes());

                            }
                        } else {
                            resultText.setText("Getting public key: failed response = null");
                        }

                        isAddPassLoading = false;
                    }
                });

            }
        }).start();
    }

    /**
     * Write card
     */
    private void testAddWritePass(final String cardParams) {
        resultText.setText("Writing card, please wait ...");
        isAddPassLoading = true;

        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)
                || TextUtils.isEmpty(cardParams)) {
            Toast
                    .makeText(WalletPassCnActivity.this, "appId && passType && cardParams Can not be empty",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                mWalletPassApi.modifyNFCCard(WHITECARD_HUTLON_PASSTYPEID, WHITECARD_HUTLON_PASSID, cardParams, "", sing,
                        mIpassModifyNFCCardImpl);

            }
        }).start();
    }

    IpassModifyNFCCardImpl mIpassModifyNFCCardImpl = new IpassModifyNFCCardImpl.Stub() {
        @Override
        public void modifyCallBack(final WalletPassApiResponse walletPassApiResponse) {
            Log.i(TAG, "3th App mIpassModifyNFCCardImpl");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (walletPassApiResponse != null) {
                        if ("0".equals(walletPassApiResponse.getReturnCode())) {
                            resultText.setText("Write Card: Success");
                        } else {
                            resultText
                                    .setText("Write Card: Failure returnCode = " + walletPassApiResponse.getReturnCode()
                                            + " returnRes = " + walletPassApiResponse.getReturnRes());
                        }
                    } else {
                        resultText.setText("Write Card: Failure response = null");
                    }
                }
            });

            isAddPassLoading = false;
        }
    };

    @TargetApi(Build.VERSION_CODES.O)
    public String createParam1() {
        JSONObject jsonObject = new JSONObject();
        // The content to be written in the card is defined by the third party (the data below is the structure data of
        // the test example lv, which needs to be filled)
        String EncryptedKeyId1 =
                "182C010033481965BB3C6CC7C261926D54701D479103DBF3BB0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String EncryptedKeyId2 =
                "181C010033A5BA5A40C5ADA8F5E123D2D2F434F95603AF9D660000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String EncryptedKeyId3 =
                "188C01003377ECB36BE6F034797AE61053C5620F48038DC0640000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String EncryptedKeyId4 =
                "1CDF5A085555555555555555DF3806DF0101DF02108D0600130391133800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String EncryptedKeyId5 =
                "0803aaddccaabbccdd000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        try {
            jsonObject.put("E001", TestUtil.encryptByPublicKey(EncryptedKeyId1, returnRes));
            jsonObject.put("E002", TestUtil.encryptByPublicKey(EncryptedKeyId2, returnRes));
            jsonObject.put("E003", TestUtil.encryptByPublicKey(EncryptedKeyId3, returnRes));
            jsonObject.put("E004", TestUtil.encryptByPublicKey(EncryptedKeyId4, returnRes));
            jsonObject.put("E005", TestUtil.encryptByPublicKey(EncryptedKeyId5, returnRes));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonLetter = new JSONObject();
        String cardParamsStr = jsonObject.toString();
        /*
         * try { jsonLetter.put("appletParamsSec", cardParamsStr); jsonLetter.put("transKeySec", "aa"); } catch
         * (JSONException e) { e.printStackTrace(); }
         */
        Log.i(TAG, "appletParamsSec0:" + cardParamsStr);
        try {
            String cKey = "1234567890123456";
            byte[] raw = cKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            String ivKey = "0102030405060708";
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
            aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = aesCipher.doFinal(cardParamsStr.getBytes());
            Log.i(TAG, "appletParamsSec1:" + new String(encrypted));
            String appletParamStr = Base64.getEncoder().encodeToString(encrypted);
            Log.i(TAG, "appletParamsSec2:" + appletParamStr);
            jsonLetter.put("appletParamsSec", appletParamStr);

            // Encrypt with the TA public key on the App side and return the encrypted result string, using RSA / ECB /
            // OAEPwithSHA-256 and MGF1Padding
            byte[] keyBytes = Base64.getDecoder().decode(teeTempAccessSec);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
            Log.i(TAG, "transKeyStr0:" + cKey + ivKey);
            byte[] mi = rsaCipher.doFinal((teeTempTransId + cKey + ivKey).getBytes());
            Log.i(TAG, "transKeyStr1:" + mi.length);
            String transKeyStr = this.bytes2Hex(mi);
            Log.i(TAG, "transKeyStr2:" + transKeyStr);
            jsonLetter.put("transKeySec", transKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonLetter.toString();
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * Read NFC parameter information
     */
    private void testReadNFCCard(final String cardParams) {

        resultText.setText("Reading card, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)
                || TextUtils.isEmpty(cardParams)) {
            Toast
                    .makeText(WalletPassCnActivity.this, "appId && passType && cardParams Can not be empty",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.readNFCCard(WHITECARD_HUTLON_PASSTYPEID,
                        WHITECARD_HUTLON_PASSID, cardParams, "", sing);
                Log.i(TAG, "3th App canAddPass");

                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                String result = "";
                                try {
                                    String readNfcResult = response.getReadNFCResult();
                                    byte[] readResultArr = Base64.getDecoder().decode(readNfcResult);

                                    String privateKey = (String) readNFCKeyMap.get("privateKey");
                                    byte[] keyBytes = Base64.getDecoder().decode(privateKey);
                                    // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
                                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                    PrivateKey priKey = keyFactory.generatePrivate(keySpec);
                                    // Cipher rsaCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA-256AndMGF1Padding");
                                    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");
                                    rsaCipher.init(Cipher.DECRYPT_MODE, priKey);
                                    Log.i(TAG, "readNFCResult1:" + readNfcResult);
                                    byte[] mi = rsaCipher.doFinal(readResultArr);

                                    result = new String(mi);
                                    Log.i(TAG, "readNFCResult0:" + result);
                                    // JSONObject obj = new JSONObject(readNfcResult);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                resultText.setText("Card reading result: success" + result);
                            } else {
                                resultText.setText("Card reading result: failure returnCode = "
                                        + response.getReturnCode() + " returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Get token: no response = null");
                        }

                        isAddPassLoading = false;
                    }
                });
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public String createReadParam1() {
        JSONObject jsonObject = new JSONObject();
        // The content to be written in the card is defined by the third party (the data below is the structure data of
        // the test example lv, which needs to be filled)

        try {
            // KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            // KeyPair keyPair = generator.genKeyPair();
            // PublicKey publicKey = keyPair.getPublic();
            // PrivateKey privateKey = keyPair.getPrivate();
            // readNFCKeyMap.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            // readNFCKeyMap.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            readNFCKeyMap.put("publicKey",
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhAj4Z58JcmkuKYfgCk68yPlEAg5EiyA5Eyv1HE+utJjHK1FMuiUqyPoOIUoVclchb5AZalLsvSuNnwoca2XjwWWxhc8f/CuNVvtwEZTuJts3Co93cBziiGuoPlmyZMlKvN+lk3B1CvG6/Zs2GVuXh5tC8BmULutXgYFzHfiC083ydaGQZYyeSR4KKa22QwZiC8vLFRyYdr4Bgvxc4BR01BmhU1RMW4zvLeJcpcIch4nz2dLj277HT5Qveo0AtOzzLVeCD+bUajMJvh7tYcxUjxExQ66TMx8J*******************************************************************");
            readNFCKeyMap.put("privateKey",
                    "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCECPhnnwlyaS4ph+AKTrzI+UQCDkSLIDkTK/UcT660mMcrUUy6JSrI+g4hShVyVyFvkBlqUuy9K42fChxrZePBZbGFzx/8K41W+3ARlO4m2zcKj3dwHOKIa6g+WbJkyUq836WTcHUK8br9mzYZW5eHm0LwGZQu61eBgXMd+ILTzfJ1oZBljJ5JHgoprbZDBmILy8sVHJh2vgGC/FzgFHTUGaFTVExbjO8t4lylwhyHifPZ0uPbvsdPlC96jQC07PMtV4IP5tRqMwm+Hu1hzFSPETFDrpMzHwl1cuvPwJOqPwKP2rfp/lgWEAZcgQlV5H44htPx2XOUSZMkMNjIBn/JAgMBAAECggEACfNPHSgMIzYwB2s5BC3Y5mB6spj2q3F7O6mlaEPNC8kzwVz4JlMkvU6y/+lAYoSUfX7jm3KyarM8kg7GA4YDOxD55vasF2oE1dhPItQRepYsuLwFKUrT+6n5XPGPky7kVgWYydrAx1leWTx4QEL50Xtu+P9EfqC30Ie78DdJTMLtzy8Jxn9vCIevQMS5AHY49H/9YnC8rGZqftqgWlQ91zKVvHc1f7TftqTb9q03Tc1cMdV8zujkKFAHlGSY57IxWiB1QjLqk1zCcbtT9lbep5RjzUYDFFRvqCEExMX1A37Q5XQosUl9zi59o+ZmcO8IRvrZAGnLhnAtUFIA5sTWAQKBgQDDJPeN5caRO3/rT9rQ8KH7siVYaSJpc+JRfCoUyDUGZtR7bVh3W4+C4h7c55I2bwiHxx+8hywEISi5SmgcwCUQ7EoBCWXleEXbgHD1JyAvcbA7SH7AoWuPFZ/FjGVv29arHCnDgpg8Sst3ni0gvWhDALuf1mZK6wn***********************************************************************************************************************************************");

            jsonObject.put("car_key_sn", "");
            jsonObject.put("car_key_group", "");
            jsonObject.put("read_publickey", readNFCKeyMap.get("publicKey"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static Key getKey(String keySeed) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get token
     */
    private void testRequirePassDeviceId() {

        resultText.setText("Getting PassDeviceId, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "passTypeId Can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.requirePassDeviceId(WHITECARD_HUTLON_PASSTYPEID);
                Log.i(TAG, "3th App requirePassDeviceId");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Get PassDeviceId: Yes" + response.getPassDeviceId());
                            } else {
                                resultText.setText("Get PassDeviceId: No returnCode = " + response.getReturnCode()
                                        + " returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("Get PassDeviceId: No response = null");
                        }

                        isAddPassLoading = false;
                    }
                });
            }
        }).start();
    }

    /**
     * Delete card
     */
    private void testDeletePass() {
        resultText.setText("Deleting card, please wait ...");
        isAddPassLoading = true;
        if (TextUtils.isEmpty(WHITECARD_HUTLON_PASSID) || TextUtils.isEmpty(WHITECARD_HUTLON_PASSTYPEID)) {
            Toast.makeText(WalletPassCnActivity.this, "appId and passType Can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                final WalletPassApiResponse response =
                        mWalletPassApi.deletePass(WHITECARD_HUTLON_PASSTYPEID, WHITECARD_HUTLON_PASSID, sing);
                Log.i(TAG, "3th App deletePass");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("Delete card: successful returnCode = " + response.getReturnCode());

                                // testAddWritePass();

                            } else {
                                resultText.setText("Delete card: failed returnCode = " + response.getReturnCode()
                                        + " returnRes = " + response.getReturnRes());

                            }
                        } else {
                            resultText.setText("Delete card: failed response = null");
                        }

                        isAddPassLoading = false;
                    }
                });

            }
        }).start();
    }

    private void testrequestRegister(final String registerRequestBody) {
        resultText.setText("正在认证注册请求，请稍后...");
        isAddPassLoading = true;
        requestRegisterBtn.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.requestRegister(registerRequestBody);
                String json = response.getResponseStr();
                Log.i(TAG, "3th App requestRegister: " + json);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("认证注册请求：成功");
                            } else {
                                resultText.setText("认证注册请求：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("认证注册请求：失败\n response = null");
                        }
                        isAddPassLoading = false;
                        requestRegisterBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    private void testconfirmRegister(final String registerConfirmBody) {
        resultText.setText("正在认证注册确认，请稍后...");
        isAddPassLoading = true;
        confirmRegisterBtn.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.confirmRegister(registerConfirmBody);
                Log.i(TAG, "3th App confirmRegister");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("认证注册确认：成功");
                            } else {
                                resultText.setText("认证注册确认：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("认证注册确认：失败\n response = null");
                        }
                        isAddPassLoading = false;
                        confirmRegisterBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    private void testrequestPersonalize(final String personalizeRequestBody) {
        resultText.setText("正在个人化请求，请稍后...");
        isAddPassLoading = true;
        requestPersonalizeBtn.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.requestPersonalize(personalizeRequestBody);
                Log.i(TAG, "3th App requestPersonalize: " + response.getResponseStr());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("个人化请求：成功");
                            } else {
                                resultText.setText("个人化请求：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("个人化请求：失败\n response = null");
                        }
                        isAddPassLoading = false;
                        requestPersonalizeBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    private void testconfirmPersonalize(final String personalizeConfirmBody) {
        resultText.setText("正在个人化确认，请稍后...");
        isAddPassLoading = true;
        confirmPersonalizeBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.confirmPersonalize(personalizeConfirmBody);
                Log.i(TAG, "3th App confirmPersonalize: " + response.getReturnCode());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("个人化确认：成功");
                            } else {
                                resultText.setText("个人化确认：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("个人化确认：失败\n response = null");
                        }
                        isAddPassLoading = false;
                        confirmPersonalizeBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    private void testrequestTransaction(final String requestTransBody) {
        resultText.setText("正在请求交易，请稍后...");
        isAddPassLoading = true;
        confirmPersonalizeBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.requestTransaction(requestTransBody);
                Log.i(TAG, "3th App requestTransaction: " + response.getReturnCode());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                resultText.setText("请求交易：成功");
                            } else {
                                resultText.setText("请求交易：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("请求交易：失败\n response = null");
                        }
                        isAddPassLoading = false;
                        confirmPersonalizeBtn.setEnabled(true);
                    }
                });

            }
        }).start();
    }

    private void testconfirmTransaction(final String str) {
        resultText.setText("正在申请握手，请稍后...");
        isAddPassLoading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final WalletPassApiResponse response = mWalletPassApi.confirmTransaction(str);
                Log.i(TAG, "3th App confirmTransaction: " + response.getResponseStr());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response != null) {
                            if ("0".equals(response.getReturnCode())) {
                                String res = response.getResponseStr();
                                resultText.setText("测试握手：成功: " + res);
                            } else {
                                resultText.setText("测试握手：失败\n returnCode = " + response.getReturnCode()
                                        + "\n returnRes = " + response.getReturnRes());
                            }
                        } else {
                            resultText.setText("测试握手：失败\n response = null");
                        }
                        isAddPassLoading = false;
                    }
                });

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "3th App MainActivity onDestroy");

        // Unlink PassSdk from HmsPass when exiting the app
        mWalletPassApi.disconnectHmsPassService();
        super.onDestroy();
    }

}
