package id.co.rscov.di

import id.co.data.domain.iterator.Iterator
import id.co.data.domain.usecase.UseCase
import id.co.home.ui.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module{
        factory<UseCase>{
            Iterator(get())
        }
    }

}