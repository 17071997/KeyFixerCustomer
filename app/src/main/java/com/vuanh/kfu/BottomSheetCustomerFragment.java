package com.vuanh.kfu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomSheetCustomerFragment extends BottomSheetDialogFragment {
    String strTag;

    public static BottomSheetCustomerFragment newInstance(String Tag){
        BottomSheetCustomerFragment bottomSheetCustomerFragment = new BottomSheetCustomerFragment();
        Bundle args = new Bundle();
        args.putString("TAG", Tag);
        bottomSheetCustomerFragment.setArguments(args);
        return bottomSheetCustomerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strTag = getArguments().getString("TAG");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_customer, container, false);
        return view;
    }
}
