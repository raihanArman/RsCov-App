package id.co.list_rs.module

import id.co.list_rs.MethodViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MethodModule {
    val methodModule = module{
        viewModel{
            MethodViewModel(get())
        }
    }
}