package krasnikov.project.scoreapp.ui.dialogs

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.databinding.DialogTimerSetupBinding
import krasnikov.project.scoreapp.utils.TimerStringFormatter.TIME_TEMPLATE

class TimerSetupDialog : BottomSheetDialogFragment(), View.OnClickListener,
    View.OnLongClickListener {

    private lateinit var binding: DialogTimerSetupBinding

    private val input = intArrayOf(0, 0, 0, 0, 0, 0)
    private var inputPointer = -1

    private lateinit var timeView: TextView
    private lateinit var deleteButton: View
    private lateinit var okButton: View
    private lateinit var digitViews: Array<TextView>

    val timeInMillis: Long
        get() {
            val seconds = input[1] * 10 + input[0]
            val minutes = input[3] * 10 + input[2]
            val hours = input[5] * 10 + input[4]
            return seconds * DateUtils.SECOND_IN_MILLIS +
                    minutes * DateUtils.MINUTE_IN_MILLIS +
                    hours * DateUtils.HOUR_IN_MILLIS
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            it.getIntArray(BUNDLE_INPUT)?.copyInto(input)
            inputPointer = it.getInt(BUNDLE_INPUT_POINTER)
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
        initView()
        setupListeners()
        updateTime()
        updateDelete()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(BUNDLE_INPUT, input)
        outState.putInt(BUNDLE_INPUT_POINTER, inputPointer)
    }

    override fun onClick(view: View) {
        when (view) {
            deleteButton -> {
                delete()
            }
            okButton -> {
                if (hasValidInput()) {
                    sendResult()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.msg_invalid_time),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                append(getDigitForId(view.id))
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view === deleteButton) {
            reset()
            return true
        }
        return false
    }

    private fun initView() {
        timeView = binding.tvSetupTime
        deleteButton = binding.btnDelete
        okButton = binding.btnOK
        digitViews = arrayOf(
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
    }

    private fun setupListeners() {
        deleteButton.setOnClickListener(this)
        deleteButton.setOnLongClickListener(this)
        okButton.setOnClickListener(this)
        digitViews.forEach { it.setOnClickListener(this) }
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
        val seconds = input[1] * 10 + input[0]
        val minutes = input[3] * 10 + input[2]
        val hours = input[5] * 10 + input[4]

        timeView.text = String.format(TIME_TEMPLATE, hours, minutes, seconds)
    }

    private fun append(digit: Int) {
        require(!(digit < 0 || digit > 9)) { "Invalid digit: $digit" }

        // Pressing "0" as the first digit does nothing.
        if (inputPointer == -1 && digit == 0) {
            return
        }

        // No space for more digits, so ignore input.
        if (inputPointer == input.size - 1) {
            return
        }

        // Append the new digit.
        System.arraycopy(input, 0, input, 1, inputPointer + 1)
        input[0] = digit
        inputPointer++
        updateTime()

        // Update the delete when we have valid input.
        if (inputPointer == 0) {
            updateDelete()
        }
    }

    private fun delete() {
        // Nothing exists to delete so return.
        if (inputPointer < 0) {
            return
        }

        System.arraycopy(input, 1, input, 0, inputPointer)
        input[inputPointer] = 0
        inputPointer--
        updateTime()

        // Update the delete when we no longer have valid input.
        if (inputPointer == -1) {
            updateDelete()
        }
    }

    private fun updateDelete() {
        val enabled = hasValidInput()
        deleteButton.isEnabled = enabled
    }

    private fun reset() {
        if (inputPointer != -1) {
            input.fill(0)
            inputPointer = -1
            updateTime()
            updateDelete()
        }
    }

    private fun hasValidInput(): Boolean {
        return inputPointer != -1
    }

    private fun sendResult() {
        val listener = targetFragment as? TimerSetupDialogListener
        listener?.onTimerSetupResult(timeInMillis)
        dismiss()
    }

    interface TimerSetupDialogListener {
        fun onTimerSetupResult(timeInMillis: Long)
    }

    companion object {
        private const val BUNDLE_INPUT = "BUNDLE_INPUT"
        private const val BUNDLE_INPUT_POINTER = "BUNDLE_INPUT_POINTER"

        @JvmStatic
        fun newInstance() = TimerSetupDialog()
    }
}

