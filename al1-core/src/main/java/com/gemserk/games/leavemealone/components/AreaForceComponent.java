package com.gemserk.games.leavemealone.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class AreaForceComponent extends Component {

	public final Vector2 force = new Vector2();

	public AreaForceComponent(float x, float y) {
		this.force.set(x, y);
	}

}
