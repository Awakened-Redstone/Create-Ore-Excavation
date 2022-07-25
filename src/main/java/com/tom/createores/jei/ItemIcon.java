package com.tom.createores.jei;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import mezz.jei.api.gui.drawable.IDrawable;

public class ItemIcon implements IDrawable {

	private Supplier<ItemStack> primarySupplier;
	private ItemStack primaryStack;

	public ItemIcon(Supplier<ItemStack> primary) {
		this.primarySupplier = primary;
	}

	@Override
	public int getWidth() {
		return 18;
	}

	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
		if (primaryStack == null) {
			primaryStack = primarySupplier.get();
		}

		RenderSystem.enableDepthTest();
		matrixStack.pushPose();
		matrixStack.translate(xOffset, yOffset, 0);

		matrixStack.pushPose();
		matrixStack.translate(1, 1, 0);
		GuiGameElement.of(primaryStack)
		.render(matrixStack);
		matrixStack.popPose();

		matrixStack.popPose();
	}
}
