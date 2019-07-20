package net.runelite.client.plugins.maxhit;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;

public class EquipmentSet
{
	public Map<EquipmentInventorySlot, ItemStats> stats;
	private Map<EquipmentInventorySlot, ItemComposition> compositions;

	public EquipmentSet()
	{
		stats = new HashMap();
		compositions = new HashMap();
	}

	public EquipmentSet(ItemManager m, Map<EquipmentInventorySlot, Integer> equipment)
	{
		this();

		equipment.forEach((k, i) ->
		{
			ItemStats s = m.getItemStats(i.intValue(), false);
			ItemEquipmentStats es = s.getEquipment();


			if (es == null)
			{
				throw new Error("no equipment stats found for the item you provided.");
			}
			else if (es.getSlot() != k.getSlotIdx())
			{
				throw new Error("an item you gave was given in the wrong equipment slot.");
			}

			stats.put(k, s);
			compositions.put(k, m.getItemComposition(i.intValue()));
		});
	}

	public Integer getId(EquipmentInventorySlot t)
	{
		ItemComposition c = compositions.get(t);
		return c == null ? null : c.getId();
	}

	public ItemComposition getComposition(EquipmentInventorySlot t)
	{
		return compositions.get(t);
	}

	public ItemStats getStats(EquipmentInventorySlot t)
	{
		return stats.get(t);
	}

	public boolean includes(Integer equipmentID)
	{
		boolean ans = false;

		if (equipmentID == null)
		{
			return false;
		}

		for (Map.Entry<EquipmentInventorySlot, ItemComposition> entry : compositions.entrySet())
		{
			if ((entry.getValue() != null) && (entry.getValue().getId() == equipmentID.intValue()))
			{
				ans = true;
				break;
			}
		}


		return ans;
	}

	public boolean includesMeleeVoidSet()
	{
		return includes(ItemID.VOID_MELEE_HELM) &&
			includes(ItemID.VOID_KNIGHT_TOP) &&
			includes(ItemID.VOID_KNIGHT_ROBE) &&
			includes(ItemID.VOID_KNIGHT_GLOVES);
	}

	public boolean includesDharokSet()
	{

		return
			(
				includes(ItemID.DHAROKS_GREATAXE) ||
				includes(ItemID.DHAROKS_GREATAXE_25) ||
				includes(ItemID.DHAROKS_GREATAXE_50) ||
				includes(ItemID.DHAROKS_GREATAXE_75) ||
				includes(ItemID.DHAROKS_GREATAXE_100)
			) &&
			(
				includes(ItemID.DHAROKS_HELM) ||
				includes(ItemID.DHAROKS_HELM_25) ||
				includes(ItemID.DHAROKS_HELM_50) ||
				includes(ItemID.DHAROKS_HELM_75) ||
				includes(ItemID.DHAROKS_HELM_100)
			) &&
			(
				includes(ItemID.DHAROKS_PLATEBODY) ||
				includes(ItemID.DHAROKS_PLATEBODY_25) ||
				includes(ItemID.DHAROKS_PLATEBODY_50) ||
				includes(ItemID.DHAROKS_PLATEBODY_75) ||
				includes(ItemID.DHAROKS_PLATEBODY_100)
			) &&
			(
				includes(ItemID.DHAROKS_PLATELEGS) ||
				includes(ItemID.DHAROKS_PLATELEGS_25) ||
				includes(ItemID.DHAROKS_PLATELEGS_50) ||
				includes(ItemID.DHAROKS_PLATELEGS_75) ||
				includes(ItemID.DHAROKS_PLATELEGS_100)
			);
	}

	public boolean includesObsidianArmourSet()
	{
		return includes(ItemID.OBSIDIAN_HELMET) &&
			includes(ItemID.OBSIDIAN_PLATEBODY) &&
			includes(ItemID.OBSIDIAN_PLATELEGS);
	}

	public boolean includesObsidianWeapon()
	{
		return includes(ItemID.TOKTZXILAK) ||
			includes(ItemID.TZHAARKETOM) ||
			includes(ItemID.TZHAARKETEM) ||
			includes(ItemID.TOKTZXILEK);
	}

	public boolean includesDragonDagger()
	{
		return includes(ItemID.DRAGON_DAGGER) ||
			includes(ItemID.DRAGON_DAGGERP) ||
			includes(ItemID.DRAGON_DAGGER_20407) ||
			includes(ItemID.DRAGON_DAGGERP_5680) ||
			includes(ItemID.DRAGON_DAGGERP_5698);
	}

	public boolean includesCrystalHalberd()
	{
		return includes(ItemID.CRYSTAL_HALBERD_FULL) ||
			includes(ItemID.CRYSTAL_HALBERD_FULL_I) ||
			includes(ItemID.CRYSTAL_HALBERD_110) ||
			includes(ItemID.CRYSTAL_HALBERD_110_I) ||
			includes(ItemID.CRYSTAL_HALBERD_210) ||
			includes(ItemID.CRYSTAL_HALBERD_210_I) ||
			includes(ItemID.CRYSTAL_HALBERD_310) ||
			includes(ItemID.CRYSTAL_HALBERD_310_I) ||
			includes(ItemID.CRYSTAL_HALBERD_410) ||
			includes(ItemID.CRYSTAL_HALBERD_410_I) ||
			includes(ItemID.CRYSTAL_HALBERD_510) ||
			includes(ItemID.CRYSTAL_HALBERD_510_I) ||
			includes(ItemID.CRYSTAL_HALBERD_610) ||
			includes(ItemID.CRYSTAL_HALBERD_610_I) ||
			includes(ItemID.CRYSTAL_HALBERD_710) ||
			includes(ItemID.CRYSTAL_HALBERD_710_I) ||
			includes(ItemID.CRYSTAL_HALBERD_810) ||
			includes(ItemID.CRYSTAL_HALBERD_810_I) ||
			includes(ItemID.CRYSTAL_HALBERD_910) ||
			includes(ItemID.CRYSTAL_HALBERD_910_I) ||
			includes(ItemID.NEW_CRYSTAL_HALBERD_FULL) ||
			includes(ItemID.NEW_CRYSTAL_HALBERD_FULL_I);
	}

	public boolean includesAbyssalDagger()
	{
		return includes(ItemID.ABYSSAL_DAGGER) ||
			includes(ItemID.ABYSSAL_DAGGER_P) ||
			includes(ItemID.ABYSSAL_DAGGER_P_13269) ||
			includes(ItemID.ABYSSAL_DAGGER_P_13271);
	}

	public boolean includesCastleWarsBracelet()
	{
		return includes(ItemID.CASTLE_WARS_BRACELET1) ||
			includes(ItemID.CASTLE_WARS_BRACELET2) ||
			includes(ItemID.CASTLE_WARS_BRACELET3);
	}
}
