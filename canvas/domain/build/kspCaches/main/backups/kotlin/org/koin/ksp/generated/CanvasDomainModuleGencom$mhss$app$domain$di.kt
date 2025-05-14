package org.koin.ksp.generated

import org.koin.core.module.Module
import org.koin.dsl.*


public val com_mhss_app_domain_di_CanvasDomainModule : Module = module {
	factory() { com.mhss.app.domain.use_case.GetDrawingsUseCase(repository=get()) } 
	factory() { com.mhss.app.domain.use_case.SaveDrawingUseCase(repository=get()) } 
}
public val com.mhss.app.domain.di.CanvasDomainModule.module : org.koin.core.module.Module get() = com_mhss_app_domain_di_CanvasDomainModule