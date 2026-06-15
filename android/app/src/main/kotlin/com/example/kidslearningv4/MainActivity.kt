package com.example.kidslearningv4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Base64
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.File

class MainActivity: FlutterActivity() {
    private val channelName = "kids_audio"
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null
    private var activeRecordingFile: File? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName).setMethodCallHandler { call, result ->
            when (call.method) {
                "playAsset" -> {
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
                }
                "playRecordingOrAsset" -> {
                    val key = call.argument<String>("key") ?: ""
                    val file = call.argument<String>("file") ?: ""
                    if (file.isEmpty()) {
                        result.error("NO_FILE", "No file path provided", null)
                        return@setMethodCallHandler
                    }
                    try {
                        val recording = recordingFile(key)
                        if (key.isNotEmpty() && recording.exists() && recording.length() > 0L) {
                            playFile(recording)
                        } else {
                            playAsset(file)
                        }
                        result.success(true)
                    } catch (e: Exception) {
                        result.error("PLAY_ERROR", e.message, null)
                    }
                }
                "startRecording" -> {
                    val key = call.argument<String>("key") ?: ""
                    if (key.isEmpty()) {
                        result.error("NO_KEY", "No recording key provided", null)
                        return@setMethodCallHandler
                    }
                    try {
                        if (!hasRecordPermission()) {
                            requestRecordPermission()
                            result.success(false)
                            return@setMethodCallHandler
                        }
                        startRecording(key)
                        result.success(true)
                    } catch (e: Exception) {
                        result.error("RECORD_ERROR", e.message, null)
                    }
                }
                "stopRecording" -> {
                    try {
                        result.success(stopRecording())
                    } catch (e: Exception) {
                        result.error("STOP_ERROR", e.message, null)
                    }
                }
                "playRecording" -> {
                    val key = call.argument<String>("key") ?: ""
                    try {
                        val file = recordingFile(key)
                        if (!file.exists() || file.length() == 0L) {
                            result.success(false)
                            return@setMethodCallHandler
                        }
                        playFile(file)
                        result.success(true)
                    } catch (e: Exception) {
                        result.error("PLAY_RECORDING_ERROR", e.message, null)
                    }
                }
                "hasRecording" -> {
                    val key = call.argument<String>("key") ?: ""
                    val file = recordingFile(key)
                    result.success(file.exists() && file.length() > 0L)
                }
                "deleteRecording" -> {
                    val key = call.argument<String>("key") ?: ""
                    try {
                        val file = recordingFile(key)
                        if (activeRecordingFile?.absolutePath == file.absolutePath) {
                            stopRecording()
                        }
                        result.success(file.exists() && file.delete())
                    } catch (e: Exception) {
                        result.error("DELETE_ERROR", e.message, null)
                    }
                }
                "loadInt" -> {
                    val key = call.argument<String>("key") ?: ""
                    val prefs = getSharedPreferences("kids_progress", Context.MODE_PRIVATE)
                    result.success(prefs.getInt(key, 0))
                }
                "saveInt" -> {
                    val key = call.argument<String>("key") ?: ""
                    val value = call.argument<Int>("value") ?: 0
                    val prefs = getSharedPreferences("kids_progress", Context.MODE_PRIVATE)
                    prefs.edit().putInt(key, value).apply()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun hasRecordPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestRecordPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 8105)
        }
    }

    private fun recordingsDir(): File {
        val dir = File(filesDir, "parent_recordings")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun recordingFile(key: String): File {
        val safe = Base64.encodeToString(
            key.toByteArray(Charsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP
        ).replace("=", "")
        return File(recordingsDir(), "$safe.m4a")
    }

    private fun startRecording(key: String) {
        stopRecording()
        player?.stop()
        player?.release()
        player = null

        val file = recordingFile(key)
        if (file.exists()) {
            file.delete()
        }

        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setAudioSamplingRate(44100)
        mediaRecorder.setAudioEncodingBitRate(96000)
        mediaRecorder.setOutputFile(file.absolutePath)
        mediaRecorder.prepare()
        mediaRecorder.start()

        recorder = mediaRecorder
        activeRecordingFile = file
    }

    private fun stopRecording(): Boolean {
        val currentRecorder = recorder ?: return false
        val file = activeRecordingFile
        var saved = false

        try {
            currentRecorder.stop()
            saved = file != null && file.exists() && file.length() > 0L
        } catch (_: RuntimeException) {
            file?.delete()
        } finally {
            currentRecorder.release()
            recorder = null
            activeRecordingFile = null
        }

        return saved
    }

    private fun playAsset(file: String) {
        player?.stop()
        player?.release()
        player = null

        val assetKey = FlutterInjector.instance().flutterLoader().getLookupKeyForAsset(file)
        val afd: AssetFileDescriptor = assets.openFd(assetKey)

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        mediaPlayer.prepare()
        mediaPlayer.start()

        player = mediaPlayer
        mediaPlayer.setOnCompletionListener {
            it.release()
            if (player == it) {
                player = null
            }
        }
    }

    private fun playFile(file: File) {
        player?.stop()
        player?.release()
        player = null

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()

        player = mediaPlayer
        mediaPlayer.setOnCompletionListener {
            it.release()
            if (player == it) {
                player = null
            }
        }
    }

    override fun onDestroy() {
        stopRecording()
        player?.release()
        player = null
        super.onDestroy()
    }
}
