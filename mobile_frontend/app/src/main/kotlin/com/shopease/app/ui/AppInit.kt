package com.shopease.app.ui

import android.content.Context
import androidx.startup.Initializer
import com.shopease.app.data.repository.ServiceLocator

/**
 * PUBLIC_INTERFACE
 * AppInitializer initializes ServiceLocator on app start.
 */
class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ServiceLocator.init(context.applicationContext)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
