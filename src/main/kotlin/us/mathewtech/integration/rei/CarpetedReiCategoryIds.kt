package us.mathewtech.integration.rei

import me.shedaniel.rei.api.common.category.CategoryIdentifier

object CarpetedReiCategoryIds {
    val CARPETING: CategoryIdentifier<CarpetingDisplay> = CategoryIdentifier.of("carpeted-mod", "carpeting")
    val RECOLORING: CategoryIdentifier<RecoloringDisplay> = CategoryIdentifier.of("carpeted-mod", "recoloring")
    val REMOVAL: CategoryIdentifier<RemovalDisplay> = CategoryIdentifier.of("carpeted-mod", "removal")
}
