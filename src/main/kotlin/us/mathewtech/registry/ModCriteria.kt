package us.mathewtech.registry

import net.minecraft.advancements.triggers.PlayerTrigger
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import us.mathewtech.CarpetedMod

object ModCriteria {
    private val APPLIED_CARPET = register("applied_carpet")
    private val DYED_CARPET = register("dyed_carpet")
    private val REMOVED_CARPET = register("removed_carpet")
    private val SILK_TOUCHED_CARPETED_BLOCK = register("silk_touched_carpeted_block")

    fun initialize() {
    }

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
