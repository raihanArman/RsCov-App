package id.co.rscov.di

import id.co.data.domain.iterator.Iterator
import id.co.data.domain.usecase.UseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module{
        factory<UseCase>{
            Iterator(get())
        }
    }
//
//    val viewModelModule = module{
//        viewModel{AnimeViewModel(get())}
//    }

}