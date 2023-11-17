package com.android.fastfood.dependencyinjection

import com.android.fastfood.data.local.database.AppDatabase
import com.android.fastfood.data.local.database.datasource.CartDataSource
import com.android.fastfood.data.local.database.datasource.CartDatabaseDataSource
import com.android.fastfood.data.network.api.datasource.FoodMenuApiDataSource
import com.android.fastfood.data.network.api.datasource.FoodMenuDataSource
import com.android.fastfood.data.network.api.service.FastFoodApiService
import com.android.fastfood.data.repository.CartRepository
import com.android.fastfood.data.repository.CartRepositoryImpl
import com.android.fastfood.data.repository.FoodMenuRepository
import com.android.fastfood.data.repository.FoodMenuRepositoryImpl
import com.android.fastfood.presentation.auth.firebase.FirebaseAuthDataSource
import com.android.fastfood.presentation.auth.firebase.FirebaseAuthDataSourceImpl
import com.android.fastfood.presentation.auth.firebase.repository.UserRepository
import com.android.fastfood.presentation.auth.firebase.repository.UserRepositoryImpl
import com.android.fastfood.presentation.auth.login.LoginViewModel
import com.android.fastfood.presentation.auth.register.RegisterViewModel
import com.android.fastfood.presentation.auth.splashscreen.SplashScreenViewModel
import com.android.fastfood.presentation.checkoutactivity.CheckOutViewModel
import com.android.fastfood.presentation.detailactivity.DetailViewModel
import com.android.fastfood.presentation.fragmentProfile.ProfileViewModel
import com.android.fastfood.presentation.fragmentcart.CartViewModel
import com.android.fastfood.presentation.fragmenthome.HomeViewModel
import com.android.fastfood.utils.AssetWrapper
import com.android.fastfood.utils.PreferenceDataStoreHelper
import com.android.fastfood.utils.PreferenceDataStoreHelperImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModules {

    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { FastFoodApiService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<FoodMenuDataSource> { FoodMenuApiDataSource(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<FoodMenuRepository> { FoodMenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
        viewModel { params -> DetailViewModel(params.get(), get()) }
        viewModelOf(::CheckOutViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::SplashScreenViewModel)

    }

    private val utilsModule = module {
        single { AssetWrapper(androidContext()) }
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule,
        utilsModule
    )
}
