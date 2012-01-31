package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.utils.StoreFactory;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.SpawnerComponent;
import com.gemserk.games.ludumdare.al1.components.StoreComponent;
import com.gemserk.games.ludumdare.al1.triggers.PeriodicTriggerLogic;
import com.gemserk.games.ludumdare.al1.triggers.Trigger;

public class EnemyParticleSpawnerScript extends ScriptJavaImpl {

	EventManager eventManager;
	EntityFactory entityFactory;

	PeriodicTriggerLogic periodicTriggerLogic;

	@Override
	public void init(World world, final Entity e) {
		final SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
		spawnerComponent.store = new EntityStore(new StoreFactory<Entity>() {

			final ParametersWrapper parameters = new ParametersWrapper();

			@Override
			public Entity createObject() {
				EntityTemplate enemyParticleTemplate = spawnerComponent.entityTemplate;
				Entity entity = entityFactory.instantiate(enemyParticleTemplate, parameters//
						.put("spatial", new SpatialImpl(0f, 0f)));
				entity.addComponent(new StoreComponent(spawnerComponent.store));
				return entity;
			}
		});
		spawnerComponent.store.preCreate(5);

		periodicTriggerLogic = new PeriodicTriggerLogic(3, 0.5f, new Trigger() {
			@Override
			public void trigger() {
				spawnerComponent.store.get();
			}
		});
	}

	@Override
	public void update(World world, Entity e) {
		periodicTriggerLogic.update(GlobalTime.getDelta());
		// SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
		// spawnerComponent.timeToSpawn -= GlobalTime.getDelta();
		//
		// if (spawnerComponent.timeToSpawn > 0)
		// return;
		//
		// // instantiate an entity from the store.
		// spawnerComponent.store.get();
		//
		// spawnerComponent.timeToSpawn = MathUtils.random( //
		// spawnerComponent.spawnInterval.getMin(), //
		// spawnerComponent.spawnInterval.getMax());
	}

}
