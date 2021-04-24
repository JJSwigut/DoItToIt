package com.jjswigut.feature.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jjswigut.core.base.BaseBottomSheetDialogFragment
import com.jjswigut.data.local.AddType
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.feature.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : BaseBottomSheetDialogFragment<AddViewModel>() {

    override val viewModel: AddViewModel by activityViewModels()

    private val args: AddFragmentArgs by navArgs()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discernFragmentUse()
    }

    override fun onResume() {
        super.onResume()
        binding.addEditText.focusAndShowKeyboard()
    }

    private fun discernFragmentUse() {
        when (args.listOrTask) {
            AddType.IsList.ordinal -> setUpButtons("Add List") { setupToAddList(it) }
            AddType.IsTask.ordinal -> setUpButtons("Add Task") { setupToAddTask(it) }
        }
    }

    private fun EditText.onSend(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                callback.invoke()
                true
            }
            false
        }
    }

    private fun setUpButtons(hint: String, callback: (String) -> Unit) {
        with(binding) {
            addEditText.hint = hint
            addEditText.onSend {
                callback.invoke(addEditText.text.toString())
            }
            sendButton.setOnClickListener {
                callback.invoke(addEditText.text.toString())
            }
        }
    }

    private fun setupToAddTask(text: String) {
        viewModel.addTask(
            TaskEntity(
                body = text,
                parentListId = args.parentListId
            )
        )
        findNavController().navigateUp()
    }

    private fun setupToAddList(text: String) {
        viewModel.addList(
            ListEntity(
                name = text
            )
        )
        findNavController().navigateUp()
    }

    fun View.focusAndShowKeyboard() {
        fun View.showTheKeyboardNow() {
            if (isFocused) {
                post {
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        requestFocus()
        if (hasWindowFocus()) {

            showTheKeyboardNow()
        } else {

            viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {

                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {

                            this@focusAndShowKeyboard.showTheKeyboardNow()

                            viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }
}
