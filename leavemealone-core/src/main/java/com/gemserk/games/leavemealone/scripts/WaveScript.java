package com.gemserk.games.leavemealone.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.games.leavemealone.EntityStores;
import com.gemserk.games.leavemealone.triggers.PeriodicTriggerLogic;
import com.gemserk.games.leavemealone.triggers.Trigger;

public class WaveScript extends ScriptJavaImpl {

	EntityStores entityStores;

	PeriodicTriggerLogic periodicTriggerLogic;
	
	private final EntityTemplate entityTemplate;
	private final int count;
	private final float interval;
	
	public WaveScript(EntityTemplate entityTemplate, int count, float interval) {
		this.entityTemplate = entityTemplate;
		this.count = count;
		this.interval = interval;
	}

	@Override
	public void init(World world, final Entity e) {
		periodicTriggerLogic = new PeriodicTriggerLogic(count, interval, new Trigger() {
			@Override
			public void trigger() {
				EntityStore store = entityStores.get(entityTemplate.getClass());
				store.get();
			}
		});
	}

	@Override
	public void update(World world, Entity e) {
		periodicTriggerLogic.update(GlobalTime.getDelta());
		if (periodicTriggerLogic.isFinished())
			e.delete();
	}

}
