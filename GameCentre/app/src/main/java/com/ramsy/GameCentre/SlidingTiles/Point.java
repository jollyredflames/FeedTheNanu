package com.ramsy.GameCentre.SlidingTiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A class that represents a Point.
 */

public class Point {

    /**
     * the x coordinate
     */

    public int x;

    /**
     * the y coordinate
     */

    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point() {}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return a list of all points that are adjacent to self,
     * and that are within the square of size n whose top left point is (0, 0)
     * and whose bottom right point is (n - 1, n - 1)
     * @param n
     * @return a list of adjacent points
     */

    public List<Point> adjacentPointsInsideSquareOfSize(int n) {


        Point[] adjacentPoints = this.adjacentPoints();
        List<Point> points = new ArrayList<>();

        for (Point p : adjacentPoints) {
            if (p.x >= 0 && p.y >= 0 && p.x < n && p.y < n) {
                points.add(p);
            }
        }
        return points;

    }

    /**
     * Returns a list of 4 points that are adjacent to self,
     * in the following order:
     * left, above, right, below
     * @return all 4 adjacent points
     */

    private Point[] adjacentPoints() {

        Point left = new Point(this.x - 1, this.y);
        Point right = new Point(this.x + 1, this.y);
        Point below = new Point(this.x, this.y + 1);
        Point above = new Point(this.x, this.y - 1);

        return new Point[] {left, above, right, below};

    }

    /**
     * Returns true if other Point is adjacent.
     * @param other The point that self is compared with.
     * @return true if other point is adjacent.
     */

    public boolean isAdjacentTo(Point other) {

        Point[] points = this.adjacentPoints();

        return (other.equals(points[0]) || other.equals(points[1]) || other.equals(points[2]) || other.equals(points[3]));
    }

    /**
     * Returns where other Point is, relative to self.
     * @param other The point that self is compared with.
     * @return a Direction representing where other Point is, relative to self.
     */

    public Direction directionOf(Point other) {
        // PRECONDITION:
        // other has the same x value, or the same y value, not both.

        if (this.x == other.x) {
            // Other is either Above or Below
            if (other.y > this.y) {
                return Direction.BELOW;
            } else {
                return Direction.ABOVE;
            }

        } else {
            // Other is either Left or Right
            if (other.x < this.x) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        }

    }

    // These methods are needed, since an instance method checks if two points are equal or not.

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    /**
     * compare if 2 Points are equal
     * @param obj Point to compare 'this' to
     * @return boolean on whether same or not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Point)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Point other = (Point) obj;
        return (this.x == other.x && this.y == other.y);

    }
}
