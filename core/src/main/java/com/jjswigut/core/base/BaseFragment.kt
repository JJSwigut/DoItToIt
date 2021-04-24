package com.jjswigut.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jjswigut.core.utils.NavCommand

abstract class BaseFragment<ViewModel : BaseViewModel> : Fragment() {

    protected abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navCommand.observe(viewLifecycleOwner, { command ->
            when (command) {
                is NavCommand.To -> findNavController().navigate(command.directions)
            }
        })
    }
}
