package net.runelite.client.plugins.maxhit;

import java.util.Map;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
import net.runelite.http.api.item.ItemStats;

public class MaxMeleeHit
{
	public static double calc(MaxMeleeHitOpts o)
	{
		EquipmentSet e = o.getEquipment();
		int strBonus = getStrBonus(o.getEquipment());
		double prayBonus = getPrayBonus(o.getPrayer());
		int styleBonus = getStyleBonus(o.getStyle());
		double potionBonus = getPotionBonus(o);
		double otherBonus = getOtherBonus(o);

		double effectiveStrength = MaxMeleeHit.effectiveStrength(o.getStrLevel(),
			potionBonus, prayBonus, otherBonus, styleBonus);
		double baseDamage = MaxMeleeHit.baseDamage(effectiveStrength, strBonus);

		if (e.includes(ItemID.KERIS))
		{
			if (o.isFightingKalphitesOrScarabites())
			{
				if (o.isWithKerisCriticalAttack())
				{
					return Math.floor(baseDamage) * 3;
				}
				else
				{
					return Math.floor(baseDamage + 1.25);
				}
			}
		}

		if (e.includes(ItemID.SARADOMIN_SWORD))
		{
			return Math.floor(baseDamage) + 15;
		}

		return Math.floor(baseDamage * getSpecialBonus(o));
	}

	private static int getStrBonus(EquipmentSet equipment)
	{
		int strBonus = 0;

		for (Map.Entry<EquipmentInventorySlot, ItemStats> entry : equipment.stats.entrySet())
		{
			if ((entry.getValue() != null))
			{
				strBonus += entry.getValue().getEquipment().getStr();
			}
		}

		return strBonus;
	}

	private static double getPrayBonus(Prayer prayer)
	{
		double prayerBonus = 1;

		if (prayer == Prayer.BURST_OF_STRENGTH)
		{
			prayerBonus = 1.05;
		}
		else if (prayer == Prayer.SUPERHUMAN_STRENGTH)
		{
			prayerBonus = 1.1;
		}
		else if (prayer == Prayer.ULTIMATE_STRENGTH)
		{
			prayerBonus = 1.15;
		}
		else if (prayer == Prayer.CHILVARY)
		{
			prayerBonus = 1.18;
		}
		else if (prayer == Prayer.PIETY)
		{
			prayerBonus = 1.23;
		}

		return prayerBonus;
	}

	private static int getStyleBonus(Style style)
	{
		// acc/defensive is 0
		int styleBonus = 0;

		if (style == Style.AGGRESSIVE)
		{
			styleBonus = 3;
		}
		else if (style == Style.CONTROLLED)
		{
			styleBonus = 1;
		}

		return styleBonus;
	}

	private static double getPotionBonus(MaxMeleeHitOpts o)
	{
		double potionBonus = 0;
		Potion potion = o.getPotion();

		if (potion == Potion.STRENGTH)
		{
			potionBonus = 3 + Math.floor(.1 * o.getStrLevel());
		}
		else if (potion == Potion.SUPER_STRENGTH)
		{
			potionBonus = 5 + Math.floor(.15 * o.getStrLevel());
		}
		else if (potion == Potion.ZAMORAK_BREW)
		{
			potionBonus = 2 + Math.floor(.12 * o.getStrLevel());
		}
		else if (potion == Potion.DRAGON_BATTLEAXE)
		{
			potionBonus = 10 + Math.floor(.25 * (Math.floor(Math.floor(.10 * o.getMageLevel()) +
				Math.floor(.1 * o.getRangeLevel()) + Math.floor(.1 * o.getDefLevel()) + Math.floor(.1 * o.getAttLevel()))));
		}

		return potionBonus;
	}

	private static double getOtherBonus(MaxMeleeHitOpts o)
	{
		double otherBonus = 1;
		EquipmentSet equipment = o.getEquipment();

		if (equipment.includesMeleeVoidSet())
		{
			otherBonus += .1;
		}

		// slayer helm doesnt stack w/ salve, but salve does with void
		if (o.isFightingUndead() && (equipment.includes(ItemID.SALVE_AMULET) || equipment.includes(ItemID.SALVE_AMULETI)))
		{
			otherBonus += .15;
		}
		else if (o.isFightingUndead() && (equipment.includes(ItemID.SALVE_AMULET_E) || equipment.includes(ItemID.SALVE_AMULETEI)))
		{
			otherBonus += .2;
		}
		else if (o.isOnSlayerTask() && (equipment.includes(ItemID.BLACK_MASK) || equipment.includes(ItemID.BLACK_MASK_I) ||
			equipment.includes(ItemID.SLAYER_HELMET) || equipment.includes(ItemID.SLAYER_HELMET_I)))
		{
			otherBonus += (7.0 / 6.0);
		}

		return otherBonus;
	}

	private static double getSpecialBonus(MaxMeleeHitOpts o)
	{
		EquipmentSet e = o.getEquipment();
		double specialBonus = 1;

		if (e.includesDharokSet())
		{
			return 1 + (.1 * o.getHitpointsMissing());
		}
		else if (e.includesObsidianWeapon())
		{
			if (e.includesObsidianArmourSet())
			{
				specialBonus += .1;
			}

			if (e.includes(ItemID.BERSERKER_NECKLACE))
			{
				specialBonus += .2;
			}
		}
		else if (o.isWithSpecialAttack())
		{
			if (e.includes(ItemID.ARMADYL_GODSWORD))
			{
				specialBonus += .375;
			}
			else if (e.includes(ItemID.BANDOS_GODSWORD))
			{
				specialBonus += .21;
			}
			else if (e.includes(ItemID.SARADOMIN_GODSWORD) || e.includes(ItemID.ZAMORAK_GODSWORD))
			{
				specialBonus += .1;
			}
			else if (e.includesDragonDagger())
			{
				specialBonus += .15;
			}
			else if (e.includesCrystalHalberd())
			{
				specialBonus += .1;
			}
			else if (e.includes(ItemID.DRAGON_LONGSWORD))
			{
				specialBonus += .15;
			}
			else if (e.includes(ItemID.DRAGON_MACE))
			{
				specialBonus += .5;
			}
			else if (e.includes(ItemID.DRAGON_WARHAMMER) || e.includes(ItemID.DRAGON_WARHAMMER_20785))
			{
				specialBonus += .5;
			}
			else if (e.includes(ItemID.RUNE_CLAWS))
			{
				specialBonus += .1;
			}
			else if (e.includesAbyssalDagger())
			{
				specialBonus -= .15;
			}
			else if (e.includes(ItemID.ABYSSAL_BLUDGEON))
			{
				specialBonus += (.005 * o.getPrayerPointsMissing());
			}
			else if (e.includes(ItemID.SARADOMINS_BLESSED_SWORD))
			{
				specialBonus += .25;
			}
		}

		if (!e.includesObsidianArmourSet() && o.isInCastleWars() && e.includesCastleWarsBracelet())
		{
			specialBonus += .2;
		}

		return specialBonus;
	}

	private static double effectiveStrength(int strLvl, double potionBonus, double prayerBonus, double otherBonus, int styleBonus)
	{
		return Math.floor((strLvl + potionBonus) * prayerBonus * otherBonus) + styleBonus;
	}

	private static double baseDamage(double effectiveStrength, double strengthBonus)
	{
		return 1.3 + (effectiveStrength / 10) + (strengthBonus / 80) + ((effectiveStrength * strengthBonus) / 640);
	}
}
