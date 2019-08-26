package com.ciyuanplus.mobile.dialog


import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ciyuanplus.mobile.R
import com.kris.baselibrary.util.NumberUtil
import kotlinx.android.synthetic.main.fragment_pay_result_dialog.*


/**
 * A simple [Fragment] subclass.
 */
class PayResultDialogFragment : DialogFragment() {


    private var type: Int = 0

    private var money: String? = null


    companion object {

        const val paySuccess = 1
        const val payFail = 0

        fun newInstance(payResult: Int, money: String?): PayResultDialogFragment {

            val dialog = PayResultDialogFragment()
            val args = Bundle()
            args.putInt("payResult", payResult)
            args.putString("payMoney", money)
            dialog.arguments = args
            return dialog
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            money = it.getString("payMoney", "-1")
            type = it.getInt("payResult", -1)
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return activity!!.layoutInflater.inflate(R.layout.fragment_pay_result_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (money is String && "-1"!=money) {
            priceText.visibility = View.VISIBLE
            priceText.text = "￥${NumberUtil.getAmountValue(money)}"
        }else{
            priceText.visibility = View.GONE
            priceText.text = "￥0.00}"
        }

        when (type) {

            payFail -> {
                resultImage.setImageResource(R.drawable.icon_pay_result_fail)
                resultText.text = "支付失败"
            }
            else -> {
                resultImage.setImageResource(R.drawable.icon_pay_result_success)
                resultText.text = "支付成功"

            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val dialogWindow = dialog?.window
        val lp = dialogWindow?.attributes
        lp!!.dimAmount = 0.5f
        dialogWindow.attributes = lp


        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialogWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.invoke(Unit)
    }

    var listener: ((Unit) -> Unit)? = null


    fun addListener(e: (Unit) -> Unit) {
        this.listener = e
    }
}
