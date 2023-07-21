package com.tom.createores.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import com.mojang.datafixers.util.Pair;

import com.tom.createores.Config;
import com.tom.createores.OreDataCapability;
import com.tom.createores.OreDataCapability.OreData;
import com.tom.createores.OreVeinGenerator;
import com.tom.createores.recipe.VeinRecipe;
import com.tom.createores.util.ComponentJoiner;
import com.tom.createores.util.RandomSpreadGenerator;

public class OreVeinFinderItem extends Item {

	public OreVeinFinderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		if(!level.isClientSide)
			detect(level, player.blockPosition(), player);

		return InteractionResultHolder.success(player.getItemInHand(interactionHand));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		if(!ctx.getLevel().isClientSide)
			detect(ctx.getLevel(), ctx.getClickedPos(), ctx.getPlayer());

		return InteractionResult.SUCCESS;
	}

	private void detect(Level level, BlockPos pos, Player player) {
		ChunkPos center = new ChunkPos(pos);
		OreData found = null;
		List<OreData> nearby = new ArrayList<>();
		int near = Config.veinFinderNear;
		for(int x = -near;x <= near;x++) {
			for(int z = -near;z <= near;z++) {
				OreData d = OreDataCapability.getData(level.getChunk(center.x + x, center.z + z));
				if(x == 0 && z == 0)found = d;
				else nearby.add(d);
			}
		}
		player.displayClientMessage(Component.translatable("chat.coe.veinFinder.info"), false);
		player.displayClientMessage(Component.translatable("chat.coe.veinFinder.pos", center.x, center.z), false);
		RecipeManager m = level.getRecipeManager();
		Component f;
		Component nothing = Component.translatable("chat.coe.veinFinder.nothing");
		Component comma = Component.literal(", ");
		if(found != null && found.getRecipe(m) != null)f = found.getRecipe(m).getName();
		else f = nothing;
		player.displayClientMessage(Component.translatable("chat.coe.veinFinder.found", f), false);

		f = nearby.stream().map(d -> d.getRecipe(m)).filter(r -> r != null).map(r -> r.getName()).collect(ComponentJoiner.joining(nothing, comma));
		player.displayClientMessage(Component.translatable("chat.coe.veinFinder.nearby", f), false);

		Pair<BlockPos, VeinRecipe> nearest = OreVeinGenerator.getPicker((ServerLevel) level).locate(pos, (ServerLevel) level, 16);
		if(nearest != null) {
			BlockPos at = nearest.getFirst();
			int i = Math.round(RandomSpreadGenerator.distance2d(at, pos) / Config.veinFinderFar) * Config.veinFinderFar;
			player.displayClientMessage(Component.translatable("chat.coe.veinFinder.far", Component.translatable("chat.coe.veinFinder.distance", nearest.getSecond().getName(), i)), false);
		} else {
			player.displayClientMessage(Component.translatable("chat.coe.veinFinder.far", nothing), false);
		}
		player.getCooldowns().addCooldown(this, Config.veinFinderCd);
	}
}
