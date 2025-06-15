package com.github.speak2me.app.sample

import com.github.speak2me.kcp.tracing.annotations.Tracing

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

@Tracing(`return` = true)
actual fun getPlatform(): Platform = AndroidPlatform()
