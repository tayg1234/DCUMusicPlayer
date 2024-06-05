package kr.ac.cu.moai.dcumusicplayer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Locale;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        int mp3file = intent.getIntExtra("mp3", -1);

        if (mp3file == -1) {
            Log.e(TAG, "MP3 file is null");
            finish();
            return;
        }

        try (AssetFileDescriptor afd = getResources().openRawResourceFd(mp3file)) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            ImageView ivCover = findViewById(R.id.ivCover);
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            byte[] b = retriever.getEmbeddedPicture();
            if (b != null) {
                ivCover.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
            } else {
                ivCover.setImageResource(R.drawable.default_cover); // 기본 커버 이미지 설정
            }

            TextView tvTitle = findViewById(R.id.tvTitle);
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            tvTitle.setText(title);

            TextView tvDuration = findViewById(R.id.tvDuration);
            tvDuration.setText(ListViewMP3Adapter.getDuration(retriever));

            TextView tvArtist = findViewById(R.id.tvArtist);
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            tvArtist.setText(artist);

            retriever.release();
        } catch (IOException e) {
            Log.e(TAG, "Error loading MP3 file", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
