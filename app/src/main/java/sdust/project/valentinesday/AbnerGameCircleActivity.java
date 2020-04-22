package sdust.project.valentinesday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AbnerGameCircleActivity extends AppCompatActivity {

    public static AbnerGameCircleActivity mAbnerGameCircleActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abner_game_circle);
        mAbnerGameCircleActivity = this;
    }


    /**
     * 游戏结束后回调此
     */
    public void showAchievement(int achievement) {
        saveAchievement(achievement);
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveAchievement(int achievement) {
        SharedPreferences sp = getSharedPreferences(
                "Preferences", 0);
        sp.edit().putInt("Achievement1", achievement).commit();
        int top = sp.getInt("Achievement2", 0);
        if (achievement > top) {
            sp.edit().putInt("Achievement2", achievement).commit();
        }
    }


}