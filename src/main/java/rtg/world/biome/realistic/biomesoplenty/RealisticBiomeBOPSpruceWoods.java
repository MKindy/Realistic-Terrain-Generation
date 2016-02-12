package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import rtg.api.biome.BiomeConfig;
import rtg.api.biome.biomesoplenty.config.BiomeConfigBOPSpruceWoods;
import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.feature.WorldGenLog;
import rtg.world.gen.surface.biomesoplenty.SurfaceBOPSpruceWoods;
import rtg.world.gen.terrain.biomesoplenty.TerrainBOPSpruceWoods;
import biomesoplenty.api.content.BOPCBiomes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;

public class RealisticBiomeBOPSpruceWoods extends RealisticBiomeBOPBase
{	
	public static BiomeGenBase bopBiome = BOPCBiomes.spruceWoods;
	
	public static Block topBlock = bopBiome.topBlock;
	public static Block fillerBlock = bopBiome.fillerBlock;
	
	public RealisticBiomeBOPSpruceWoods(BiomeConfig config)
	{
		super(config, 
			bopBiome, BiomeGenBase.river,
			new TerrainBOPSpruceWoods(58f, 76f, 20f),
			new SurfaceBOPSpruceWoods(config, 
				    topBlock, //Block top 
	                (byte)0, //byte topByte
	                fillerBlock, //Block filler, 
	                (byte)0, //byte fillerByte
	                topBlock, //Block mixTop, 
	                (byte)0, //byte mixTopByte, 
	                fillerBlock, //Block mixFill, 
	                (byte)0, //byte mixFillByte,
	                80f, //float mixWidth, 
	                -0.15f, //float mixHeight, 
	                10f, //float smallWidth, 
	                0.5f //float smallStrength
	            )
		);
		
		this.config = config;
	}
	
    @Override
    public void rDecorate(World world, Random rand, int chunkX, int chunkY, OpenSimplexNoise simplex, CellNoise cell, float strength, float river)
    {
        
        /**
         * Using rDecorateSeedBiome() to partially decorate the biome? If so, then comment out this method.
         */
        //rOreGenSeedBiome(world, rand, chunkX, chunkY, simplex, cell, strength, river, baseBiome);

        float l = simplex.noise2(chunkX / 100f, chunkY / 100f) * 6f + 0.8f;
        
        for (int i23 = 0; i23 < 1; i23++)
        {
            int i1 = chunkX + rand.nextInt(16) + 8;
            int j1 = chunkY + rand.nextInt(16) + 8;
            int k1 = world.getHeightValue(i1, j1);
            
            if (rand.nextInt(16) == 0) {
                
                (new WorldGenBlockBlob(Blocks.cobblestone, 0)).generate(world, rand, i1, k1, j1);
            }
        }

        if (this.config.getPropertyById(BiomeConfigBOPSpruceWoods.decorationLogsId).valueBoolean) {
        
            if (rand.nextInt(24) == 0)
            {
                int x22 = chunkX + rand.nextInt(16) + 8;
                int z22 = chunkY + rand.nextInt(16) + 8;
                int y22 = world.getHeightValue(x22, z22);
                
                Block log;
                byte logMeta;
    
                if (rand.nextBoolean()) {
                    
                    log = Blocks.log;
                    logMeta = (byte)0;
                }
                else {
                    
                    log = Blocks.log;
                    logMeta = (byte)1;
                }
                
                (new WorldGenLog(log, logMeta, Blocks.leaves, -1, 3 + rand.nextInt(2))).generate(world, rand, x22, y22, z22);            
            }
        }
        
        rDecorateSeedBiome(world, rand, chunkX, chunkY, simplex, cell, strength, river, baseBiome);
    }
}
