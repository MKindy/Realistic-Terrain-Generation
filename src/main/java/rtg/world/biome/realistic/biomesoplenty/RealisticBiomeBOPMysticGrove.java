package rtg.world.biome.realistic.biomesoplenty;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;

import rtg.api.biome.BiomeConfig;
import rtg.api.biome.biomesoplenty.config.BiomeConfigBOPMysticGrove;
import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.biome.deco.DecoFallenTree;
import rtg.world.gen.surface.biomesoplenty.SurfaceBOPMysticGrove;
import rtg.world.gen.terrain.TerrainBase;

public class RealisticBiomeBOPMysticGrove extends RealisticBiomeBOPBase {

    public static Biome biome = BOPBiomes.mystic_grove.get();
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeBOPMysticGrove(BiomeConfig config) {

        super(config, biome, river,
            new rtg.world.gen.terrain.biomesoplenty.TerrainBOPMysticGrove(),
            new SurfaceBOPMysticGrove(config, biome.topBlock, biome.fillerBlock)
        );

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.distribution.noiseDivisor = 80f;
        decoFallenTree.distribution.noiseFactor = 60f;
        decoFallenTree.distribution.noiseAddend = -15f;
        decoFallenTree.logCondition = DecoFallenTree.LogCondition.RANDOM_CHANCE;
        decoFallenTree.logConditionChance = 12;
        decoFallenTree.randomLogBlocks = new IBlockState[]{Blocks.LOG.getDefaultState(), BOPBlocks.log_3.getStateFromMeta(4), BOPBlocks.log_1.getStateFromMeta(5)};
        decoFallenTree.minSize = 3;
        decoFallenTree.maxSize = 5;
        this.addDeco(decoFallenTree, this.config._boolean(BiomeConfigBOPMysticGrove.decorationLogsId));

        DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
        this.addDeco(decoBaseBiomeDecorations);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPMysticGrove();
    }

    public class TerrainBOPMysticGrove extends TerrainBase {

        public TerrainBOPMysticGrove() {

        }

        @Override
        public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river) {

            return terrainPlains(x, y, simplex, river, 160f, 10f, 60f, 200f, 66f);
        }
    }
}
