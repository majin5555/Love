/*
 *  http://www.appcodes.cn APP精品源码下载站声明：
 * 1、本站源码为网上搜集或网友提供，如果涉及或侵害到您的版 权，请立即通知我们。
 * 2、 本站提供免费代码只可供研究学习使用，切勿用于商业用途 由此引起一切后果与本站无关。
 * 3、 商业源码请在源码授权范围内进行使用。
 * 4、更多APP精品源码下载请访问:http://www.appcodes.cn。
 * 5、如有疑问请发信息至appcodes@qq.com。
 */
package sdust.project.valentinesday;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class PlayGround extends SurfaceView implements OnTouchListener {

	// 行数
	private static final int      ROW = 9;
	// 列数
	private static final int      COL = 9;
	// 障碍的数量
	private static final int      BOCKS = COL * ROW / 5;
	// 屏幕宽度
	private              int      SCREEN_WIDTH;
	// 每个通道的宽度
	private              int      WIDTH;
	// 奇数行和偶数行通道间的位置偏差量
	private              int      DISTANCE;
	// 屏幕顶端和通道最顶端间的距离
	private              int      OFFSET;
	// 整个通道与屏幕两端间的距离
	private              int      length;
	// 做成神经猫动态图效果的单张图片
	private              Drawable cat_drawable;
	// 背景图
	private              Drawable background;
	// 神经猫动态图的索引
	private              int      index = 0;

	private Dot[][] matrix;

	private Dot cat;

	private Timer timer = null;

	private TimerTask timerttask = null;

	private Context context;

	private int steps;

	private int[] images = { R.drawable.cat1, R.drawable.cat2, R.drawable.cat3,
			R.drawable.cat4, R.drawable.cat5, R.drawable.cat6, R.drawable.cat7,
			R.drawable.cat8, R.drawable.cat9, R.drawable.cat10,
			R.drawable.cat11, R.drawable.cat12, R.drawable.cat13,
			R.drawable.cat14, R.drawable.cat15, R.drawable.cat16 };

	@SuppressLint("ClickableViewAccessibility")
	public PlayGround(Context context) {
		super(context);
		matrix = new Dot[ROW][COL];
		cat_drawable = getResources().getDrawable(images[index]);
		background = getResources().getDrawable(R.drawable.bg);
		this.context = context;
		initGame();
		getHolder().addCallback(callback);
		setOnTouchListener(this);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			stopTimer();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 初始化游戏
	private void initGame() {
		steps=0;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				matrix[i][j] = new Dot(j, i);
			}
		}
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				matrix[i][j].setStatus(Dot.STATUS_OFF);
			}
		}
		cat = new Dot(COL / 2 - 1, ROW / 2 - 1);
		getDot(cat.getX(), cat.getY()).setStatus(Dot.STATUS_IN);
		for (int i = 0; i < BOCKS;) {
			int x = (int) ((Math.random() * 1000) % COL);
			int y = (int) ((Math.random() * 1000) % ROW);
			if (getDot(x, y).getStatus() == Dot.STATUS_OFF) {
				getDot(x, y).setStatus(Dot.STATUS_ON);
				i++;
			}
		}
	}

	// 绘图
	private void redraw() {
		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor(Color.GRAY);
		Paint paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				DISTANCE = 0;
				if (i % 2 != 0) {
					DISTANCE = (int) WIDTH / 2;
				}
				Dot dot = getDot(j, i);
				switch (dot.getStatus()) {
				case Dot.STATUS_IN:
					paint.setColor(0XFFEEEEEE);
					break;
				case Dot.STATUS_ON:
					paint.setColor(0XFFFFAA00);
					break;
				case Dot.STATUS_OFF:
					paint.setColor(0X74000000);
					break;
				default:
					break;
				}
				canvas.drawOval(new RectF(dot.getX() * WIDTH + DISTANCE
						+ length, dot.getY() * WIDTH + OFFSET, (dot.getX() + 1)
						* WIDTH + DISTANCE + length, (dot.getY() + 1) * WIDTH
						+ OFFSET), paint);
			}
		}
		int left = 0;
		int top = 0;
		if (cat.getY() % 2 == 0) {
			left = cat.getX() * WIDTH;
			top = cat.getY() * WIDTH;
		} else {
			left = (int) (WIDTH / 2) + cat.getX() * WIDTH;
			top = cat.getY() * WIDTH;
		}
		// 此处神经猫图片的位置是根据效果图来调整的
		cat_drawable.setBounds(left - WIDTH / 6 + length, top - WIDTH / 2
				+ OFFSET, left + WIDTH + length, top + WIDTH + OFFSET);
		cat_drawable.draw(canvas);
		background.setBounds(0, 0, SCREEN_WIDTH, OFFSET);
		background.draw(canvas);
		getHolder().unlockCanvasAndPost(canvas);
	}

	Callback callback = new Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			redraw();
			startTimer();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
			WIDTH = width / (COL + 1);
			OFFSET = height - WIDTH * ROW - 2 * WIDTH;
			length = WIDTH / 3;
			SCREEN_WIDTH = width;
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			stopTimer();
		}
	};

	// 开启定时任务
	private void startTimer() {
		timer = new Timer();
		timerttask = new TimerTask() {
			public void run() {
				gifImage();
			}
		};

		timer.schedule(timerttask, 50, 65);
	}

	// 停止定时任务
	public void stopTimer() {
		timer.cancel();
		timer.purge();
	}

	// 动态图
	private void gifImage() {
		index++;
		if (index > images.length - 1) {
			index = 0;
		}
		cat_drawable = getResources().getDrawable(images[index]);
		redraw();
	}

	// 获取通道对象
	private Dot getDot(int x, int y) {
		return matrix[y][x];
	}

	// 判断神经猫是否处于边界
	private boolean inEdge(Dot dot) {
		if (dot.getX() * dot.getY() == 0 || dot.getX() + 1 == COL
				|| dot.getY() + 1 == ROW) {
			return true;
		}
		return false;
	}

	// 移动cat至指定点
	private void moveTo(Dot dot) {
		dot.setStatus(Dot.STATUS_IN);
		getDot(cat.getX(), cat.getY()).setStatus(Dot.STATUS_OFF);
		cat.setXY(dot.getX(), dot.getY());
	}

	// 获取one在方向dir上的可移动距离
	private int getDistance(Dot one, int dir) {
		int distance = 0;
		if (inEdge(one)) {
			return 1;
		}
		Dot ori = one;
		Dot next;
		while (true) {
			next = getNeighbour(ori, dir);
			if (next.getStatus() == Dot.STATUS_ON) {
				return distance * -1;
			}
			if (inEdge(next)) {
				distance++;
				return distance;
			}
			distance++;
			ori = next;
		}
	}

	// 获取dot的相邻点，返回其对象
	private Dot getNeighbour(Dot dot, int dir) {
		switch (dir) {
		case 1:
			return getDot(dot.getX() - 1, dot.getY());
		case 2:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX() - 1, dot.getY() - 1);
			} else {
				return getDot(dot.getX(), dot.getY() - 1);
			}
		case 3:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX(), dot.getY() - 1);
			} else {
				return getDot(dot.getX() + 1, dot.getY() - 1);
			}
		case 4:
			return getDot(dot.getX() + 1, dot.getY());
		case 5:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX(), dot.getY() + 1);
			} else {
				return getDot(dot.getX() + 1, dot.getY() + 1);
			}
		case 6:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX() - 1, dot.getY() + 1);
			} else {
				return getDot(dot.getX(), dot.getY() + 1);
			}
		}
		return null;
	}

	// cat的移动算法
	private void move() {
		if (inEdge(cat)) {
			failure();
			return;
		}
		Vector<Dot> available = new Vector<Dot>();
		Vector<Dot> direct = new Vector<Dot>();
		HashMap<Dot, Integer> hash = new HashMap<Dot, Integer>();
		for (int i = 1; i < 7; i++) {
			Dot n = getNeighbour(cat, i);
			if (n.getStatus() == Dot.STATUS_OFF) {
				available.add(n);
				hash.put(n, i);
				if (getDistance(n, i) > 0) {
					direct.add(n);
				}
			}
		}
		if (available.size() == 0) {
			win();
		} else if (available.size() == 1) {
			moveTo(available.get(0));
		} else {
			Dot best = null;
			if (direct.size() != 0) {
				int min = 20;
				for (int i = 0; i < direct.size(); i++) {
					if (inEdge(direct.get(i))) {
						best = direct.get(i);
						break;
					} else {
						int t = getDistance(direct.get(i),
								hash.get(direct.get(i)));
						if (t < min) {
							min = t;
							best = direct.get(i);
						}
					}
				}
			} else {
				int max = 1;
				for (int i = 0; i < available.size(); i++) {
					int k = getDistance(available.get(i),
							hash.get(available.get(i)));
					if (k < max) {
						max = k;
						best = available.get(i);
					}
				}
			}
			moveTo(best);
		}
		// redraw();
		if (inEdge(cat)) {
			failure();
		}
	}

	// 通关失败
	private void failure() {
		Builder dialog = new Builder(context);
		dialog.setTitle("通关失败");
		dialog.setMessage("你让神经猫逃出精神院啦(ˉ▽ˉ；)...");
		dialog.setCancelable(false);
		dialog.setNegativeButton("再玩一次", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				initGame();
			}
		});
		dialog.setPositiveButton("取消", null);
		dialog.show();
	}

	// 通关成功
	private void win() {
		Builder dialog = new Builder(context);
		dialog.setTitle("通关成功");
		dialog.setMessage("你用" + (steps + 1) + "步捕捉到了神经猫耶( •̀ ω •́ )y");
		dialog.setCancelable(false);
		dialog.setNegativeButton("再玩一次", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				initGame();
			}
		});
		dialog.setPositiveButton("取消", null);
		dialog.show();
	}

	// 触屏事件
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(View v, MotionEvent event) {
		int x, y;
		if (event.getAction() == MotionEvent.ACTION_UP) {

			if (event.getY() <= OFFSET) {
				return true;
			}
			y = (int) ((event.getY() - OFFSET) / WIDTH);
			if (y % 2 == 0) {
				if (event.getX() <= length
						|| event.getX() >= length + WIDTH * COL) {
					return true;
				}
				x = (int) ((event.getX() - length) / WIDTH);
			} else {
				if (event.getX() <= (length + WIDTH / 2)
						|| event.getX() > (length + WIDTH / 2 + WIDTH * COL)) {
					return true;
				}
				x = (int) ((event.getX() - WIDTH / 2 - length) / WIDTH);
			}

			if (x + 1 > COL || y + 1 > ROW) {
				initGame();
			} else if (inEdge(cat)) {
				return true;
			} else if (getDot(x, y).getStatus() == Dot.STATUS_OFF) {
				getDot(x, y).setStatus(Dot.STATUS_ON);
				move();
				steps++;
			}
			// redraw();
		}
		return true;
	}

}
