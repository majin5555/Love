package sdust.project.valentinesday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/6.
 */
public class HeartView extends View {
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    private class Node
    {
        public int mImageSeq;
        public int mPositionY;
        public int mDestXPos;
        public boolean mRight;
        public int mImageWidth;

        public int mLimitLeft;
        public int mLimitRight;

        Node()
        {

            mImageSeq = 0;
            mDestXPos = 0;
            mPositionY = 100;
            mRight = true;
            mImageWidth = 0;

            mLimitLeft = 0;
            mLimitRight = 0;
        }
    }

    private List<Node>   mList       = new LinkedList<>();
    private List<Bitmap> mHeartImage = new ArrayList<>();

    private Random random      = new Random();
    private int    mJumpHeight = 0;

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        ini();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取当前实例的高
        mViewHeight = this.getHeight();
        //获取当前实例的宽
        mViewWidth = this.getWidth();

        mJumpHeight = (mViewHeight - 150) / 30;
    }

    public void ini()
    {
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart0)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart1)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart2)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart3)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart4)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart5)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart0)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart1)).getBitmap());
        mHeartImage.add(((BitmapDrawable)getResources().getDrawable(R.mipmap.heart8)).getBitmap());
    }

    long lastClick=0;
    public void addFavor()
    {
        long curClick = System.currentTimeMillis();
        if (curClick - lastClick < 300) {
            Log.i("AAA", "not enough");
            return;
        }

        Log.i("BBB", "Begin show");

        lastClick = curClick;

        Node n = new Node();
        n.mPositionY = getHeight() - 100;
        n.mImageSeq = random.nextInt(8);
        //n.mDestXPos = random.nextInt(getWidth());
        n.mRight = random.nextBoolean();
        n.mImageWidth = mHeartImage.get(n.mImageSeq).getWidth();
        n.mDestXPos = (getWidth() - n.mImageWidth) / 2;
        int nGap = (n.mDestXPos > 30 ? 30 : n.mDestXPos);
        n.mLimitLeft = n.mDestXPos - nGap;
        n.mLimitRight = n.mDestXPos + nGap;
        mList.add(n);

        if (!mIsRefreshing)
        {
            mTimer = new Timer();
            mTimer.schedule(new MyTask(), 1, 200);
            mIsRefreshing = true;
        }
    }

    Timer   mTimer        = null;
    Paint   paint;
    Boolean mIsRefreshing = false;

    public class MyTask extends TimerTask {
        @Override
        public void run(){
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);



        Log.i("AAA", "Total num is " + mList.size());

        Iterator<Node> iter = mList.iterator();

        // 每次所有的又要上飘，并且将已经出界的移除
        while (iter.hasNext())
        {
            Node node = iter.next();

            if (node.mPositionY < mJumpHeight)
            {
                iter.remove();
                if (mList.size() == 0)
                {
                    mTimer.cancel();
                    mIsRefreshing = false;
                }
            }
            else
            {
                node.mPositionY -= mJumpHeight;
                if (node.mRight)
                    node.mDestXPos += random.nextInt(800);
                else
                    node.mDestXPos -= random.nextInt(200);

                if (node.mDestXPos > node.mLimitRight)
                    node.mRight = false;
                else if (node.mDestXPos < node.mLimitLeft)
                    node.mRight = true;

                canvas.drawBitmap(mHeartImage.get(node.mImageSeq), node.mDestXPos, node.mPositionY, paint);
            }
        }
    }
}
