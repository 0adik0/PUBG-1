package battle.royale.bestapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;




public class Premium1Activity extends AppCompatActivity {

    private long backPressedTime;

    ImageView imageView3;
    VideoView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium1);

        VideoView videoView = findViewById(R.id.videoPlayer);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.pubgvideo;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Premium1Activity.this, Premium2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
            return;
        }
        backPressedTime = System.currentTimeMillis();
    }
}