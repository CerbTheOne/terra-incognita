package azmalent.terraincognita.common.world;

import azmalent.terraincognita.TIConfig;
import azmalent.terraincognita.common.block.plants.SmallLilypadBlock;
import azmalent.terraincognita.common.init.ModBlocks;
import azmalent.terraincognita.common.init.ModBlocks.PottablePlantEntry;
import azmalent.terraincognita.common.init.ModFeatures;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ModVegetation {
    public static ConfiguredFeature<?, ?> FORGET_ME_NOT;
    public static ConfiguredFeature<?, ?> EDELWEISS;
    public static ConfiguredFeature<?, ?> MARIGOLD;
    public static ConfiguredFeature<?, ?> IRIS;
    public static ConfiguredFeature<?, ?> FIREWEED;
    public static ConfiguredFeature<?, ?> WITHER_ROSE;
    public static ConfiguredFeature<?, ?> LOTUS;
    public static ConfiguredFeature<?, ?> SMALL_LILYPADS;
    public static ConfiguredFeature<?, ?> REEDS;

    public static void addVegetation(BiomeLoadingEvent event, ConfiguredFeature feature) {
        if (feature != null) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, feature);
        }
    }

    private static BlockClusterFeatureConfig createConfig(PottablePlantEntry plant, int tries) {
        return createConfig(plant.getBlock().getDefaultState(), tries);
    }

    private static BlockClusterFeatureConfig createConfig(BlockState blockState, int tries) {
        SimpleBlockStateProvider provider = new SimpleBlockStateProvider(blockState);
        return createConfig(provider, tries);
    }

    private static BlockClusterFeatureConfig createConfig(BlockStateProvider provider, int tries) {
        return new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.PLACER).tries(tries).build();
    }

    private static ConfiguredFeature<?, ?> initFlowerFeature(PottablePlantEntry flower, int tries) {
        return initFlowerFeature(flower.getBlock().getDefaultState(), tries);
    }

    private static ConfiguredFeature<?, ?> initFlowerFeature(BlockState blockState, int tries) {
        SimpleBlockStateProvider provider = new SimpleBlockStateProvider(blockState);
        return initFlowerFeature(provider, tries);
    }

    private static ConfiguredFeature<?, ?> initFlowerFeature(BlockStateProvider provider, int tries) {
        BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.PLACER)
                .tries(tries).build();

        return initFlowerFeature(config);
    }

    private static ConfiguredFeature<?, ?> initFlowerFeature(BlockClusterFeatureConfig config) {
        return Feature.FLOWER.withConfiguration(config)
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(4);
    }

    private static ConfiguredFeature<?, ?> initLilypadFeature(BlockStateProvider provider, int tries) {
        BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.PLACER)
                .tries(tries).whitelist(Sets.newHashSet(Blocks.WATER)).build();

        return Feature.RANDOM_PATCH.withConfiguration(config).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(4);
    }

    public static void configureFeatures() {
        if (TIConfig.Flora.forgetMeNot.get())   FORGET_ME_NOT = initFlowerFeature(ModBlocks.FORGET_ME_NOT, 32);
        if (TIConfig.Flora.fireweed.get())      FIREWEED = initFlowerFeature(ModBlocks.FIREWEED, 48);

        if (TIConfig.Flora.edelweiss.get()) {
            BlockClusterFeatureConfig config = createConfig(ModBlocks.EDELWEISS, 48);
            EDELWEISS = ModFeatures.EDELWEISS.get().withConfiguration(config)
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(4);
        }

        if (TIConfig.Flora.marigold.get()) {
            MARIGOLD = initFlowerFeature(ModBlocks.MARIGOLD, 32);
        }

        if (TIConfig.Flora.iris.get()) {
            WeightedBlockStateProvider provider = new WeightedBlockStateProvider();
            provider.addWeightedBlockstate(ModBlocks.BLUE_IRIS.getBlock().getDefaultState(), 1);
            provider.addWeightedBlockstate(ModBlocks.PURPLE_IRIS.getBlock().getDefaultState(), 1);

            IRIS = initFlowerFeature(provider, 64);
        }

        if (TIConfig.Flora.smallLilypad.get()) {
            WeightedBlockStateProvider provider = new WeightedBlockStateProvider();
            for (int i = 0; i < 4; i++) {
                BlockState blockState = ModBlocks.SMALL_LILYPAD.getBlock().getDefaultState().with(SmallLilypadBlock.LILYPADS, i + 1);
                provider.addWeightedBlockstate(blockState, 1);
            }

            SMALL_LILYPADS = initLilypadFeature(provider, 10);
        }

        if (TIConfig.Flora.lotus.get()) {
            WeightedBlockStateProvider provider = new WeightedBlockStateProvider();
            provider.addWeightedBlockstate(Blocks.LILY_PAD.getDefaultState(), 3);
            provider.addWeightedBlockstate(ModBlocks.PINK_LOTUS.getBlock().getDefaultState(), 1);
            provider.addWeightedBlockstate(ModBlocks.WHITE_LOTUS.getBlock().getDefaultState(), 1);
            provider.addWeightedBlockstate(ModBlocks.YELLOW_LOTUS.getBlock().getDefaultState(), 1);

            LOTUS = initLilypadFeature(provider, 10);
        }

        if (TIConfig.Flora.reeds.get()) {
            REEDS = ModFeatures.REEDS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242732_c(4);
        }

        if (TIConfig.Tweaks.witherRoseGeneration.get()) {
            WITHER_ROSE = initFlowerFeature(Blocks.WITHER_ROSE.getDefaultState(), 64);
        }
    }
}
