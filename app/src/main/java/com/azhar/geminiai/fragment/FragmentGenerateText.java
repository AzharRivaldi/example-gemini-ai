package com.azhar.geminiai.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.azhar.geminiai.R;
import com.azhar.geminiai.utils.CustomLoadingDialog;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

@RequiresApi(api = Build.VERSION_CODES.P)
public class FragmentGenerateText extends Fragment {

    TextView tvResult;
    TextInputEditText inputSearch;
    MaterialButton btnCopy;
    ClipboardManager clipboardManager;
    ClipData clipData;
    String strInputText;
    CustomLoadingDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomLoadingDialog(getContext());

        tvResult = view.findViewById(R.id.tvResult);
        inputSearch = view.findViewById(R.id.inputSearch);
        btnCopy = view.findViewById(R.id.btnCopy);

        //untuk memasukkan keyword yang akan dicari
        inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            strInputText = inputSearch.getText().toString();
            if (strInputText.isEmpty()) {
                Toast.makeText(getContext(), "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressDialog.show();
                    getResultGeminiAPI(strInputText);
                    InputMethodManager inputMethodManager = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
            }
            return false;
        });

        //untuk menyalin data hasil pencarian dari Gemini Ai
        btnCopy.setOnClickListener(v -> {
            strInputText = inputSearch.getText().toString();
            if (strInputText.isEmpty()) {
                Toast.makeText(getContext(), "Result tidak boleh kosong!",
                        Toast.LENGTH_SHORT).show();
            } else {
                clipboardManager = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("result", tvResult.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Result telah disalin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method untuk memanggil function Gemini AI
    //jangan lupa membuat API KEY terlebih dahulu
    public void getResultGeminiAPI(String inputText) {
        GenerativeModel generativeModel = new GenerativeModel("gemini-pro",
                "API KEY");

        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(generativeModel);
        Content content = new Content.Builder()
                .addText(inputText) //bisa pakai selain bahasa Indonesia
                .build();

        ListenableFuture<GenerateContentResponse> responseFuture = modelFutures.generateContent(content);
        Futures.addCallback(responseFuture, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                tvResult.setText(result.getText().replace("*", ""));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                tvResult.setText(t.toString());
                progressDialog.dismiss();
            }
        }, getContext().getMainExecutor());
    }

}