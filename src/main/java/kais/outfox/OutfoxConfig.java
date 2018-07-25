/**
 * Copyright © 2018 Aiden Vaughn "ItsTheKais"
 *
 * This file is part of Outfox.
 *
 * The code of Outfox is free and available under the terms of the latest version of the GNU Lesser General
 * Public License. Outfox is distributed with no warranty, implied or otherwise. Outfox should have come with
 * a copy of the GNU Lesser General Public License; if not, see: <https://www.gnu.org/licenses/>
 */

package kais.outfox;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * style note: the individual lines of multiline comments should be no longer than 100 characters long;
 * including indentation & quotes, that's 115 characters total, 116 with the comma;
 * this makes the config file look a bit nicer because the comments won't be longer than the category headers;
 * never let anyone tell you the little things don't count!
 */
@Mod.EventBusSubscriber(modid = OutfoxResources.MODID)
@Config(modid = OutfoxResources.MODID, category = "")
public class OutfoxConfig {

    @Config.Comment("Fox spawn biome configuration")
    @Config.LangKey("outfox.config.category_biomes")
    public static final Biomes biomes = new Biomes();

    @Config.Comment("Miscellaneous config settings")
    @Config.LangKey("outfox.config.category_general")
    public static final General general = new General();

    @Config.Comment("Block searching AI configuration")
    @Config.LangKey("outfox.config.category_search")
    public static final Search search = new Search();

    public static class Biomes {

        @Config.Comment({
            "List of biomes that foxes should be common spawns in.",
            "This setting requires a Minecraft restart if changed from the in-game config menu!",
            "Default:",
            "  minecraft:roofed_forest",
            "  minecraft:mutated_roofed_forest"
        })
        @Config.LangKey("outfox.config.common_biomes")
        @Config.RequiresMcRestart
        public String[] common_biomes = OutfoxResources.DEFAULT_COMMON_BIOMES;

        @Config.Comment({
            "List of biome dictionary types that foxes should be common spawns in.",
            "This is for modpacks with large numbers of biomes to lazily add foxes to many biomes at once.",
            "For a list of valid biome types, check the Forge biome dictionary source at:",
            "https://github.com/MinecraftForge/MinecraftForge/blob/1.12.x/src/main/java/net/minecraftforge/common/BiomeDictionary.java", // except for this line :/
            "This setting requires a Minecraft restart if changed from the in-game config menu!",
            "Default: (empty)"
        })
        @Config.LangKey("outfox.config.common_types")
        @Config.RequiresMcRestart
        public String[] common_types = {};

        @Config.Comment({
            "List of biomes that foxes should be rare spawns in.",
            "This setting requires a Minecraft restart if changed from the in-game config menu!",
            "Default:",
            "  minecraft:forest",
            "  minecraft:forest_hills",
            "  minecraft:birch_forest",
            "  minecraft:birch_forest_hills",
            "  minecraft:mutated_forest",
            "  minecraft:mutated_birch_forest",
            "  minecraft:mutated_birch_forest_hills"
        })
        @Config.LangKey("outfox.config.rare_biomes")
        @Config.RequiresMcRestart
        public String[] rare_biomes = OutfoxResources.DEFAULT_RARE_BIOMES;

        @Config.Comment({
            "List of biome dictionary types that foxes should be rare spawns in.",
            "This is for modpacks with large numbers of biomes to lazily add foxes to many biomes at once.",
            "This setting requires a Minecraft restart if changed from the in-game config menu!",
            "Default: (empty)"
        })
        @Config.LangKey("outfox.config.rare_types")
        @Config.RequiresMcRestart
        public String[] rare_types = {};
    }

    public static class General {

        @Config.Comment({
            "Averts workplace accidents in tight spaces.",
            "If the second half of an item's ID name (e.g. minecraft:iron_pickaxe) contains one of these keys,",
            "you will be unable to attack your own foxes with that item. Please submit a bug report if you find",
            "a stone- or dirt-mining tool that is able to attack your foxes so it can be added to the defaults!",
            "Default:",
            "  pick",
            "  shovel",
            "  hammer",
            "  excavator",
            "  mattock",
            "  paxel",
            "  drill",
            "  disassembler",
            "  destructionwand"
        })
        @Config.LangKey("outfox.config.immune_tools")
        public String[] immune_tools = OutfoxResources.DEFAULT_IMMUNE_TOOLS;
    }

    public static class Search {

        @Config.Comment("Is block searching enabled?")
        @Config.LangKey("outfox.config.search_enabled")
        public boolean search_enabled = true;

        @Config.Comment({
            "The frequency, in ticks, with which the block search AI scans the area.",
            "Lower values will cause foxes to find blocks more quickly, but will probably impact game",
            "performance.",
            "Default: 20 (1 second)"
        })
        @Config.LangKey("outfox.config.search_frequency")
        @Config.RangeInt(min = 1)
        public int search_frequency = 20;

        @Config.Comment({"A list of block IDs (e.g. minecraft:diamond_ore) that foxes will not be allowed to",
            "search for.",
            "Default: (empty)"})
        @Config.LangKey("outfox.config.search_list")
        public String[] search_list = { };

        @Config.Comment({"Whether 'Search Blacklist' should be used as a whitelist instead. If true, foxes will",
            "only be allowed to search for blocks specified on that list."})
        @Config.LangKey("outfox.config.search_listmode")
        public boolean search_listmode = false;

        @Config.Comment({
            "The probability that any block search will fail to detect a block even if there is one in range.",
            "0 means searches will never fail, 99 means a 1% chance of success.",
            "Default: 0"
        })
        @Config.LangKey("outfox.config.search_odds")
        @Config.RangeInt(min = 0, max = 99)
        public int search_odds = 0;

        @Config.Comment("Should particles be used to convey block search status?")
        @Config.LangKey("outfox.config.particles_enabled")
        public boolean search_particles = true;

        @Config.Comment({
            "The distance, in blocks, that foxes should search away from themselves.",
            "The resulting search area will be a cube, 2n + 1 blocks to a side, centered around the block space",
            "the fox is in. At higher values, this also influences the pathfinding length of foxes' path-",
            "-navigators. Being greedy with this value will probably rapidly impact game performance!",
            "Default: 10 (searches 21^3 blocks)"
        })
        @Config.LangKey("outfox.config.search_range")
        @Config.RangeInt(min = 1)
        public int search_range = 10;

        @Config.Comment({
            "The number of waypoints that the search AI pathfinder should use.",
            "This setting is slightly experimental and the effects of changing it are not guaranteed to be",
            "visible (or even to exist at all). Higher values may make foxes smarter in situations with multiple",
            "paths that all lead somewhat close to the target block, but may also impact performance. Foxes may",
            "become appallingly stupid while searching if this is set too low; most (all?) vanilla pathfinders",
            "use 32 waypoints. If you're not sure, probably leave this alone.",
            "This setting requires a save & quit if changed from the in-game config menu!",
            "Default: 96",
        })
        @Config.LangKey("outfox.config.search_waypoints")
        @Config.RangeInt(min = 4)
        @Config.RequiresWorldRestart
        public int search_waypoints = 96;

        @Config.Comment({
            "The block state properties to be matched when searching for a block.",
            "The entries in this list allow foxes to tell the difference between distinct blocks that share the",
            "same block ID (e.g. different colors of stained glass which are all minecraft:stained_glass with tag",
            "'color', or andesite et al. which are all minecraft:stone with tag 'variant'). If you run into a",
            "problem where foxes track multiple unrelated blocks when searching for one, you can fix it by adding",
            "the relevant block state property (hint: F3) to this list... and also submit a bug report so it can",
            "be added to the defaults for future releases!",
            "Note: be careful of adding tags like 'facing' or 'orientation' as these will cause idiocy such as",
            "foxes only being able to track blocks that use those tags if you click the block on the fox while",
            "facing a certain direction. Which might make for a neat puzzle in a challenge map, but would get",
            "quite annoying in regular survival!",
            "Default:",
            "  color",
            "  colour",
            "  type",
            "  variant",
            "  compression_level_",
            "  decorstates",
            "  foliage",
            "  shade"
        })
        @Config.LangKey("outfox.config.state_matches")
        public String[] state_matches = OutfoxResources.DEFAULT_STATE_MATCHES;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e) {

        if (e.getModID().equals(OutfoxResources.MODID)) { sync(); }
    }

    public static void sync() {

        ConfigManager.sync(OutfoxResources.MODID, Config.Type.INSTANCE);
    }
}