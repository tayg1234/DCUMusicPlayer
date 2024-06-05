package kr.ac.cu.moai.dcumusicplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ListViewMP3Adapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Integer> mp3files;
    private static final String TAG = "ListViewMP3Adapter";

    public ListViewMP3Adapter(Context context, ArrayList<Integer> mp3files) {
        this.context = context;
        this.mp3files = mp3files;
    }

    @Override
    public int getCount() {
        return mp3files.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_view_mp3, parent, false);
        }

        ImageView ivCover = convertView.findViewById(R.id.ivCover);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvArtist = convertView.findViewById(R.id.tvArtist);
        TextView tvDuration = convertView.findViewById(R.id.tvDuration);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try (AssetFileDescriptor afd = context.getResources().openRawResourceFd(mp3files.get(position))) {
            retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            byte[] coverBytes = retriever.getEmbeddedPicture();
            if (coverBytes != null) {
                ivCover.setImageBitmap(BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length));
            } else {
                ivCover.setImageResource(R.drawable.default_cover); // 기본 커버 이미지 설정
            }

            tvTitle.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            tvArtist.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            tvDuration.setText(getDuration(retriever));
        } catch (IOException e) {
            Log.e(TAG, "Error loading MP3 file", e);
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return convertView;
    }

    public static String getDuration(MediaMetadataRetriever retriever) {
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (durationStr != null) {
            int duration = Integer.parseInt(durationStr);
            int minutes = (duration / 1000) / 60;
            int seconds = (duration / 1000) % 60;
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        return "00:00";
    }
}
