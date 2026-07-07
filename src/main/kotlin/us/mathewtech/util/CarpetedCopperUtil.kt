package us.mathewtech.util

import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import us.mathewtech.block.CarpetedBlock
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.registry.ModBlocks
import java.util.Optional

object CarpetedCopperUtil {
    fun isWeatheringCarpeted(block: Block): Boolean {
        return block is WeatheringCopper && block is CarpetedBlock
    }

    fun isWaxedCarpeted(block: Block): Boolean {
        if (block !is CarpetedBlock) {
            return false
        }

        return HoneycombItem.WAX_OFF_BY_BLOCK.get().containsKey(block.baseBlock)
    }

    fun toBaseState(state: BlockState, carpeted: CarpetedBlock): BlockState {
        return when (carpeted) {
            is CarpetedSlabBlock -> StateCopyUtil.copyCarpetedToBaseSlab(state, carpeted)
            is CarpetedStairBlock -> StateCopyUtil.copyCarpetedToBaseStairs(state, carpeted)
            else -> error("Unsupported carpeted block: $carpeted")
        }
    }

    fun waxedState(state: BlockState): Optional<BlockState> {
        val carpeted = state.block as? CarpetedBlock ?: return Optional.empty()
        val waxedBase = HoneycombItem.getWaxed(toBaseState(state, carpeted)).orElse(null) ?: return Optional.empty()
        return carpetedStateForBase(state, waxedBase.block)
    }

    fun unwaxedState(state: BlockState): Optional<BlockState> {
        val carpeted = state.block as? CarpetedBlock ?: return Optional.empty()
        val unwaxedBase = HoneycombItem.WAX_OFF_BY_BLOCK.get()[carpeted.baseBlock] ?: return Optional.empty()
        return carpetedStateForBase(state, unwaxedBase)
    }

    fun previousOxidationState(state: BlockState): Optional<BlockState> {
        val carpeted = state.block as? CarpetedBlock ?: return Optional.empty()
        val previousBase = WeatheringCopper.getPrevious(toBaseState(state, carpeted)).orElse(null)
            ?: return Optional.empty()
        return carpetedStateForBase(state, previousBase.block)
    }

    fun nextOxidationState(state: BlockState): Optional<BlockState> {
        val carpeted = state.block as? CarpetedBlock ?: return Optional.empty()
        if (carpeted.baseBlock !is WeatheringCopper) {
            return Optional.empty()
        }

        val nextBase = WeatheringCopper.getNext(carpeted.baseBlock).orElse(null) ?: return Optional.empty()
        return carpetedStateForBase(state, nextBase)
    }

    private fun carpetedStateForBase(state: BlockState, baseBlock: Block): Optional<BlockState> {
        val carpetedBlock = ModBlocks.carpetedForBase(baseBlock) ?: return Optional.empty()
        return Optional.of(carpetedBlock.withPropertiesOf(state))
    }
}
