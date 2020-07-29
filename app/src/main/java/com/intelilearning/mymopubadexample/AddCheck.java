package com.intelilearning.mymopubadexample;

import android.content.Context;

public class AddCheck {

    private Context mContext ;
    public AddCheck(Context context) {
        mContext = context;
    }

    final static String MySourceActivity = "MainActivity";

    static boolean adEnabled = true; // Assumes that by default ad is required

    boolean bannerAdRequired(String activity){

        switch (activity){
            case MySourceActivity:
                adEnabled = ! inAdsFreePeriod();
                if (adEnabled){
                    return true;
                }
                else {
                    return false;
                }
            default:
                return false;
        }
    }

    boolean interstitialAdRequired(String activity){
        switch (activity){
            case MySourceActivity:
                adEnabled = ! inAdsFreePeriod();
                if (adEnabled){
                    return true;
                }
                else {
                    return false;
                }
            default:
                return false;
        }
    }

    private boolean inAdsFreePeriod(){
        try {
            ReadSaveHandle RSH = new ReadSaveHandle(mContext);
            double adFreeEndPeriod = Double.parseDouble(RSH.getKeyValue(MyConstants.keyForAdFreeEndDate));
            double currentTime = System.currentTimeMillis();
            if (adFreeEndPeriod-currentTime>=0){
                return true;
            }
            else return false;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean shouldLoadRewardVideo(){
        adEnabled = ! inAdsFreePeriod();
        if (adEnabled){
            return true;
        }
        else {
            return false;
        }
    }
}
