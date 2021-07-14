package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.util.VectorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleIcosphere extends DefaultParticleStyle {

    private int ticksPerSpawn;
    private double radius;
    private int particlesPerLine;
    private int divisions;
    private double angularVelocityX;
    private double angularVelocityY;
    private double angularVelocityZ;

    private int step;

    protected ParticleStyleIcosphere() {
        super("icosphere", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        if (this.step % this.ticksPerSpawn != 0)
            return particles;

        Icosahedron icosahedron = new Icosahedron(this.divisions, this.radius);
        Set<Vector> points = new HashSet<>();
        for (Icosahedron.Triangle triangle : icosahedron.getTriangles())
            points.addAll(this.getPointsAlongTriangle(triangle, this.particlesPerLine));

        double multiplier = ((double) this.step / this.ticksPerSpawn);
        double xRotation = multiplier * this.angularVelocityX;
        double yRotation = multiplier * this.angularVelocityY;
        double zRotation = multiplier * this.angularVelocityZ;

        if (particle.getColor().equals(OrdinaryColor.RAINBOW)) {
            double lowest = points.stream().mapToDouble(Vector::getY).min().orElse(1);
            double highest = points.stream().mapToDouble(Vector::getY).max().orElse(2);
            double range = highest - lowest;
            double offset = (this.step % 90) / 90.0;
            for (Vector point : points) {
                double hue = (((point.getY() - lowest) / range) + offset) % 1;
                Color color = Color.getHSBColor((float) hue, 1.0F, 1.0F);
                OrdinaryColor optionalData = new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
                VectorUtils.rotateVector(point, xRotation, yRotation, zRotation);
                particles.add(new PParticle(location.clone().add(point), 0, 0, 0, 0, false, optionalData));
            }
        } else if (particle.getColor().equals(new OrdinaryColor(0, 0, 0))) {
            double lowest = points.stream().mapToDouble(Vector::getY).min().orElse(1);
            double highest = points.stream().mapToDouble(Vector::getY).max().orElse(2);
            double range = highest - lowest;
            double offset = this.step / 20.0;
            for (Vector point : points) {
                double theta = ((point.getY() - lowest) / range) + offset;
                int value = (int) ((Math.cos(theta) + 1) / 2 * 255);
                OrdinaryColor optionalData = new OrdinaryColor(value, value, value);
                VectorUtils.rotateVector(point, xRotation, yRotation, zRotation);
                particles.add(new PParticle(location.clone().add(point), 0, 0, 0, 0, false, optionalData));
            }
        } else {
            for (Vector point : points) {
                VectorUtils.rotateVector(point, xRotation, yRotation, zRotation);
                particles.add(new PParticle(location.clone().add(point), 0, 0, 0, 0, false, null));
            }
        }

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step++;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("MAGMA_CREAM");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("radius", 3.0, "The radius of the sphere");
        this.setIfNotExists("particles-per-line", 8, "The number of particles to spawn per tick");
        this.setIfNotExists("divisions", 1, "The number of divisions to make, more divisions will be closer to an actual sphere");
        this.setIfNotExists("angular-velocity-x", 0.00314159265, "The angular velocity on the x-axis");
        this.setIfNotExists("angular-velocity-y", 0.00369599135, "The angular velocity on the y-axis");
        this.setIfNotExists("angular-velocity-z", 0.00405366794, "The angular velocity on the z-axis");
        this.setIfNotExists("ticks-per-spawn", 5, "How many ticks to wait between particle spawns");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.radius = config.getDouble("radius");
        this.particlesPerLine = config.getInt("particles-per-line");
        this.divisions = config.getInt("divisions");
        this.angularVelocityX = config.getDouble("angular-velocity-x");
        this.angularVelocityY = config.getDouble("angular-velocity-y");
        this.angularVelocityZ = config.getDouble("angular-velocity-z");
        this.ticksPerSpawn = config.getInt("ticks-per-spawn");
    }

    private Set<Vector> getPointsAlongTriangle(Icosahedron.Triangle triangle, int pointsPerLine) {
        Set<Vector> points = new HashSet<>();
        points.addAll(this.getPointsAlongLine(triangle.point1, triangle.point2, pointsPerLine));
        points.addAll(this.getPointsAlongLine(triangle.point2, triangle.point3, pointsPerLine));
        points.addAll(this.getPointsAlongLine(triangle.point3, triangle.point1, pointsPerLine));
        return points;
    }

    private Set<Vector> getPointsAlongLine(Vector point1, Vector point2, int pointsPerLine) {
        double distance = point1.distance(point2);
        Vector angle = point2.clone().subtract(point1).normalize();
        double distanceBetween = distance / pointsPerLine;

        Set<Vector> points = new HashSet<>();
        for (double i = 0; i < distance; i += distanceBetween)
            points.add(point1.clone().add(angle.clone().multiply(i)));

        return points;
    }

    /**
     * Largely taken from https://www.javatips.net/api/vintagecraft-master/src/main/java/at/tyron/vintagecraft/Client/Render/Math/Icosahedron.java
     */
    public static class Icosahedron {

        public static double X = 0.525731112119133606f;
        public static double Z = 0.850650808352039932f;

        public static double[][] vdata = {{-X, 0, Z}, {X, 0, Z}, {-X, 0, -Z}, {X, 0, -Z}, {0, Z, X}, {0, Z, -X},
                {0, -Z, X}, {0, -Z, -X}, {Z, X, 0}, {-Z, X, 0}, {Z, -X, 0}, {-Z, -X, 0}};

        public static int[][] tindx = {{0, 4, 1}, {0, 9, 4}, {9, 5, 4}, {4, 5, 8}, {4, 8, 1}, {8, 10, 1}, {8, 3, 10},
                {5, 3, 8}, {5, 2, 3}, {2, 7, 3}, {7, 10, 3}, {7, 6, 10}, {7, 11, 6}, {11, 0, 6}, {0, 1, 6}, {6, 1, 10},
                {9, 0, 11}, {9, 11, 2}, {9, 2, 5}, {7, 2, 11}};

        public Icosahedron(int depth, double radius) {
            for (int[] ints : tindx)
                this.subdivide(vdata[ints[0]], vdata[ints[1]], vdata[ints[2]], depth, radius);
        }

        private final List<Triangle> triangles = new ArrayList<>();

        private void addTriangle(double[] vA0, double[] vB1, double[] vC2, double radius) {
            Triangle triangle = new Triangle(
                    new Vector(vA0[0], vA0[1], vA0[2]).multiply(radius),
                    new Vector(vB1[0], vB1[1], vB1[2]).multiply(radius),
                    new Vector(vC2[0], vC2[1], vC2[2]).multiply(radius)
            );
            this.triangles.add(triangle);
        }

        private void subdivide(double[] vA0, double[] vB1, double[] vC2, int depth, double radius) {
            double[] vAB = new double[3];
            double[] vBC = new double[3];
            double[] vCA = new double[3];

            if (depth == 0) {
                this.addTriangle(vA0, vB1, vC2, radius);
                return;
            }

            for (int i = 0; i < 3; i++) {
                vAB[i] = (vA0[i] + vB1[i]) / 2;
                vBC[i] = (vB1[i] + vC2[i]) / 2;
                vCA[i] = (vC2[i] + vA0[i]) / 2;
            }

            double modAB = mod(vAB);
            double modBC = mod(vBC);
            double modCA = mod(vCA);

            for (int i = 0; i < 3; i++) {
                vAB[i] /= modAB;
                vBC[i] /= modBC;
                vCA[i] /= modCA;
            }

            this.subdivide(vA0, vAB, vCA, depth - 1, radius);
            this.subdivide(vB1, vBC, vAB, depth - 1, radius);
            this.subdivide(vC2, vCA, vBC, depth - 1, radius);
            this.subdivide(vAB, vBC, vCA, depth - 1, radius);
        }

        public static double mod(double[] v) {
            return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        }

        public List<Triangle> getTriangles() {
            return this.triangles;
        }

        public static class Triangle {
            public Vector point1;
            public Vector point2;
            public Vector point3;

            public Triangle(Vector point1, Vector point2, Vector point3) {
                this.point1 = point1;
                this.point2 = point2;
                this.point3 = point3;
            }
        }
    }

}
