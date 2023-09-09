package com.example.mynotes.utils.di

import androidx.fragment.app.Fragment
import com.example.mynotes.ui.add.AddContract
import com.example.mynotes.ui.home.HomeContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object BaseFragmentView {
    @Provides
    fun view(fragment: Fragment) = fragment as HomeContract.View
    @Provides
    fun provideAddView(fragment: Fragment) = fragment as AddContract.View
}