package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.util.noise.OpenSimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.CliffCalculator;
import rtg.world.biome.deco.*;
import rtg.world.gen.feature.tree.rtg.TreeRTG;
import rtg.world.gen.feature.tree.rtg.TreeRTGBetulaPapyrifera;
import rtg.world.gen.surface.SurfaceBase;
import rtg.world.gen.terrain.TerrainBase;

public class RealisticBiomeVanillaBirchForestM extends RealisticBiomeVanillaBase {

    public static Biome biome = Biomes.MUTATED_BIRCH_FOREST;
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeVanillaBirchForestM() {

        super(biome, river);

        this.noLakes = true;
    }

    @Override
    public void initConfig() {

        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);

        this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK).set("");
        this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK_META).set(0);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaBirchForestM();
    }

    public class TerrainVanillaBirchForestM extends TerrainBase {

        public TerrainVanillaBirchForestM() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainPlains(x, y, rtgWorld.simplex, river, 160f, 10f, 60f, 80f, 65f);
        }
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaBirchForestM(config, biome.topBlock, biome.fillerBlock, 0f, 1.5f, 60f, 65f, 1.5f, Blocks.DIRT.getDefaultState(), 0.15f);
    }

    public class SurfaceVanillaBirchForestM extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        private IBlockState mixBlock;
        private float mixHeight;

        public SurfaceVanillaBirchForestM(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff,
                                          float stoneHeight, float stoneStrength, float clayCliff, IBlockState mix, float mixSize) {

            super(config, top, fill);
            min = minCliff;

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;

            mixBlock = this.getConfigBlock(config.SURFACE_MIX_BLOCK.get(), config.SURFACE_MIX_BLOCK_META.get(), mix);
            mixHeight = mixSize;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand;
            OpenSimplexNoise simplex = rtgWorld.simplex;
            float c = CliffCalculator.calc(x, z, noise);
            int cliff = 0;
            boolean m = false;

            Block b;
            for (int k = 255; k > -1; k--) {
                b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (depth == 0) {

                        float p = simplex.noise3(i / 8f, j / 8f, k / 8f) * 0.5f;
                        if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                            cliff = 1;
                        }
                        if (c > cCliff) {
                            cliff = 2;
                        }

                        if (cliff == 1) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble(rtgWorld, i, j, x, z, k));
                            }
                            else {

                                primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                            }
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock(rtgWorld, i, j, x, z, k));
                        }
                        else if (k < 63) {
                            if (k < 62) {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, topBlock);
                            }
                        }
                        else if (simplex.noise2(i / 12f, j / 12f) > mixHeight) {
                            primer.setBlockState(x, k, z, mixBlock);
                            m = true;
                        }
                        else {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                    }
                    else if (depth < 6) {
                        if (cliff == 1) {
                            primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock(rtgWorld, i, j, x, z, k));
                        }
                        else {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initDecos() {

        TreeRTG tallBirch = new TreeRTGBetulaPapyrifera();
        tallBirch.logBlock = BlockUtil.getStateLog(2);
        tallBirch.leavesBlock = BlockUtil.getStateLeaf(2);
        tallBirch.minTrunkSize = 16;
        tallBirch.maxTrunkSize = 23;
        tallBirch.minCrownSize = 4;
        tallBirch.maxCrownSize = 11;
        this.addTree(tallBirch);

        DecoTree superTallBirch = new DecoTree(tallBirch);
        superTallBirch.strengthFactorForLoops = 16f;
        superTallBirch.strengthNoiseFactorForLoops = true;
        superTallBirch.treeType = DecoTree.TreeType.RTG_TREE;
        superTallBirch.distribution.noiseDivisor = 80f;
        superTallBirch.distribution.noiseFactor = 60f;
        superTallBirch.distribution.noiseAddend = -15f;
        superTallBirch.treeCondition = DecoTree.TreeCondition.ALWAYS_GENERATE;
        superTallBirch.maxY = 100;
        this.addDeco(superTallBirch);

        DecoLargeFernDoubleTallgrass decoDoublePlants = new DecoLargeFernDoubleTallgrass();
        decoDoublePlants.maxY = 128;
        decoDoublePlants.strengthFactor = 8f;
        this.addDeco(decoDoublePlants);

        DecoGrass decoGrass = new DecoGrass();
        decoGrass.maxY = 128;
        decoGrass.strengthFactor = 24f;
        this.addDeco(decoGrass);

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.logCondition = DecoFallenTree.LogCondition.RANDOM_CHANCE;
        decoFallenTree.logConditionChance = 20;
        decoFallenTree.logBlock = BlockUtil.getStateLog(2);
        decoFallenTree.leavesBlock = BlockUtil.getStateLeaf(2);
        decoFallenTree.minSize = 3;
        decoFallenTree.maxSize = 6;
        this.addDeco(decoFallenTree, this.getConfig().ALLOW_LOGS.get());

        DecoShrub decoShrub = new DecoShrub();
        decoShrub.maxY = 110;
        decoShrub.strengthFactor = 2f;
        this.addDeco(decoShrub);
    }
}
