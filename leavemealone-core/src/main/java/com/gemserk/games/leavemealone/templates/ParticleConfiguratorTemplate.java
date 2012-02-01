package com.gemserk.games.leavemealone.templates;

import com.artemis.Entity;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.games.leavemealone.components.Components;

public class ParticleConfiguratorTemplate extends EntityTemplateImpl {

	@Override
	public void apply(Entity entity) {
		Spatial spatial = parameters.get("spatial");
		Spatial entitySpatial = Components.getSpatialComponent(entity).getSpatial();
		entitySpatial.set(spatial);
	}

}
