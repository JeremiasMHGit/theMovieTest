package com.example.themovietest.model.remote

import com.example.themovietest.model.local.roomdb.DatabaseRepository
import com.example.themovietest.model.local.sharedpref.SharedPreferenceHelper
import com.example.themovietest.model.remote.network.RemoteRepository

interface Repository : RemoteRepository, SharedPreferenceHelper, DatabaseRepository