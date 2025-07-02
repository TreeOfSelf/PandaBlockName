package me.TreeOfSelf.PandaBlockName;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PandaBlockName implements ModInitializer {
	public static BlockEntityType<EmptyBlockEntity> EMPTY_BLOCK_ENTITY_TYPE;
    public static final Logger LOGGER = LoggerFactory.getLogger("panda-block-name");

	@Override
	public void onInitialize() {
		EMPTY_BLOCK_ENTITY_TYPE = Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				Identifier.of("panda-block-name", "emptyblock"),
				FabricBlockEntityTypeBuilder.create(EmptyBlockEntity::new).build(null));


		PolymerBlockUtils.registerBlockEntity(EMPTY_BLOCK_ENTITY_TYPE);
		PandaBlockNameConfig.loadConfig();
		LOGGER.info("PandaBlockName started!");
	}
}