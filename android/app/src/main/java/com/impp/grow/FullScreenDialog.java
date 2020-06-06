package com.impp.grow;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FullScreenDialog extends DialogFragment {

    private Button loadSheetButton;
    private Button cancelButton;
    private View view;
    private Dialog dialog;
    private TextInputEditText textInputEditText;
    private TextInputLayout textInputLayout;
    private MainActivity mainActivity;
    private boolean atStart;
    private CheckBox checkBox;

    public static String TAG = "FullScreenDialog";


    public FullScreenDialog(MainActivity mainActivity, boolean atStart){

        this.mainActivity = mainActivity;
        this.atStart = atStart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        view = inflater.inflate(R.layout.fragment_edit_sheet_url, container, false);
        textInputEditText = (TextInputEditText) view.findViewById(R.id.textUrl);
        cancelButton = (Button) view.findViewById(R.id.buttonCancel);
        loadSheetButton = (Button) view.findViewById(R.id.buttonLoadSheet);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.filledTextField);
        checkBox = (CheckBox)  view.findViewById(R.id.checkbox_sample_table);

        if(atStart==true) {
            cancelButton.setVisibility(View.GONE);
        }
        else {
            // set onclicklistener
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    LinearLayout linearLayoutSheetUrl = mainActivity.findViewById(R.id.sheet_url_section);
                    linearLayoutSheetUrl.setEnabled(true);

                }
            });
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    loadSheetButton.setEnabled(true);
                    textInputEditText.setText("");
                    textInputLayout.setErrorEnabled(false);
                    textInputEditText.setText("https://docs.google.com/spreadsheets/d/e/2PACX-1vTZMOCrZdhsWPB4O-YiLrfE_sR2DcU3hgHQyg1y-_R648YOP3uX9eb0-gAqJN4Re70swEOONzS5t-Yc/pubhtml");
                    textInputLayout.setEnabled(false);
                    textInputEditText.setEnabled(false);
                }
                else {
                    loadSheetButton.setEnabled(false);
                    textInputEditText.setText("");
                    textInputLayout.setEnabled(true);
                }
            }
        });


        // set onclicklistener
        loadSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()) {
                    if(checkBox.isChecked())
                    {
                        textInputEditText.setEnabled(false);
                        loadSheetButton.setEnabled(false);
                        cancelButton.setEnabled(false);
                        mainActivity.setGoogleSheet("https://docs.google.com/spreadsheets/d/e/2PACX-1vTZMOCrZdhsWPB4O-YiLrfE_sR2DcU3hgHQyg1y-_R648YOP3uX9eb0-gAqJN4Re70swEOONzS5t-Yc/pubhtml");
                        dialog.hide();
                        LinearLayout linearLayoutSheetUrl = mainActivity.findViewById(R.id.sheet_url_section);
                        linearLayoutSheetUrl.setEnabled(true);
                    }
                    else {

                        String url = textInputEditText.getText().toString();

                        if (BackendInterface.checkGoogleSheetURL(url)) {
                            textInputEditText.setEnabled(false);
                            loadSheetButton.setEnabled(false);
                            cancelButton.setEnabled(false);
                            mainActivity.setGoogleSheet(url);
                            dialog.hide();
                            LinearLayout linearLayoutSheetUrl = mainActivity.findViewById(R.id.sheet_url_section);
                            linearLayoutSheetUrl.setEnabled(true);
                        } else {
                            textInputLayout.setError("Enter a valid google sheet url.");
                        }
                    }
                }
                else {
                    Snackbar snackbar = Snackbar.make(loadSheetButton, "Please connect to the internet and try again", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });


        textInputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (textInputEditText.getRight() - textInputEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        textInputLayout.setErrorEnabled(false);

                        if(clipboard.hasPrimaryClip()){
                            textInputEditText.setText("");
                            if(checkBox.isChecked()){
                                checkBox.setChecked(false);
                            }
                            CharSequence clipboardText = clipboard.getPrimaryClip().getItemAt(0).getText();
                            textInputEditText.setText(clipboardText);
                            if(!BackendInterface.checkGoogleSheetURL(clipboardText.toString())){
                                textInputLayout.setError("Enter a valid google sheet url.");
                            }
                            loadSheetButton.setEnabled(true);
                        }
                        else {
                            Snackbar snackbar = Snackbar.make(loadSheetButton, "No content in clipboard. Please copy your url.", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        textInputEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    loadSheetButton.setEnabled(true);
                }
                else
                {
                    loadSheetButton.setEnabled(false);
                }
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        dialog = new Dialog(getActivity());
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E7BFF")));
        if(atStart==false)
        {
            dialog.getWindow().getAttributes().windowAnimations = R.style.FullScreenDialogStyle; //style id
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
                Log.d("Internet", "Wifi connected: " + isWifiConn);
                return true;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
                Log.d("Internet", "Mobile connected: " + isMobileConn);
                return true;
            }
        }
        return false;
    }


}