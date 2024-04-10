package com.gmail.goosius.siegewar.listeners;

import com.gmail.goosius.siegewar.events.BattleSessionEndedEvent;
import com.gmail.goosius.siegewar.events.BattleSessionPreStartEvent;
import com.gmail.goosius.siegewar.events.BattleSessionStartedEvent;
import com.gmail.goosius.siegewar.events.SiegeCampStartEvent;
import com.gmail.goosius.siegewar.events.SiegeWarStartEvent;
import com.gmail.goosius.siegewar.events.SiegeEndEvent;
import com.gmail.goosius.siegewar.events.SiegeRemoveEvent;
import com.gmail.goosius.siegewar.objects.Siege;
import com.gmail.goosius.siegewar.utils.DiscordWebhook;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.goosius.siegewar.SiegeController;
import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SiegeWarSelfListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onBattleSessionPreStart(BattleSessionPreStartEvent event) {
		if (SiegeWarSettings.cancelBattleSessionWhenNoActiveSieges()
		&& (SiegeController.getSieges().isEmpty() || SiegeController.getSieges().stream().noneMatch(siege -> siege.getStatus().isActive()))) {
			event.setCancelled(true);
			event.setCancellationMsg(Translatable.of("battle_session_cancelled_no_sieges").defaultLocale());
		}
	}

	@EventHandler
	public void onBattleSessionStarted(BattleSessionStartedEvent event) {
		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSessionStartNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(SiegeWarSettings.getFallbackColor(), event.getMessage());
	}

	@EventHandler
	public void onBattleSessionEnded(BattleSessionEndedEvent event) {
		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSessionEndNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(SiegeWarSettings.getFallbackColor(), event.getMessage());
	}

	@EventHandler
	public void onSiegeCampStart(SiegeCampStartEvent event) {
		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSiegeCampStartNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(Color.decode("#" + event.getNation().getMapColorHexCode()), event.getMessage());
	}

	@EventHandler
	public void onSiegeWarStart(SiegeWarStartEvent event) {
		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSiegeStartNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(Color.decode("#" + event.getNation().getMapColorHexCode()), event.getMessage());
	}

	@EventHandler
	public void onSiegeEnd(SiegeEndEvent event) {
		Siege siege = event.getSiege();

		//experimental
		Block flagBlock = siege.getFlagBlock();
		ItemStack fancyFlag = new ItemStack(Material.WHITE_BANNER, 1);
		ItemMeta fancyFlagMeta = fancyFlag.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("Attacker: " + siege.getAttackerName() + " Defender: " + siege.getDefenderName())

        fancyFlagMeta.setLore(lore);
		fancyFlagMeta.setDisplayName("test trophy banner");
		flagBlock.getDrops().clear(); //clear vanilla banner drops
		flagBlock.getDrops().add(fancyFlag);

		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSiegeEndNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(Color.decode("#" + event.getNation().getMapColorHexCode()), siege.getEndMessage(), siege.getStatus().isActive());
	}

	@EventHandler
	public void onSiegeRemove(SiegeRemoveEvent event) {
		if (!SiegeWarSettings.isDiscordWebhookEnabled() || !SiegeWarSettings.isSiegeRemoveNotificationEnabled())
			return;

		DiscordWebhook.sendWebhookNotification(Color.decode("#" + event.getNation().getMapColorHexCode()), event.getMessage());
	}
}
