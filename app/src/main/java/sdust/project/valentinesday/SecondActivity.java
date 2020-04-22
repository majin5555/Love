package sdust.project.valentinesday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity {
    //标题栏处理文本
    private ScrollTextView tv_title1;
    //返回主界面按钮
    private ImageView      btn_backFirstPage;
    //跳转按钮
    private Button         videoButton;
    private HeartView      heartView;
    private TextView       tvcontent;


    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //设置文本内容
        ((TextView) findViewById(R.id.Tvcontent)).setText(String.format(this.getResources().getString(R.string.confession), (int) calculationtime()));
        //首先处理标题栏
        tv_title1 = findViewById(R.id.ScrollTextView);

        tv_title1.setText(LOVE);
        heartView = findViewById(R.id.my_view);
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                heartView.addFavor();
            }
        }, 1, 600);

        //获得返回按钮
        btn_backFirstPage = (ImageView) findViewById(R.id.ib_title_back);
        //跳转播放页面
        videoButton = (Button) findViewById(R.id.btn_video);

        //跳转播放视频页面
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });

        AnimationSet aniSet = new AnimationSet(true);

        // 尺寸变化动画，设置尺寸从1倍变化到1.7倍
        ScaleAnimation scaleAni = new ScaleAnimation(1f, 1.7f, 1f, 1.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(1000);    // 设置动画效果持续时间1秒

        // 向右运动200，向下运动100
        TranslateAnimation transAni = new TranslateAnimation(0, 200, 0, - 100);
        transAni.setDuration(1000);    // 设置持续时间

        aniSet.addAnimation(scaleAni);    // 将动画效果添加到动画集中
        aniSet.addAnimation(transAni);

        videoButton.startAnimation(aniSet);    // 设置动画效果，一边尺寸放大、一边移动

    }

    /**
     * 计算时间
     *
     * @return
     */
    private double calculationtime() {

        String str1 = "20200207"; //相遇时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String str2 = simpleDateFormat.format(date);//当前时间
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
        return dayCount;
    }

    private String LOVE = "    世界这么大，遇见就是缘分，传说语言是最好的沟通方法...   只可惜我不善言谈，在此只想大声对你说----->" +
            "汉语：我爱你 \n" +
            "德语：Ich liebe dich.\n" +
            "法语：Je t'aime / Je t'adore\n" +
            "希腊语：S'agapo\n" +
            "匈牙利：Szeretlek\n" +
            "爱尔兰：taim i'ngra leat\n" +
            "爱沙尼亚：Mina armastan sind\n" +
            "芬兰：Min rakastan sinua\n" +
            "比利时佛兰芒语：IK zie u graag\n" +
            "意大利语：ti amo,ti vogliobene\n" +
            "拉丁语：Te amo,Vos amo\n" +
            "拉托维亚：Es tevi Milu\n" +
            "里斯本：lingo gramo-te bue',chavalinha\n" +
            "立陶宛：Tave Myliu\n" +
            "马其顿：Te sakam\n" +
            "马耳他：Inhobbok\n" +
            "波兰语：Kocham Cie,Ja cie kocham\n" +
            "葡萄牙：Eu amo-te\n" +
            "罗马尼亚：Te iu besc,Te Ador\n" +
            "荷兰：IK hou van jou\n" +
            "英语：I love you\n" +
            "捷克：Miluji te\n" +
            "丹麦：Jeg elsker dig\n" +
            "阿尔萨斯：Ich hoan dich gear\n" +
            "亚美尼亚：Yes Kezi Seeroom yem\n" +
            "巴伐利亚：I mog di narrisch gern\n" +
            "保加利亚：ahs te obicham\n" +
            "西班牙加泰隆语：T'estim\n" +
            "克罗地亚：Volim te\n" +
            "阿塞疆语：Men seni serivem\n" +
            "孟加拉：Ami tomay bhalobashi\n" +
            "缅甸：chit pa de\n" +
            "柬埔寨：Bong salang oun\n" +
            "菲律宾：Mahal Kita,Iniibig Kita\n" +
            "印度古吉拉特语：Hoon tane prem karun chuun\n" +
            "北印度语：main tumse pyar karta hoon\n" +
            "印度尼西亚：Saja kasih saudari\n" +
            "朝鲜：???\n" +
            "爪哇语：aku tresno marang sliromu\n" +
            "老挝：Khoi huk chau\n" +
            "马来语：saya Cinta Mu\n" +
            "马来西亚：Saya Cintamu\n" +
            "蒙古语：bi chamd hairtai\n" +
            "尼泊尔：Ma tumilai maya garchu,Ma timilai man parauchu\n" +
            "波斯语：Tora dost daram\n" +
            "他加禄语：Mahal kita\n" +
            "南非语：Ek het jou lief Ek is lief vir jou\n" +
            "加纳：Me do wo\n" +
            "埃塞俄比亚阿姆哈雷地区：Ene ewedechalu(for ladies)\n" +
            "Ene ewedehalwe(for men)\n" +
            "阿拉伯语：Ana Ahebak(to a male)\n" +
            "Arabic Ana ahebek(to a female)\n" +
            "瑞士德语：Ich li b Dich\n" +
            "克里奥尔语：Mon kontan ou\n" +
            "豪萨语：Ina sonki\n" +
            "肯尼亚班图语：Nigwedete\n" +
            "马达加斯加语：tiako ianao\n" +
            "印度阿萨姆邦语：Moi tomak bhal pau\n" +
            "南亚泰米尔语：Tamil n'an unnaik kathalikkinren\n" +
            "印度泰卢固语：Neenu ninnu pra'mistu'nnanu\n" +
            "泰国：Ch'an Rak Khun\n" +
            "乌尔都语：Mein tumhay pyar karti hun(woman to man)\n" +
            "Mein tumhay pyar karta hun(man to woman)\n" +
            "越南：Em ye'u anh(woman to man)\n" +
            "Anh ye'u em(man to woman)\n" +
            "新西兰毛里语：kia hoahai\n" +
            "爱斯基摩：Nagligivaget\n" +
            "格陵兰岛：Asavakit\n" +
            "冰岛：e'g elska tig\n" +
            "阿尔巴尼亚：T Dua Shume\n" +
            "俄罗斯：Ya vas Iyublyu,Ya Tibia Lyublyu\n" +
            "塞尔维亚：Volim Te\n" +
            "斯洛文尼亚语：Ljubim te\n" +
            "西班牙：Te amo,Te quiero\n" +
            "瑞典：Jag lskar dig\n" +
            "土耳其：Seni seviyorum\n" +
            "乌克兰：ja vas kokhaju\n" +
            "威尔士：Rwy'n dy garu di\n" +
            "亚述语：ana bayanookh(female to male)\n" +
            "ana bayinakh(male to female)\n" +
            "高加索切尔克斯语：wise cas\n" +
            "日本：あいしてる " +
            "犹太语：Ani ohev otach(male o* **male";
}
