# 🗺️ WordQuest: Hidden Trails

[![Android CI/CD](https://img.shields.io/badge/GitHub_Actions-Build_&_Release-blue?logo=github-actions)](.github/workflows/build-apk.yml)
[![Platform](https://img.shields.io/badge/Platform-Android-green?logo=android)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Unity Ads](https://img.shields.io/badge/Monetization-Unity_Ads_v4.12.5-black?logo=unity)](https://unity.com/solutions/monetization)

An enchanting word search adventure game built natively for Android using **Kotlin** and **Jetpack Compose (Material 3)**. Embark on a journey across mythical biomes, connect letters to discover hidden words, collect coins and stars, unlock unique relics and badges, and challenge yourself with daily puzzles!

---

## ✨ Features

- 🌲 **Biomes & World Map Trail**: Journey across Enchanted Forest, Whispering Ruins, Crystal Peaks, Sunken Kingdom, and more with an interactive world trail map.
- 🔠 **Interactive Word Grid**: Smooth touch-drag selection to connect letters horizontally, vertically, or diagonally with dynamic line drawing and sound effects.
- 💡 **Multi-Tier Hints System**:
  - First Letter reveal
  - Random Letter reveal
  - Full Word highlight
- 🪙 **Reward Economy**:
  - Earn 100 coins + stars on level completion
  - Exchange coins for hints in the Reward Vault
  - Daily login streak rewards
- 🏆 **Artifact & Relic Collection**: Unlock achievements, badges, and rare artifacts as you progress through the adventure.
- 📅 **Daily Challenge Puzzles**: New curated puzzles every calendar day with special bonus rewards.
- 🎵 **Sound & Haptics Engine**: Interactive audio feedback for letter selection, word completion, star animations, coin earnings, and errors.

---

## 💰 Monetization (Unity Ads SDK)

The app integrates **Unity Ads Android SDK (v4.12.5)** with balanced, player-friendly ad placements:

| Ad Type | Placement | Trigger & Benefit |
|---|---|---|
| **Rewarded Video** | `Rewarded_Android` | **Reward Vault**: +50 Free Coins or +1 Free Hint<br>**Gameplay**: Free hint when tokens run out<br>**Level Complete**: +50 Bonus Coins |
| **Interstitial Ad** | `Interstitial_Android` | Natural screen transitions after every completed level (Next Level, Map, Home) |

* **Unity Game ID**: `6189204`
* **Ad Configuration**: Managed centrally in `com.example.ads.UnityAdsManager`

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material Design 3](https://m3.material.io/)
- **Architecture**: MVVM / StateFlow reactive state management
- **Local Persistence**: `EncryptedSharedPreferences` / `SharedPreferences`
- **Audio Engine**: Android `SoundPool` for ultra-low latency audio
- **Ad Network**: [Unity Ads Android SDK](https://developers.unity.com/monetization)
- **CI/CD**: GitHub Actions (Java 17 + Gradle 9.3.1) with automated Release APK publishing

---

## 🚀 Automated Builds & GitHub Actions

This repository includes an automated CI/CD pipeline (`.github/workflows/build-apk.yml`) that:

1. **Triggers on Push**: Automatically builds whenever changes are pushed to `main` (or via `workflow_dispatch`).
2. **Builds Debug APK**: Uses Java 17, Gradle 9.3.1, and generates a temporary debug signing keystore.
3. **Stores Artifact**: Uploads `app-debug-apk` directly to GitHub Actions artifacts.
4. **Publishes Release**: Automatically creates a GitHub Release with the downloadable `.apk` file attached.

---

## 💻 Local Development Setup

### Prerequisites
- **Android Studio Ladybug (or newer)**
- **JDK 17**
- **Android SDK (API 34 / 35)**

### Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/<your-username>/<your-repo-name>.git
   cd <your-repo-name>
   ```
2. **Open in Android Studio**: Open the root folder in Android Studio.
3. **Build the Debug APK via Gradle**:
   ```bash
   gradle :app:assembleDebug
   ```
4. The output APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more details.
