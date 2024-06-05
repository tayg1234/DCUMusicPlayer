package kr.ac.cu.moai.dcumusicplayer;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewMP3;
    ArrayList<Integer> mp3files;
    int selectedMP3;
    ListViewMP3Adapter adapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp3files = new ArrayList<>();
        mp3files.add(R.raw.aespa_01_supernova); // 파일 이름을 소문자로 하고, 확장자를 제거합니다.
        mp3files.add(R.raw.ive_01_heya); // 파일 이름을 소문자로 하고, 확장자를 제거합니다.

        Log.i(TAG, mp3files.toString());

        listViewMP3 = findViewById(R.id.listViewMP3);
        adapter = new ListViewMP3Adapter(this, mp3files);
        listViewMP3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMP3.setAdapter(adapter);
        listViewMP3.setOnItemClickListener((parent, view, position, id) -> {
            selectedMP3 = mp3files.get(position);
            Log.i(TAG, "Selected MP3: " + selectedMP3);

            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            intent.putExtra("mp3", selectedMP3);
            startActivity(intent);
        });
    }
}
