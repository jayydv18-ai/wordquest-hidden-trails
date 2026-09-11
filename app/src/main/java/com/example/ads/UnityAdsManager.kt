package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.UnityAdsLoadError
import com.unity3d.ads.UnityAds.UnityAdsShowCompletionState
import com.unity3d.ads.UnityAds.UnityAdsShowError
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

object UnityAdsManager {
    private const val TAG = "UnityAdsManager"

    const val GAME_ID = "6189204"
    const val INTERSTITIAL_AD_UNIT = "Interstitial_Android"
    const val REWARDED_AD_UNIT = "Rewarded_Android"
    const val BANNER_AD_UNIT = "Banner_Android"

    // Set testMode to false for live ads (or true if testing on debug device)
    var testMode: Boolean = false

    private var isInitialized = false
    private var isInterstitialLoading = false
    private var isInterstitialReady = false
    private var isRewardedLoading = false
    private var isRewardedReady = false

    // Smart pacing: don't bombard player with interstitial ads after every single 10-second level
    private var levelsCompletedSinceLastInterstitial = 0
    private const val INTERSTITIAL_FREQUENCY = 2 // Show every 2 completed levels

    fun initialize(context: Context, testMode: Boolean = false) {
        if (isInitialized) return
        this.testMode = testMode

        UnityAds.initialize(
            context.applicationContext,
            GAME_ID,
            testMode,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    isInitialized = true
                    Log.d(TAG, "Unity Ads initialized successfully")
                    loadInterstitial(context.applicationContext)
                    loadRewarded(context.applicationContext)
                }

                override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {
                    isInitialized = false
                    Log.e(TAG, "Unity Ads initialization failed: $error, msg: $message")
                }
            }
        )
    }

    fun loadInterstitial(context: Context? = null) {
        if (!isInitialized || isInterstitialLoading) return
        isInterstitialLoading = true

        UnityAds.load(
            INTERSTITIAL_AD_UNIT,
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String) {
                    isInterstitialLoading = false
                    isInterstitialReady = true
                    Log.d(TAG, "Interstitial loaded: $placementId")
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String,
                    error: UnityAdsLoadError,
                    message: String
                ) {
                    isInterstitialLoading = false
                    isInterstitialReady = false
                    Log.w(TAG, "Failed to load interstitial: $placementId - $error: $message")
                }
            }
        )
    }

    fun showInterstitial(
        activity: Activity,
        onClosed: () -> Unit = {}
    ) {
        if (!isInitialized || !isInterstitialReady) {
            Log.d(TAG, "Interstitial ad not ready, proceeding")
            loadInterstitial(activity)
            onClosed()
            return
        }

        isInterstitialReady = false
        UnityAds.show(
            activity,
            INTERSTITIAL_AD_UNIT,
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowStart(placementId: String) {
                    Log.d(TAG, "Interstitial show started: $placementId")
                }

                override fun onUnityAdsShowClick(placementId: String) {
                    Log.d(TAG, "Interstitial clicked: $placementId")
                }

                override fun onUnityAdsShowComplete(
                    placementId: String,
                    state: UnityAdsShowCompletionState
                ) {
                    Log.d(TAG, "Interstitial completed: $placementId state: $state")
                    loadInterstitial(activity)
                    onClosed()
                }

                override fun onUnityAdsShowFailure(
                    placementId: String,
                    error: UnityAdsShowError,
                    message: String
                ) {
                    Log.w(TAG, "Interstitial show failed: $placementId - $error: $message")
                    loadInterstitial(activity)
                    onClosed()
                }
            }
        )
    }

    /**
     * Check if an interstitial should be presented based on gameplay pacing.
     * Shows ad every [INTERSTITIAL_FREQUENCY] levels, avoiding intrusive spam.
     */
    fun onLevelFinished(activity: Activity, onFinished: () -> Unit) {
        levelsCompletedSinceLastInterstitial++
        if (levelsCompletedSinceLastInterstitial >= INTERSTITIAL_FREQUENCY && isInterstitialReady) {
            levelsCompletedSinceLastInterstitial = 0
            showInterstitial(activity, onFinished)
        } else {
            // If not showing ad this turn or ad not ready, proceed directly
            if (!isInterstitialReady && !isInterstitialLoading) {
                loadInterstitial(activity)
            }
            onFinished()
        }
    }

    fun loadRewarded(context: Context? = null) {
        if (!isInitialized || isRewardedLoading) return
        isRewardedLoading = true

        UnityAds.load(
            REWARDED_AD_UNIT,
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String) {
                    isRewardedLoading = false
                    isRewardedReady = true
                    Log.d(TAG, "Rewarded ad loaded: $placementId")
                }

                override fun onUnityAdsFailedToLoad(
                    placementId: String,
                    error: UnityAdsLoadError,
                    message: String
                ) {
                    isRewardedLoading = false
                    isRewardedReady = false
                    Log.w(TAG, "Failed to load rewarded ad: $placementId - $error: $message")
                }
            }
        )
    }

    fun isRewardedAdReady(): Boolean = isInitialized && isRewardedReady

    fun showRewarded(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        if (!isInitialized || !isRewardedReady) {
            Log.w(TAG, "Rewarded ad not ready")
            loadRewarded(activity)
            onAdDismissed()
            return
        }

        isRewardedReady = false
        UnityAds.show(
            activity,
            REWARDED_AD_UNIT,
            object : IUnityAdsShowListener {
                private var userEarnedReward = false

                override fun onUnityAdsShowStart(placementId: String) {
                    Log.d(TAG, "Rewarded ad show started: $placementId")
                }

                override fun onUnityAdsShowClick(placementId: String) {
                    Log.d(TAG, "Rewarded ad clicked: $placementId")
                }

                override fun onUnityAdsShowComplete(
                    placementId: String,
                    state: UnityAdsShowCompletionState
                ) {
                    Log.d(TAG, "Rewarded ad completed: $placementId state: $state")
                    if (state == UnityAdsShowCompletionState.COMPLETED) {
                        userEarnedReward = true
                        onRewardEarned()
                    }
                    loadRewarded(activity)
                    onAdDismissed()
                }

                override fun onUnityAdsShowFailure(
                    placementId: String,
                    error: UnityAdsShowError,
                    message: String
                ) {
                    Log.w(TAG, "Rewarded ad show failed: $placementId - $error: $message")
                    loadRewarded(activity)
                    onAdDismissed()
                }
            }
        )
    }
}

/**
 * Composable Banner ad container that embeds the Unity BannerView cleanly.
 * Designed to fit standard 320x50 dimensions without interfering with user interaction.
 */
@Composable
fun UnityBannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    val bannerView = remember {
        BannerView(activity, UnityAdsManager.BANNER_AD_UNIT, UnityBannerSize(320, 50)).apply {
            listener = object : BannerView.IListener {
                override fun onBannerLoaded(bannerAdView: BannerView) {
                    Log.d("UnityBannerAd", "Banner loaded successfully")
                }

                override fun onBannerShown(bannerAdView: BannerView) {
                    Log.d("UnityBannerAd", "Banner shown")
                }

                override fun onBannerFailedToLoad(
                    bannerAdView: BannerView,
                    errorInfo: BannerErrorInfo
                ) {
                    Log.w("UnityBannerAd", "Banner failed to load: ${errorInfo.errorMessage}")
                }

                override fun onBannerClick(bannerAdView: BannerView) {
                    Log.d("UnityBannerAd", "Banner clicked")
                }

                override fun onBannerLeftApplication(bannerAdView: BannerView) {
                    Log.d("UnityBannerAd", "Banner left application")
                }
            }
        }
    }

    DisposableEffect(bannerView) {
        bannerView.load()
        onDispose {
            bannerView.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Color(0xFF0F1B2B)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { bannerView },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}
