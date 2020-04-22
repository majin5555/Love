package sdust.project.valentinesday;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;


public class AbnerGameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private              SurfaceHolder holder;
    private              Paint         mPaint;
    private              boolean       isStartGame;
    private              Thread        thread;
    /**
     * 游戏前
     */
    private static final int           GAME_START = 0;
    /**
     * 游戏中
     */
    private static final int           GAME_ING   = 1;
    /**
     * 游戏结束
     */
    private static final int           GAME_OVER  = - 1;
    /**
     * 游戏状态
     */
    private static       int           gameState  = GAME_START;
    /**
     * 屏幕宽度和高度
     */
    private              int           windowWidth, windowHeight;

    /**
     * 底部虚线xy坐标
     */
    private int[] floorLine = new int[2];

    /**
     * 虚线宽度
     */
    private int floorLine_width = 15;

    /**
     * 移动速度
     */
    private int              speed               = 4;
    /**
     * 关数坐标
     */
    private int[]            levelText           = new int[2];
    /**
     * 第几关
     */
    private int              level_value         = 1;
    /**
     * 球的xy坐标
     */
    private int[]            circle              = new int[2];
    private int              circle_width        = 10;
    private int              circle_v            = 0;
    private int              circle_a            = 2;
    private int              circle_vUp          = - 12;
    private int              wall_w              = 50;
    private int              wall_h              = 100;
    private int              wall_step           = 30;
    private int              move_step           = 0;
    private ArrayList<int[]> removeRectangleList = new ArrayList<int[]>();
    private ArrayList<int[]> rectangleList       = new ArrayList<int[]>();
    private Canvas           canvas;

    public AbnerGameSurfaceView(Context context) {
        super(context);
        init();

    }

    public AbnerGameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public AbnerGameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔,获得SurfaceHolder:提供访问和控制SurfaceView背后的Surface 相关的方法
     * //设置抗锯齿,图像边缘相对清晰一点
     * //空心
     * //设置可以获取焦点
     * //设置此视图是否可以在触摸模式下接收焦点
     * //控制屏幕是否应继续
     */
    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(50);
        mPaint.setStyle(Paint.Style.STROKE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        playFromAssetsFile();
    }

    /**
     * surface创建的时候调用，一般在该方法中启动绘图的线程
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isStartGame = true;
        windowWidth = this.getWidth();
        windowHeight = this.getHeight();
        initGame();
        thread = new Thread(this);
        thread.start();
    }

    /**
     * 游戏开始前进行初始化
     */
    private void initGame() {
        if (gameState == GAME_START) {
            floorLine[0] = 0;
            floorLine[1] = windowHeight - windowHeight / 5;

            levelText[0] = windowWidth / 2;
            levelText[1] = windowHeight - 100;
            level_value = 1;

            circle[0] = windowWidth / 3;
            circle[1] = windowHeight / 2;

            rectangleList.clear();

            floorLine_width = dp2px(15);

            speed = dp2px(4);

            circle_width = dp2px(10);
            circle_a = dp2px(2);
            circle_vUp = - dp2px(12);

            wall_w = dp2px(45);
            wall_h = dp2px(100);

            wall_step = wall_w * 4;
        }
    }

    private int dp2px(float dp) {
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
        return px;
    }

    /**
     * surface尺寸发生改变的时候调用，如横竖屏切换
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * surface被销毁的时候调用，如退出游戏画面，一般在该方法中停止绘图线程。
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isStartGame = false;
        stopPlayIng();
    }

    @Override
    public void run() {
        while (isStartGame) {
            long start = System.currentTimeMillis();
            drawGame();
            startGame();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        switch (gameState) {
            case GAME_START://游戏开始前

                break;
            case GAME_ING://游戏进行时
                circle_v += circle_a;
                circle[1] += circle_v;
                if (circle[1] > floorLine[1] - circle_width) {
                    circle[1] = floorLine[1] - circle_width;
                    gameState = GAME_OVER;//圆掉到了虚线以下
                }

                //虚线的滚动
                if (floorLine[0] < - floorLine_width) {
                    floorLine[0] += floorLine_width * 2;
                }
                floorLine[0] -= speed;


                removeRectangleList.clear();
                for (int i = 0; i < rectangleList.size(); i++) {
                    int[] wall = rectangleList.get(i);
                    wall[0] -= speed;
                    if (wall[0] < - wall_w) {
                        removeRectangleList.add(wall);
                    } else if (wall[0] - circle_width <= circle[0] && wall[0] + wall_w + circle_width >= circle[0]
                            && (circle[1] <= wall[1] + circle_width || circle[1] >= wall[1] + wall_h - circle_width)) {
                        gameState = GAME_OVER;
                    }

                    int pass = wall[0] + wall_w + circle_width - circle[0];
                    if (pass < 0 && - pass <= speed) {
                        level_value++;
                    }
                }
                if (removeRectangleList.size() > 0) {
                    rectangleList.removeAll(removeRectangleList);
                }

                move_step += speed;
                if (move_step > wall_step) {
                    //坐标随机产生
                    int[] wall = new int[]{windowWidth, (int) (Math.random() * (floorLine[1] - 2 * wall_h) + 0.5 * wall_h)};
                    rectangleList.add(wall);
                    move_step = 0;
                }

                break;
            case GAME_OVER:
                if (circle[1] < floorLine[1] - circle_width) {
                    circle_v += circle_a;
                    circle[1] += circle_v;
                    if (circle[1] >= floorLine[1] - circle_width) {
                        circle[1] = floorLine[1] - circle_width;
                    }
                } else {
                    AbnerGameCircleActivity.mAbnerGameCircleActivity.showAchievement(level_value);
                    gameState = GAME_START;
                    initGame();
                }
                break;
        }
    }

    /**
     * 绘制小球,虚线,障碍物,关数
     */
    private void drawGame() {

        try {
            canvas = holder.lockCanvas();//获取画笔
            mPaint.setColor(Color.WHITE);
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                int floor_start = floorLine[0];

                mPaint.setStrokeWidth(20);
                //绘制虚线
                while (floor_start < windowWidth) {
                    canvas.drawLine(floor_start, floorLine[1], floor_start + floorLine_width, floorLine[1], mPaint);
                    floor_start += floorLine_width * 2;
                }
                mPaint.setStrokeWidth(1);
                //绘制圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(circle[0], circle[1], circle_width, mPaint);
                mPaint.setColor(Color.BLACK);
                canvas.drawText("驴", circle[0] - 25, circle[1] + 20, mPaint);
                mPaint.setColor(Color.WHITE);
                //绘制关数
                canvas.drawText("第" + String.valueOf(level_value) + "关", levelText[0] - 50, levelText[1], mPaint);
                //绘制阻碍物
                for (int i = 0; i < rectangleList.size(); i++) {
                    int[] wall = rectangleList.get(i);
                    float[] pts = {
                            wall[0], 0, wall[0], wall[1],
                            wall[0], wall[1] + wall_h, wall[0], floorLine[1],
                            wall[0] + wall_w, 0, wall[0] + wall_w, wall[1],
                            wall[0] + wall_w, wall[1] + wall_h, wall[0] + wall_w, floorLine[1],
                            wall[0], wall[1], wall[0] + wall_w, wall[1],
                            wall[0], wall[1] + wall_h, wall[0] + wall_w, wall[1] + wall_h
                    };
                    canvas.drawLines(pts, mPaint);
                }
                //canvas.drawText("author:AbnerMing", 20, 60, mPaint);

            }
        } catch (Exception e) {

        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (gameState) {
                case GAME_START:

                    gameState = GAME_ING;
                case GAME_ING:
                    circle_v = circle_vUp;
                    break;
                case GAME_OVER:
                    if (circle[1] >= floorLine[1] - circle_width) {
                        gameState = GAME_START;
                        initGame();
                        stopPlayIng();
                    }

                    break;
            }
        }
        return true;
    }

    private void stopPlayIng() {
        if (player.isPlaying()) {
            player.release();
            Log.d("majin", "游戏结束，停止播放");
        }
    }


    /**
     * 播放背景音
     */
    MediaPlayer player = null;

    private void playFromAssetsFile() {
        AssetManager assetManager;
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player != null && ! player.isPlaying()) {
            assetManager = getResources().getAssets();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd("huohongdesarilang.mp3");
                player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                player.prepare();
                player.start();
                Log.d("majin", "游戏开始，开始播放啊");

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("majin", "e.printStackTrace()" );
            }
        }
    }
}
