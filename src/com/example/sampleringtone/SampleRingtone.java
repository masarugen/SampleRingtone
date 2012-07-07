package com.example.sampleringtone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SampleRingtone extends Activity {
	
	private final static String TAG = "SampleRingtone";
	
	public static final int RINGTONE_PICKER = 10001;
	
	private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_ringtone);
        // 存在するRingtoneの名称を取得
        RingtoneManager ringtoneManager = new RingtoneManager(getApplicationContext());
        Cursor cursor = ringtoneManager.getCursor();
        while (cursor.moveToNext()) {
            Log.d(TAG, "Ringtone Title: " + cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sample_ringtone, menu);
        return true;
    }

	@Override
	protected void onResume() {
		super.onResume();
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "タイトル");
        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false); // サイレントは見せない
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM); // アラーム音
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);// デフォルトは表示しない
        if (mUri != null) {
        	intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mUri); // 選択済みを選択する
        }
        startActivityForResult(intent, RINGTONE_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == RINGTONE_PICKER) {
				// RINGTONE_PICKERからの選択されたデータを取得する
				mUri = (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
				TextView ringView = (TextView) findViewById(R.id.ringtone);
				ringView.setText(ringtone.getTitle(getApplicationContext()));
				ringtone.setStreamType(AudioManager.STREAM_ALARM);
				ringtone.play();
				SystemClock.sleep(1000);
				ringtone.stop();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
