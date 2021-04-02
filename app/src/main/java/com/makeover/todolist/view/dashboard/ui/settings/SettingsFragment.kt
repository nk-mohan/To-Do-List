package com.makeover.todolist.view.dashboard.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.makeover.todolist.databinding.FragmentSettingsBinding
import com.makeover.todolist.databinding.SettingsDataBinding
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(),
    ViewBindingHolder<FragmentSettingsBinding> by ViewBindingHolderImpl() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var settingsDataBinding: SettingsDataBinding

    private var _settingFragmentBinding: FragmentSettingsBinding? = null

    private val settingFragmentBinding get() = _settingFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentSettingsBinding.inflate(layoutInflater), this) {
        _settingFragmentBinding = requireBinding()

        settingFragmentBinding.settingsBindingData = settingsDataBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsDataBinding.setActivity(requireActivity(), settingsViewModel)
        settingsViewModel.getSelectedTheme()

        setObservers()
    }

    private fun setObservers() {
        settingsViewModel.selectedTheme.observe(viewLifecycleOwner, {
           settingFragmentBinding.themeTextView.text = settingsDataBinding.selectedTheme
        })
    }
}