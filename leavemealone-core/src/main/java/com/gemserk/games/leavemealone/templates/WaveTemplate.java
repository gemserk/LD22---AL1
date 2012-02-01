package com.gemserk.games.leavemealone.templates;

import java.util.ArrayList;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.leavemealone.EntityStores;
import com.gemserk.games.leavemealone.spawner.ElementSpawner;
import com.gemserk.games.leavemealone.spawner.SpawnElement;
import com.gemserk.games.leavemealone.spawner.Wave;
import com.gemserk.games.leavemealone.spawner.WaveSpawner;

public class WaveTemplate extends EntityTemplateImpl {

	Injector injector;

	public static class WaveScript extends ScriptJavaImpl {

		EntityFactory entityFactory;
		EntityStores entityStores;

		WaveSpawner waveSpawner;

		public WaveScript(Wave wave) {
			waveSpawner = new WaveSpawner(new ArrayList<SpawnElement>(wave.elements), new ElementSpawner() {
				@Override
				public void spawn(SpawnElement element) {
					// entityStores.get(element.template);
					entityFactory.instantiate(element.template, element.parameters);
				}
			});
		}

		@Override
		public void init(World world, Entity e) {
			super.init(world, e);
			// play spawner sound
		}

		@Override
		public void update(World world, Entity e) {
			if (waveSpawner.isDone()) {
				e.delete();
				return;
			}
			waveSpawner.update(GlobalTime.getDelta());
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
