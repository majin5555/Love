package sdust.project.valentinesday;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /**
     * 是否跳转
     */
    boolean isJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //首先处理标题栏
        TextView tv_title0 = (TextView) findViewById(R.id.tv_title0);
        tv_title0.setText("I LOVE YOU");
        tv_title0.setGravity(Gravity.CENTER);


        findViewById(R.id.exitbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        final CountDownProgressBar cpb_countdown = (CountDownProgressBar) findViewById(R.id.cpb_countdown);
        cpb_countdown.setDuration(10000, new CountDownProgressBar.OnFinishListener() {
            @Override
            public void onFinish() {
                if (! isJump) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        findViewById(R.id.btn_loginIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isJump = true;
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.tishi);
        builder.setTitle("温馨提示o(≧v≦)o");
        builder.setMessage("点击 EXIT 退出，谢谢观看，爱你呦！");
        builder.setPositiveButton("EXIT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
