package com.jjswigut.feature.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jjswigut.core.base.BaseBottomSheetDialogFragment
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.feature.databinding.FragmentAddBinding
import com.jjswigut.feature.viewmodels.AddViewModel
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


    private fun discernFragmentUse() {
        when (args.listOrTask) {
            isList -> setupToAddList()
            isTask -> setupToAddTask()
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

    private fun setupToAddTask() {
        with(binding) {
            addEditText.hint = "Add Task"
            with(viewModel) {
                addEditText.onSend {
                    addTask(
                        TaskEntity(
                            body = addEditText.text.toString(),
                            parentListId = args.parentListId
                        )
                    )
                    findNavController().navigateUp()
                }
                sendButton.setOnClickListener {
                    addTask(
                        TaskEntity(
                            body = addEditText.text.toString(),
                            parentListId = args.parentListId
                        )
                    )
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupToAddList() {
        with(binding) {
            addEditText.hint = "Add List"
            with(viewModel) {
                addEditText.onSend {
                    addList(ListEntity(name = addEditText.text.toString()))
                    navigate(AddFragmentDirections.addToHome())
                }
                sendButton.setOnClickListener {
                    addList(ListEntity(name = addEditText.text.toString()))
                    navigate(AddFragmentDirections.addToHome())
                }
            }
        }
    }


    companion object {
        const val isList = 0
        const val isTask = 1
    }

}
