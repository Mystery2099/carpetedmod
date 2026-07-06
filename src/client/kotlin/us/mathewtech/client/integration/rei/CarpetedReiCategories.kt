package us.mathewtech.client.integration.rei

import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.item.DyeColor
import us.mathewtech.util.CarpetColorUtil

object CarpetedReiCategoryIds {
    val CARPETING: CategoryIdentifier<CarpetingDisplay> = CategoryIdentifier.of("carpeted-mod", "carpeting")
    val RECOLORING: CategoryIdentifier<RecoloringDisplay> = CategoryIdentifier.of("carpeted-mod", "recoloring")
    val REMOVAL: CategoryIdentifier<RemovalDisplay> = CategoryIdentifier.of("carpeted-mod", "removal")
}

class CarpetingCategory : DisplayCategory<CarpetingDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out CarpetingDisplay> = CarpetedReiCategoryIds.CARPETING

    override fun getIcon(): Renderer = EntryStacks.of(CarpetColorUtil.carpetItem(DyeColor.WHITE))

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.carpeting")

    override fun setupDisplay(display: CarpetingDisplay, bounds: Rectangle): List<Widget> {
        return buildActionDisplay(bounds) { startPoint, widgets ->
            widgets.add(Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5)).entries(display.baseBlock).markInput())
            widgets.add(Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4)))
            widgets.add(Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5)))
            widgets.add(Widgets.createSlot(Point(startPoint.x + 61, startPoint.y + 5)).entries(display.result).disableBackground().markOutput())
        }
    }

    override fun getDisplayHeight(): Int = 36
}

class RecoloringCategory : DisplayCategory<RecoloringDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out RecoloringDisplay> = CarpetedReiCategoryIds.RECOLORING

    override fun getIcon(): Renderer = EntryStacks.of(CarpetColorUtil.dyeItem(DyeColor.MAGENTA))

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.recoloring")

    override fun setupDisplay(display: RecoloringDisplay, bounds: Rectangle): List<Widget> {
        return buildActionDisplay(bounds) { startPoint, widgets ->
            widgets.add(Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5)).entries(display.source).markInput())
            widgets.add(Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4)))
            widgets.add(Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5)))
            widgets.add(Widgets.createSlot(Point(startPoint.x + 61, startPoint.y + 5)).entries(display.result).disableBackground().markOutput())
        }
    }

    override fun getDisplayHeight(): Int = 36
}

class RemovalCategory : DisplayCategory<RemovalDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out RemovalDisplay> = CarpetedReiCategoryIds.REMOVAL

    override fun getIcon(): Renderer = EntryStacks.of(Items.SHEARS)

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.removal")

    override fun setupDisplay(display: RemovalDisplay, bounds: Rectangle): List<Widget> {
        return buildActionDisplay(bounds) { startPoint, widgets ->
            widgets.add(Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5)).entries(display.source).markInput())
            widgets.add(Widgets.createArrow(Point(startPoint.x + 28, startPoint.y + 4)))
            widgets.add(Widgets.createResultSlotBackground(Point(startPoint.x + 62, startPoint.y + 5)))
            widgets.add(Widgets.createResultSlotBackground(Point(startPoint.x + 84, startPoint.y + 5)))
            widgets.add(Widgets.createSlot(Point(startPoint.x + 62, startPoint.y + 5)).entries(display.baseBlock).disableBackground().markOutput())
            widgets.add(Widgets.createSlot(Point(startPoint.x + 84, startPoint.y + 5)).entries(display.carpet).disableBackground().markOutput())
        }
    }

    override fun getDisplayHeight(): Int = 36
}

private fun buildActionDisplay(bounds: Rectangle, content: (Point, MutableList<Widget>) -> Unit): List<Widget> {
    val startPoint = Point(bounds.getCenterX() - 56, bounds.getCenterY() - 13)
    return buildList {
        add(Widgets.createRecipeBase(bounds))
        content(startPoint, this)
    }
}
