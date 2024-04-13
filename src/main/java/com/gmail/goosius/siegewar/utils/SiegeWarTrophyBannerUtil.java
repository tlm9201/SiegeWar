package com.gmail.goosius.siegewar.utils;

import com.gmail.goosius.siegewar.enums.SiegeStatus;
import com.gmail.goosius.siegewar.objects.Siege;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.persistence.PersistentDataType;

public class SiegeWarTrophyBannerUtil {

    public void setBannerPersistentData(Block block, Siege siege) {
        SiegeStatus siegeStatus = siege.getStatus();
        BlockState blockState = block.getState();

        //Ensure siege has ended, block is a banner, and a key has not already been applied to the block
        if (!siegeStatus.isActive() && blockState instanceof Banner) {

            Banner banner = (Banner) blockState;
            banner.getPersistentDataContainer().set(null, PersistentDataType.STRING, );
        }



    }
}
