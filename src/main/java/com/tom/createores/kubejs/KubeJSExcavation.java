package com.tom.createores.kubejs;

import com.tom.createores.CreateOreExcavation;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

public class KubeJSExcavation extends KubeJSPlugin {

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(CreateOreExcavation.DRILLING_RECIPES.getSerializer().getRegistryName(), DrillingRecipeJS::new);
		event.register(CreateOreExcavation.EXTRACTING_RECIPES.getSerializer().getRegistryName(), ExtractorRecipeJS::new);
		CreateOreExcavation.LOGGER.info("Loaded KubeJS integration");
	}
}
