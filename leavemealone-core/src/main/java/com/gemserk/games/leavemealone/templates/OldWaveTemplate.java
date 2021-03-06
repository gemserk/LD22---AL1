package com.gemserk.games.leavemealone.templates;

import com.artemis.Entity;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.leavemealone.scripts.WaveScript;

public class OldWaveTemplate extends EntityTemplateImpl {

	Injector injector;

	@Override
	public void apply(Entity entity) {
		EntityTemplate entityTemplate = parameters.get("entityTemplate");
		WaveScript spawnerScript = new WaveScript(entityTemplate, 3, 0.75f);
		injector.injectMembers(spawnerScript);
		entity.addComponent(new ScriptComponent(spawnerScript));
	}
	
}
