package id.co.home.module

import id.co.home.ui.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    val homeModule = module{
        viewModel{
            HomeViewModel(get())
        }
    }
}