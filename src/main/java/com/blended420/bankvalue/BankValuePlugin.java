package com.blended420.bankvalue;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.io.IOException;

@Slf4j
@PluginDescriptor(
		name = "Bank Value Overlay",
		description = "Adds an overlay showing your overall bank value"
)
public class BankValuePlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	ClientThread clientThread;
	@Inject
	private ItemManager itemManager;
	@Inject
	private BankValueConfig config;
	@Inject
	private ChatCommandManager commandManager;
	@Inject
	private BankValueOverlay overlay;
	@Inject
	private OverlayManager overlayManager;
	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged e){
		if (e.getItemContainer() == client.getItemContainer(InventoryID.BANK)) {
			final Item[] items = client.getItemContainer(InventoryID.BANK).getItems();
			updateTotalBankValue(items);
		}
	}

	private void updateTotalBankValue(Item[] items){
		final BankedItems prices = calc(items);
		overlay.updateInventoryValue( prices.getGeValue(), prices.getHaValue());
	}

	BankedItems calc(Item[] items) {
		long geTotal = 0;
		long haTotal = 0;
		for (Item item : client.getItemContainer(InventoryID.BANK).getItems()) {
			final boolean isPlaceholder = itemManager.getItemComposition(item.getId()).getPlaceholderTemplateId() != -1;

			if (item.getId() != ItemID.BANK_FILLER || !isPlaceholder) {
				String name = itemManager.getItemComposition(item.getId()).getName();
				final int qty = item.getQuantity();
				geTotal += itemManager.getItemPrice(item.getId()) * qty;
				haTotal += getHaValue(item.getId()) * qty;
			}
		}
		return new BankedItems(geTotal,haTotal);
	}

	private int getHaValue(int itemId)
	{
		switch (itemId)
		{
			case ItemID.COINS_995:
				return 1;
			case ItemID.PLATINUM_TOKEN:
				return 1000;
			default:
				return itemManager.getItemComposition(itemId).getHaPrice();
		}
	}





	@Provides
	BankValueConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankValueConfig.class);
	}


	}
