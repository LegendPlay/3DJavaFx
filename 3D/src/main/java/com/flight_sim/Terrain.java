package com.flight_sim;

import javafx.scene.shape.TriangleMesh;

public class Terrain {
    public TriangleMesh getMesh() {
        return mesh;
    }

    private TriangleMesh mesh;

    Terrain(int seed, int minX, int minZ, int maxX, int maxZ) {
        OpenSimplex2S simplex = new OpenSimplex2S();
        mesh = new TriangleMesh();
        for (float x = minX; x < maxX; x += 0.25) {
            for (float z = minZ; z < maxZ; z += 0.25) {
                float y = (300*simplex.noise2(seed, x, z));
                mesh.getPoints().addAll(x, y, z);
            }
        }
        mesh.getTexCoords().addAll(0, 0);
        addFaces();
    }

    private void addFaces() {// add faces to the mesh
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
}
