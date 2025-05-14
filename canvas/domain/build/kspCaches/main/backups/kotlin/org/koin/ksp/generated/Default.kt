package org.koin.ksp.generated

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.*

public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule : Module = module {
	factory() { com.mhss.app.domain.use_case.GetDrawingsUseCase(repository=get()) } 
	factory() { com.mhss.app.domain.use_case.SaveDrawingUseCase(repository=get()) } 
}