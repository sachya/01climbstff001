package iostudio.in.et.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import iostudio.in.et.R;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.utility.Constant;

public class SetPinActivity extends BaseActivity {
    private OtpView pinView;
    private MaterialButton btnSubmit;
    private AppCompatTextView tvEnterPin;
    private SwitchCompat switchCompatTouchId;
    private boolean isPinConfirmed;
    private String pinOne, pinTwo;
    static final String DEFAULT_KEY_NAME = "default_key";

//    private KeyStore mKeyStore;
//    private KeyGenerator mKeyGenerator;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    AppCompatTextView tv_terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(iostudio.in.et.R.layout.activity_set_pin);
        initBase();

    }

    @Override
    protected void initView() {
        pinView = findViewById(iostudio.in.et.R.id.otp_view);
        btnSubmit = findViewById(iostudio.in.et.R.id.btn_submit);
        tvEnterPin = findViewById(iostudio.in.et.R.id.tv_enter_pin);
        switchCompatTouchId = findViewById(iostudio.in.et.R.id.switch_enable_fingerprint);
        tv_terms = findViewById(R.id.tv_terms);

        String s = getString(R.string.terms_n_conditions_policy);
        tv_terms.setText(getSpannedText(s));

        SpannableString ss = new SpannableString("Terms & Conditions | Privacy Policy");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(SetPinActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, true);
                startActivity(i);
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(SetPinActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, false);
                startActivity(i);
            }
        };

        ss.setSpan(span1, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 21, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_terms.setText(ss);
        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void initData() {
        pinOne = "";
        pinTwo = "";
        tvEnterPin.setText(getString(iostudio.in.et.R.string.set_pin_enter_4_digit));
        btnSubmit.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switchCompatTouchId.setVisibility(View.VISIBLE);
            switchCompatTouchId.setChecked(IOPref.getInstance().getBoolean(this, IOPref.PreferenceKey.IS_TOUCH_ID_ENABLED, false));
/*            try {
                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            } catch (KeyStoreException e) {
                throw new RuntimeException("Failed to get an instance of KeyStore", e);
            }
            try {
                mKeyGenerator = KeyGenerator
                        .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
            }
            Cipher defaultCipher;
            Cipher cipherNotInvalidated;
            try {
                defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                throw new RuntimeException("Failed to get an instance of Cipher", e);
            }*/

             keyguardManager = getSystemService(KeyguardManager.class);
             fingerprintManager = getSystemService(FingerprintManager.class);
           /* Button purchaseButton = findViewById(R.id.purchase_button);
            Button purchaseButtonNotInvalidated = findViewById(R.id.purchase_button_not_invalidated);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                purchaseButtonNotInvalidated.setEnabled(true);
                purchaseButtonNotInvalidated.setOnClickListener(
                        new PurchaseButtonClickListener(cipherNotInvalidated,
                                KEY_NAME_NOT_INVALIDATED));
            } else {
                // Hide the purchase button which uses a non-invalidated key
                // if the app doesn't work on Android N preview
                purchaseButtonNotInvalidated.setVisibility(View.GONE);
                findViewById(R.id.purchase_button_not_invalidated_description).setVisibility(View.GONE);
            }*/


//            createKey(DEFAULT_KEY_NAME, true);
         //   createKey(KEY_NAME_NOT_INVALIDATED, false);

            switchCompatTouchId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!keyguardManager.isKeyguardSecure()) {
                            // Show a message that the user hasn't set up a fingerprint or lock screen.
                            Toast.makeText(SetPinActivity.this,
                                    "Secure lock screen hasn't set up.\n"
                                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                                    Toast.LENGTH_LONG).show();
                            switchCompatTouchId.setChecked(false);
                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            // Now the protection level of USE_FINGERPRINT permission is normal instead of dangerous.
                            // See http://developer.android.com/reference/android/Manifest.permission.html#USE_FINGERPRINT
                            // The line below prevents the false positive inspection from Android Studio
                            // noinspection ResourceType
                            // This happens when no fingerprints are registered.
                            Toast.makeText(SetPinActivity.this,
                                    "Go to 'Settings -> Security -> Fingerprint' and register at least one" +
                                            " fingerprint",
                                    Toast.LENGTH_LONG).show();
                            switchCompatTouchId.setChecked(false);
                        } else {
                            IOPref.getInstance().saveBoolean(SetPinActivity.this, IOPref.PreferenceKey.IS_TOUCH_ID_ENABLED, true);
                        }

                    }
                }
            });
        }
    }

    @Override
    protected void bindEvent() {
        pinView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                if (pinOne.length() > 0) {
                    hideKeyboard(SetPinActivity.this);
                    pinTwo = pinView.getText().toString().trim();
                    btnSubmit.setEnabled(true);
                } else {
                    pinOne = pinView.getText().toString().trim();
                    tvEnterPin.setText(getString(iostudio.in.et.R.string.set_pin_re_enter));
                    pinView.setText("");
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinOne.equals(pinTwo)) {
                    isPinConfirmed = true;
                    IOPref.getInstance().saveString(SetPinActivity.this, IOPref.PreferenceKey.PIN, pinTwo);
                    startActivity(new Intent(SetPinActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    showMessage(getString(iostudio.in.et.R.string.err_both_pin_not_match));
                    isPinConfirmed = false;
                    initData();
                    pinView.setText("");
                }
            }
        });

    }
}
