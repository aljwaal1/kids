package com.explapp.kidslegacy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Locale;

public final class EnhancedMainActivity extends Activity implements TextToSpeech.OnInitListener {
    private WebView webView;
    private TextToSpeech tts;
    private boolean ttsReady;
    private ToneGenerator tones;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.rgb(57, 38, 190));
            getWindow().setNavigationBarColor(Color.rgb(25, 20, 82));
        }

        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 46);
        tts = new TextToSpeech(this, this);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(245, 247, 255));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setTextZoom(100);
        webView.addJavascriptInterface(new AppBridge(), "Android");
        webView.setWebViewClient(new WebViewClient());
        if (Build.VERSION.SDK_INT <= 19) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        setContentView(webView);
        webView.loadUrl("file:///android_asset/learning.html");
    }

    @Override
    public void onInit(int status) {
        ttsReady = status == TextToSpeech.SUCCESS;
        if (ttsReady) {
            tts.setSpeechRate(0.78f);
            tts.setPitch(1.04f);
        }
    }

    private void speakNow(final String value, final String language) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if (!ttsReady || value == null || value.trim().length() == 0) return;
                Locale locale = "en".equals(language) ? Locale.ENGLISH : new Locale("ar");
                int result = tts.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) return;
                if (Build.VERSION.SDK_INT >= 21) {
                    tts.speak(value, TextToSpeech.QUEUE_FLUSH, null, "kids_learning_item");
                } else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
                    tts.speak(value, TextToSpeech.QUEUE_FLUSH, params);
                }
            }
        });
    }

    public final class AppBridge {
        @JavascriptInterface
        public void speak(String value, String language) {
            speakNow(value, language);
        }

        @JavascriptInterface
        public void tap() {
            if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_BEEP, 45);
        }

        @JavascriptInterface
        public void openClassic() {
            runOnUiThread(new Runnable() {
                @Override public void run() {
                    startActivity(new Intent(EnhancedMainActivity.this, MainActivity.class));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.removeJavascriptInterface("Android");
            webView.destroy();
            webView = null;
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if (tones != null) {
            tones.release();
            tones = null;
        }
        super.onDestroy();
    }
}
