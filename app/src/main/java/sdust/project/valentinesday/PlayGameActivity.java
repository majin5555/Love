package sdust.project.valentinesday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 小驴跳跳跳
 */
public class PlayGameActivity extends AppCompatActivity {
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayGameActivity.this, AbnerGameCircleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                 finish();
            }
        });
        message = (TextView) findViewById(R.id.tv_message);
        int[] data = getAchievement();
        message.setText("本次成绩: " + data[0] + "\n最好成绩: " + data[1]);
    }



    private int[] getAchievement() {
        SharedPreferences sp = getSharedPreferences("Preferences", 0);
        int last = sp.getInt("Achievement1", 0);
        int top = sp.getInt("Achievement2", 0);
        return new int[]{last, top};
    }

    /**
     * 按下返回键,杀死程序并退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                AbnerGameCircleActivity.mAbnerGameCircleActivity.finish();
            } catch (Exception e) {

            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
