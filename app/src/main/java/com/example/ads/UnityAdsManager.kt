package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.UnityAdsLoadError
import com.unity3d.ads.UnityAds.UnityAdsShowCompletionState
import com.unity3d.ads.UnityAds.UnityAdsShowError

object UnityAdsManager {
    private const val TAG = "UnityAdsManager"

    // Unity Ads Project & Placement IDs
    const val GAME_ID = "6189204"
    const val INTERSTITIAL_AD_UNIT = "Interstitial_Android"
    const val REWARDED_AD_UNIT = "Rewarded_Android"

    // Set testMode to false for live ads (true during development/testing)
    var testMode: Boolean = false

    private var isInitialized = false
    private var isInterstitialLoading = false
    private var isInterstitialReady = false
    private var isRewardedLoading = false
    private var isRewardedReady = false
    private var isAdCurrentlyShowing = false

    // Pacing controls
    private var levelsCompletedSinceLastInterstitial = 0
    const val INTERSTITIAL_LEVEL_INTERVAL = 1 // Show after every completed level (1, 2, 3, 4, 5...)
    private const val REWARDED_MIN_COOLDOWN_MS = 10_000L // 10s cooldown between rewarded ad requests

    private var lastInterstitialShownTimestamp: Long = 0L
    private var lastRewardedShownTimestamp: Long = 0L
    private var lastAnyAdShownTimestamp: Long = 0L

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
                    Log.d(TAG, "Unity Ads initialized successfully (Game ID: $GAME_ID)")
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

    fun isSdkInitialized(): Boolean = isInitialized

    fun loadInterstitial(context: Context? = null) {
        if (!isInitialized || isInterstitialLoading) return
        isInterstitialLoading = true

        UnityAds.load(
            INTERSTITIAL_AD_UNIT,
            object : IUnityAdsLoadListener {
                override fun onUnityAdsAdLoaded(placementId: String) {
                    isInterstitialLoading = false
                    isInterstitialReady = true
                    Log.d(TAG, "Interstitial loaded successfully: $placementId")
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

    fun isInterstitialAvailable(): Boolean = isInitialized && isInterstitialReady && !isAdCurrentlyShowing

    /**
     * Checks whether an interstitial ad is eligible to be shown after level completion.
     * Ready to show after every level if the ad is loaded and no other ad is active.
     */
    fun canShowInterstitial(levelId: Int = 0): Boolean {
        return isInitialized && isInterstitialReady && !isAdCurrentlyShowing
    }

    fun showInterstitial(
        activity: Activity,
        onClosed: () -> Unit = {}
    ) {
        if (!isInitialized || !isInterstitialReady || isAdCurrentlyShowing) {
            Log.d(TAG, "Interstitial ad not ready or ad already showing, proceeding immediately")
            loadInterstitial(activity)
            onClosed()
            return
        }

        isInterstitialReady = false
        isAdCurrentlyShowing = true

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
                    isAdCurrentlyShowing = false
                    val now = System.currentTimeMillis()
                    lastInterstitialShownTimestamp = now
                    lastAnyAdShownTimestamp = now
                    levelsCompletedSinceLastInterstitial = 0
                    loadInterstitial(activity)
                    onClosed()
                }

                override fun onUnityAdsShowFailure(
                    placementId: String,
                    error: UnityAdsShowError,
                    message: String
                ) {
                    Log.w(TAG, "Interstitial show failed: $placementId - $error: $message")
                    isAdCurrentlyShowing = false
                    loadInterstitial(activity)
                    onClosed()
                }
            }
        )
    }

    /**
     * Trigger called after completing every level when user navigates (Next Level, Map, Home).
     * Presents an interstitial ad after every level if available, then immediately preloads the next one.
     */
    fun onLevelFinished(activity: Activity, levelId: Int = 0, onFinished: () -> Unit) {
        levelsCompletedSinceLastInterstitial++
        Log.d(TAG, "Level $levelId finished. Showing interstitial after level completion.")

        if (canShowInterstitial(levelId)) {
            showInterstitial(activity, onFinished)
        } else {
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
                    Log.d(TAG, "Rewarded ad loaded successfully: $placementId")
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

    fun isRewardedAdReady(): Boolean = isInitialized && isRewardedReady && !isAdCurrentlyShowing

    fun canShowRewarded(): Boolean {
        if (!isInitialized || !isRewardedReady || isAdCurrentlyShowing) return false
        val now = System.currentTimeMillis()
        return (now - lastRewardedShownTimestamp) >= REWARDED_MIN_COOLDOWN_MS
    }

    /**
     * Shows a rewarded video ad. Grants the reward ONLY if the user completes watching the ad.
     */
    fun showRewarded(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        if (!isInitialized || !isRewardedReady || isAdCurrentlyShowing) {
            Log.w(TAG, "Rewarded ad not ready or ad already showing")
            loadRewarded(activity)
            onAdDismissed()
            return
        }

        isRewardedReady = false
        isAdCurrentlyShowing = true

        UnityAds.show(
            activity,
            REWARDED_AD_UNIT,
            object : IUnityAdsShowListener {
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
                    isAdCurrentlyShowing = false
                    val now = System.currentTimeMillis()
                    lastRewardedShownTimestamp = now
                    lastAnyAdShownTimestamp = now

                    if (state == UnityAdsShowCompletionState.COMPLETED) {
                        Log.d(TAG, "Rewarded ad fully completed. Awarding reward.")
                        onRewardEarned()
                    } else {
                        Log.d(TAG, "Rewarded ad skipped or incomplete. No reward awarded.")
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
                    isAdCurrentlyShowing = false
                    loadRewarded(activity)
                    onAdDismissed()
                }
            }
        )
    }

    /**
     * Convenience wrapper for Rewarded Ad to earn +1 Hint
     */
    fun showRewardedForHint(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        showRewarded(
            activity = activity,
            onRewardEarned = onRewardEarned,
            onAdDismissed = onAdDismissed
        )
    }

    /**
     * Convenience wrapper for Rewarded Ad to earn +50 Bonus Coins
     */
    fun showRewardedForCoins(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        showRewarded(
            activity = activity,
            onRewardEarned = onRewardEarned,
            onAdDismissed = onAdDismissed
        )
    }
}
