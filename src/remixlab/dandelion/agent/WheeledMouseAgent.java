/*********************************************************************************
 * dandelion_tree
 * Copyright (c) 2014 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 *********************************************************************************/

package remixlab.dandelion.agent;

import java.util.Iterator;
import java.util.Map.Entry;

import remixlab.bias.branch.profile.*;
import remixlab.bias.event.*;
import remixlab.bias.event.shortcut.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.*;

public class WheeledMouseAgent extends MotionAgent<DOF2Action> {
	public static int					LEFT_ID	= 1, CENTER_ID = 2, RIGHT_ID = 3, WHEEL_ID = 4;

	protected float						xSens		= 1f;
	protected float						ySens		= 1f;

	public WheeledMouseAgent(AbstractScene scn, String n) {
		super(scn, n);
		// todo pending
		// this.setWheelSensitivity(20.0f);
	}

	@Override
	public float[] sensitivities(MotionEvent event) {
		if (event instanceof DOF2Event)
			return new float[] { xSens, ySens, 1f, 1f, 1f, 1f };
		else
			return super.sensitivities(event);
	}

	/**
	 * Defines the {@link #xSensitivity()}.
	 */
	public void setXSensitivity(float sensitivity) {
		xSens = sensitivity;
	}

	/**
	 * Returns the x sensitivity.
	 * <p>
	 * Default value is 1. A higher value will make the event more efficient (usually meaning a faster motion). Use a
	 * negative value to invert the along x-Axis motion direction.
	 * 
	 * @see #setWheelSensitivity(float)
	 */
	public float xSensitivity() {
		return xSens;
	}

	/**
	 * Defines the {@link #ySensitivity()}.
	 */
	public void setYSensitivity(float sensitivity) {
		ySens = sensitivity;
	}

	/**
	 * Returns the y sensitivity.
	 * <p>
	 * Default value is 1. A higher value will make the event more efficient (usually meaning a faster motion). Use a
	 * negative value to invert the along y-Axis motion direction.
	 * 
	 * @see #setWheelSensitivity(float)
	 */
	public float ySensitivity() {
		return ySens;
	}

	@Override
	public DOF2Event feed() {
		return null;
	}

	// high-level API

	/**
	 * Set mouse bindings as 'arcball'. Bindings are as follows:
	 * <p>
	 * 1. <b>InteractiveFrame bindings</b><br>
	 * Left button -> ROTATE<br>
	 * Center button -> SCALE<br>
	 * Right button -> TRANSLATE<br>
	 * Shift + Center button -> SCREEN_TRANSLATE<br>
	 * Shift + Right button -> SCREEN_ROTATE<br>
	 * <p>
	 * 2. <b>InteractiveFrame bindings</b><br>
	 * Left button -> ROTATE<br>
	 * Center button -> ZOOM<br>
	 * Right button -> TRANSLATE<br>
	 * Shift + Left button -> ZOOM_ON_REGION<br>
	 * Shift + Center button -> SCREEN_TRANSLATE<br>
	 * Shift + Right button -> SCREEN_ROTATE.
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * 
	 * @see #dragToFirstPerson()
	 * @see #dragToThirdPerson()
	 */
	public void dragToArcball() {
		removeFrameBindings();
		removeEyeBindings();
		eyeProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, DOF2Action.ROTATE);
		eyeProfile()
				.setBinding(buttonModifiersFix(CENTER_ID), CENTER_ID, scene.is3D() ? DOF2Action.TRANSLATE_Z : DOF2Action.SCALE);
		eyeProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, DOF2Action.TRANSLATE);
		eyeProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, LEFT_ID), LEFT_ID, DOF2Action.ZOOM_ON_REGION);
		eyeProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, CENTER_ID), CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		eyeProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, RIGHT_ID), RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		frameProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, DOF2Action.ROTATE);
		frameProfile().setBinding(buttonModifiersFix(CENTER_ID), CENTER_ID, DOF2Action.SCALE);
		frameProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, DOF2Action.TRANSLATE);
		frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, CENTER_ID), CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, RIGHT_ID), RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		setCommonBindings();
	}

	/**
	 * Set mouse bindings as 'arcball'. Bindings are as follows:
	 * <p>
	 * 1. <b>InteractiveFrame bindings</b><br>
	 * No-button -> ROTATE<br>
	 * Shift + No-button -> SCALE<br>
	 * Ctrl + No-button -> TRANSLATE<br>
	 * Center button -> SCREEN_TRANSLATE<br>
	 * Right button -> SCREEN_ROTATE<br>
	 * <p>
	 * 2. <b>InteractiveFrame bindings</b><br>
	 * No-button -> ROTATE<br>
	 * Shift + No-button -> ZOOM<br>
	 * Ctrl + No-button -> TRANSLATE<br>
	 * Ctrl + Shift + No-button -> ZOOM_ON_REGION<br>
	 * Center button -> SCREEN_TRANSLATE<br>
	 * Right button -> SCREEN_ROTATE.
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * <p>
	 * Note that Alt + No-button is bound to the null action.
	 * 
	 * @see #dragToFirstPerson()
	 * @see #dragToThirdPerson()
	 */
	public void moveToArcball() {
		removeFrameBindings();
		removeEyeBindings();
		eyeProfile().setBinding(DOF2Action.ROTATE);
		eyeProfile().setBinding(MotionEvent.SHIFT, MotionEvent.NO_ID,
				scene.is3D() ? DOF2Action.TRANSLATE_Z : DOF2Action.SCALE);
		eyeProfile().setBinding(MotionEvent.CTRL, MotionEvent.NO_ID, DOF2Action.TRANSLATE);
		eyeProfile().setBinding((MotionEvent.CTRL | MotionEvent.SHIFT), MotionEvent.NO_ID, DOF2Action.ZOOM_ON_REGION);
		setButtonBinding(Target.EYE, CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		setButtonBinding(Target.EYE, RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		eyeProfile().setBinding(MotionEvent.ALT, MotionEvent.NO_ID, null);
		frameProfile().setBinding(DOF2Action.ROTATE);
		frameProfile().setBinding(MotionEvent.SHIFT, MotionEvent.NO_ID, DOF2Action.SCALE);
		frameProfile().setBinding(MotionEvent.CTRL, MotionEvent.NO_ID, DOF2Action.TRANSLATE);
		setButtonBinding(Target.FRAME, CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		setButtonBinding(Target.FRAME, RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		frameProfile().setBinding(MotionEvent.ALT, MotionEvent.NO_ID, null);
		setCommonBindings();
	}

	/**
	 * Set mouse bindings as 'first-person'. Bindings are as follows:
	 * <p>
	 * 1. <b>InteractiveFrame bindings</b><br>
	 * Left button -> ROTATE<br>
	 * Center button -> SCALE<br>
	 * Right button -> TRANSLATE<br>
	 * Shift + Center button -> SCREEN_TRANSLATE<br>
	 * Shift + Right button -> SCREEN_ROTATE<br>
	 * <p>
	 * 2. <b>InteractiveFrame bindings</b><br>
	 * Left button -> MOVE_FORWARD<br>
	 * Center button -> LOOK_AROUND<br>
	 * Right button -> MOVE_BACKWARD<br>
	 * Shift + Left button -> ROTATE_Z<br>
	 * Shift + Center button -> DRIVE<br>
	 * Ctrl + Wheel -> ROLL<br>
	 * Shift + Wheel -> DRIVE<br>
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * 
	 * @see #dragToArcball()
	 * @see #dragToThirdPerson()
	 */
	public void dragToFirstPerson() {
		removeFrameBindings();
		removeEyeBindings();
		eyeProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, DOF2Action.MOVE_FORWARD);
		eyeProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, DOF2Action.MOVE_BACKWARD);
		eyeProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, LEFT_ID), LEFT_ID, DOF2Action.ROTATE_Z);
		setWheelBinding(Target.EYE, MotionEvent.CTRL, DOF1Action.ROTATE_Z);
		if (scene.is3D()) {
			eyeProfile().setBinding(buttonModifiersFix(CENTER_ID), CENTER_ID, DOF2Action.LOOK_AROUND);
			eyeProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, CENTER_ID), CENTER_ID, DOF2Action.DRIVE);
		}
		frameProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, DOF2Action.ROTATE);
		frameProfile().setBinding(buttonModifiersFix(CENTER_ID), CENTER_ID, DOF2Action.SCALE);
		frameProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, DOF2Action.TRANSLATE);
		frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, CENTER_ID), CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, RIGHT_ID), RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		setCommonBindings();
	}

	/**
	 * Set mouse bindings as 'first-person'. Bindings are as follows:
	 * <p>
	 * 1. <b>InteractiveFrame bindings</b><br>
	 * No-button -> ROTATE<br>
	 * Shift + No-button -> SCALE<br>
	 * Ctrl + No-button -> TRANSLATE<br>
	 * Center button -> SCREEN_TRANSLATE<br>
	 * Right button -> SCREEN_ROTATE<br>
	 * <p>
	 * 2. <b>InteractiveFrame bindings</b><br>
	 * Ctrl + No-button -> MOVE_FORWARD<br>
	 * No-button -> LOOK_AROUND<br>
	 * Shift + No-button -> MOVE_BACKWARD<br>
	 * Right button -> ROTATE_Z<br>
	 * Ctrl + Shift + No-button -> DRIVE<br>
	 * Ctrl + Shift + Wheel -> ROTATE_Z<br>
	 * Shift + Wheel -> DRIVE<br>
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * <p>
	 * Note that Alt + No-button is bound to the null action.
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * 
	 * @see #dragToArcball()
	 * @see #dragToThirdPerson()
	 */
	public void moveToFirstPerson() {
		removeFrameBindings();
		removeEyeBindings();
		eyeProfile().setBinding(MotionEvent.CTRL, MotionEvent.NO_ID, DOF2Action.MOVE_FORWARD);
		eyeProfile().setBinding(MotionEvent.SHIFT, MotionEvent.NO_ID, DOF2Action.MOVE_BACKWARD);
		eyeProfile().setBinding(MotionEvent.ALT, MotionEvent.NO_ID, null);
		setButtonBinding(Target.EYE, RIGHT_ID, DOF2Action.ROTATE_Z);
		this.setWheelBinding(Target.EYE, (MotionEvent.CTRL | MotionEvent.SHIFT), DOF1Action.ROTATE_Z);
		if (scene.is3D()) {
			eyeProfile().setBinding(DOF2Action.LOOK_AROUND);
			eyeProfile().setBinding((MotionEvent.CTRL | MotionEvent.SHIFT), MotionEvent.NO_ID, DOF2Action.DRIVE);
		}
		frameProfile().setBinding(DOF2Action.ROTATE);
		frameProfile().setBinding(MotionEvent.SHIFT, MotionEvent.NO_ID, DOF2Action.SCALE);
		frameProfile().setBinding(MotionEvent.CTRL, MotionEvent.NO_ID, DOF2Action.TRANSLATE);
		frameProfile().setBinding(MotionEvent.ALT, MotionEvent.NO_ID, null);
		setButtonBinding(Target.FRAME, CENTER_ID, DOF2Action.SCREEN_TRANSLATE);
		setButtonBinding(Target.FRAME, RIGHT_ID, DOF2Action.SCREEN_ROTATE);
		setCommonBindings();
	}

	/**
	 * Set mouse bindings as 'third-person'. Bindings are as follows: *
	 * <p>
	 * Left button -> MOVE_FORWARD<br>
	 * Center button -> LOOK_AROUND<br>
	 * Right button -> MOVE_BACKWARD<br>
	 * Shift + Left button -> ROLL<br>
	 * Shift + Center button -> DRIVE<br>
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * 
	 * @see #dragToArcball()
	 * @see #dragToFirstPerson()
	 */
	public void dragToThirdPerson() {
		removeFrameBindings();
		removeEyeBindings();
		frameProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, DOF2Action.MOVE_FORWARD);
		frameProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, DOF2Action.MOVE_BACKWARD);
		frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, LEFT_ID), LEFT_ID, DOF2Action.ROTATE_Z);
		if (scene.is3D()) {
			frameProfile().setBinding(buttonModifiersFix(CENTER_ID), CENTER_ID, DOF2Action.LOOK_AROUND);
			frameProfile().setBinding(buttonModifiersFix(MotionEvent.SHIFT, CENTER_ID), CENTER_ID, DOF2Action.DRIVE);
		}
		setCommonBindings();
	}

	/**
	 * Set mouse bindings as 'third-person'. Bindings are as follows:
	 * <p>
	 * Ctrl + No-button -> MOVE_FORWARD<br>
	 * No-button -> LOOK_AROUND<br>
	 * Shift + No-button -> MOVE_BACKWARD<br>
	 * Ctrl + Shift + Wheel -> ROTATE_Z<br>
	 * Ctrl + Shift + No-button -> DRIVE<br>
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * <p>
	 * Note that Alt + No-button is bound to the null action.
	 * <p>
	 * Also set the following (common) bindings are:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * 
	 * @see #dragToArcball()
	 * @see #dragToFirstPerson()
	 */
	public void moveToPerson() {
		removeFrameBindings();
		removeEyeBindings();
		frameProfile().setBinding(MotionEvent.CTRL, MotionEvent.NO_ID, DOF2Action.MOVE_FORWARD);
		frameProfile().setBinding(MotionEvent.SHIFT, MotionEvent.NO_ID, DOF2Action.MOVE_BACKWARD);
		setWheelBinding(Target.FRAME, (MotionEvent.CTRL | MotionEvent.SHIFT), DOF1Action.ROTATE_Z);
		frameProfile().setBinding(MotionEvent.ALT, MotionEvent.NO_ID, null);
		if (scene.is3D()) {
			frameProfile().setBinding(DOF2Action.LOOK_AROUND);
			frameProfile().setBinding((MotionEvent.CTRL | MotionEvent.SHIFT), MotionEvent.NO_ID, DOF2Action.DRIVE);
		}
		setCommonBindings();
	}

	public void removeEyeBindings() {
		removeMotionBindings(Target.EYE);
		removeClickBindings(Target.EYE);
		removeWheelBindings(Target.EYE);
	}

	public void removeFrameBindings() {
		removeMotionBindings(Target.FRAME);
		removeClickBindings(Target.FRAME);
		removeWheelBindings(Target.FRAME);
	}

	/**
	 * Set the following (common) bindings:
	 * <p>
	 * 2 left clicks -> ALIGN_FRAME<br>
	 * 2right clicks -> CENTER_FRAME<br>
	 * Wheel in 2D -> SCALE both, InteractiveFrame and InteractiveFrame<br>
	 * Wheel in 3D -> SCALE InteractiveFrame, and ZOOM InteractiveFrame<br>
	 * <p>
	 * which are used in {@link #dragToArcball()}, {@link #dragToFirstPerson()} and {@link #dragToThirdPerson()}
	 */
	protected void setCommonBindings() {
		eyeClickProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, 2, ClickAction.ALIGN_FRAME);
		eyeClickProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, 2, ClickAction.CENTER_FRAME);
		frameClickProfile().setBinding(buttonModifiersFix(LEFT_ID), LEFT_ID, 2, ClickAction.ALIGN_FRAME);
		frameClickProfile().setBinding(buttonModifiersFix(RIGHT_ID), RIGHT_ID, 2, ClickAction.CENTER_FRAME);
		this.setWheelBinding(Target.EYE, MotionEvent.NO_MODIFIER_MASK, scene.is3D() ? DOF1Action.TRANSLATE_Z
				: DOF1Action.SCALE);
		this.setWheelBinding(Target.FRAME, MotionEvent.NO_MODIFIER_MASK, DOF1Action.SCALE);
	}

	// WRAPPERS

	// Gestures -> mouse move

	/**
	 * Binds the mouse shortcut to the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME).
	 */
	public void setGestureBinding(Target target, DOF2Action action) {
		setBinding(target, new MotionShortcut(MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID), action);
	}

	/**
	 * Binds the mouse shortcut to the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME).
	 */
	public void setGestureBinding(Target target, int mask, DOF2Action action) {
		setBinding(target, new MotionShortcut(mask, MotionEvent.NO_ID), action);
	}

	/**
	 * Removes the mouse shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeGestureShortcut(Target target) {
		removeShortcut(target, new MotionShortcut(MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID));
	}

	/**
	 * Removes the mouse shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeGestureShortcut(Target target, int mask) {
		removeShortcut(target, new MotionShortcut(mask, MotionEvent.NO_ID));
	}

	/**
	 * Returns {@code true} if the mouse shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasGestureShortcut(Target target) {
		return hasShortcut(target, new MotionShortcut(MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID));
	}

	/**
	 * Returns {@code true} if the mouse shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasGestureShortcut(Target target, int mask) {
		return hasShortcut(target, new MotionShortcut(mask, MotionEvent.NO_ID));
	}

	/**
	 * Returns the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given mouse shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF2Action gestureAction(Target target) {
		return action(target, new MotionShortcut(MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID));
	}

	/**
	 * Returns the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given mouse shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF2Action gestureAction(Target target, int mask) {
		return action(target, new MotionShortcut(mask, MotionEvent.NO_ID));
	}

	// Button -> button + drag

	/**
	 * Binds the mask-button mouse shortcut to the (DOF2) dandelion action to be performed by the given {@code target}
	 * (EYE or FRAME).
	 */
	public void setButtonBinding(Target target, int mask, int button, DOF2Action action) {
		setBinding(target, new MotionShortcut(buttonModifiersFix(mask, button), button), action);
	}

	/**
	 * Binds the button mouse shortcut to the (DOF2) dandelion action to be performed by the given {@code target} (EYE or
	 * FRAME).
	 */
	public void setButtonBinding(Target target, int button, DOF2Action action) {
		setBinding(target, new MotionShortcut(buttonModifiersFix(button), button), action);
	}

	/**
	 * Removes the mask-button mouse shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeButtonShortcut(Target target, int mask, int button) {
		removeShortcut(target, new MotionShortcut(buttonModifiersFix(mask, button)));
	}

	/**
	 * Removes the button mouse shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeButtonShortcut(Target target, int button) {
		removeShortcut(target, new MotionShortcut(buttonModifiersFix(button), button));
	}

	/**
	 * Returns {@code true} if the mask-button mouse shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasButtonShortcut(Target target, int mask, int button) {
		return hasShortcut(target, new MotionShortcut(buttonModifiersFix(mask, button), button));
	}

	/**
	 * Returns {@code true} if the button mouse shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasButtonShortcut(Target target, int button) {
		return hasShortcut(target, new MotionShortcut(buttonModifiersFix(button), button));
	}

	/**
	 * Returns the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given mask-button mouse shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF2Action buttonAction(Target target, int mask, int button) {
		return action(target, new MotionShortcut(buttonModifiersFix(mask, button), button));
	}

	/**
	 * Returns the (DOF2) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given button mouse shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF2Action buttonAction(Target target, int button) {
		return action(target, new MotionShortcut(buttonModifiersFix(button), button));
	}

	// wheel here

	/**
	 * Binds the mask-wheel shortcut to the (DOF1) dandelion action to be performed by the given {@code target} (EYE or
	 * FRAME).
	 */
	public void setWheelBinding(Target target, int mask, DOF1Action action) {
		setBinding(target, new MotionShortcut(mask, WHEEL_ID), action.dof2Action());
	}

	/**
	 * Binds the wheel to the (DOF1) dandelion action to be performed by the given {@code target} (EYE or FRAME).
	 */
	public void setWheelBinding(Target target, DOF1Action action) {
		setBinding(target, new MotionShortcut(WHEEL_ID), action.dof2Action());
	}

	/**
	 * Removes the mask-wheel shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeWheelShortcut(Target target, int mask) {
		removeShortcut(target, new MotionShortcut(mask, WHEEL_ID));
	}

	/**
	 * Removes the wheel binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeWheelShortcut(Target target) {
		removeShortcut(target, new MotionShortcut(WHEEL_ID));
	}

	/**
	 * Removes all wheel bindings from the given {@code target} (EYE or FRAME).
	 */
	public void removeWheelBindings(Target target) {
		//bit of a hack ;)
		MotionProfile<DOF2Action> profile = target == Target.EYE ? eyeProfile() : frameProfile();
		Iterator<Entry<MotionShortcut, DOF2Action>> it = profile.map().entrySet().iterator();
		while (it.hasNext()) {
			Entry<MotionShortcut, DOF2Action> entry = it.next();
			if (entry.getKey().id() == WHEEL_ID && entry.getValue().dof1Action() != null)
				it.remove();
		}
	}

	/**
	 * Returns {@code true} if the mask-wheel shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasWheelShortcut(Target target, int mask) {
		return hasShortcut(target, new MotionShortcut(mask, WHEEL_ID));
	}

	/**
	 * Returns {@code true} if the wheel is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasWheelShortcut(Target target) {
		return hasShortcut(target, new MotionShortcut(WHEEL_ID));
	}

	/**
	 * Returns {@code true} if the mouse wheel action is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean isWheelActionBound(Target target, DOF1Action action) {
		return isActionBound(target, action.dof2Action());
	}

	/**
	 * Returns the (DOF1) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given mask-wheel shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF1Action wheelAction(Target target, int mask, DOF1Action action) {
		return action(target, new MotionShortcut(mask, WHEEL_ID)).dof1Action();
	}

	/**
	 * Returns the (DOF1) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to the
	 * given wheel shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public DOF1Action wheelAction(Target target, DOF1Action action) {
		return action(target, new MotionShortcut(WHEEL_ID)).dof1Action();
	}
	
	// click

	/**
	 * Binds the mask-button-ncs (number-of-clicks) click-shortcut to the (click) dandelion action to be performed by the
	 * given {@code target} (EYE or FRAME).
	 */
	public void setClickBinding(Target target, int mask, int button, int ncs, ClickAction action) {
		setBinding(target, new ClickShortcut(buttonModifiersFix(mask, button), button, ncs), action);
	}

	/**
	 * Binds the button-ncs (number-of-clicks) click-shortcut to the (click) dandelion action to be performed by the given
	 * {@code target} (EYE or FRAME).
	 */
	public void setClickBinding(Target target, int button, int ncs, ClickAction action) {
		setBinding(target, new ClickShortcut(buttonModifiersFix(button), button, ncs), action);
	}

	/**
	 * Binds the single-clicked button shortcut to the (click) dandelion action to be performed by the given
	 * {@code target} (EYE or FRAME).
	 */
	public void setClickBinding(Target target, int button, ClickAction action) {
		setBinding(target, new ClickShortcut(buttonModifiersFix(button), button, 1), action);
	}

	/**
	 * Removes the mask-button-ncs (number-of-clicks) click-shortcut binding from the
	 * {@link remixlab.dandelion.core.InteractiveFrame} (if {@code eye} is {@code true}) or from the
	 * {@link remixlab.dandelion.core.InteractiveFrame} (if {@code eye} is {@code false}).
	 */
	public void removeClickShortcut(Target target, int mask, int button, int ncs) {
		removeShortcut(target, new ClickShortcut(buttonModifiersFix(mask, button), button, ncs));
	}

	/**
	 * Removes the button-ncs (number-of-clicks) click-shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeClickShortcut(Target target, int button, int ncs) {
		removeShortcut(target, new ClickShortcut(buttonModifiersFix(button), button, ncs));
	}

	/**
	 * Removes the single-clicked button shortcut binding from the given {@code target} (EYE or FRAME).
	 */
	public void removeClickShortcut(Target target, int button) {
		removeShortcut(target, new ClickShortcut(buttonModifiersFix(button), button, 1));
	}

	/**
	 * Returns {@code true} if the mask-button-ncs (number-of-clicks) click-shortcut is bound to the given {@code target}
	 * (EYE or FRAME).
	 */
	public boolean hasClickShortcut(Target target, int mask, int button, int ncs) {
		return hasShortcut(target, new ClickShortcut(buttonModifiersFix(mask, button), button, ncs));
	}

	/**
	 * Returns {@code true} if the button-ncs (number-of-clicks) click-shortcut is bound to the given {@code target} (EYE
	 * or FRAME).
	 */
	public boolean hasClickShortcut(Target target, int button, int ncs) {
		return hasShortcut(target, new ClickShortcut(buttonModifiersFix(button), button, ncs));
	}

	/**
	 * Returns {@code true} if the single-clicked button shortcut is bound to the given {@code target} (EYE or FRAME).
	 */
	public boolean hasClickShortcut(Target target, int button) {
		return hasShortcut(target, new ClickShortcut(buttonModifiersFix(button), button, 1));
	}

	/**
	 * Returns the (click) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to
	 * the given mask-button-ncs (number-of-clicks) click-shortcut. Returns {@code null} if no action is bound to the
	 * given shortcut.
	 */
	public ClickAction clickAction(Target target, int mask, int button, int ncs) {
		return action(target, new ClickShortcut(buttonModifiersFix(mask, button), button, ncs));
	}

	/**
	 * Returns the (click) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to
	 * the given button-ncs (number-of-clicks) click-shortcut. Returns {@code null} if no action is bound to the given
	 * shortcut.
	 */
	public ClickAction clickAction(Target target, int button, int ncs) {
		return action(target, new ClickShortcut(buttonModifiersFix(button), button, ncs));
	}

	/**
	 * Returns the (click) dandelion action to be performed by the given {@code target} (EYE or FRAME) that is bound to
	 * the given single-clicked button shortcut. Returns {@code null} if no action is bound to the given shortcut.
	 */
	public ClickAction clickAction(Target target, int button) {
		return action(target, new ClickShortcut(buttonModifiersFix(button), button, 1));
	}
}