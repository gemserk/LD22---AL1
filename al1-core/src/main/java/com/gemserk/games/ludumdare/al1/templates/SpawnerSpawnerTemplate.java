package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.componentsengine.utils.Interval;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.SpawnerComponent;
import com.gemserk.games.ludumdare.al1.scripts.EnemyParticleSpawnerScript;

public class SpawnerSpawnerTemplate extends EntityTemplateImpl {

	Injector injector;

	public static class SpawnerSpawnerScript extends ScriptJavaImpl {

		Injector injector;
		EntityFactory entityFactory;

		@Override
		public void update(World world, Entity e) {
			final SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
			spawnerComponent.timeToSpawn -= GlobalTime.getDelta();

			if (spawnerComponent.timeToSpawn > 0)
				return;

			// instantiate an entity from the store.

			entityFactory.instantiate(new EntityTemplateImpl() {
				@Override
				public void apply(Entity entity) {
					EnemyParticleSpawnerScript spawnerScript = new EnemyParticleSpawnerScript();
					injector.injectMembers(spawnerScript);
					entity.addComponent(new SpawnerComponent(spawnerComponent.entityTemplate, null, 0f));
					entity.addComponent(new ScriptComponent(spawnerScript));
				}
			});

			spawnerComponent.timeToSpawn = MathUtils.random( //
					spawnerComponent.spawnInterval.getMin(), //
					spawnerComponent.spawnInterval.getMax());

		}

	}

	@Override
	public void apply(Entity entity) {
		Interval interval = parameters.get("interval");
		Float timeToSpawn = parameters.get("timeToSpawn", new Float(0f));
		EntityTemplate entityTemplate = parameters.get("entityTemplate");

		// EntityTemplate entityTemplate = injector.getInstance(EnemyParticleSimpleTemplate.class);

		entity.addComponent(new SpawnerComponent(entityTemplate, interval, timeToSpawn.floatValue()));
		entity.addComponent(new ScriptComponent(injector.getInstance(SpawnerSpawnerScript.class)));
	}
}
