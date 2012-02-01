package com.gemserk.games.leavemealone.templates;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.leavemealone.spawner.SpawnElement;
import com.gemserk.games.leavemealone.spawner.Wave;

public class WaveTemplate extends EntityTemplateImpl {

	Injector injector;

	public static class WaveScript extends ScriptJavaImpl {
		
		List<SpawnElement> elementsToSpawn;

		public WaveScript(Wave wave) {
			this.elementsToSpawn = new ArrayList<SpawnElement>(wave.elements);
		}

		@Override
		public void init(World world, Entity e) {
			super.init(world, e);
			
			// play spawner sound
		}
		
		@Override
		public void update(World world, Entity e) {
			
			if (elementsToSpawn.isEmpty()) {
				e.delete();
				return;
			}
			
			
		}

	}

	@Override
	public void apply(Entity entity) {
		Wave wave = parameters.get("wave");
		
		WaveScript waveScript = new WaveScript(wave);
		injector.injectMembers(waveScript);
		entity.addComponent(new ScriptComponent(waveScript));
	}

}
