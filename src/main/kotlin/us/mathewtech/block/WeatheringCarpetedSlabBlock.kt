package us.mathewtech.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import us.mathewtech.util.CarpetedCopperUtil
import java.util.Optional

class WeatheringCarpetedSlabBlock(
    baseBlock: SlabBlock,
    properties: Properties
) : CarpetedSlabBlock(baseBlock, properties), WeatheringCopper {
    private val weatheringBase: WeatheringCopper
        get() = baseBlock as WeatheringCopper

    override fun getAge(): WeatheringCopper.WeatherState {
        return weatheringBase.age
    }

    override fun getNext(state: BlockState): Optional<BlockState> {
        return CarpetedCopperUtil.nextOxidationState(state)
    }

    override fun isRandomlyTicking(state: BlockState): Boolean {
        return getNext(state).isPresent
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        changeOverTime(state, level, pos, random)
    }
}
