package sdust.project.valentinesday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class CatchCatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_catch_cat);
    }
    public void onClick(View view) {
        Intent intent = new Intent(CatchCatActivity.this, CatchCatIngActivity.class);
        startActivity(intent);
    }

}
