package id.co.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.co.data.domain.usecase.UseCase

class HomeViewModel(useCase: UseCase): ViewModel() {
    val getHospital = useCase.getHospital().asLiveData()
}