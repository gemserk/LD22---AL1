package com.gemserk.games.leavemealone.templates;

import java.util.ArrayList;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.utils.RandomUtils;
import com.gemserk.componentsengine.utils.Interval;
import com.gemserk.componentsengine.utils.Parameters;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.leavemealone.components.Components;
import com.gemserk.games.leavemealone.components.SpawnerComponent;
import com.gemserk.games.leavemealone.spawner.SpawnElement;
import com.gemserk.games.leavemealone.spawner.Wave;

public class WaveSpawnerTemplate extends EntityTemplateImpl {

	Injector injector;

	public static class WaveSpawnerScript extends ScriptJavaImpl {

		Injector injector;
		EntityFactory entityFactory;
		Rectangle worldBounds;

		Parameters parameters = new ParametersWrapper();

		ArrayList<Wave> waves = new ArrayList<Wave>();

		@Override
		public void init(World world, Entity e) {
			super.init(world, e);

			Class<? extends EntityTemplate> greenParticleTemplate = EnemyParticleSimpleTemplate.class;
			Class<? extends EntityTemplate> blueParticleTemplate = EnemyParticleTemplate.class;

			Class<? extends EntityTemplate> particleConfiguratorTemplate = ParticleConfiguratorTemplate.class;

			waves.add(new Wave( //
					new SpawnElement(0f, greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(worldBounds.x + 0.5f, 0f, 0.5f, 0.5f, 0f)) //
					), //
					new SpawnElement(1f, greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(worldBounds.x + worldBounds.getWidth() - 0.5f, 0f, 0.5f, 0.5f, 0f)) //
					), //
					new SpawnElement(2f, greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(worldBounds.x + 0.5f, 0f, 0.5f, 0.5f, 0f)) //
					), //
					new SpawnElement(3f, greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(worldBounds.x + worldBounds.getWidth() - 0.5f, 0f, 0.5f, 0.5f, 0f)) //
					) //
			));

			waves.add(new Wave( //
					new SpawnElement(1f, blueParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(0f, worldBounds.y + 0.5f, 0.5f, 0.5f, 0f)) //
					), //
					new SpawnElement(2f, blueParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
							.put("spatial", new SpatialImpl(0f, worldBounds.y + worldBounds.getHeight() - 0.5f, 0.5f, 0.5f, 0f)) //
					)//
			));

			Wave wave3 = new Wave();
			
			for (int i = 0; i < 5; i += 2) {
				wave3.elements.add(new SpawnElement(0.5f * i, greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
						.put("spatial", new SpatialImpl(worldBounds.x + 0.5f, worldBounds.y + 0.5f, 0.5f, 0.5f, 0f)) //
						));
				wave3.elements.add(new SpawnElement(0.5f * (i + 1), greenParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
						.put("spatial", new SpatialImpl(worldBounds.x + 0.5f, worldBounds.y + 0.5f, 0.5f, 0.5f, 0f)) //
						));
			}

			waves.add(wave3);
			
			Wave wave4 = new Wave();
			
			for (int i = 0; i < 5; i += 2) {
				wave4.elements.add(new SpawnElement(0.5f * i, blueParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
						.put("spatial", new SpatialImpl(worldBounds.x + worldBounds.getWidth() - 0.5f, worldBounds.y + 0.5f, 0.5f, 0.5f, 0f)) //
						));
				wave4.elements.add(new SpawnElement(0.5f * (i + 1), blueParticleTemplate, particleConfiguratorTemplate, new ParametersWrapper() //
						.put("spatial", new SpatialImpl(worldBounds.x + worldBounds.getWidth() - 0.5f, worldBounds.y + 0.5f, 0.5f, 0.5f, 0f)) //
						));
			}

			waves.add(wave4);

		}

		@Override
		public void update(World world, Entity e) {
			final SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
			spawnerComponent.timeToSpawn -= GlobalTime.getDelta();

			if (spawnerComponent.timeToSpawn > 0)
				return;

			// instantiate an entity from the store.

			Wave randomWave = RandomUtils.random(waves);

			entityFactory.instantiate(injector.getInstance(WaveTemplate.class), parameters //
					.put("wave", randomWave));

			// entityFactory.instantiate(injector.getInstance(OldWaveTemplate.class), parameters //
			// .put("entityTemplate", spawnerComponent.entityTemplate));

			// entityFactory.instantiate(new EntityTemplateImpl() {
			// @Override
			// public void apply(Entity entity) {
			// PeriodicEntitySpawnerScript spawnerScript = new PeriodicEntitySpawnerScript(spawnerComponent.entityTemplate);
			// injector.injectMembers(spawnerScript);
			// // entity.addComponent(new SpawnerComponent(spawnerComponent.entityTemplate, null, 0f));
			// entity.addComponent(new ScriptComponent(spawnerScript));
			// }
			// });

			spawnerComponent.timeToSpawn = MathUtils.random( //
					spawnerComponent.spawnInterval.getMin(), //
					spawnerComponent.spawnInterval.getMax());

		}
	}

	@Override
	public void apply(Entity entity) {
		Interval interval = parameters.get("interval");
		Float timeToSpawn = parameters.get("timeToSpawn", new Float(0f));

		// EntityTemplate entityTemplate = injector.getInstance(EnemyParticleSimpleTemplate.class);

		entity.addComponent(new SpawnerComponent(interval, timeToSpawn.floatValue()));
		entity.addComponent(new ScriptComponent(injector.getInstance(WaveSpawnerScript.class)));
	}
}
