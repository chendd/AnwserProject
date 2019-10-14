package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import android.support.v4.content.ContextCompat
import android.text.format.DateUtils
import android.widget.SeekBar
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.service.AudioPlayer
import com.wetin.anwserproject.service.OnPlayerEventListener
import com.wetin.anwserproject.utils.Preferences
import com.wetin.anwserproject.utils.SystemUtils
import com.wetin.anwserproject.widget.MusicListDialog
import kotlinx.android.synthetic.main.activity_music_play.*


class MusicPlayActivity : BaseAnswerActivity<IBaseContract.BasePresenter>(), SeekBar.OnSeekBarChangeListener,
    OnPlayerEventListener {
    var mDialogList:MusicListDialog?=null
    private var isDraggingProgress: Boolean = false
    private var mLastProgress: Int = 0
    override fun getContentId(): Int=R.layout.activity_music_play
    override fun initPresenter(): IBaseContract.BasePresenter? =null

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("音乐播放器",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))

        onChangeImpl(AudioPlayer.get().playMusic)
        initPlayMode()
        AudioPlayer.get().addOnPlayEventListener(this)
    }

    private fun onChangeImpl(playMusic: ChapterBean?) {
        if (playMusic == null) {
            return
        }
        wg_seekBar.progress = AudioPlayer.get().audioPosition.toInt()
        wg_ci.setText("词")
        wg_seekBar.secondaryProgress = 0
        wg_seekBar.max = playMusic?.videoTime.toInt()
        wg_tv_current_time.text = "00:00"
        wg_totle_time.text = SystemUtils.formatTime("mm:ss", playMusic.videoTime.toLong())
    }

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        AudioPlayer.get().addAndPlay(intent.getSerializableExtra("data") as ChapterBean)
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_seekBar.setOnSeekBarChangeListener(this)
        wg_btn_pres.setOnClickListener {
            AudioPlayer.get().prev()
        }

        wg_btn_next.setOnClickListener {
            AudioPlayer.get().next()
        }

        wg_btn_list.setOnClickListener {
            if(mDialogList==null) {
                mDialogList= MusicListDialog(this)
                mDialogList?.setData(AudioPlayer.get().musicList as ArrayList<ChapterBean>)
            }
            mDialogList?.show()
        }
    }

    private fun initPlayMode() {
        val mode = Preferences.getPlayMode()
        wg_model.setImageLevel(mode)
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
            wg_tv_current_time.text = SystemUtils.formatTime("mm:ss", progress.toLong())
            AudioPlayer.get().seekTo(progress)
            this.mLastProgress = progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        AudioPlayer.get().startPlayer()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        AudioPlayer.get().stopPlayer()
    }

    override fun onChange(music: ChapterBean?) {
    }

    override fun onPlayerStart() {

    }

    override fun onPlayerPause() {

    }

    override fun onPublish(progress: Int) {
        if (!isDraggingProgress) {
            wg_seekBar.progress = progress
        }
    }

    override fun onBufferingUpdate(percent: Int) {
        wg_seekBar.secondaryProgress = wg_seekBar.getMax() * 100 / percent
    }


    override fun onDestroy() {
        AudioPlayer.get().removeOnPlayEventListener(this)
        super.onDestroy()
    }

    companion object {
        fun launcher(fromActivity: Activity,chapterBean: ChapterBean){
            val intent = Intent(fromActivity, MusicPlayActivity::class.java)
            intent.putExtra("data",chapterBean)
            fromActivity.startActivity(intent)
        }
    }

}
