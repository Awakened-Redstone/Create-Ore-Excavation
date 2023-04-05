package com.tom.createores.emi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import com.simibubi.create.compat.emi.DoubleItemIcon;

import com.tom.createores.CreateOreExcavation;
import com.tom.createores.Registration;
import com.tom.createores.recipe.DrillingRecipe;
import com.tom.createores.recipe.ExtractorRecipe;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;

public class EMIPlugin implements EmiPlugin {
	public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();

	public static final EmiRecipeCategory
	DRILLING = register("drilling", DoubleItemIcon.of(Registration.DRILL_BLOCK.get(), Registration.NORMAL_DRILL_ITEM.get())),
	EXTRACTING = register("extractor", DoubleItemIcon.of(Registration.EXTRACTOR_BLOCK.get(), Items.BUCKET));

	@Override
	public void register(EmiRegistry registry) {
		ALL.forEach((id, category) -> {
			registry.addCategory(category);
		});

		registry.addWorkstation(DRILLING, EmiStack.of(Registration.DRILL_BLOCK.get()));
		registry.addWorkstation(EXTRACTING, EmiStack.of(Registration.EXTRACTOR_BLOCK.get()));

		consumeAllRecipes(r -> {
			if(r instanceof ExtractorRecipe e)registry.addRecipe(new ExtractingEmiRecipe(e));
			else if(r instanceof DrillingRecipe e)registry.addRecipe(new DrillingEmiRecipe(e));
		});
	}

	public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
		Minecraft.getInstance().level.getRecipeManager()
		.getRecipes()
		.forEach(consumer);
	}

	private static EmiRecipeCategory register(String name, EmiRenderable icon) {
		ResourceLocation id = new ResourceLocation(CreateOreExcavation.MODID, name);
		EmiRecipeCategory category = new EmiRecipeCategory(id, icon) {
			@Override
			public Component getName() {
				return Component.translatable("jei.coe.recipe." + name);
			}
		};
		ALL.put(id, category);
		return category;
	}
}
