package com.mhss.app.data

import com.mhss.app.domain.di.CanvasDomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
//import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.mhss.app.data")
internal class CanvasDataModule


val canvasDataModule = module {
    includes(CanvasDomainModule)
}