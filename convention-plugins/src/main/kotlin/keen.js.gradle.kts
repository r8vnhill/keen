import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("keen.kotlin")
}

kotlin {
    js {
        nodejs()
    }

    // FIXME: java.lang.NullPointerException:
    //  null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor
//   @OptIn(ExperimentalWasmDsl::class)
//   wasmJs {
//      browser()
//      nodejs()
//   }

    /* FIXME: enable wasmWasi when there is support in kotlinx-coroutines-core (1.8.0-RC does only wasmJs)
       wasmWasi {
          nodejs()
       }
       */

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        group("common") {
            group("jsHosted") {
                withJs()
//               withWasm() // FIXME with Kotlin 2.0.0: KT-63417 – to be split into `withWasmJs` and `withWasmWasi`
            }
        }
    }
}
