package com.ajrock.knowyourwoof.extensions

import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

val WindowSizeClass.isExpanded
    get() = windowWidthSizeClass == WindowWidthSizeClass.EXPANDED