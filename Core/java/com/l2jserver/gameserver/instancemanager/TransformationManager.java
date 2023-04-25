/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.instancemanager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.L2Transformation;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.scripts.transformations.*;

/**
 * @author KenM
 */
public class TransformationManager
{
	private static final Logger _log = Logger.getLogger(TransformationManager.class.getName());
	
	public static TransformationManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final Map<Integer, L2Transformation> _transformations = new HashMap<>();
	
	private TransformationManager()
	{
		registerTransformation(new Akamanah());
		registerTransformation(new Anakim());
		registerTransformation(new AquaElf());
		registerTransformation(new ArcherCaptain());
		registerTransformation(new AurabirdFalcon());
		registerTransformation(new AurabirdOwl());
		registerTransformation(new Benom());
		registerTransformation(new Buffalo());
		registerTransformation(new DarkElfMercenary());
		registerTransformation(new DemonPrince());
		registerTransformation(new DemonRace());
		registerTransformation(new DivineEnchanter());
		registerTransformation(new DivineHealer());
		registerTransformation(new DivineKnight());
		registerTransformation(new DivineRogue());
		registerTransformation(new DivineSummoner());
		registerTransformation(new DivineWarrior());
		registerTransformation(new DivineWizard());
		registerTransformation(new DollBlader());
		registerTransformation(new DoomWraith());
		registerTransformation(new DragonBomberNormal());
		registerTransformation(new DragonBomberStrong());
		registerTransformation(new DragonBomberWeak());
		registerTransformation(new DwarfGolem());
		registerTransformation(new DwarfMercenary());
		registerTransformation(new ElfMercenary());
		registerTransformation(new FlyingFinalForm());
		registerTransformation(new EpicQuestChild());
		registerTransformation(new EpicQuestFrog());
		registerTransformation(new EpicQuestNative());
		registerTransformation(new FortressCaptain());
		registerTransformation(new GolemGuardianNormal());
		registerTransformation(new GolemGuardianStrong());
		registerTransformation(new GolemGuardianWeak());
		registerTransformation(new Gordon());
		registerTransformation(new GrailApostleNormal());
		registerTransformation(new GrailApostleStrong());
		registerTransformation(new GrailApostleWeak());
		registerTransformation(new GrizzlyBear());
		registerTransformation(new GuardianStrider());
		registerTransformation(new GuardsoftheDawn());
		registerTransformation(new HeavyTow());
		registerTransformation(new Heretic());
		registerTransformation(new HumanMercenary());
		registerTransformation(new InfernoDrakeNormal());
		registerTransformation(new InfernoDrakeStrong());
		registerTransformation(new InfernoDrakeWeak());
		registerTransformation(new InquisitorBishop());
		registerTransformation(new InquisitorElvenElder());
		registerTransformation(new InquisitorShilienElder());
		registerTransformation(new JetBike());
		registerTransformation(new Kadomas());
		registerTransformation(new Kamael());
		registerTransformation(new KamaelGuardCaptain());
		registerTransformation(new KamaelMercenary());
		registerTransformation(new Kiyachi());
		registerTransformation(new KnightofDawn());
		registerTransformation(new LavaGolem());
		registerTransformation(new LightPurpleManedHorse());
		registerTransformation(new LilimKnightNormal());
		registerTransformation(new LilimKnightStrong());
		registerTransformation(new LilimKnightWeak());
		registerTransformation(new LureTow());
		registerTransformation(new MagicLeader());
		registerTransformation(new MyoRace());
		registerTransformation(new Native());
		registerTransformation(new OlMahum());
		registerTransformation(new OnyxBeast());
		registerTransformation(new OrcMercenary());
		registerTransformation(new Pig());
		registerTransformation(new Pixy());
		registerTransformation(new PumpkinGhost());
		registerTransformation(new Rabbit());
		registerTransformation(new Ranku());
		registerTransformation(new RoyalGuardCaptain());
		registerTransformation(new SaberToothTiger());
		registerTransformation(new Scarecrow());
		registerTransformation(new ScrollBlue());
		registerTransformation(new ScrollRed());
		registerTransformation(new SnowKung());
		registerTransformation(new SteamBeatle());
		registerTransformation(new SujinChild());
		registerTransformation(new TawnyManedLion());
		registerTransformation(new Teleporter());
		registerTransformation(new Teleporter2());
		registerTransformation(new Timitran());
		registerTransformation(new TinGolem());
		registerTransformation(new Tow());
		registerTransformation(new TrejuoChild());
		registerTransformation(new Treykan());
		registerTransformation(new Unicorniun());
		registerTransformation(new UnicornNormal());
		registerTransformation(new UnicornStrong());
		registerTransformation(new UnicornWeak());
		registerTransformation(new ValeMaster());
		registerTransformation(new VanguardDarkAvenger());
		registerTransformation(new VanguardPaladin());
		registerTransformation(new VanguardShilienKnight());
		registerTransformation(new VanguardTempleKnight());
		registerTransformation(new WingTow());
		registerTransformation(new Yeti());
		registerTransformation(new Yeti2());
		registerTransformation(new Zaken());
		registerTransformation(new Zariche());
		registerTransformation(new Zombie());
	}
	
	public void report()
	{
		_log.info("Transformation Manager: Loaded: " + _transformations.size() + " transformations.");
	}
	
	public boolean transformPlayer(int id, L2PcInstance player)
	{
		L2Transformation template = getTransformationById(id);
		if (template != null)
		{
			L2Transformation trans = template.createTransformationForPlayer(player);
			trans.start();
			return true;
		}
		return false;
	}
	
	public L2Transformation getTransformationById(int id)
	{
		return _transformations.get(id);
	}
	
	public L2Transformation registerTransformation(L2Transformation transformation)
	{
		return _transformations.put(transformation.getId(), transformation);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TransformationManager _instance = new TransformationManager();
	}
}
