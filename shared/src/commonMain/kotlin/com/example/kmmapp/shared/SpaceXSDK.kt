package com.example.kmmapp.shared

import com.example.kmmapp.shared.cache.Database
import com.example.kmmapp.shared.cache.DatabaseDriverFactory
import com.example.kmmapp.shared.entity.RocketLaunch
import com.example.kmmapp.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunched = database.getAllLaunches()
        return if (cachedLaunched.isNotEmpty() && !forceReload) {
            cachedLaunched
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}