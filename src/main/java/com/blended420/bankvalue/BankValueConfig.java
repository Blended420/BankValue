package com.blended420.bankvalue;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("bankValue")
public interface BankValueConfig extends Config
{
	@ConfigItem(
		keyName = "showPriceSuffix",
		name = "Show Suffix",
		description = "Adds a K/M/B suffix to Bank Value"
	)
	default boolean showPriceSuffix()
	{
		return true;
	}
	@ConfigItem(
			keyName = "showHaValue",
			name = "Show High Alchemy Value",
			description = "Adds Ha Value of bank to the overlay"
	)
	default boolean showHaValue()
	{
		return true;
	}
}
