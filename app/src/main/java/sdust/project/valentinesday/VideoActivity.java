package sdust.project.valentinesday;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    //标题栏处理文本
    private TextView       tv_title1;
    //视频播放
    private VideoView      videoLocal;
    //返回贺卡界面按钮
    private ImageView      btn_backPage;
    private RelativeLayout erweima;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video);
        //首先处理标题栏
        tv_title1 = findViewById(R.id.tv_title1);
        tv_title1.setTextColor(getResources().getColor(R.color.colorBlue));
        erweima = findViewById(R.id.erweima);
        tv_title1.setText("认真看，说你呢...瞅啥?就是你...");
        tv_title1.setGravity(Gravity.CENTER);
        //获得返回按钮
        btn_backPage = (ImageView) findViewById(R.id.ib_title_back);
        //视频播放模块
        initLocalVideo();
        //设置返回主界面按钮
        btn_backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //播放本地视频
    private void initLocalVideo() {
        //获得视频的id
        videoLocal = (VideoView) findViewById(R.id.videoView);
        /*跳转游戏界面*/
        findViewById(R.id.playgames).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceGameDialogFragment.newInstance(1).show(getSupportFragmentManager(), "111");
            }
        });
        //设置有进度条可以拖动快进
        MediaController localMediaController = new MediaController(VideoActivity.this);
        String uri = ("android.resource://" + getPackageName() + "/" + R.raw.hui);
        videoLocal.setVideoURI(Uri.parse(uri));
        // 设置videoView和mController建立关联
        videoLocal.setMediaController(localMediaController);
        // 设置mController和videoView建立关联
        localMediaController.setMediaPlayer(videoLocal);
        // 让VideoView获取焦点
        videoLocal.requestFocus();
        videoLocal.start();

        //或者
        videoLocal.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                String uri = ("android.resource://" + getPackageName() + "/" + R.raw.hui);
                videoLocal.setVideoURI(Uri.parse(uri));
                videoLocal.start();
                erweima.setVisibility(View.VISIBLE);
            }
        });
    }
}
