package us.mathewtech.registry

import net.minecraft.advancements.triggers.PlayerTrigger
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import us.mathewtech.CarpetedMod

object ModCriteria {
    val APPLIED_CARPET: PlayerTrigger = register("applied_carpet")
    val DYED_CARPET: PlayerTrigger = register("dyed_carpet")
    val REMOVED_CARPET: PlayerTrigger = register("removed_carpet")
    val SILK_TOUCHED_CARPETED_BLOCK: PlayerTrigger = register("silk_touched_carpeted_block")

    fun appliedCarpet(player: ServerPlayer) {
        APPLIED_CARPET.trigger(player)
    }

    fun dyedCarpet(player: ServerPlayer) {
        DYED_CARPET.trigger(player)
    }

    fun removedCarpet(player: ServerPlayer) {
        REMOVED_CARPET.trigger(player)
    }

    fun silkTouchedCarpetedBlock(player: ServerPlayer) {
        SILK_TOUCHED_CARPETED_BLOCK.trigger(player)
    }

    private fun register(path: String): PlayerTrigger {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, CarpetedMod.id(path), PlayerTrigger())
    }
}
