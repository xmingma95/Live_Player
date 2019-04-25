package com.ming.live_player.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.ming.live_player.bean.Lyric;
import com.ming.live_player.model.SystemUtil;

import java.util.ArrayList;


/**
 * @author Admin
 * @version $Rev$
 * @des 自定义歌词滚动控件
 */

public class ShowLyric extends android.support.v7.widget.AppCompatTextView {

    private ArrayList<Lyric> lyrics;
    private TextPaint paint;
    private TextPaint whitepaint;
    private int width;
    private int height;
    private int index;
    private int textHeightOther;
    private int CurrentPosition;
    private long mSleepTime;
    private long mTimePoint;
    private SystemUtil systemUtil;
    private Context mContext;
    private int textPadding;
    private int textHeightCur;


    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyric(Context context) {
        this(context,null);
    }

    public ShowLyric(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShowLyric(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    private void initView() {
        lyrics=new ArrayList<>();
        paint=new TextPaint();
        systemUtil=SystemUtil.getInstance(mContext);
        textHeightCur =systemUtil.dip2px(18);
        textHeightOther =systemUtil.dip2px(16);
        textPadding =systemUtil.dip2px(10);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textHeightCur);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        whitepaint=new TextPaint();
        whitepaint.setColor(Color.parseColor("#55FFFFFF"));
        whitepaint.setTextSize(textHeightOther);
        whitepaint.setAntiAlias(true);
        whitepaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics!=null&&lyrics.size()>0){

            String currentText=lyrics.get(index).getContent();
            canvas.drawText(currentText,width/2,height/2,paint);

            int tempY=height/2;
            for (int i = index-1; i >=0; i--) {
                String preContent=lyrics.get(i).getContent();
                tempY=tempY-textHeightCur-textPadding;
                if(tempY<0){
                    break;
                }
                canvas.drawText(preContent,width/2,tempY,whitepaint);
            }
            tempY=height/2;
            for (int i = index+1; i <lyrics.size(); i++) {
                String nextContent=lyrics.get(i).getContent();

                tempY=tempY+textHeightCur+textPadding;
                if(tempY>height){
                    break;
                }
                canvas.drawText(nextContent,width/2,tempY,whitepaint);
            }
        }else{
            canvas.drawText("没有找到相关歌词",width/2,height/2,paint);
        }
    }

    public void setNextLyric(int currentPosition) {
        this.CurrentPosition=currentPosition;
        if(lyrics==null||lyrics.size()==0){
            return;
        }
        for (int i = 1; i <lyrics.size(); i++) {
            if(CurrentPosition<lyrics.get(i).getTimePoint()){
                int timeIndex=i-1;
                if(CurrentPosition>=lyrics.get(timeIndex).getTimePoint()){
                    index=timeIndex;
                    mSleepTime = lyrics.get(index).getSleepTime();
                    mTimePoint = lyrics.get(index).getTimePoint();
                    break;
                }
            }
        }
        invalidate();
    }


}
