package com.camera360.yuyidong.fragment.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.camera360.yuyidong.fragment.listener.SeekBarChangeListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuyidong on 14-11-5.
 */
public class ShowView extends SurfaceView implements View.OnTouchListener,Runnable,SeekBarChangeListener.StatusCallBack{
    /**
     * 隐藏SeekBar
     */
    private static final int HIDDEN_SEEKBAR=0;
    /**
     * 要去操作的seekbar
     */
    private SeekBar mSeekBar;
    /**
     * 显示seekbar的标志
     * true为显示
     * false为隐藏
     */
    private boolean mShowFlag=true;
    /**
     *手指是否还在seekbar上
     * true为在上面
     * false为离开
     */
    private boolean mFingerOnFlag = false;
    /**
     * 按下时候的时间戳
     */
    private long mDonwTime;
    /**
     * 单线程池,去运行Runnable
     */
    ExecutorService mSinglePool = Executors. newSingleThreadExecutor();

    /**
     * handler
     */
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case HIDDEN_SEEKBAR:
                    if(mFingerOnFlag == false && mShowFlag == true) {
                        //隐藏
                        mSeekBar.setVisibility(View.INVISIBLE);
                        mShowFlag = false;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public ShowView(Context context) {
        super(context);
        mDonwTime = System.currentTimeMillis();
        mSinglePool.execute(this);
        this.setOnTouchListener(this);
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDonwTime = System.currentTimeMillis();
        mSinglePool.execute(this);
        this.setOnTouchListener(this);
    }

    public ShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDonwTime = System.currentTimeMillis();
        mSinglePool.execute(this);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction())
        {

            case MotionEvent.ACTION_DOWN:
                mSeekBar.setVisibility(View.VISIBLE);//显示seekbar
                mDonwTime = System.currentTimeMillis();//获取手指放下的时间
                mShowFlag = true;//seekbar为显示
                break;
            case MotionEvent.ACTION_UP:
                mSinglePool.execute(this);
                break;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //如果seekbar再显示&&手指不再seekbar上
        if(mShowFlag == true && mFingerOnFlag == false)
        {
            handler.sendEmptyMessage(HIDDEN_SEEKBAR);
        }
    }

    /**
     * 得到seekbar
     * @param mSeekBar
     */
    public void setmSeekBar(SeekBar mSeekBar) {
        this.mSeekBar = mSeekBar;
    }

    /**
     * 判断手指是否再seekbar上的回调函数
     * @param status false为不再seekbar上，seekbar为再seekbar上
     */
    @Override
    public void getStatus(boolean status) {
        if(status == false)
        {
            mSinglePool.execute(this);
        }
        mFingerOnFlag = status;
    }
}
