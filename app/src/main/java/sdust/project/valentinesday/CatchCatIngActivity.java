package sdust.project.valentinesday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class CatchCatIngActivity extends AppCompatActivity {

    PlayGround playground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playground = new PlayGround(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(playground);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // playground.stopTimer();
    }
}
