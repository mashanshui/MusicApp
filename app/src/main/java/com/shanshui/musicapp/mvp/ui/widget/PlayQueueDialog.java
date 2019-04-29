package com.shanshui.musicapp.mvp.ui.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.shanshui.musicapp.R;

/**
 * @author mashanshui
 * @date 2019-04-29
 * @desc TODO
 */
public class PlayQueueDialog extends BottomSheetDialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = ConvertUtils.dp2px(300);
        window.setAttributes(layoutParams);
        window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.transparent));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
