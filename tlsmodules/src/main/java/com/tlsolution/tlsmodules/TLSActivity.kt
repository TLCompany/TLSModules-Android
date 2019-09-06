package com.tlsolution.tlsmodules


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.action_bar.view.*

open class TLSActivity: AppCompatActivity() {

    internal var actionBar: View? = null
    internal var actionBarType = ActionBarType.presented

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    /**
     * 뒤로 버튼을 눌렀을 때 이벤트 처리 함수
     */
    open fun backAction() {
        finish()
        setTransition()
    }

    private fun setTransition() {
        if (actionBarType == ActionBarType.pushedLeft) overridePendingTransition(R.anim.push_right_out, R.anim.push_left_in)
    }

    /**
     * 오른쪽 버튼이 있어서 눌렀을 때 이벤트 처리 함수
     */
    open fun rightAction() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                setTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            setTransition()
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * Custom Action Bar를 설정한다.
     */
    fun setUpActionBar(actionBar: View,
                       title: String,
                       rightButtonTitle: String? = null,
                       rightButtonTitleColor: Int = Color.BLACK,
                       rightButtonImage: Drawable? = null) {


        this.actionBar = actionBar
        actionBar.titleTextView.setText(title)
        actionBar.backButton.setOnClickListener {
            backAction()
        }

//        actionBar.backButton.setImageDrawable(getDrawable(this.actionBarType.leftImage))

        if (rightButtonTitle == null) {
            actionBar.rightTextButton.visibility = View.INVISIBLE
            actionBar.rightImageButton.visibility = if (rightButtonImage == null) View.VISIBLE else View.INVISIBLE
            actionBar.rightImageButton.setImageDrawable(rightButtonImage)
            actionBar.rightImageButton.setOnClickListener {
                rightAction()
            }
        } else {
            actionBar.rightImageButton.visibility = View.INVISIBLE
            actionBar.rightTextButton.setTextColor(rightButtonTitleColor)
            actionBar.rightTextButton.setText(rightButtonTitle)
            actionBar.rightTextButton.setOnClickListener {
                rightAction()
            }
        }
        if (actionBarType == ActionBarType.pushedLeft) overridePendingTransition(R.anim.push_left_out, R.anim.push_right_in)
    }
}




enum class ActionBarType {
    pushedLeft, presented;

    var leftImage: Int = R.drawable.action_bar_back
        get() {
            return if (this == presented) R.drawable.action_bar_close else R.drawable.action_bar_back
        }
}