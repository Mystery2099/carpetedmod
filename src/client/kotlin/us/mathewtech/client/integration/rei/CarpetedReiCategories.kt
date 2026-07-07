package us.mathewtech.client.integration.rei

import us.mathewtech.integration.rei.CarpetedReiCategoryIds
import us.mathewtech.integration.rei.CarpetingDisplay
import us.mathewtech.integration.rei.RecoloringDisplay
import us.mathewtech.integration.rei.RemovalDisplay
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.item.DyeColor
import us.mathewtech.util.CarpetColorUtil

class CarpetingCategory : DisplayCategory<CarpetingDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out CarpetingDisplay> = CarpetedReiCategoryIds.CARPETING

    override fun getIcon(): Renderer = EntryStacks.of(CarpetColorUtil.carpetItem(DyeColor.WHITE))

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.carpeting")

    override fun setupDisplay(display: CarpetingDisplay, bounds: Rectangle): List<Widget> {
        return buildTwoInputDisplay(bounds) { startPoint, widgets ->
            widgets.addSingleOutputRecipe(startPoint, display)
        }
    }

    override fun getDisplayHeight(): Int = 36
}

class RecoloringCategory : DisplayCategory<RecoloringDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out RecoloringDisplay> = CarpetedReiCategoryIds.RECOLORING

    override fun getIcon(): Renderer = EntryStacks.of(CarpetColorUtil.dyeItem(DyeColor.MAGENTA))

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.recoloring")

    override fun setupDisplay(display: RecoloringDisplay, bounds: Rectangle): List<Widget> {
        return buildTwoInputDisplay(bounds) { startPoint, widgets ->
            widgets.addSingleOutputRecipe(startPoint, display)
        }
    }

    override fun getDisplayHeight(): Int = 36
}

class RemovalCategory : DisplayCategory<RemovalDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out RemovalDisplay> = CarpetedReiCategoryIds.REMOVAL

    override fun getIcon(): Renderer = EntryStacks.of(Items.SHEARS)

    override fun getTitle(): Component = Component.translatable("category.carpeted-mod.removal")

    override fun setupDisplay(display: RemovalDisplay, bounds: Rectangle): List<Widget> {
        return buildTwoInputDisplay(bounds) { startPoint, widgets ->
            widgets.addInputsAndArrow(startPoint, display)
            widgets.addOutputSlot(startPoint, 72, display, 0)
            widgets.addOutputSlot(startPoint, 94, display, 1)
        }
    }

    override fun getDisplayHeight(): Int = 36
}

private fun buildTwoInputDisplay(bounds: Rectangle, content: (Point, MutableList<Widget>) -> Unit): List<Widget> {
    val startPoint = Point(bounds.getCenterX() - 67, bounds.getCenterY() - 13)
    return buildList {
        add(Widgets.createRecipeBase(bounds))
        content(startPoint, this)
    }
}

private fun MutableList<Widget>.addSingleOutputRecipe(startPoint: Point, display: BasicDisplay) {
    addInputsAndArrow(startPoint, display)
    addOutputSlot(startPoint, 72, display, 0)
}

private fun MutableList<Widget>.addInputsAndArrow(startPoint: Point, display: BasicDisplay) {
    add(Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries()[0]).markInput())
    add(Widgets.createSlot(Point(startPoint.x + 26, startPoint.y + 5)).entries(display.getInputEntries()[1]).markInput())
    add(Widgets.createArrow(Point(startPoint.x + 49, startPoint.y + 4)))
}

private fun MutableList<Widget>.addOutputSlot(startPoint: Point, xOffset: Int, display: BasicDisplay, outputIndex: Int) {
    val point = Point(startPoint.x + xOffset, startPoint.y + 5)
    add(Widgets.createResultSlotBackground(point))
    add(Widgets.createSlot(point).entries(display.getOutputEntries()[outputIndex]).disableBackground().markOutput())
}
