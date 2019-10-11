package com.saumya.chattery;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.hololo.tutorial.library.PermissionStep;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class SplashActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.automatic_data))
                        .setContent(getString(R.string.gm_finds_photos))
                        .setBackgroundColor(Color.parseColor("#FF0957"))
                        .setDrawable(R.drawable.undraw_chatting_2yvo)
                        .setSummary(getString(R.string.continue_and_learn))
                        .build());
        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.choose_the_song))
                        .setContent(getString(R.string.swap_to_the_tab))
                        .setBackgroundColor(Color.parseColor("#00D4BA"))
                        .setDrawable(R.drawable.undraw_begin_chat_c6pj)
                        .setSummary(getString(R.string.continue_and_update))
                        .build());
        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.edit_data))
                        .setContent(getString(R.string.update_easily))
                        .setBackgroundColor(Color.parseColor("#1098FE"))
                        .setDrawable(R.drawable.undraw_group_hangout_5gmq)
                        .setSummary(getString(R.string.continue_and_result))
                        .build());
        addFragment(
                new Step.Builder()
                        .setTitle(getString(R.string.result_awesome))
                        .setContent(getString(R.string.after_updating))
                        .setBackgroundColor(Color.parseColor("#CA70F3"))
                        .setDrawable(R.drawable.undraw_calling_kpbp)
                        .setSummary(getString(R.string.thank_you))
                        .build());
    }
    @Override
    public void finishTutorial() {
        //Toast.makeText(this, "Tutorial finished", Toast.LENGTH_SHORT).show();
        //finish();

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void currentFragmentPosition(int position) {
        Toast.makeText(this, "Position : " + position, Toast.LENGTH_SHORT).show();






    }
}
