package com.example.rlindoso.rxandroid

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


val serverDownloadObservable = Observable.create<Int> { emitter ->
    for (i in 0..10){
        SystemClock.sleep(1000) // simulate delay
        emitter.onNext(i)
    }
    emitter.onComplete()
}!!

var disposable = CompositeDisposable()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { v ->
            v.isEnabled = false // disables the button until execution has finished
            edttext.text = "esperando"
            val subscribe =
                serverDownloadObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe { integer ->
                        if (integer == 0){
                            edttext.text = integer.toString()
                        } else {
                            edttext.text = edttext.text as String +", " +integer.toString() // this methods updates the ui
                        }
                        v.isEnabled = true
                    }

            disposable.add(subscribe)
        }
        button2.setOnClickListener { v ->
            v.isEnabled = false // disables the button until execution has finished
            edttext2.text = "esperando"
            val subscribe =
                serverDownloadObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe { integer ->
                        edttext2.text = integer.toString() // this methods updates the ui
                        v.isEnabled = true // enables it again
                    }
            disposable.add(subscribe)
        }
    }
}
