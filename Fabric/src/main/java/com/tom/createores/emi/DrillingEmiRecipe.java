package com.tom.createores.emi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.ponder.ui.LayoutHelper;

import com.tom.createores.CreateOreExcavation;
import com.tom.createores.Registration;
import com.tom.createores.recipe.DrillingRecipe;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

public class DrillingEmiRecipe extends ExcavatingEmiRecipe<DrillingRecipe> {

	public DrillingEmiRecipe(DrillingRecipe recipe) {
		super(EMIPlugin.DRILLING, recipe);
		ResourceLocation rid = recipe.getId();
		this.id = new ResourceLocation("emi", CreateOreExcavation.MODID + "/drilling/" + rid.getNamespace() + "/" + rid.getPath());
		if(recipe.drillingFluid != FluidIngredient.EMPTY)
			input.add(fluidStack(recipe.drillingFluid.getMatchingFluidStacks().get(0)));
		output = recipe.output.stream().map(ProcessingOutput::getStack).map(EmiStack::of).toList();
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		super.addWidgets(widgets);

		if(recipe.drillingFluid != FluidIngredient.EMPTY)
			addSlot(widgets, input.get(2), 29 + 18, 6);

		int xOffset = 134 / 2;
		int yOffset = 86;
		layoutOutput(recipe).forEach(layoutEntry -> {
			addSlot(widgets, EmiStack.of(layoutEntry.output.getStack()).setChance(layoutEntry.output.getChance()), (xOffset) + layoutEntry.posX() + 1, yOffset + layoutEntry.posY() + 1).recipeContext(this);
		});
	}

	private List<LayoutEntry> layoutOutput(DrillingRecipe recipe) {
		int size = recipe.getOutput().size();
		List<LayoutEntry> positions = new ArrayList<>(size);

		LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
		for (ProcessingOutput result : recipe.getOutput()) {
			positions.add(new LayoutEntry(result, layout.getX(), layout.getY()));
			layout.next();
		}

		return positions;
	}

	private record LayoutEntry(
			ProcessingOutput output,
			int posX,
			int posY
			) {}

	@Override
	protected BlockState getBlock() {
		return Registration.DRILL_BLOCK.getDefaultState();
	}
}
