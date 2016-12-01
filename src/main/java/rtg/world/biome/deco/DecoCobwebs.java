package rtg.world.biome.deco;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;

import rtg.api.util.RandomUtil;
import rtg.api.world.RTGWorld;
import rtg.world.biome.realistic.RealisticBiomeBase;
import rtg.world.gen.feature.WorldGenBlock;

/**
 * @author WhichOnesPink
 */
public class DecoCobwebs extends DecoBase {

    public float strengthFactor; // Higher = more/bigger boulders.
    public int minY; // Lower height restriction.
    public int maxY; // Upper height restriction.
    public int chance; // Higher = more rare.
    public IBlockState adjacentBlock;
    public int minAdjacents;

    public DecoCobwebs() {

        super();

        /**
         * Default values.
         * These can be overridden when configuring the Deco object in the realistic biome.
         */
        this.strengthFactor = 2f;
        this.minY = 1; // No lower height limit by default.
        this.maxY = 255; // No upper height limit by default.
        this.chance = 10;
        this.adjacentBlock = Blocks.AIR.getDefaultState();
        this.minAdjacents = 1;

        this.addDecoTypes(DecoType.COBWEB);
    }

    @Override
    public void generate(RealisticBiomeBase biome, RTGWorld rtgWorld, Random rand, int worldX, int worldZ, float strength, float river, boolean hasPlacedVillageBlocks) {

        if (this.allowed) {

            WorldGenerator worldGenerator = new WorldGenBlock(Blocks.WEB.getDefaultState(), Blocks.AIR.getDefaultState(), this.adjacentBlock, this.minAdjacents);

            for (int l1 = 0; l1 < this.strengthFactor * strength; ++l1) {
                int i1 = worldX + rand.nextInt(16);// + 8;
                int j1 = worldZ + rand.nextInt(16);// + 8;
                int k1 = RandomUtil.getRandomInt(rand, this.minY, this.maxY);

                if (rand.nextInt(this.chance) == 0) {
                    worldGenerator.generate(rtgWorld.world, rand, new BlockPos(i1, k1, j1));
                }
            }
        }
    }
}
