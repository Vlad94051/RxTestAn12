package com.tms.rxtestan12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.tms.rxtestan12.utils.TextEmitterImpl
import com.tms.rxtestan12.utils.TextsEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.hwTextView) }
    private val textEmitter: TextsEmitter by lazy { TextEmitterImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val texts = listOf(
            "Начинаем загрузку",
            "Загружаем",
            "Продолжаем загрузку",
            "Загрузили"
        )

        val result = textEmitter.getTexts(texts, false, 2L)

            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { item ->
                textView.text = item
                Log.e("!!!: ", item)
            }
    }
}