package com.liuhai.wcbox.lock.ads;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class AdsSplash_YouMi {
//
//    private WindowManager mWindowManager;
//    private WindowManager.LayoutParams mLayoutParams;
//    private LayoutInflater mLayoutInflater;
//    private FrameLayout mFloatView;
//    private ImageView icon;
//    private TextView title;
//    private Context con;
//
//    private int [] layoutids={R.layout.float_open_other_app,R.layout.float_open_other_app01,R.layout.float_open_other_app02,R.layout.float_open_other_app03,R.layout.float_open_other_app04};
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0)
//                initView();
//            if (msg.what == 1)
//                remove();
//
//
//        }
//    };
//
//    public AdsSplash_YouMi(Context con) {
//        this.con = con;
//
//    }
//
//    BeanApp app;
//    public void show(BeanApp app) {
//        this.app=app;
//        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        mLayoutInflater = LayoutInflater.from(con);
//        mFloatView = (FrameLayout) mLayoutInflater.inflate(layoutids[(int)(Math.random()*4)], null);
//        icon= (ImageView) mFloatView.findViewById(R.id.icon);
//        title= (TextView) mFloatView.findViewById(R.id.title);
//        icon.setBackground(app.getIcon());
//        title.setText(app.getTitle());
//        mLayoutParams = new WindowManager.LayoutParams();
//        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
//        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        mLayoutParams.format = PixelFormat.RGBA_8888;
//        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        mLayoutParams.x = 1;
//        mLayoutParams.y = 1;
//
//        mFloatView.setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        handler.sendEmptyMessageDelayed(0, 100);
//
//
//    }
//
//
//    public void initView() {
//        if(MySettings.isAdsShowTimeOut(con)) {
//            try {
//                MyAdManager.init(con);
//                setupSplashAd();
//                mWindowManager.addView(mFloatView, mLayoutParams);
//                handler.sendEmptyMessageDelayed(1, 6000);
//                log("开屏展示.........");
//            } catch (Exception e) {
//
//            }
//
//        }
//    }
//
//
//    public void remove() {
//        try {
//            if (mWindowManager != null && mFloatView != null)
//                mWindowManager.removeView(mFloatView);
//            mFloatView = null;
//            log("开屏隐藏.........");
//        } catch (Exception e) {
//
//        }
//
//
//    }
//
//
//    /**
//     * 设置开屏广告
//     */
//    private void setupSplashAd() {
//        log("setupSplashAd");
//        final RelativeLayout splashLayout = (RelativeLayout) mFloatView.findViewById(R.id.open_other_app_rl_splash);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        SplashViewSettings splashViewSettings = new SplashViewSettings();
//        splashViewSettings.setTargetClass(EmptyActivity.class);
//        splashViewSettings.setSplashViewContainer(splashLayout);
//        SpotManager.getInstance(con)
//                .showSplash(con, splashViewSettings, new SpotListener() {
//
//                    @Override
//                    public void onShowSuccess() {
//                        log("广告展示成功");
//                        MySettings.setAdsShowTime(con);
//                        MobclickAgent.onEvent(con,"click26");
//                    }
//
//                    @Override
//                    public void onShowFailed(int errorCode) {
//                        MobclickAgent.onEvent(con,"click28");
//                        log("开屏展示失败");
//                        handler.sendEmptyMessageDelayed(1, 0);
//                        switch (errorCode) {
//                            case ErrorCode.NON_NETWORK:
//                                log("网络异常");
//                                break;
//                            case ErrorCode.NON_AD:
//                                log("暂无开屏广告");
//                                break;
//                            case ErrorCode.RESOURCE_NOT_READY:
//                                log("开屏资源还没准备好");
//                                break;
//                            case ErrorCode.SHOW_INTERVAL_LIMITED:
//                                log("开屏展示间隔限制");
//                                break;
//                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
//                                log("开屏控件处在不可见状态");
//                                break;
//                            default:
//                                log("errorCode: " + errorCode);
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onSpotClosed() {
//                        log("开屏被关闭");
//                    }
//
//                    @Override
//                    public void onSpotClicked(boolean isWebPage) {
//                        log("开屏被点击");
//                        MobclickAgent.onEvent(con,"click27");
//                    }
//                });
//    }
//
//    private void log(String s) {
//        Log.i(getClass().getSimpleName(), s);
//    }

}
