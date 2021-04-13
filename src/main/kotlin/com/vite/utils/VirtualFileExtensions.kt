package com.vite.utils

import com.intellij.openapi.vfs.VirtualFile

val VirtualFile.isViteFolder: Boolean
    get() = findFileByRelativePath("vite.config.ts") != null ||
            findFileByRelativePath("vite.config.js") != null
