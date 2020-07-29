package com.intelilearning.mymopubadexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button showInterstitial;
    Button showRewardVideo;
    Button loadRewardVideo;

    public static int adsShownCounter = 0;
    public static int adsShownSinceLastRewardVideoPrompt = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showInterstitial = findViewById(R.id.interstitial);
        showRewardVideo = findViewById(R.id.rewardVideo);
        loadRewardVideo = findViewById(R.id.loadRewardVideo);

        MoPubAdsHandlerBannerNdInterstitial adsHandler = new MoPubAdsHandlerBannerNdInterstitial(this,AddCheck.MySourceActivity);
        MoPubAdsHandlerRewardVideo AdsHandlerReward = new MoPubAdsHandlerRewardVideo(this,AddCheck.MySourceActivity);
        adsHandler.handleBannerAds();
        adsHandler.handleInterstitialAds();
        showInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adsHandler.showInterstitial();
            }
        });
        loadRewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsHandlerReward.loadRewardVideo();
            }
        });
        showRewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsHandlerReward.showRewardVideo();
            }
        });


    }
}
