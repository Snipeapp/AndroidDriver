package ru.snipe.snipedriver.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataManager
@Inject constructor() {
    fun login() {

    }

    fun resendCode(phone: String): Observable<String> =
            Observable.just(phone)
                    .map { it.replace("[^0-9]+".toRegex(), "") }
                    .map { it.substring(Math.max(0, it.length - 4)) }
                    .delay(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())

    fun verifyCode(phone: String, code: String): Observable<Boolean> =
            Observable.just(phone)
                    .map { it.replace("[^0-9]+".toRegex(), "") }
                    .map { it.substring(Math.max(0, it.length - 4)) }
                    .map { it == code }
                    .delay(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())

    fun sendCode(phone: String): Completable =
            Completable.timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
}