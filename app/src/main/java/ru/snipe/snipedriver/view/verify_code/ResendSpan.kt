package ru.snipe.snipedriver.view.verify_code

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ResendSpan(val verifyCodeView: VerifyCodeView) : ClickableSpan() {
    override fun onClick(widget: View?) {
        verifyCodeView.resendClicked().onNext(true)
    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.isUnderlineText = false
        ds?.isFakeBoldText = false
    }
}