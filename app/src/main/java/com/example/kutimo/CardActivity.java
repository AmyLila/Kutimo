package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Get images for card collections
 *
 * @author Amy and Timothy
 */
public class CardActivity extends AppCompatActivity {
    private static final String TAG = "CardActivity";

    /**
     *
     * @param name
     * @return
     */
    private int getImageIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    /**
     *
     * @param name
     * @return
     */
    private int getViewIdByName(String name) {
        return getResources().getIdentifier(name, "id", this.getPackageName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        float faithPoints = new Data(this).loadFloat(StorageKeys.FAITH_POINTS);
        for (int i = 0; i < GetLevel.getCurrentLevel(faithPoints) - 1; i++) {
            ImageView image_view = (ImageView) findViewById(getViewIdByName("image_level" + (i + 1)));
            image_view.setImageResource(getImageIdByName("image" + (i + 1)));
        }
    }
}