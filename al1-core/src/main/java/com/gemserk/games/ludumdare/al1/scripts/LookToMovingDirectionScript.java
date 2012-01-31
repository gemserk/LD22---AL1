package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.games.ludumdare.al1.components.Components;

public class LookToMovingDirectionScript extends ScriptJavaImpl {
	
	@Override
	public void update(World world, Entity e) {
		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);

		Body body = physicsComponent.getBody();

		SpriteComponent spriteComponent = Components.getSpriteComponent(e);
		spriteComponent.setUpdateRotation(false);

		Sprite sprite = spriteComponent.getSprite();
		sprite.setRotation(body.getLinearVelocity().angle());
	}

}