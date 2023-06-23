package com.javafx;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Terrain {
    MeshView generateTerrain(int seed, CameraHandler camera) {
        int minX = (int) camera.getCameraTranslateX() - 2000;
        int maxX = (int) camera.getCameraTranslateX() + 2000;
        int minZ = (int) camera.getCameraTranslateY() - 2000;
        int maxZ = (int) camera.getCameraTranslateY() + 2000;

        OpenSimplex2S simplex = new OpenSimplex2S();
        TriangleMesh mesh = new TriangleMesh();
        // generating triangles
        for (float x = minX; x < maxX; x += 5) {
            for (float z = minZ; z < maxZ; z += 5) {
                // ytemp hoplds the y value calculated by the noise function
                float ytemp = (1 * simplex.noise2(seed, 0.005 * x, 0.005 * z));
                // add more variety to map
                ytemp += (0.4 * simplex.noise2(seed, 0.01 * x, 0.01 * z));
                ytemp += (0.2 * simplex.noise2(seed, 0.02 * x, 0.02 * z));
                ytemp/=1.6;
                float y = (float) Math.pow(Math.abs(ytemp), 3);
                y *= 200;
                // System.out.println("x:  "+x+"  y:   "+y+"  z:   "+z);
                mesh.getPoints().addAll(x, y, z);
            }
        }
        mesh.getTexCoords().addAll(0, 0);
        addFaces(mesh);
        MeshView finishView = generateGroup(mesh);

        return finishView;
    }

    private void addFaces(TriangleMesh mesh) {
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

    private MeshView generateGroup(TriangleMesh mesh) {
        // define materials
        final PhongMaterial grass = new PhongMaterial();
        grass.setSpecularColor(Color.LIGHTGREEN);
        grass.setDiffuseColor(Color.GREEN);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(grass);
        //meshView.setDrawMode(DrawMode.LINE);

        return meshView;
    }
}
