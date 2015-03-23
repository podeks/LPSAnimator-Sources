package animationcomps;

import api.IndexedNavigableRootedNeighborGraph;
import api.NeighborGraph;
import basic_operations.Arithmetic;
import drawingapi.VertexPositionLabelling;
import drawingcomponents.CartesianPoint3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import quaternion.IntegerThreeSpaceUtility;
import quaternion.ProjectiveReducedQuaternion;
import quaterniongroup.ProjectiveUnitQuaternion;

/**
 *
 * @author pdokos
 */
public class LPSEmbedding implements VertexPositionLabelling<ProjectiveUnitQuaternion> {

    private IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph;
    
    private Map<ProjectiveUnitQuaternion, CartesianPoint3D> embeddedPtsAtInfty;
    private Map<ProjectiveUnitQuaternion, Double> scalingFactor;
    
    private Map<ProjectiveUnitQuaternion, CartesianPoint3D> concentratedSpecVertEmb;
    private Map<ProjectiveUnitQuaternion, CartesianPoint3D> distributedSpecVertEmb;
    
    private Map<ProjectiveUnitQuaternion, Double> concentratedScalingFactor;
    private Map<ProjectiveUnitQuaternion, Double> distributedScalingFactor;
    
    private Map<Integer, Set<IntPt>> ptsAtInfPrimitives;
    
    private boolean gridEmbedding;
    private boolean concentratedSpecVerts;
    
    private int modulus;
    
    private int radialOverlaps;

    public LPSEmbedding(IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph, boolean gridEmbedding, boolean concentratedSpecVerts) {
        this.lpsGraph = lpsGraph;
        this.gridEmbedding = gridEmbedding;
        this.concentratedSpecVerts = concentratedSpecVerts;
        this.modulus = lpsGraph.getRoot().getQuat().getModulus();

        embeddedPtsAtInfty = new HashMap<ProjectiveUnitQuaternion, CartesianPoint3D>();
        scalingFactor = new HashMap<ProjectiveUnitQuaternion, Double>();
        
        concentratedSpecVertEmb = new HashMap<ProjectiveUnitQuaternion, CartesianPoint3D>();
        distributedSpecVertEmb = new HashMap<ProjectiveUnitQuaternion, CartesianPoint3D>();
        concentratedScalingFactor = new HashMap<ProjectiveUnitQuaternion, Double>();
        distributedScalingFactor = new HashMap<ProjectiveUnitQuaternion, Double>();
        
        ptsAtInfPrimitives = new HashMap<Integer, Set<IntPt>>();
        
        initializePtsAtInf(concentratedSpecVerts);
        initializeRegularPtsScalingFactor();
    }

    public void switchSpVertConc() {
        if (concentratedSpecVerts) {
            
            for (Map.Entry<ProjectiveUnitQuaternion, CartesianPoint3D> mapEntry : distributedSpecVertEmb.entrySet()) {
                embeddedPtsAtInfty.put(mapEntry.getKey(), mapEntry.getValue());
                scalingFactor.put(mapEntry.getKey(), distributedScalingFactor.get(mapEntry.getKey()));
            }

        } else {
            for (Map.Entry<ProjectiveUnitQuaternion, CartesianPoint3D> mapEntry : concentratedSpecVertEmb.entrySet()) {
                embeddedPtsAtInfty.put(mapEntry.getKey(), mapEntry.getValue());
                scalingFactor.put(mapEntry.getKey(), concentratedScalingFactor.get(mapEntry.getKey()));
            }
        }
        concentratedSpecVerts = !concentratedSpecVerts;
    }
    
    public NeighborGraph<ProjectiveUnitQuaternion> getGraph() {
        return lpsGraph;
    }

    public CartesianPoint3D getPosition(ProjectiveUnitQuaternion s) {
        ProjectiveReducedQuaternion rep = s.getQuat();

        if (gridEmbedding) {
            if (rep.getEntry(0) == 0) {
                return embeddedPtsAtInfty.get(s);
            }
            return new CartesianPoint3D(rep.getEntry(1), rep.getEntry(2), rep.getEntry(3));
        }

        Double lambda = scalingFactor.get(s);
        if (rep.getEntry(0) == 0) {
            CartesianPoint3D gridPos = embeddedPtsAtInfty.get(s);
            return new CartesianPoint3D(lambda * gridPos.getX(), lambda * gridPos.getY(), lambda * gridPos.getZ());
        }
        return new CartesianPoint3D(lambda * rep.getEntry(1), lambda * rep.getEntry(2), lambda * rep.getEntry(3));
    }

    public double getDiameter() {
        if (gridEmbedding) {
            return 2.25 * (1 + modulus);
        }
        return 3 * lpsGraph.getMaxDistanceFromRoot();
    }
    
    public int getRadialOverlaps() {
        return radialOverlaps;
    }

    public void switchEmbedding() {
        gridEmbedding = !gridEmbedding;
    }
    
    

    private void initializePtsAtInf(boolean concentratedSpecVerts) {
        
        for (int i = 0; i<= lpsGraph.getMaxDistanceFromRoot(); i++) {
            ptsAtInfPrimitives.put(i, new HashSet<IntPt>());
        }
        
        for (ProjectiveUnitQuaternion vert : lpsGraph.getVertices()) {
            ProjectiveReducedQuaternion quat = vert.getQuat();

            int x = 30;
            int y = 0;
            int z = 2;
            if (quat.getEntry(0) == 0) {
                List<ProjectiveReducedQuaternion> fundReps = new ArrayList<ProjectiveReducedQuaternion>();
                ProjectiveReducedQuaternion fundRepA = quat.getFundamentalRepresentative();
                fundReps.add(fundRepA);
                if (fundRepA.getEntry(1) == 1 && fundRepA.getEntry(2) == 1 && fundRepA.getEntry(3) != 1) {
                    int xInvAbs = Math.abs(Arithmetic.centeredInverse(fundRepA.getEntry(3), quat.getModulus()));
                    fundReps.add(new ProjectiveReducedQuaternion(0, 1, xInvAbs, xInvAbs, quat.getModulus()));
                }
                for (int i=0; i<fundReps.size(); i++)  {
                    List<int[]> coxOrbit = getTransformedPosCoxOrb(fundReps.get(i));
                    Iterator<int[]> iterator = coxOrbit.iterator();
                    while (iterator.hasNext()) {
                        int[] next = iterator.next();
                        if (quat.equals(new ProjectiveReducedQuaternion(0, next[0], next[1], next[2], quat.getModulus()))) {
                            x = next[0];
                            y = next[1];
                            z = next[2];

                            //Push affine planes to faces of outermost cube
                            if (Math.abs(x) == 1) {
                                x = x * (quat.getModulus() + 1) / 2;
                            }
                            if (Math.abs(y) == 1) {
                                y = y * (quat.getModulus() + 1) / 2;
                            }
                            if (Math.abs(z) == 1) {
                                z = z * (quat.getModulus() + 1) / 2;
                            }

                        }
                    }
                    
                    int gcd = Arithmetic.gcd(Arithmetic.gcd(x, y), z);
                    
                    ptsAtInfPrimitives.get(lpsGraph.getDistanceFromTheRoot(vert)).add(new IntPt(new int[]{x / gcd, y / gcd, z / gcd}));
                    
                    CartesianPoint3D pt = new CartesianPoint3D(x, y, z);
                    
                    double metricDistance = Math.sqrt(x * x + y * y + z * z);
                    double lambda;
                    if (metricDistance == 0) {
                        lambda = 0.0;
                    } else {
                        lambda = (lpsGraph.getDistanceFromTheRoot(vert)) / metricDistance;
                    }
                    
                    if (fundReps.size() > 1) {
                        if (i == 0) {
                            distributedSpecVertEmb.put(vert, pt);
                            distributedScalingFactor.put(vert, lambda);
                            if (!concentratedSpecVerts) {
                                embeddedPtsAtInfty.put(vert, pt);
                                scalingFactor.put(vert, lambda);
                            }
                        } else {
                            concentratedSpecVertEmb.put(vert, pt);
                            concentratedScalingFactor.put(vert, lambda);
                            if (concentratedSpecVerts) {
                                embeddedPtsAtInfty.put(vert, pt);
                                scalingFactor.put(vert, lambda);
                            }
                        }
                    } else {
                        embeddedPtsAtInfty.put(vert, pt);
                        scalingFactor.put(vert, lambda);
                    }
                
                }
            }
        }
    }
    
    private void initializeRegularPtsScalingFactor() {
        int overlaps = 0;
        int diam = lpsGraph.getMaxDistanceFromRoot();

        //Iteration over radial shells under path distance from the root vertex.
        for (int pathDist = 0; pathDist <= diam; pathDist++) {

            Map<IntPt, Integer> primitiveReps = new HashMap<IntPt, Integer>();  //For counting overlaps.
            for (IntPt ptAtInfPrim : ptsAtInfPrimitives.get(pathDist)) {
                primitiveReps.put(ptAtInfPrim, 1);
            }
            
            
            for (ProjectiveUnitQuaternion vert : lpsGraph.getShell(pathDist)) {
                ProjectiveReducedQuaternion quat = vert.getQuat();

                if (quat.getEntry(0) != 0) {
                    int x = quat.getEntry(1);
                    int y = quat.getEntry(2);
                    int z = quat.getEntry(3);

                    //Set scalingFactor
                    int gcd = 1;                           //Initialize to gcd to 1, in case x=y=z=0 (which have gcd 0).
                    if (x != 0 || y != 0 || z != 0) {
                        gcd = Arithmetic.gcd(Arithmetic.gcd(x, y), z);
                    }

                    IntPt primitiveRep = new IntPt(new int[]{x / gcd, y / gcd, z / gcd});
                    int numOccurences = 1;
                    if (primitiveReps.containsKey(primitiveRep)) {
                        numOccurences = 1 + primitiveReps.get(primitiveRep);
                        overlaps++;
                    }
                    primitiveReps.put(primitiveRep, numOccurences);

                    double metricDistance = Math.sqrt(x * x + y * y + z * z);
                    double lambda;
                    if (metricDistance == 0) {
                        lambda = 0.0;
                    } else {
                        lambda = (pathDist - (numOccurences - 1) * 0.1) / metricDistance;  //nudge by (numOccurences - 1) * 0.1 
                    }
                    scalingFactor.put(vert, lambda);
                }

            }
            primitiveReps.clear();
        }
        
        radialOverlaps = overlaps;
        //setSpecialVertexConcentration(false);
        //System.out.println("RADIAL OVERLAP ADJUSTMENTS:  " + overlaps);
    }

    private static List<int[]> getTransformedPosCoxOrb(ProjectiveReducedQuaternion fundRep) {
        List<int[]> coxOrbit = IntegerThreeSpaceUtility.getPositiveCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        List<int[]> transformedOrbit = new ArrayList<int[]>();
        Iterator<int[]> iterator = coxOrbit.iterator();
        while (iterator.hasNext()) {

            int[] next = iterator.next();
            int x = next[0];
            int y = next[1];
            int z = next[2];

            if (fundRep.getEntry(3) != 1) {
                if (fundRep.getEntry(1) == 0) {
                    if (x == -1 || y == -1 || z == -1) {
                        x = -x;
                        y = -y;
                        z = -z;
                    }
                } else if (fundRep.getEntry(1) == fundRep.getEntry(2)) {
                    if ((z > 1 && x == -1) || (x > 1 && y == -1) || (y > 1 && z == -1)) {
                        x = -x;
                        y = -y;
                        z = -z;
                    }

                } else if (fundRep.getEntry(2) == fundRep.getEntry(3)) {
                    if (x == -1 || y == -1 || z == -1) {
                        x = -x;
                        y = -y;
                        z = -z;
                    }
                }
            }

            transformedOrbit.add(new int[]{x, y, z});

        }
        return transformedOrbit;
    }

    /*
     * Used in createEmbedding to count overlaps in the radial embedding.  
     * Objects of this class are used to represent primitive integer 3-tuples,
     * meaning that their coordinates have gcd equal to 1.
     */
    private static class IntPt {

        int[] pt;

        public IntPt(int[] pt) {
            this.pt = pt;
        }

        @Override
        public boolean equals(Object b) {
            if (b instanceof IntPt) {
                return equals((IntPt) b);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7 * pt[0] + 19 * pt[1] + 47 * pt[2];
            return hash;
        }

        private boolean equals(IntPt b) {

            return b.pt[0] == pt[0] && b.pt[1] == pt[1] && b.pt[2] == pt[2];
        }
    }
}
