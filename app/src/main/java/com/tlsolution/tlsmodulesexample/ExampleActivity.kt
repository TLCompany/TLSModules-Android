package com.tlsolution.tlsmodulesexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tlsolution.tlsmodules.Models.Inquery
import com.tlsolution.tlsmodules.Deprecated.InqueryManager
import com.tlsolution.tlsmodules.Models.Notice
import com.tlsolution.tlsmodules.Deprecated.NoticeManager
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.Deprecated.PolicyManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
