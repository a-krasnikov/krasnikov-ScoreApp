package krasnikov.project.scoreapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.DialogTimerSetupBinding
import krasnikov.project.scoreapp.utils.TimerStringFormatter.HOUR_IN_SECONDS
import krasnikov.project.scoreapp.utils.TimerStringFormatter.MINUTE_IN_SECONDS

class TimerSetupDialog : BottomSheetDialogFragment(), View.OnClickListener,
    View.OnLongClickListener {

    private lateinit var binding: DialogTimerSetupBinding

    private val mInput = intArrayOf(0, 0, 0, 0, 0, 0)

    private var mInputPointer = -1
    private val mTimeTemplate = "%02d:%02d:%02d"

    private lateinit var mTimeView: TextView
    private lateinit var mDeleteView: View
    private lateinit var mOkView: View
    private lateinit var mDigitViews: Array<TextView>

    private val timeInSeconds: Int
        get() {
            val seconds = mInput[1] * 10 + mInput[0]
            val minutes = mInput[3] * 10 + mInput[2]
            val hours = mInput[5] * 10 + mInput[4]
            return seconds +
                    minutes * MINUTE_IN_SECONDS +
                    hours * HOUR_IN_SECONDS
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            it.getIntArray(BUNDLE_INPUT)?.copyInto(mInput)
            mInputPointer = it.getInt(BUNDLE_INPUT_POINTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTimerSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTimeView = binding.tvSetupTime
        mDeleteView = binding.btnDelete
        mOkView = binding.btnOK
        mDigitViews = arrayOf(
            binding.btn0,
            binding.btn2,
            binding.btn3,
            binding.btn1,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )

        setupListeners()
        updateTime()
        updateDelete()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(BUNDLE_INPUT, mInput)
        outState.putInt(BUNDLE_INPUT_POINTER, mInputPointer)
    }

    override fun onClick(view: View) {

        when (view) {
            mDeleteView -> {
                delete()
            }
            mOkView -> {
                if (hasValidInput()) {
                    sendResult()
                }
                //TODO show toast enter valid input
            }
            else -> {
                append(getDigitForId(view.id))
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view === mDeleteView) {
            reset()
            return true
        }
        return false
    }

    private fun setupListeners() {
        mDeleteView.setOnClickListener(this)
        mDeleteView.setOnLongClickListener(this)
        mOkView.setOnClickListener(this)
        mDigitViews.forEach { it.setOnClickListener(this) }
    }

    private fun getDigitForId(@IdRes id: Int): Int = when (id) {
        R.id.btn0 -> 0
        R.id.btn1 -> 1
        R.id.btn2 -> 2
        R.id.btn3 -> 3
        R.id.btn4 -> 4
        R.id.btn5 -> 5
        R.id.btn6 -> 6
        R.id.btn7 -> 7
        R.id.btn8 -> 8
        R.id.btn9 -> 9
        else -> throw IllegalArgumentException("Invalid id: $id")
    }

    private fun updateTime() {
        val seconds = mInput[1] * 10 + mInput[0]
        val minutes = mInput[3] * 10 + mInput[2]
        val hours = mInput[5] * 10 + mInput[4]

        mTimeView.text = String.format(mTimeTemplate, hours, minutes, seconds)
    }

    private fun append(digit: Int) {
        require(!(digit < 0 || digit > 9)) { "Invalid digit: $digit" }

        // Pressing "0" as the first digit does nothing.
        if (mInputPointer == -1 && digit == 0) {
            return
        }

        // No space for more digits, so ignore input.
        if (mInputPointer == mInput.size - 1) {
            return
        }

        // Append the new digit.
        System.arraycopy(mInput, 0, mInput, 1, mInputPointer + 1)
        mInput[0] = digit
        mInputPointer++
        updateTime()

        // Update the delete when we have valid input.
        if (mInputPointer == 0) {
            updateDelete()
        }
    }

    private fun delete() {
        // Nothing exists to delete so return.
        if (mInputPointer < 0) {
            return
        }

        System.arraycopy(mInput, 1, mInput, 0, mInputPointer)
        mInput[mInputPointer] = 0
        mInputPointer--
        updateTime()

        // Update the delete when we no longer have valid input.
        if (mInputPointer == -1) {
            updateDelete()
        }
    }

    private fun updateDelete() {
        val enabled = hasValidInput()
        mDeleteView.isEnabled = enabled
    }

    private fun reset() {
        if (mInputPointer != -1) {
            mInput.fill(0)
            mInputPointer = -1
            updateTime()
            updateDelete()
        }
    }

    private fun hasValidInput(): Boolean {
        return mInputPointer != -1
    }

    private fun sendResult() {
        val listener = targetFragment as? TimerSetupDialogListener
        listener?.onTimerSetupResult(timeInSeconds)
        dismiss()
    }

    interface TimerSetupDialogListener {
        fun onTimerSetupResult(timeInSecond: Int)
    }

    companion object {
        private const val BUNDLE_INPUT = "BUNDLE_INPUT"
        private const val BUNDLE_INPUT_POINTER = "BUNDLE_INPUT_POINTER"

        @JvmStatic
        fun newInstance() = TimerSetupDialog()
    }
}

