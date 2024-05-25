package com.example.kursach.DB.Routs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kursach.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetRout extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetRout"; // Добавили тег для журналирования

    public BottomSheetListener mListener;

    public interface BottomSheetListener {
        void onButtonClicked(String title, String length, String description);
    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_routs, container, false);

        EditText editTextTitle = view.findViewById(R.id.field1);
        EditText editTextLength = view.findViewById(R.id.field2);
        EditText editTextDescription = view.findViewById(R.id.field3);
        Button buttonSubmit = view.findViewById(R.id.save_button);

        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String length = editTextLength.getText().toString();
            String description = editTextDescription.getText().toString();
            if (mListener != null) {
                mListener.onButtonClicked(title, length, description);
            } else {
                Log.e(TAG, "BottomSheetListener is null!");
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.RoundedTopBottomSheetDialogTheme);
    }
}
