package com.example.kidslearningv3

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val channelName = "kids_audio"
    private var player: MediaPlayer? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName).setMethodCallHandler { call, result ->
            if (call.method == "playAsset") {
                val file = call.argument<String>("file")
                if (file.isNullOrEmpty()) {
                    result.error("NO_FILE", "No file path provided", null)
                    return@setMethodCallHandler
                }

                try {
                    playAsset(file)
                    result.success(true)
                } catch (e: Exception) {
                    result.error("PLAY_ERROR", e.message, null)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    private fun playAsset(file: String) {
        player?.stop()
        player?.release()
        player = null

        val assetKey = FlutterInjector.instance().flutterLoader().getLookupKeyForAsset(file)
        val afd: AssetFileDescriptor = assets.openFd(assetKey)

        val mp = MediaPlayer()
        mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        mp.prepare()
        mp.start()

        player = mp

        mp.setOnCompletionListener {
            it.release()
            if (player == it) {
                player = null
            }
        }
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}
