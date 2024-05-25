package com.example.kursach.DB.Sights;

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

public class BottomSheetSight extends BottomSheetDialogFragment {

    public static final String TAG = "CustomBottomSheet"; // Добавили тег для журналирования

    public BottomSheetListener mListener;

    public interface BottomSheetListener {
        void onButtonClicked(String title, String data, String description);
    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_sight, container, false);

        EditText editTextTitle = view.findViewById(R.id.field1);
        EditText editTextData = view.findViewById(R.id.field2);
        EditText editTextDescription = view.findViewById(R.id.field3);
        Button buttonSubmit = view.findViewById(R.id.save_button);

        buttonSubmit.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String data = editTextData.getText().toString();
            String description = editTextDescription.getText().toString();
            if (mListener != null) {
                mListener.onButtonClicked(title, data, description);
            } else {
                Log.e("CustomBottomSheet", "BottomSheetListener is null!");
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
