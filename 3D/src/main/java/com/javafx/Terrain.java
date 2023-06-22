package com.javafx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Terrain {
    Terrain(Group group, int seed, CameraHandler camera) {
        int minX = (int) camera.getCameraTranslateX() - 2000;
        int maxX = (int) camera.getCameraTranslateX() + 2000;
        int minZ = (int) camera.getCameraTranslateY() - 2000;
        int maxZ = (int) camera.getCameraTranslateY() + 2000;

        OpenSimplex2S simplex = new OpenSimplex2S();
        TriangleMesh mesh = new TriangleMesh();
        // generating triangles
        for (float x = minX; x < maxX; x += 5) {
            for (float z = minZ; z < maxZ; z += 5) {
                float y = (150 * simplex.noise2(seed, 0.01*x, 0.01*z));
                // add more variety to map
                y += (20 * simplex.noise2(seed, 0.02*x, 0.02*y));
                y += (10 * simplex.noise2(seed, 0.04*x, 0.04*y));
                mesh.getPoints().addAll(x, y, z);
            }
        }
        mesh.getTexCoords().addAll(0, 0);
        addFaces(mesh);
        generateGroup(mesh, group);
    }

    private void addFaces(TriangleMesh mesh) {// add faces to the mesh
        int numPoints = mesh.getPoints().size() / 3;

        int numRows = (int) Math.sqrt(numPoints);
        int numCols = numRows;

        for (int row = 0; row < numRows - 1; row++) {
            for (int col = 0; col < numCols - 1; col++) {
                int i = row * numCols + col;
                mesh.getFaces().addAll(i, 0, i + numCols + 1, 0, i + numCols, 0);
                mesh.getFaces().addAll(i + numCols + 1, 0, i, 0, i + 1, 0);
            }
        }
    }

    private void generateGroup(TriangleMesh mesh, Group group) {
        // define materials
        final PhongMaterial grass = new PhongMaterial();
        grass.setSpecularColor(Color.LIGHTGREEN);
        grass.setDiffuseColor(Color.GREEN);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(grass);

        group.getChildren().add(meshView);
    }
}
