package us.mathewtech.integration.rei

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import java.util.Optional

class CarpetingDisplay(
    inputs: List<EntryIngredient>,
    outputs: List<EntryIngredient>,
    location: Optional<Identifier> = Optional.empty()
) : BasicDisplay(inputs, outputs, location) {
    constructor(inputs: List<EntryIngredient>, outputs: List<EntryIngredient>) : this(inputs, outputs, Optional.empty())

    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.CARPETING

    override fun getSerializer(): DisplaySerializer<out Display> = SERIALIZER

    companion object {
        val SERIALIZER: DisplaySerializer<CarpetingDisplay> = displaySerializer(::CarpetingDisplay)
    }
}

class RecoloringDisplay(
    inputs: List<EntryIngredient>,
    outputs: List<EntryIngredient>,
    location: Optional<Identifier> = Optional.empty()
) : BasicDisplay(inputs, outputs, location) {
    constructor(inputs: List<EntryIngredient>, outputs: List<EntryIngredient>) : this(inputs, outputs, Optional.empty())

    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.RECOLORING

    override fun getSerializer(): DisplaySerializer<out Display> = SERIALIZER

    companion object {
        val SERIALIZER: DisplaySerializer<RecoloringDisplay> = displaySerializer(::RecoloringDisplay)
    }
}

class RemovalDisplay(
    inputs: List<EntryIngredient>,
    outputs: List<EntryIngredient>,
    location: Optional<Identifier> = Optional.empty()
) : BasicDisplay(inputs, outputs, location) {
    constructor(inputs: List<EntryIngredient>, outputs: List<EntryIngredient>) : this(inputs, outputs, Optional.empty())

    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.REMOVAL

    override fun getSerializer(): DisplaySerializer<out Display> = SERIALIZER

    companion object {
        val SERIALIZER: DisplaySerializer<RemovalDisplay> = displaySerializer(::RemovalDisplay)
    }
}

private fun <T : BasicDisplay> displaySerializer(
    constructor: (List<EntryIngredient>, List<EntryIngredient>, Optional<Identifier>) -> T
): DisplaySerializer<T> {
    return DisplaySerializer.of(
        RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BasicDisplay::getInputEntries),
                EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BasicDisplay::getOutputEntries),
                Identifier.CODEC.optionalFieldOf("location").forGetter(BasicDisplay::getDisplayLocation)
            ).apply(instance, constructor)
        },
        StreamCodec.composite(
            EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
            BasicDisplay::getInputEntries,
            EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
            BasicDisplay::getOutputEntries,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC),
            BasicDisplay::getDisplayLocation,
            constructor
        )
    )
}
