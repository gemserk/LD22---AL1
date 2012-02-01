package com.gemserk.games.leavemealone.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.leavemealone.EntityStores;
import com.gemserk.games.leavemealone.components.Components;
import com.gemserk.games.leavemealone.components.WaveComponent;
import com.gemserk.games.leavemealone.spawner.ElementSpawner;
import com.gemserk.games.leavemealone.spawner.ElementsSpawner;
import com.gemserk.games.leavemealone.spawner.SpawnElement;
import com.gemserk.games.leavemealone.spawner.Wave;

public class WaveTemplate extends EntityTemplateImpl {

	Injector injector;

	public static class WaveScript extends ScriptJavaImpl {

		EntityFactory entityFactory;
		EntityStores entityStores;

		Injector injector;

		ElementSpawner elementSpawner;

		@Override
		public void init(World world, Entity e) {
			super.init(world, e);

			elementSpawner = new ElementSpawner() {
				@Override
				public void spawn(SpawnElement element) {
					EntityStore entityStore = entityStores.get(element.templateClass);
					Entity e = entityStore.get();
					EntityTemplate configuratorTemplate = injector.getInstance(element.configuratorTemplateClass);
					configuratorTemplate.setParameters(element.parameters);
					configuratorTemplate.apply(e);
				}
			};

		}

		@Override
		public void update(World world, Entity e) {
			WaveComponent waveComponent = Components.getWaveComponent(e);
			if (waveComponent.elementsSpawner.isDone()) {
				e.delete();
				return;
			}
			waveComponent.elementsSpawner.update(GlobalTime.getDelta(), elementSpawner);
		}

	}

	@Override
	public void apply(Entity entity) {
		Wave wave = parameters.get("wave");

		entity.addComponent(new ScriptComponent(injector.getInstance(WaveScript.class)));
		entity.addComponent(new WaveComponent(new ElementsSpawner(wave.elements)));

	}

}
