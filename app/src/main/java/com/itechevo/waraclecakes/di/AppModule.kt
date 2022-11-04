package com.itechevo.waraclecakes.di

import com.itechevo.data.CakeRepositoryImpl
import com.itechevo.data.source.RetrofitProvider
import com.itechevo.data.source.WaracleApiService
import com.itechevo.domain.CakeInteractor
import com.itechevo.domain.repository.CakeRepository
import com.itechevo.domain.usecase.CakeUseCase
import com.itechevo.waraclecakes.ui.CakeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { CakeViewModel(get()) }
}

val domainModule = module {

    single<CakeUseCase> { CakeInteractor(get(), Dispatchers.IO) }
}

val dataModule = module {

    single<CakeRepository> { CakeRepositoryImpl(get(), Dispatchers.IO) }

    single<WaracleApiService> {
        RetrofitProvider.get(WaracleApiService.BASE_URL)
            .create(WaracleApiService::class.java)
    }
}
