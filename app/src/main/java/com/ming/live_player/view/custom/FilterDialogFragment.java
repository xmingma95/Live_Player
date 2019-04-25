package com.ming.live_player.view.custom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ming.live_player.R;

import java.util.ArrayList;

/**
 *滤镜设置Fragemnt
 */

public class FilterDialogFragment extends DialogFragment {

    private static final String TAG = FilterDialogFragment.class.getName();
    ArrayList<String> mFilterList;
    HorizontalPickerView mFilterPicker;
    ArrayAdapter<String> mFilterAdapter;
    private Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_filter);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        mContext = dialog.getContext();
        initView(dialog);
        initData();
        Log.d(TAG, "create fragment");
        return dialog;
    }

    private void initView(Dialog dialog) {
        mFilterPicker = (HorizontalPickerView) dialog.findViewById(R.id.filterPicker);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
    }

    private void initData() {
        mFilterList = new ArrayList<String>();
        mFilterList.add("无滤镜");
        mFilterList.add("浪漫滤镜");
        mFilterList.add("清新滤镜");
        mFilterList.add("唯美滤镜");
        mFilterList.add("粉嫩滤镜");
        mFilterList.add("怀旧滤镜");
        mFilterList.add("蓝调滤镜");
        mFilterList.add("清凉滤镜");
        mFilterList.add("日系滤镜");
        mFilterAdapter = new ArrayAdapter<String>(mContext, 0, mFilterList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String value = getItem(position);
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                }
                TextView view = (TextView) convertView.findViewById(android.R.id.text1);
                view.setTag(position);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                view.setText(value);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        ViewGroup group = (ViewGroup) mFilterPicker.getChildAt(0);
                        for (int i = 0; i < mFilterAdapter.getCount(); i++) {
                            View v = group.getChildAt(i);
                            if (v instanceof TextView) {
                                if (i == index) {
                                    ((TextView) v).setTextColor(Color.GRAY);
                                } else {
                                    ((TextView) v).setTextColor(Color.BLACK);
                                }
                            }
                        }
                        setFilter(index);
                    }
                });
                return convertView;

            }
        };
        mFilterPicker.setAdapter(mFilterAdapter);
        mFilterPicker.setClicked(0);
    }

    public void setFilterCallback(FilterCallback callback) {
            mFilterCallback = callback;
    }

    FilterCallback mFilterCallback;

    public interface FilterCallback {
        void setFilter(Bitmap filterBitmap);
    }

    private int mFilterType = FILTERTYPE_NONE; //滤镜类型
    /**
     * 滤镜定义
     */
    public static final int FILTERTYPE_NONE = 0;    //无特效滤镜
    public static final int FILTER_LANGMAN = 1;    //浪漫滤镜
    public static final int FILTER_QINGXIN = 2;    //清新滤镜
    public static final int FILTER_WEIMEI = 3;    //唯美滤镜
    public static final int FILTER_FENNEN = 4;    //粉嫩滤镜
    public static final int FILTER_HUAIJIU = 5;    //怀旧滤镜
    public static final int FILTER_LANDIAO = 6;    //蓝调滤镜
    public static final int FILTER_QINGLIANG = 7;    //清凉滤镜
    public static final int FILTER_RIXI = 8;    //日系滤镜


    protected void setFilter(int filterType) {
        mFilterType = filterType;
        Bitmap bmp = null;
        switch (filterType) {
            case FILTER_LANGMAN:
                bmp = decodeResource(getResources(), R.drawable.filter_langman);
                break;
            case FILTER_QINGXIN:
                bmp = decodeResource(getResources(), R.drawable.filter_qingxin);
                break;
            case FILTER_WEIMEI:
                bmp = decodeResource(getResources(), R.drawable.filter_weimei);
                break;
            case FILTER_FENNEN:
                bmp = decodeResource(getResources(), R.drawable.filter_fennen);
                break;
            case FILTER_HUAIJIU:
                bmp = decodeResource(getResources(), R.drawable.filter_huaijiu);
                break;
            case FILTER_LANDIAO:
                bmp = decodeResource(getResources(), R.drawable.filter_landiao);
                break;
            case FILTER_QINGLIANG:
                bmp = decodeResource(getResources(), R.drawable.fliter_qingliang);
                break;
            case FILTER_RIXI:
                bmp = decodeResource(getResources(), R.drawable.filter_rixi);
                break;
            default:
                bmp = null;
                break;
        }
        if (mFilterCallback != null) {
            mFilterCallback.setFilter(bmp);
        }
    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}
