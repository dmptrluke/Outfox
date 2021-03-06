/**
 * Copyright � 2018 Aiden Vaughn "ItsTheKais"
 *
 * This file is part of Outfox.
 *
 * The code of Outfox is free and available under the terms of the latest version of the GNU Lesser General
 * Public License. Outfox is distributed with no warranty, implied or otherwise. Outfox should have come with
 * a copy of the GNU Lesser General Public License; if not, see: <https://www.gnu.org/licenses/>
 */

package kais.outfox.fox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import kais.outfox.OutfoxConfig;
import kais.outfox.OutfoxResources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAISearchForBlock extends EntityAIBase {

    private final EntityFox fox;
    private final double speed;
    private int frequency;
    private int range;
    private int odds;
    private int searchesSinceBlockFound; // -1 if no block found; counts up from 0 to 7 when a block is found
    private int[] target; // x-y-z coordinates of found block

    /**
     * searches around the fox for the block specified at [this.fox.searchedBlock];
     * will not fire if the fox is wild, sitting, or does not have a block set;
     * searches occur every [this.frequency] ticks;
     * begins when a block is found less than [this.range] blocks away;
     * sets a new path each time a search finds a block;
     * uses [this.fox.searchNavigator] as its PathNavigator;
     * stops if [this.fox.searchedBlock] is not found within [this.range] again in eight searches;
     */
    public EntityAISearchForBlock(EntityFox foxIn, double speedIn) {

        this.fox = foxIn;
        this.speed = speedIn;
        this.searchesSinceBlockFound = -1;
        this.target = new int[3];
        this.updateStats();
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {

        return OutfoxConfig.search.search_enabled
            && this.fox.isTamed()
            && !this.fox.isSitting()
            && this.fox.searchedBlock != null
            && OutfoxResources.checkBlockIdIsBlacklisted(this.fox.searchedBlock.getRegistryName().toString())
            && ((!this.isBlockFound() && this.fox.ticksExisted % this.frequency == 0 && this.doBlockSearch())
                || this.isBlockFound());
    }

    public boolean shouldContinueExecuting() {

        return OutfoxConfig.search.search_enabled
            && !this.fox.isSitting()
            && this.fox.searchedBlock != null
            && OutfoxResources.checkBlockIdIsBlacklisted(this.fox.searchedBlock.getRegistryName().toString())
            && this.isBlockFound();
    }

    public void startExecuting() {

        this.doPath();
    }

    public void updateTask() {

        if (this.fox.ticksExisted % this.frequency == 0) {

            boolean b = isBlockFound();
            this.updateStats();

            if (this.searchesSinceBlockFound < 8) { ++this.searchesSinceBlockFound; }
            else { this.reset(); }

            if (this.fox.world.getBlockState(new BlockPos(target[0], target[1], target[2])).getBlock() == this.fox.searchedBlock) {

                b = true;
                this.reset();
                this.searchesSinceBlockFound = 0;
            }
            else { this.reset(); }

            b = b || this.doBlockSearch();
            if (b && this.fox.onGround) { this.doPath(); }
        }
    }

    public boolean isBlockFound() {

        return this.searchesSinceBlockFound != -1;
    }

    protected void reset() {

        this.searchesSinceBlockFound = -1;
        this.fox.searchNavigator.clearPath();
        this.fox.getNavigator().clearPath();
    }

    protected void updateStats() {

        this.frequency = Math.max(1, OutfoxConfig.search.search_frequency);
        this.range = Math.max(1, OutfoxConfig.search.search_range);
        this.odds = Math.max(0, Math.min(100, OutfoxConfig.search.search_odds));
        this.fox.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.max(56.0D, this.range));
    }

    private void doPath() {

        this.fox.searchNavigator.tryMoveToXYZ(target[0] + 0.5D, target[1] + 1.0D, target[2] + 0.5D, this.speed);
        this.fox.world.setEntityState(this.fox, (byte)9); // signal the fox to emit a feedback particle
    }

    private boolean doBlockSearch() {

        if (this.odds > 0 && this.fox.getRNG().nextInt(99) < this.odds) { return false; }

        double d = Double.MAX_VALUE;
        BlockPos p = this.fox.getPosition();

        for (BlockPos.MutableBlockPos i : BlockPos.getAllInBoxMutable(
            new BlockPos(p.getX() - this.range, p.getY() - this.range, p.getZ() - this.range),
            new BlockPos(p.getX() + this.range, p.getY() + this.range, p.getZ() + this.range))) {

            IBlockState state = this.fox.world.getBlockState(i);
            if (state.getBlock() == this.fox.searchedBlock && checkTagMatches(state)) {

                double d1 = this.fox.getDistanceSq(i);
                if (d1 < d) {

                    this.searchesSinceBlockFound = 0;
                    this.target[0] = i.getX();
                    this.target[1] = i.getY();
                    this.target[2] = i.getZ();
                    d = d1;
                }
            }
        }
        return d < Double.MAX_VALUE;
    }

    private boolean checkTagMatches(IBlockState state) {

        Iterator<Entry<String, String>> i = this.fox.stateTags.entrySet().iterator();
        HashMap<String, String> properties = OutfoxResources.stateStringToHashMap(state);

        while (i.hasNext()) {

            Map.Entry<String, String> e = i.next();
            if (e.getKey() == null || e.getValue() == null) { continue; }
            if (properties.containsKey(e.getKey()) && !properties.get(e.getKey()).equals(e.getValue())) { return false; }
        }
        return true;
    }
}