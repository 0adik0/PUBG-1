package battle.royale.bestapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;



public class Premium2Activity extends AppCompatActivity {

    private long backPressedTime;

    ImageView imageView3;

    TextView t1, t2;
    ImageView i1, i2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium2);
        imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Premium2Activity.this, Premium3Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
            }
        });

        VideoView videoView = findViewById(R.id.videoPlayer);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.pubgvideo;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView6);
        i1 = findViewById(R.id.imageView5);
        i2 = findViewById(R.id.imageView6);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setTextColor(Color.parseColor("#FF9800"));
                i1.setColorFilter(Color.parseColor("#FF9800"));
                t2.setTextColor(Color.parseColor("#FFFFFF"));
                i2.setColorFilter(Color.parseColor("#FFFFFF"));
            }
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t1.setTextColor(Color.parseColor("#FF9800"));
                i1.setColorFilter(Color.parseColor("#FF9800"));
                t2.setTextColor(Color.parseColor("#FFFFFF"));
                i2.setColorFilter(Color.parseColor("#FFFFFF"));
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t2.setTextColor(Color.parseColor("#FF9800"));
                i2.setColorFilter(Color.parseColor("#FF9800"));
                t1.setTextColor(Color.parseColor("#FFFFFF"));
                i1.setColorFilter(Color.parseColor("#FFFFFF"));
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t2.setTextColor(Color.parseColor("#FF9800"));
                i2.setColorFilter(Color.parseColor("#FF9800"));
                t1.setTextColor(Color.parseColor("#FFFFFF"));
                i1.setColorFilter(Color.parseColor("#FFFFFF"));
            }
        });


    }
}