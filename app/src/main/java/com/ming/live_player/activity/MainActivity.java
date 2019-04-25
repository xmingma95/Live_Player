package com.ming.live_player.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.ming.live_player.R;
import com.ming.live_player.base.BaseActivity;
import com.ming.live_player.fragment.LiveFragment;
import com.ming.live_player.fragment.MusicFragment;
import com.ming.live_player.fragment.PlayerFragment;
import com.ming.live_player.service.MusicPlayerService;
import com.ming.live_player.view.MainView;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements MainView {

    public FragmentTabHost tabs;
    private ImageButton ib_menu_add;
    //fragment列表
    private final Class mFragmentArray[] = {PlayerFragment.class, LiveFragment.class, MusicFragment.class};
    private int mImageViewArray[] = {R.layout.tab_view_player,
            R.layout.tab_view_live, R.layout.tab_view_music};
    private String mTextviewArray[] = {"live", "publish", "my"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performCodeWithPermission("SD", new PermissionCallback() {
            @Override
            public void hasPermission() {

            }

            @Override
            public void noPermission() {

            }
        },Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
        initView();
        initFragment();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        ib_menu_add =(ImageButton)findViewById(R.id.ib_menu_add);
        tabs =(FragmentTabHost) findViewById(R.id.tabs);
        //绑定layout,这一句不加会有空指针
        tabs.setup(this, getSupportFragmentManager(), R.id.fl_main);
        ib_menu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    /**
     * 初始化fragment
     */
    @Override
    public void initFragment() {
        int fragmentCount = mFragmentArray.length;
        for (int i = 0; i < fragmentCount; i++) {
            //获得第i个选项卡的View
            View view = LayoutInflater.from(this).inflate(mImageViewArray[i], null);
            //创建第i个选项卡，该选项卡的标识为tags.get(i)，该选项卡的样式为view
            TabHost.TabSpec tab = tabs.newTabSpec(mTextviewArray[i]).setIndicator(view);
            // 建立选项卡和Fragment的对应关系，当点击该选项卡时在FrameLayout中
            // 显示list中第i个Fragment
            tabs.addTab(tab, mFragmentArray[i], null);
            // 其它设置 选项卡之间没有分割线
            tabs.getTabWidget().setDividerDrawable(null);
        }
        tabs.setCurrentTab(1);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        // 这里的view代表popupMenu需要依附的view
         PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
         // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.menu_add, popupMenu.getMenu());
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()){
                    case R.id.start_live:
                        Log.e("MainActivity","startLIVE");
                        Intent intent=new Intent(MainActivity.this,PublishSettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.exit_app:
                        Log.e("MainActivity","exit_app");
                        showAlertDialog();
                        break;
                }
                return true; } });
        //popupMenu.show();
        //使用反射,强制显示菜单图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            mHelper.setForceShowIcon(true);
            mHelper.show();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("提示").setMessage("确定退出Live_Player？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //退出服务
                        Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
                        intent.setAction("com.ming.mingplayer_musicplayer");
                        stopService(intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
        //修改按键字体颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

}
