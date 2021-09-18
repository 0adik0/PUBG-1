package battle.royale.bestapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Premium3Activity extends AppCompatActivity implements ValueEventListener{

    private TextView text3;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference billing = databaseReference.child("app_billing").child("text");

    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium3);

        text3 = findViewById(R.id.textView3);

        // =====================================================================================

        VideoView videoView = findViewById(R.id.videoPlayer);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.pubgvideo;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // =====================================================================================

        imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(view -> {
            Intent intent = new Intent(Premium3Activity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slidein, R.anim.slideout);
        });
    }

    @Override
    public void onDataChange (DataSnapshot dataSnapshot) {

        if (dataSnapshot.getValue(String.class) !=null) {
            String key = dataSnapshot.getKey();
            if (key.equals("text")) {
                String text = dataSnapshot.getValue(String.class);
                text3.setText(text);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        billing.addValueEventListener(this);
    }
}