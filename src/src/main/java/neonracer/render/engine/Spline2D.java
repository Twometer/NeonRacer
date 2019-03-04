package neonracer.render.engine;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a java port of UnitySpline2D for C# licensed under the following license:
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Steve Streeting
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * The original code can be found at https://github.com/sinbad/UnitySpline2D
 */
public class Spline2D {

    private List<Vector2f> points;
    private List<Vector2f> tangents;
    private boolean closed;
    private float curvature;

    private boolean dirty = true;

    public Spline2D(List<Vector2f> points, boolean closed, float curvature) {
        this.points = points;
        this.tangents = new ArrayList<>(points.size());
        this.closed = closed;
        this.curvature = curvature;
    }

    public void addPoint(Vector2f point) {
        points.add(point);
        dirty = true;
    }

    public Vector2f interpolate(float t) {
        recalculate();
        Segment segment = getSegment(t);
        return interpolate(segment.getIndex(), segment.getT());
    }

    private Vector2f interpolate(int fromIndex, float t) {
        int toIndex = fromIndex + 1;
        if (toIndex >= points.size()) {
            if (closed) {
                toIndex %= points.size();
                fromIndex %= points.size();
            } else return points.get(points.size() - 1);
        }

        if (t == 0.0f)
            return points.get(fromIndex);
        else if (t == 1.0f)
            return points.get(toIndex);

        float t2 = t * t;
        float t3 = t2 * t;
        float h1 = 2f * t3 - 3f * t2 + 1f;
        float h2 = -2f * t3 + 3f * t2;
        float h3 = t3 - 2f * t2 + t;
        float h4 = t3 - t2;

        return buildPoint(fromIndex, toIndex, h1, h2, h3, h4);
    }

    public Vector2f getNormal(float t) {
        recalculate();
        Segment segment = getSegment(t);
        return getTangent(segment.getIndex(), segment.getT()).perpendicular();
    }

    private Vector2f getTangent(int fromIndex, float t) {
        int toIndex = fromIndex + 1;
        if (toIndex >= points.size()) {
            if (closed) {
                toIndex = toIndex % points.size();
                fromIndex = fromIndex % points.size();
            } else toIndex = fromIndex;
        }

        // Pre-calculate power
        float t2 = t * t;
        // Derivative of hermite basis parts
        float h1 = 6f * t2 - 6f * t;
        float h2 = -6f * t2 + 6f * t;
        float h3 = 3f * t2 - 4f * t + 1;
        float h4 = 3f * t2 - 2f * t;

        return buildPoint(fromIndex, toIndex, h1, h2, h3, h4);
    }

    private Vector2f buildPoint(int from, int to, float h1, float h2, float h3, float h4) {
        return points.get(from).mul(h1, new Vector2f()).add(
                points.get(to).mul(h2, new Vector2f())).add(
                tangents.get(from).mul(h3, new Vector2f())).add(
                tangents.get(to).mul(h4, new Vector2f()));
    }

    public Segment getSegment(float t) {
        float numPoints = closed ? points.size() : points.size() - 1;
        float fSeg = t * numPoints;
        int idx = (int) fSeg;
        return new Segment(idx, fSeg - idx);
    }

    private void recalculate() {
        if (dirty) {
            dirty = false;

            int numPoints = points.size();
            if (numPoints < 2) {
                // Nothing to do here
                return;
            }
            tangents.clear();

            for (int i = 0; i < numPoints; ++i) {
                Vector2f tangent;
                if (i == 0) {
                    // Special case start
                    if (closed) {
                        // Wrap around
                        tangent = makeTangent(points.get(numPoints - 1), points.get(1));
                    } else {
                        // starting tangent is just from start to point 1
                        tangent = makeTangent(points.get(i), points.get(i + 1));
                    }
                } else if (i == numPoints - 1) {
                    // Special case end
                    if (closed) {
                        // Wrap around
                        tangent = makeTangent(points.get(i - 1), points.get(0));
                    } else {
                        // end tangent just from prev point to end point
                        tangent = makeTangent(points.get(i - 1), points.get(i));
                    }
                } else {
                    // Mid point is average of previous point and next point
                    tangent = makeTangent(points.get(i - 1), points.get(i + 1));
                }
                tangents.add(tangent);
            }
        }
    }

    private Vector2f makeTangent(Vector2f p1, Vector2f p2) {
        return p2.sub(p1, new Vector2f()).mul(curvature);
    }

    public class Segment {

        private int index;

        private float t;

        Segment(int index, float t) {
            this.index = index;
            this.t = t;
        }

        public int getIndex() {
            return index;
        }

        public float getT() {
            return t;
        }
    }

}
