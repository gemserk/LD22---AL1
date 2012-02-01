package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.scripts.PeriodicEntitySpawnerScript;

public class PeriodicEntitySpawnerTemplate extends EntityTemplateImpl {

	Injector injector;

	@Override
	public void apply(Entity entity) {
		EntityTemplate entityTemplate = parameters.get("entityTemplate");
		PeriodicEntitySpawnerScript spawnerScript = new PeriodicEntitySpawnerScript(entityTemplate, 3, 0.75f);
		injector.injectMembers(spawnerScript);
		entity.addComponent(new ScriptComponent(spawnerScript));
	}
	
}
