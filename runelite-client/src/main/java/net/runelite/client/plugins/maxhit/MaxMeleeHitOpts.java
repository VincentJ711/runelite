package net.runelite.client.plugins.maxhit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaxMeleeHitOpts
{
	@Builder.Default
	private int strLevel = 1;

	@Builder.Default
	private int mageLevel = 1;

	@Builder.Default
	private int rangeLevel = 1;

	@Builder.Default
	private int defLevel = 1;

	@Builder.Default
	private int attLevel = 1;

	@Builder.Default
	private Potion potion = Potion.NONE;

	@Builder.Default
	private Prayer prayer = Prayer.NONE;

	@Builder.Default
	private Style style = Style.AGGRESSIVE;

	@Builder.Default
	private EquipmentSet equipment = new EquipmentSet();

	private boolean withSpecialAttack;
	private boolean onSlayerTask;
	private boolean fightingUndead;
	private boolean fightingKalphitesOrScarabites;
	private boolean withKerisCriticalAttack;
	private boolean inCastleWars;
	private int prayerPointsMissing = 0;
	private int hitpointsMissing;
}
