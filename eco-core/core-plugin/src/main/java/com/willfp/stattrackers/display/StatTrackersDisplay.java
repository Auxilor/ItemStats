package com.willfp.stattrackers.display;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import com.willfp.eco.util.StringUtils;
import com.willfp.stattrackers.stats.Stat;
import com.willfp.stattrackers.stats.util.StatChecks;
import com.willfp.stattrackers.tracker.util.TrackerUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StatTrackersDisplay extends DisplayModule {
    /**
     * Create new stat trackers display.
     *
     * @param plugin Instance of StatTrackers.
     */
    public StatTrackersDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.HIGHEST);
    }

    @Override
    protected void display(@NotNull final ItemStack itemStack,
                           @NotNull final Object... args) {
        ItemMeta meta = itemStack.getItemMeta();

        assert meta != null;

        if (!displayItemMeta(meta)) {
            displayTrackerMeta(meta);
        }

        itemStack.setItemMeta(meta);
    }

    private void displayTrackerMeta(@NotNull final ItemMeta meta) {
        Stat stat = TrackerUtils.getTrackedStat(meta);

        if (stat == null) {
            return;
        }

        meta.setDisplayName(this.getPlugin().getLangYml().getString("tracker"));
        List<String> lore = new ArrayList<>();

        for (String s : this.getPlugin().getLangYml().getStrings("tracker-description")) {
            lore.add(Display.PREFIX + StringUtils.translate(s.replace("%stat%", stat.getColor() + stat.getDescription())));
        }

        meta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> itemLore = getLore(meta);

        lore.addAll(itemLore);

        meta.setLore(lore);
    }

    private boolean displayItemMeta(@NotNull final ItemMeta meta) {
        Stat stat = StatChecks.getActiveStat(meta);

        if (stat == null) {
            return false;
        }

        List<String> itemLore = getLore(meta);

        itemLore.add(Display.PREFIX + "§f" + stat.getColor() + stat.getDescription() + this.getPlugin().getLangYml().getString("delimiter") +
                StringUtils.internalToString(StatChecks.getStatOnItemMeta(meta, stat)));
        meta.setLore(itemLore);

        return true;
    }

    private List<String> getLore(@NotNull final ItemMeta meta) {
        List<String> itemLore = new ArrayList<>();

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        return itemLore;
    }
}
