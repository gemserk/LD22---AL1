package com.gemserk.games.leavemealone.templates;

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

public class CameraTemplate extends EntityTemplateImpl {

	public static class CameraFollowEntityScript extends ScriptJavaImpl {

		String targetTag;
		float parallax;
		
		public CameraFollowEntityScript(String targetTag, float parallax) {
			this.targetTag = targetTag;
			this.parallax = parallax;
		}

		public void update(World world, Entity e) {
			Camera camera = Components.getCameraComponent(e).getCamera();
			Camera previousCamera = Components.getPreviousStateCameraComponent(e).getCamera();

			previousCamera.setPosition(camera.getX(), camera.getY());
			previousCamera.setZoom(camera.getZoom());

			Entity target = world.getTagManager().getEntity(targetTag);
			if (target == null)
				return;

			SpatialComponent spatialComponent = Components.getSpatialComponent(target);
			Vector2 position = spatialComponent.getPosition();

			camera.setPosition(position.x * parallax, position.y * parallax);
		}
	}

	@Override
	public void apply(Entity entity) {
		Libgdx2dCamera libgdx2dCamera = parameters.get("libgdx2dCamera");
		Camera camera = parameters.get("camera");
		String targetTag = parameters.get("targetTag");
		Float parallax = parameters.get("parallax", new Float(1f));

		entity.addComponent(new CameraComponent(libgdx2dCamera, camera));
		entity.addComponent(new PreviousStateCameraComponent(new CameraImpl(camera.getX(), camera.getY(), camera.getZoom(), camera.getAngle())));

		entity.addComponent(new ScriptComponent(new CameraFollowEntityScript(targetTag, parallax.floatValue())));
	}
}
