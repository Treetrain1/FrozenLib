/*
 * Copyright 2023 FrozenBlock
 * Copyright 2023 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 FrozenBlock
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2022 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2021-2023 QuiltMC
 * ;;match_from: \/\*\r?\n \* Copyright (\(c\) )?2022-2023 QuiltMC
 * ;;match_from: \/\/\/ Q[Uu][Ii][Ll][Tt]
 */

package net.frozenblock.lib.math.api;

import java.awt.geom.Point2D;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class is used to make animations with easings.
 * <p>
 * Defining a point A(x,y) and B(x,y) you can create an animation between those two points ( A.getY() won't affect the animation).
 * <p>
 * Learn more at <a href="https://github.com/LIUKRAST/AnimationAPI/blob/main/README.md">the README</a>
 *
 * @author LiukRast (2021-2022)
 * @since 4.0
 */
@ApiStatus.Obsolete(since = "Minecraft 1.19")
public final class AnimationAPI {
	private AnimationAPI() {
		throw new UnsupportedOperationException("AnimationAPI contains only static declarations.");
	}

	public static double relativeX(Point2D a, Point2D b, double x) {
		return (x - a.getX()) / (b.getX() - a.getX());
	}

	/**
	 * Generates a "random" number depending on another number.
	 *
	 * @deprecated Use seed() instead of this!
	 **/
	@Deprecated
	public static double rawSeed(double seed) {
		double f = Math.pow(Math.PI, 3);
		double linear = (seed + f) * f;
		double flat =  Math.floor(linear);
		return linear - flat;
	}

	/**
	 * Executes {@link #rawSeed(double)} multiple times to make the number look more "random"
	 **/
	public static double seed(double seed) {
		return rawSeed(rawSeed(rawSeed(seed)));
	}

	/**
	 * Convert a 2D position with a seed in a resulting seed
	 **/
	public static double seed2D(Point2D seed2d, double seed) {
		return rawSeed(seed2d.getX()) * rawSeed(seed2d.getX()) * rawSeed(seed);
	}

	public static double legAnimation(double base, double range, double frequency, double limbAngle, double limbDistance, boolean inverted) {
		double baseRange = 1.4;
		double baseFrequency = 0.6662;
		double wave = Math.sin(limbAngle * (baseFrequency * frequency)) * (baseRange * range) * limbDistance;
		if (inverted) {
			return base + wave;
		} else {
			return base - wave;
		}
	}

	public static double legAnimation(double base, double range, double frequency, double limbAngle, double limbDistance) {
		return legAnimation(base, range, frequency, limbAngle, limbDistance, false);
	}

	public static double legAnimation(double base, double limbAngle, double limbDistance, boolean inverted) {
		return legAnimation(base, 1, 1, limbAngle, limbDistance, inverted);
	}

	public static double legAnimation(double base, double limbAngle, double limbDistance) {
		return legAnimation(base, limbAngle, limbDistance, false);
	}


	/**
	 * SINE EASING - Generated using Math.sin()
	 */
	public static double sineEaseIn(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - Math.cos(Math.PI * (relativeX(a, b, x) / 2)));
		}
	}

	public static double sineEaseOut(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (Math.sin(Math.PI * (relativeX(a, b, x) / 2)));
		}
	}

	public static double sineEaseInOut(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (0.5F - (Math.cos(Math.PI * relativeX(a, b, x)) / 2));
		}
	}
	// -------------------------------------------------------

	/**
	 * POLYNOMIAL EASING - Generated by elevating x at a "c" value
	 */
	public static double polyEaseIn(Point2D a, Point2D b, double x, double c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (Math.pow(relativeX(a, b, x), c));
		}
	}

	public static double polyEaseOut(Point2D a, Point2D b, double x, double c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - Math.pow(-(relativeX(a, b, x) - 1), c));
		}
	}

	public static double polyEaseInOut(Point2D a, Point2D b, double x, double c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			if (x < (b.getX() - a.getX()) / 2) {
				return b.getY() * (Math.pow(2, c - 1) * Math.pow(relativeX(a, b, x), c));
			} else {
				return b.getY() * (1 - Math.pow(2 - 2 * relativeX(a, b, x), c) / 2);
			}
		}
	}
	// -------------------------------------------------------

	/**
	 * QUADRATIC EASING - Generated using Poly and assuming c = 2
	 */
	public static double quadraticEaseIn(Point2D a, Point2D b, double x) {
		return polyEaseIn(a, b, x, 2);
	}

	public static double quadraticEaseOut(Point2D a, Point2D b, double x) {
		return polyEaseOut(a, b, x, 2);
	}

	public static double quadraticEaseInOut(Point2D a, Point2D b, double x) {
		return polyEaseInOut(a, b, x, 2);
	}
	// -------------------------------------------------------

	/**
	 * CUBIC EASING - Generated using Poly and assuming c = 3
	 */
	public static double cubicEaseIn(Point2D a, Point2D b, double x) {
		return polyEaseIn(a, b, x, 3);
	}

	public static double cubicEaseOut(Point2D a, Point2D b, double x) {
		return polyEaseOut(a, b, x, 3);
	}

	public static double cubicEaseInOut(Point2D a, Point2D b, double x) {
		return polyEaseInOut(a, b, x, 3);
	}
	// -------------------------------------------------------

	/**
	 * QUARTIC EASING - Generated using Poly and assuming c = 4
	 */
	public static double quarticEaseIn(Point2D a, Point2D b, double x) {
		return polyEaseIn(a, b, x, 4);
	}

	public static double quarticEaseOut(Point2D a, Point2D b, double x) {
		return polyEaseOut(a, b, x, 4);
	}

	public static double quarticEaseInOut(Point2D a, Point2D b, double x) {
		return polyEaseInOut(a, b, x, 4);
	}
	// -------------------------------------------------------

	/**
	 * QUINTIC EASING - Generated using Poly and assuming c = 5
	 */
	public static double quinticEaseIn(Point2D a, Point2D b, double x) {
		return polyEaseIn(a, b, x, 5);
	}

	public static double quinticEaseOut(Point2D a, Point2D b, double x) {
		return polyEaseOut(a, b, x, 5);
	}

	public static double quinticEaseInOut(Point2D a, Point2D b, double x) {
		return polyEaseInOut(a, b, x, 5);
	}
	// -------------------------------------------------------

	/**
	 * EXPONENTIAL EASING - Generated by 2^x
	 */
	public static double expoEaseIn(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * Math.pow(2, (10 * relativeX(a, b, x)) - 10);
		}
	}

	public static double expoEaseOut(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - Math.pow(2, -10 * relativeX(a, b, x)));
		}
	}

	public static double expoEaseInOut(Point2D a, Point2D b, double x) {
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			if (x < (b.getX() - a.getX()) / 2) {
				return b.getY() * Math.pow(2, (20 * relativeX(a, b, x)) - 10) / 2;
			} else {
				return b.getY() * (2 - Math.pow(2, 10 - (20 * relativeX(a, b, x)))) / 2;
			}
		}
	}
	// -------------------------------------------------------

	/**
	 * CICRULAR EASING - Uses Roots and Powers to make curves
	 */
	public static double circEaseIn(Point2D a, Point2D b, double x, int roundness) {
		if (roundness < 0) {
			System.out.println("Animation API error - roundness must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - Math.pow(1 - Math.pow(relativeX(a, b, x), roundness), 1 / roundness));
		}
	}

	public static double circEaseOut(Point2D a, Point2D b, double x, int roundness) {
		if (roundness < 0) {
			System.out.println("Animation API error - roundness must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * Math.pow(1 - Math.pow(relativeX(a, b, x) - 1, roundness), 1 / roundness);
		}
	}

	public static double circEaseInOut(Point2D a, Point2D b, double x, int roundness) {
		if (roundness < 0) {
			System.out.println("Animation API error - roundness must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			if (x < (b.getX() - a.getX()) / 2) {
				return b.getY() * (1 - Math.pow(1 - Math.pow(2 * relativeX(a, b, x), roundness), 1 / roundness)) / 2;
			} else {
				return b.getY() * (Math.pow(1 - Math.pow(-2 * relativeX(a, b, x) + 2, roundness), 1 / roundness) + 1) / 2;
			}
		}
	}

	public static double circEaseIn(Point2D a, Point2D b, double x) {
		return circEaseIn(a, b, x, 2);
	}

	public static double circEaseOut(Point2D a, Point2D b, double x) {
		return circEaseOut(a, b, x, 2);
	}

	public static double circEaseInOut(Point2D a, Point2D b, double x) {
		return circEaseInOut(a, b, x, 2);
	}
	// -------------------------------------------------------

	/**
	 * ELASTIC EASING - Generated by Cosine and a variable "c" of the curves intensity
	 */
	public static double elasticEaseIn(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (Math.cos(2 * Math.PI * c * relativeX(a, b, x)) * relativeX(a, b, x));
		}
	}

	public static double elasticEaseOut(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - (Math.cos(2 * Math.PI * c * relativeX(a, b, x)) * (1 - relativeX(a, b, x))));
		}
	}

	public static double elasticEaseInOut(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (relativeX(a, b, x) + (Math.sin(2 * Math.PI * c * relativeX(a, b, x)) * Math.sin(Math.PI * relativeX(a, b, x))));
		}
	}

	// Same Equations but automaticly defines
	public static double elasticEaseIn(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseIn(a, b, x, c);
	}

	public static double elasticEaseOut(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseOut(a, b, x, c);
	}

	public static double elasticEaseInOut(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseInOut(a, b, x, c);
	}
	// -------------------------------------------------------

	/**
	 * BOUNCE EASING - Generated by an elastic absoluted
	 */
	public static double bounceEaseIn(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * Math.abs(Math.cos(2 * Math.PI * c * relativeX(a, b, x)) * relativeX(a, b, x));
		}
	}

	public static double bounceEaseOut(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 - Math.abs(Math.cos(2 * Math.PI * c * relativeX(a, b, x)) * (1 - relativeX(a, b, x))));
		}
	}

	public static double bounceEaseInOut(Point2D a, Point2D b, double x, int c) {
		if (c < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (relativeX(a, b, x) + Math.abs(Math.sin(2 * Math.PI * c * relativeX(a, b, x)) * Math.sin(Math.PI * relativeX(a, b, x))));
		}
	}

	// Same Equations but automatically defines c
	public static double bounceEaseIn(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseIn(a, b, x, c);
	}

	public static double bounceEaseOut(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseOut(a, b, x, c);
	}

	public static double bounceEaseInOut(Point2D a, Point2D b, double x) {
		int c = (int) (b.getX() - a.getX());
		return elasticEaseInOut(a, b, x, c);
	}
	// -------------------------------------------------------

	/**
	 * BACK EASING - Generates a curve that comes back a little at the end (defined by an amount a >= 0)
	 */
	public static double backEaseIn(Point2D a, Point2D b, double x, double c1) {
		double c2 = c1 + 1;
		if (c1 < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (c2 * Math.pow(relativeX(a, b, x), 3) - c1 * Math.pow(relativeX(a, b, x) - 1, 2));
		}
	}

	public static double backEaseOut(Point2D a, Point2D b, double x, double c1) {
		double c2 = c1 + 1;
		if (c1 < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			return b.getY() * (1 + c2 * Math.pow(relativeX(a, b, x) - 1, 3) + c1 * Math.pow(relativeX(a, b, x) - 1, 2));
		}
	}

	public static double backEaseInOut(Point2D a, Point2D b, double x, double c1) {
		double c2 = c1 + 1;
		double c3 = c1 * 1.525F;
		if (c1 < 0) {
			System.out.println("Animation API error - c must be >= 0");
			return Math.random();
		}
		if (x < a.getX()) {
			return 0; // before animation defining the eq as 0
		} else if (x > b.getX()) {
			return b.getY(); // after animation defining the eq as b's Y
		} else {
			if (x < (b.getX() - a.getX()) / 2) {
				return b.getY() * (Math.pow(2 * relativeX(a, b, x), 2) * ((c3 + 1) * 2 * relativeX(a, b, x) - c3)) / 2;
			} else {
				return b.getY() * (Math.pow(2 * relativeX(a, b, x) - 2, 2) * ((c3 + 1) * (2 * relativeX(a, b, x) - 2) + c3) + 2) / 2;
			}
		}
	}

	// Same method but automatically defines c1
	public static double backEaseIn(Point2D a, Point2D b, double x) {
		return backEaseIn(a, b, x, 1.70158F);
	}

	public static double backEaseOut(Point2D a, Point2D b, double x) {
		return backEaseOut(a, b, x, 1.70158F);
	}

	public static double backEaseInOut(Point2D a, Point2D b, double x) {
		return backEaseInOut(a, b, x, 1.70158F);
	}
	// -------------------------------------------------------

	/**
	 * LOOP SYSTEM
	 * Loop: defines A and B and always repeat between these two values
	 * Boomerang: creates a loop but instead of repeating it from start, it comes back and THEN loop
	 */
	public static double line(Point2D a, Point2D b, double x) {
		return (relativeX(a, b, x) * (b.getY() - a.getY()) + a.getY());
	}

	public static double flat(Point2D a, Point2D b, double x) {
		return (Math.floor(relativeX(a, b, x)) * (b.getY() - a.getY()) + a.getY());
	}

	public static double flat2(Point2D a, Point2D b, double x) {
		return (2 * Math.floor(relativeX(a, b, x) / 2) * (b.getY() - a.getY()) + a.getY());
	}

	public static double inverse(Point2D a, Point2D b, double x) {
		return (flat(a, b, x) + b.getY() - line(a, b, x));
	}

	// BOOMERANG
	public static double boomerang(Point2D a, Point2D b, double x) {
		return line(a, b, x) - flat2(a, b, x) + a.getY() < b.getY() ? (line(a, b, x) - flat2(a, b, x) + a.getY()) : inverse(a, b, x);
	}

	// LOOP
	public static double loop(Point2D a, Point2D b, double x) {
		return (line(a, b, x) - flat(a, b, x) + a.getY());
	}
	// -------------------------------------------------------

	/*
	 * Animation API - LiukRast, ALL RIGHTS RESERVED (2021-2022)
	 */
}
