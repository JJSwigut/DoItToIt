package com.jjswigut.feature.views

import androidx.fragment.app.activityViewModels
import com.jjswigut.core.base.BaseFragment
import com.jjswigut.feature.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()


}