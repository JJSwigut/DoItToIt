package com.jjswigut.core.base

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jjswigut.core.utils.NavCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseBottomSheetDialogFragment<ViewModel : BaseViewModel> :
    BottomSheetDialogFragment(), CoroutineScope {

    private lateinit var job: Job

    protected abstract val viewModel: BaseViewModel

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navCommand.observe(viewLifecycleOwner, { command ->
            when (command) {
                is NavCommand.To -> findNavController().navigate(command.directions)
                is NavCommand.DeepLink -> findNavController().navigate(command.deepLink)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
