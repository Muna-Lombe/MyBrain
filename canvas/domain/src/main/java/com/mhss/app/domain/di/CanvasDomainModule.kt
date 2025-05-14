//package com.mhss.app.domain.di
//
//import org.koin.core.annotation.ComponentScan
//import org.koin.core.annotation.Module
//
//@Module
//@ComponentScan("com.mhss.app.domain")
//class CanvasDomainModule
package com.mhss.app.domain.di

import com.mhss.app.domain.repository.CanvasRepository
import com.mhss.app.domain.use_case.CanvasUseCases
import com.mhss.app.domain.use_case.DeleteCanvasUseCase
import com.mhss.app.domain.use_case.GetAllCanvasUseCase
import com.mhss.app.domain.use_case.GetCanvasUseCase
import com.mhss.app.domain.use_case.InsertCanvasUseCase
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
internal class CanvasDomainModule {
    fun provideCanvasUseCases(
        getCanvasUseCase: GetCanvasUseCase,
        getAllCanvasesUseCase: GetAllCanvasUseCase,
        insertCanvasUseCase: InsertCanvasUseCase,
        deleteCanvasUseCase: DeleteCanvasUseCase
    ): CanvasUseCases {
        return CanvasUseCases(
            getCanvasUseCase = getCanvasUseCase,
            getAllCanvasesUseCase = getAllCanvasesUseCase,
            insertCanvasUseCase = insertCanvasUseCase,
            deleteCanvasUseCase = deleteCanvasUseCase
        )
    }

    fun provideInsertCanvasUseCase(repository: CanvasRepository): InsertCanvasUseCase{
        return InsertCanvasUseCase(repository)
    }

    fun provideDeleteCanvasUseCase(repository: CanvasRepository): DeleteCanvasUseCase{
        return DeleteCanvasUseCase(repository)
    }

    fun provideGetAllCanvasesUseCase(repository: CanvasRepository): GetAllCanvasUseCase{
        return GetAllCanvasUseCase(repository)
    }
    fun provideGetCanvasUseCase(repository: CanvasRepository): GetCanvasUseCase{
        return GetCanvasUseCase(repository)
    }
}

val canvasDomainModule = module {
    single { provideCanvasUseCases(get(),get(),get(),get()) }
    single { provideInsertCanvasUseCase(get()) }
    single { provideDeleteCanvasUseCase(get()) }
    single { provideGetAllCanvasesUseCase(get()) }
    single { provideGetCanvasUseCase(get()) }
}