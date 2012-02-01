package com.gemserk.games.leavemealone.components;

import com.artemis.Component;
import com.gemserk.commons.gdx.graphics.ConvexHull2d;

public class ConvexHullComponent extends Component {
	
	public ConvexHull2d convexHull2d;

	public ConvexHullComponent(ConvexHull2d convexHull2d) {
		this.convexHull2d = convexHull2d;
	}

}
