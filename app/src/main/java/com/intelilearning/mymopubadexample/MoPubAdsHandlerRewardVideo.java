package com.intelilearning.mymopubadexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.Set;

public class MoPubAdsHandlerRewardVideo implements MoPubRewardedVideoListener {
    private Context mContext;
    private String sActivity;
    private AddCheck adCheck;

   private boolean loadingRewardVideoRequested = false;
    private boolean adsInitializationCompleted = false;
    private boolean rewardVideoLoaded = false;

    private boolean loadRewardVideoOnInitializationComplete = false;

    public MoPubAdsHandlerRewardVideo(Context context, String requestingActivity) {
        mContext = context;
        sActivity = requestingActivity;
        adCheck = new AddCheck(mContext);
    }
    public void handleRewardVideo()
    {
        if (adCheck.shouldLoadRewardVideo()){
            MoPub.onCreate((Activity) mContext);
            if (adsInitializationCompleted){
                MoPubRewardedVideos.loadRewardedVideo(MyConstants.myRewardVideoID);
                loadingRewardVideoRequested = true;
            }
            else {
                //initializeLoadRewardVideo = true;
                loadRewardVideoOnInitializationComplete = true;
                initializeMoPubSDK(MyConstants.myRewardVideoID);
            }
            MoPubRewardedVideos.setRewardedVideoListener(this);
        }
    }

    public void loadRewardVideo(){
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled){
            handleRewardVideo();
            Toast.makeText(mContext,"loading reward video, please wait, video will be displayed once loading is complete",Toast.LENGTH_LONG).show();
        }
    }

    public boolean showRewardVideo(){
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled){
            handleRewardVideo();
            Toast.makeText(mContext,"loading reward video, please wait, video will be displayed once loading is complete",Toast.LENGTH_LONG).show();
            return false;
        }
        if (sActivity.equals(AddCheck.MySourceActivity)){
            if (rewardVideoLoaded && AddCheck.adEnabled){
                // Display Ad
                MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID);
            }
        }
        return false;
    }

    public void destroyRequested(){
        MoPub.onDestroy((Activity) mContext);//for rewarded video ads only
    }
    public void backPressed(){
        MoPub.onBackPressed((Activity) mContext); // for rewarded video ads only
    }


    private void initializeMoPubSDK(String adUnit){
        // configurations required to initialize
        /*
        Map<String, String> mediatedNetworkConfiguration1 = new HashMap<>();
        mediatedNetworkConfiguration1.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");
        Map<String, String> mediatedNetworkConfiguration2 = new HashMap<>();
        mediatedNetworkConfiguration2.put("<custom-adapter-class-data-key>", "<custom-adapter-class-data-value>");
         */
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnit)
                /*.withMediationSettings("MEDIATION_SETTINGS")
                .withAdditionalNetworks(CustomAdapterConfiguration.class.getName())
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration1)
                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration2)
                .withLogLevel(LogLevel.Debug)*/
                .withLegitimateInterestAllowed(false)
                .build();
        MoPub.initializeSdk(mContext, sdkConfiguration, initSdkListener());
    }
    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
	   /* MoPub SDK initialized.
	   Check if you should show the consent dialog here, and make your ad requests. */
                adsInitializationCompleted = true;
                Toast.makeText(mContext,"Initialization completed",Toast.LENGTH_SHORT).show();

                if (loadRewardVideoOnInitializationComplete){
                    loadRewardVideoOnInitializationComplete = false;
                    MoPubRewardedVideos.loadRewardedVideo(MyConstants.myRewardVideoID);
                    loadingRewardVideoRequested = true;
                    loadRewardVideoOnInitializationComplete = false;
                }
            }
        };
    }

    @Override
    public void onRewardedVideoLoadSuccess(String adUnitId) {
        // Called when the video for the given adUnitId has loaded. At this point you should be able to call MoPubRewardedVideos.showRewardedVideo(String) to show the video.
        if (adUnitId.equals(MyConstants.myRewardVideoID)) {
            rewardVideoLoaded = true;
            loadingRewardVideoRequested = false;

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Reward video loaded successfully, show now ?")
                    .setTitle("Reward Video")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Display Ad
                            MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User not in agreement, ignore.
                        }
                    });
            builder.create();
            builder.show();
        }
    }
    @Override
    public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
        // Called when a video fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
    }
    @Override
    public void onRewardedVideoStarted(String adUnitId) {
        // Called when a rewarded video starts playing.
    }
    @Override
    public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
        //  Called when there is an error during video playback.
    }
    @Override
    public void onRewardedVideoClicked(@NonNull String adUnitId) {
        //  Called when a rewarded video is clicked.
    }
    @Override
    public void onRewardedVideoClosed(String adUnitId) {
        // Called when a rewarded video is closed. At this point your application should resume.
        //ResultScientificCalculator.setText("Rewarded video closed");
    }
    @Override
    public void onRewardedVideoCompleted(Set<String> adUnitIds, MoPubReward reward) {
        // Called when a rewarded video is completed and the user should be rewarded.
        // You can query the reward object with boolean isSuccessful(), String getLabel(), and int getAmount().
        rewardVideoLoaded = false;
        AddCheck.adEnabled = false;
        double startPeriod = System.currentTimeMillis();
        double endPeriod = startPeriod+MyConstants.rewardedPeriod;
        ReadSaveHandle RSH = new ReadSaveHandle(mContext);
        try {
            RSH.saveKeyValue(MyConstants.keyForAdFreeStartDate, String.valueOf(startPeriod));
            RSH.saveKeyValue(MyConstants.keyForAdFreeEndDate, String.valueOf(endPeriod));
            RSH.showToastMessage(MyConstants.videoAdRewardedMessage);
        }
        catch (Exception e){
            //
        }
    }
}

