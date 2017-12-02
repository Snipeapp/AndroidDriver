package ru.snipe.snipedriver.ui.onboarding

import com.github.pwittchen.reactivenetwork.library.Connectivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.snipe.snipedriver.network.DataManager
import javax.inject.Inject

class OnBoardingPresenter
@Inject constructor(
        var dataManager: DataManager,
        val networkObservable: Observable<Connectivity>
) : BasePresenter<OnBoardingView>() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val networkSubject = BehaviorSubject.create<Connectivity>()

    override fun attachView(v: OnBoardingView) {
        super.attachView(v)

        networkObservable.subscribe(networkSubject)

        view?.apply {
            compositeDisposable.apply {
                add(loginButtonClicked()
                        .map { return@map (networkSubject.value.isAvailable) }
                        .subscribe({ if (it) view?.success() else view?.showError("Нет соединения с интернетом") }))
                return
            }
            return
        }
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }
}