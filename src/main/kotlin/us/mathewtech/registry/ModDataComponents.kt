package us.mathewtech.registry

import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetSurface

object ModDataComponents {
    val CARPET_SURFACE: DataComponentType<CarpetSurface> = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        CarpetedMod.id("carpet_surface"),
        DataComponentType.builder<CarpetSurface>()
            .persistent(CarpetSurface.CODEC)
            .build()
    )

    fun initialize() {
        // Loads the object so component registration happens during mod initialization.
    }
}

