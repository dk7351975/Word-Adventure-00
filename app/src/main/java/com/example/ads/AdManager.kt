package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManager {

    companion object {
        // Production AdMob Ad Unit IDs
        const val REWARDED_AD_UNIT_ID = "ca-app-pub-6126842539613794/1972941655"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-6126842539613794/9468288290"
        const val BANNER_AD_UNIT_ID = "ca-app-pub-6126842539613794/3267656116"
        private const val TAG = "AdManager"
    }

    private var rewardedAd: RewardedAd? = null
    private var isLoadingAd = false
    var isInitialized = false
        private set

    fun initialize(context: Context) {
        if (isInitialized) return
        MobileAds.initialize(context) { initializationStatus ->
            Log.d(TAG, "AdMob MobileAds initialized: $initializationStatus")
            isInitialized = true
            preloadRewardedAd(context)
        }
    }

    fun preloadRewardedAd(context: Context) {
        if (rewardedAd != null || isLoadingAd) return
        isLoadingAd = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded ad successfully loaded.")
                    rewardedAd = ad
                    isLoadingAd = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "Rewarded ad failed to load: ${loadAdError.message}")
                    rewardedAd = null
                    isLoadingAd = false
                }
            }
        )
    }

    fun isRewardedAdReady(): Boolean = rewardedAd != null

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdClosedOrFailed: (reason: String) -> Unit
    ) {
        val ad = rewardedAd
        if (ad != null) {
            var rewardEarned = false
            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                rewardEarned = true
                onRewardEarned()
            }
            rewardedAd = null // Reset after showing
            preloadRewardedAd(activity)
        } else {
            Log.d(TAG, "Ad not ready, triggering fallback")
            // Graceful fallback when ad is not ready or offline:
            // Allow reward so child experience is never blocked or broken!
            onRewardEarned()
            onAdClosedOrFailed("Ad unavailable - free reward granted")
            preloadRewardedAd(activity)
        }
    }
}
