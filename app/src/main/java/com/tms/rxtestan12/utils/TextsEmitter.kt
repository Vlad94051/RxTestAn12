package com.tms.rxtestan12.utils

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

interface TextsEmitter {
    fun getTexts(
        texts: List<String>,
        isCycled: Boolean,
        textChangeDelayInSeconds: Long
    ): Observable<String>
}

class TextEmitterImpl : TextsEmitter {
    override fun getTexts(
        texts: List<String>,
        isCycled: Boolean,
        textChangeDelayInSeconds: Long
    ): Observable<String> {
        val observableTexts = Observable.fromIterable(texts)
        val interval = Observable.interval(0L, textChangeDelayInSeconds, TimeUnit.SECONDS)

        return observableTexts.zipWith(interval) { text, _ ->
            text
        }.isRepeatWithDelay(isCycled, textChangeDelayInSeconds)
            .observeOn(AndroidSchedulers.mainThread())
            .materialize() 
            .flatMap { notification ->
                when {
                    notification.isOnError -> Observable.just("Error")
                    notification.isOnComplete -> Observable.just("OnComplete!")
                    notification.isOnNext -> Observable.just(notification.value)
                    else -> Observable.just("")
                }
            }
    }

    private fun Observable<String>.isRepeatWithDelay(
        isRepeat: Boolean,
        delay: Long
    ): Observable<String> {
        return compose { string ->
            if (isRepeat) {
                string.repeatWhen {
                    it.delay(delay, TimeUnit.SECONDS)
                }
            } else {
                string
            }
        }
    }
}