package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.PreviousStateCameraComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.camera.Camera;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.games.ludumdare.al1.Tags;

public class CameraTemplate extends EntityTemplateImpl {

	@Override
	public void apply(Entity entity) {
		Libgdx2dCamera libgdx2dCamera = parameters.get("libgdx2dCamera");
		Camera camera = parameters.get("camera");

		entity.addComponent(new CameraComponent(libgdx2dCamera, camera));
		entity.addComponent(new PreviousStateCameraComponent(new CameraImpl(camera.getX(), camera.getY(), camera.getZoom(), camera.getAngle())));

		entity.addComponent(new ScriptComponent( //
				new ScriptJavaImpl() {
					public void update(World world, Entity e) {
						Camera camera = Components.getCameraComponent(e).getCamera();
						Camera previousCamera = Components.getPreviousStateCameraComponent(e).getCamera();

						previousCamera.setPosition(camera.getX(), camera.getY());
						previousCamera.setZoom(camera.getZoom());

						Entity target = world.getTagManager().getEntity(Tags.MainCharacter);
						if (target == null)
							return;

						SpatialComponent spatialComponent = Components.getSpatialComponent(target);
						Vector2 position = spatialComponent.getPosition();

						camera.setPosition(position.x, position.y);
					}
				}//
		));
	}
}
