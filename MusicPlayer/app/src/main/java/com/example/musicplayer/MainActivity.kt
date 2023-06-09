package com.example.musicplayer

import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var songTitleTextView: TextView
    private var currentSongIndex = 0
    private var mp: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private var songList = mutableListOf(
        Song(R.raw.music, "Chill Song"),
        Song(R.raw.music2, "Ayaz Erdoğan Hep Mi Ben?"),
        Song(R.raw.music4, "Müslüm Gürses - Seni Yazdım ") ,
                Song(R.raw.music5, "Seni Kalbimden Kovdum "),
        Song(R.raw.music6, "Ebru Gündeş - Demir Attım Yalnızlığa "),
        Song(R.raw.music7, "Ayçin Asan - Sev Yeter ( Kamuran Akkor Cover ) "),
        Song(R.raw.music8, "Murat Kekilli - Bu Akşam Ölürüm ")



    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekbar)
        songTitleTextView = findViewById(R.id.songTitleTextView)

        controlSound(songList[0])
    }

    private fun controlSound(song: Song) {
        val fabplay: FloatingActionButton = findViewById(R.id.play)
        val fabback: FloatingActionButton = findViewById(R.id.back)
        val fabNext: FloatingActionButton = findViewById(R.id.next)

        songTitleTextView.text = song.title

        fabplay.setOnClickListener {
            if (mp == null) {
                fabplay.setImageResource(R.drawable.baseline_pause_24)
                mp = MediaPlayer.create(this, song.resId)
                mp?.setOnCompletionListener {

                }
                initialiseSeekBar()
            } else {
                if (mp?.isPlaying == true) {
                    mp?.pause()
                    fabplay.setImageResource(R.drawable.baseline_play_arrow_24)
                    Log.d(TAG, "Pause: ${mp?.currentPosition!! / 1000} saniye")
                } else {
                    mp?.start()
                    fabplay.setImageResource(R.drawable.baseline_pause_24)
                    Log.d(TAG, "Duration: ${mp?.duration?.div(1000)} saniye")
                }
            }
        }

        fabNext.setOnClickListener {
            if (songList.isNotEmpty()) {
                currentSongIndex++
                if (currentSongIndex >= songList.size) {
                    currentSongIndex = 0
                }
                val nextSong = songList[currentSongIndex]
                mp?.stop()
                mp?.reset()
                mp = MediaPlayer.create(this, nextSong.resId)
                mp?.setOnCompletionListener {
                    // Şarkı tamamlandığında yapılacak işlemler
                }
                initialiseSeekBar()
                mp?.start()
                fabplay.setImageResource(R.drawable.baseline_pause_24)

                songTitleTextView.text = nextSong.title
            }
        }

        fabback.setOnClickListener {
            if (songList.isNotEmpty()) {
                currentSongIndex--
                if (currentSongIndex < 0) {
                    currentSongIndex = songList.size - 1
                }
                val previousSong = songList[currentSongIndex]
                mp?.stop()
                mp?.reset()
                mp = MediaPlayer.create(this, previousSong.resId)
                mp?.setOnCompletionListener {
                    // Şarkı tamamlandığında yapılacak işlemler
                }
                initialiseSeekBar()
                mp?.start()
                fabplay.setImageResource(R.drawable.baseline_pause_24)

                songTitleTextView.text = previousSong.title
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mp?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // İlerleme takibi başladığında burada gerekli işlemleri yapabilirsiniz.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // İlerleme takibi durduğunda burada gerekli işlemleri yapabilirsiniz.
            }
        })
    }

    private fun initialiseSeekBar() {
        seekBar.max = mp?.duration ?: 0
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                mp?.let {
                    if (it.isPlaying) {
                        seekBar.progress = it.currentPosition
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }
}

data class Song(val resId: Int, val title: String)
