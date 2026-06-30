package us.mathewtech.block

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

enum class CarpetSurface(private val serializedName: String) : StringRepresentable {
    TOP("top"),
    BOTTOM("bottom"),
    NORTH("north"),
    EAST("east"),
    SOUTH("south"),
    WEST("west"),
    TREAD("tread");

    override fun getSerializedName(): String {
        return serializedName
    }

    companion object {
        val CODEC: Codec<CarpetSurface> = StringRepresentable.fromEnum(CarpetSurface::values)
    }
}
