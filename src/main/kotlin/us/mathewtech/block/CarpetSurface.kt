package us.mathewtech.block

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
}
