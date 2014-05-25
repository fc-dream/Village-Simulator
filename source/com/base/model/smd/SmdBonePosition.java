/* Copyright (c) 2010 "serser" http://code.google.com/u/103273266243665308417
 *
 * Distributed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
 * See accompanying file SMDIMP_LICENSE
 */

package com.base.model.smd;

import org.lwjgl.util.vector.Vector3f;

public class SmdBonePosition
{

	private int boneIndex;
	private Vector3f position;
	private Vector3f rotation;

	/**
	 * Method that stablish de position of the vertex.
	 * 
	 * @param x
	 *            float with x position
	 * @param y
	 *            float with y position
	 * @param z
	 *            float with z position
	 */
	public void setPosition(float x, float y, float z)
	{
		this.position = new Vector3f(x, y, z);
	}

	/**
	 * Method that returns de position vector.
	 * 
	 * @return Vector3f with the vector position.
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	public void setRotation(float x, float y, float z)
	{
		this.rotation = new Vector3f(x, y, z);
	}

	public Vector3f getRotation()
	{
		return rotation;
	}

	public void setBoneIndex(int boneIndex)
	{
		this.boneIndex = boneIndex;
	}

	public int getBoneIndex()
	{
		return boneIndex;
	}
}
