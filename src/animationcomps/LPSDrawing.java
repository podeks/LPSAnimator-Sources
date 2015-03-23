package animationcomps;

import api.IndexedNavigableRootedNeighborGraph;
import api.NeighborGraph;
import drawingapi.EdgeDrawingAttsLabelling;
import drawingapi.VertexDrawingAttsLabelling;
import drawingcomponents.EdgeDrawingAtts;
import drawingcomponents.VertexDrawingAtts;
import drawingimpl.DrawableNeighborGraph;
import directededge.DirectedEdge;
import quaterniongroup.ProjectiveUnitQuaternion;

/**
 *
 * @author pdokos
 */
public class LPSDrawing extends DrawableNeighborGraph<ProjectiveUnitQuaternion> {
    
    private IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph;    
    private LPSEmbedding emb;
    
    public LPSDrawing(IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> graph, boolean initRadialLayout, boolean concentratedSpecVerts) {
        super(graph);
        lpsGraph=graph;
        emb = new LPSEmbedding(lpsGraph, !initRadialLayout, concentratedSpecVerts);
        setVertDrawingAtts(new OrbClassVertAtts());
        setEdgeDrawingAtts(new PathDistEdgeAtts());
        setEmbedding(emb);
        setDimension(emb.getDiameter());
    }
    
    public int getRadialOverlaps() {
        return emb.getRadialOverlaps();
    }
    
    public void switchEmbedding() {
        emb.switchEmbedding();
        setDimension(emb.getDiameter());
    }
    
    public void switchSpVertConc() {
        emb.switchSpVertConc();
    }
    
    //**************************************************************************
    // Vertex Drawing Attributes
    //**************************************************************************
    private class OrbClassVertAtts implements VertexDrawingAttsLabelling<ProjectiveUnitQuaternion> {

        public NeighborGraph<ProjectiveUnitQuaternion> getGraph() {
            return graph;
        }

        public VertexDrawingAtts getDrawingAtts(ProjectiveUnitQuaternion s) {

            int[] coxeterOrbitClass = s.getQuat().getCoxeterOrbitClass();

            VertexDrawingAtts vAtts = new VertexDrawingAtts();

            float size = 2.0f;

            if (coxeterOrbitClass[0] == 0) {
                size = 2.0f;
            }

            if (coxeterOrbitClass[1] == 1) {
                vAtts.setColor(1.0, 0.1, 0.2);
            } else if (coxeterOrbitClass[1] == 2) {
                vAtts.setColor(0, 1.0, 0);
                size = 2.0f;
            } else if (coxeterOrbitClass[1] == 3) {
                vAtts.setColor(1.0, 1.0, 0);
                size = 2.0f;
            } else if (coxeterOrbitClass[1] == 4) {
                vAtts.setColor(1.0, 1.0, 1.0);
            }
            
            vAtts.setSize(size);
            return vAtts;
        }
    }
    
    //**************************************************************************
    // Edge Drawing Attributes
    //**************************************************************************
    private class PathDistEdgeAtts implements EdgeDrawingAttsLabelling<ProjectiveUnitQuaternion> {

        public NeighborGraph<ProjectiveUnitQuaternion> getGraph() {
            return graph;
        }

        public EdgeDrawingAtts getDrawingAtts(DirectedEdge<ProjectiveUnitQuaternion> e) {
            double pathDist = lpsGraph.getDistanceFromTheRoot(e.getSource());
            int diam = lpsGraph.getMaxDistanceFromRoot();

            //return new EdgeDrawingAtts(1.0*pathDist / (1.0*(diam)), 0.2 + pathDist / (2.0 * diam), 1 - pathDist / (2.0 * diam));
            return new EdgeDrawingAtts(1.0 * pathDist / (1.0 * (diam - 1)), 0.25 + pathDist / (2.0 * diam), 0.75 - pathDist / (2.0 * diam));
        }
    }

}
