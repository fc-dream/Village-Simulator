/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

// To stop getting a headache over 3D stuff

public class GameStateMechanicPlayground extends GameState
{
	@Override
	protected GameState simpleStep(long delta)
	{
		System.out.println(delta);
		
		return null;
	}

	@Override
	protected void simpleSetup()
	{
	}

	@Override
	protected void simpleCleanup()
	{
	}
}