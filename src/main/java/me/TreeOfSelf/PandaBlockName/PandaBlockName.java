package me.TreeOfSelf.PandaBlockName;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PandaBlockName implements ModInitializer {
	public static BlockEntityType<EmptyBlockEntity> EMPTY_BLOCK_ENTITY_TYPE;
	public static final Logger LOGGER = LoggerFactory.getLogger("panda-block-name");

	@Override
	public void onInitialize() {
		EMPTY_BLOCK_ENTITY_TYPE = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				Identifier.fromNamespaceAndPath("panda-block-name", "emptyblock"),
				FabricBlockEntityTypeBuilder.create(EmptyBlockEntity::new).build());

		PolymerBlockUtils.registerBlockEntity(EMPTY_BLOCK_ENTITY_TYPE);
		PandaBlockNameConfig.loadConfig();
		LOGGER.info("PandaBlockName started!");
	}
}
