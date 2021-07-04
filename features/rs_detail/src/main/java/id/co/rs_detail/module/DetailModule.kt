package id.co.rs_detail.module

import id.co.rs_detail.ui.DetailViewModel
import org.koin.android.viewmodel.dsl.viewModel

import org.koin.dsl.module

object DetailModule {
    val detailModule = module{
        viewModel{
            DetailViewModel(get())
        }
    }
}