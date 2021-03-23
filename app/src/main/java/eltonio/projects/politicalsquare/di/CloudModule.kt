package eltonio.projects.politicalsquare.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eltonio.projects.politicalsquare.repository.CloudRepository
import eltonio.projects.politicalsquare.repository.ProdCloudRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CloudModule {

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database

    @Singleton
    @Provides
    fun provideCloudRepository(
        firebaseAnalytics: FirebaseAnalytics,
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): CloudRepository = ProdCloudRepository(firebaseAnalytics, firebaseAuth, firebaseDatabase)

}