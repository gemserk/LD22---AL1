package com.gemserk.games.leavemealone.spawner;

import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.componentsengine.utils.Parameters;

public class SpawnElement {

	public float time;
	public Parameters parameters;

	// public EntityTemplate template;
	// public EntityTemplate configuratorTemplate;

	public Class<? extends EntityTemplate> templateClass;
	public Class<? extends EntityTemplate> configuratorTemplateClass;

	public SpawnElement(float time, Class<? extends EntityTemplate> templateClass, Class<? extends EntityTemplate> configuratorTemplateClass, Parameters parameters) {
		this.time = time;
		this.templateClass = templateClass;
		this.configuratorTemplateClass = configuratorTemplateClass;

		// this.template = template;
		// this.configuratorTemplate = configuratorTemplate;

		this.parameters = parameters;
	}

}
