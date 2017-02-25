package rtg.api.world.deco.collection;

import java.util.ArrayList;

import rtg.api.world.deco.DecoBase;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

/**
 * @author WhichOnesPink
 */
public class DecoCollectionBase {

    public ArrayList<DecoBase> decos;
    public ArrayList<TreeRTG> rtgTrees;

    public DecoCollectionBase() {

        this.decos = new ArrayList<DecoBase>();
        this.rtgTrees = new ArrayList<TreeRTG>();
    }

    public DecoCollectionBase addDeco(DecoBase deco) {

        if (!deco.properlyDefined()) {
            throw new RuntimeException();
        }
        this.decos.add(deco);
        return this;
    }

    public DecoCollectionBase addDeco(DecoBase deco, boolean allowed) {

        if (allowed) {
            if (!deco.properlyDefined()) {
                throw new RuntimeException();
            }
            this.decos.add(deco);
        }
        return this;
    }

    /**
     * Adds a tree to the list of RTG trees associated with this collection.
     * The 'allowed' parameter allows us to pass biome config booleans dynamically when configuring the trees in the collection.
     *
     * @param tree
     * @param allowed
     */
    public void addTree(TreeRTG tree, boolean allowed) {

        if (allowed) {

            this.rtgTrees.add(tree);
        }
    }

    /**
     * Convenience method for addTree() where 'allowed' is assumed to be true.
     *
     * @param tree
     */
    public void addTree(TreeRTG tree) {

        this.addTree(tree, true);
    }
}