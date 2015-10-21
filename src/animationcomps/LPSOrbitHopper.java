/*
 * Copyright (C) 2015 Pericles Dokos
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package animationcomps;

import api.IndexedNavigableRootedNeighborGraph;
import api.NeighborGraph;
import directededge.DirectedEdge;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import quaterniongroup.ProjectiveUnitQuaternion;
import quaternion.ProjectiveReducedQuaternion;

/**
 *
 * @author pdokos
 */
public class LPSOrbitHopper {

    private IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph;
    private boolean isOn;
    private int drag;
    private Random rand;
    private Set<DirectedEdge<ProjectiveUnitQuaternion>> edges;
    private List<ProjectiveUnitQuaternion> currentOrbit;
    private List<ProjectiveUnitQuaternion> nextOrbit;
    private Set<ProjectiveUnitQuaternion> currentNeighborReps;
    private Queue<Collection<DirectedEdge<ProjectiveUnitQuaternion>>> edgeSets;
    private Set<DirectedEdge<ProjectiveUnitQuaternion>> frontEdges;
    private Set<DirectedEdge<ProjectiveUnitQuaternion>> backEdges;

    public LPSOrbitHopper(IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph, int drag) {
        this.lpsGraph = lpsGraph;
        isOn = true;
        
        edges = new HashSet<DirectedEdge<ProjectiveUnitQuaternion>>();
        edgeSets = new ArrayDeque<Collection<DirectedEdge<ProjectiveUnitQuaternion>>>(drag);
        frontEdges = new HashSet<DirectedEdge<ProjectiveUnitQuaternion>>();
        backEdges = new HashSet<DirectedEdge<ProjectiveUnitQuaternion>>();
        currentOrbit = getOctahedralOrbit(lpsGraph.getRoot());
        currentNeighborReps = getNeighborReps(currentOrbit.get(0));
        rand = new Random();
        this.drag = drag;
    }

    public Set<DirectedEdge<ProjectiveUnitQuaternion>> getEdges() {
        return edges;
    }

    public Set<DirectedEdge<ProjectiveUnitQuaternion>> getFrontEdges() {
        return frontEdges;
    }

    public Set<DirectedEdge<ProjectiveUnitQuaternion>> getBackEdges() {
        return backEdges;
    }

    public NeighborGraph<ProjectiveUnitQuaternion> getGraph() {
        return lpsGraph;
    }

    public void initialize() {
        isOn = true;
        edges = new HashSet<DirectedEdge<ProjectiveUnitQuaternion>>();
        currentOrbit = getOctahedralOrbit(lpsGraph.getRoot());
        currentNeighborReps = getNeighborReps(currentOrbit.get(0));
        edgeSets = new ArrayDeque<Collection<DirectedEdge<ProjectiveUnitQuaternion>>>(drag);
    }

    public void next() {

        if (edgeSets.size() == drag) {
            edges.removeAll(backEdges);
            backEdges.clear();
            backEdges.addAll(edgeSets.remove());
        }
        ProjectiveUnitQuaternion randomNeighborRep = getRandomNeighborRep();
        nextOrbit = getOctahedralOrbit(randomNeighborRep);

        List<DirectedEdge<ProjectiveUnitQuaternion>> edgesJoiningOrbits = getEdgesJoiningOrbits(currentOrbit, nextOrbit);
        edgeSets.add(edgesJoiningOrbits);
        edges.addAll(edgesJoiningOrbits);

        frontEdges.clear();
        frontEdges.addAll(edgesJoiningOrbits);

        currentOrbit = nextOrbit;
        currentNeighborReps = getNeighborReps(currentOrbit.get(0));

    }

    public boolean isOn() {
        return isOn;
    }

    public void flipSwitch() {
        isOn = !isOn;
    }

    private ProjectiveUnitQuaternion getRandomNeighborRep() {
        int ind = rand.nextInt(currentNeighborReps.size());
        Iterator<ProjectiveUnitQuaternion> currentNeighborRepsIter = currentNeighborReps.iterator();
        ProjectiveUnitQuaternion nextRep = currentNeighborRepsIter.next();
        for (int i = 1; i <= ind; i++) {
            nextRep = currentNeighborRepsIter.next();
        }
        return nextRep;
    }

    private Set<ProjectiveUnitQuaternion> getNeighborReps(ProjectiveUnitQuaternion vert) {
        return new HashSet<ProjectiveUnitQuaternion>(lpsGraph.getNeighborsOf(vert));
    }

    private List<DirectedEdge<ProjectiveUnitQuaternion>> getEdgesJoiningOrbits(Collection<ProjectiveUnitQuaternion> srcOrbit, Collection<ProjectiveUnitQuaternion> tgtOrbit) {
        List<DirectedEdge<ProjectiveUnitQuaternion>> edgesJoiningOrbits = new ArrayList<DirectedEdge<ProjectiveUnitQuaternion>>();
        for (ProjectiveUnitQuaternion target : tgtOrbit) {
            for (ProjectiveUnitQuaternion source : lpsGraph.getNeighborsOf(target)) {
                if (srcOrbit.contains(source)) {
                    edgesJoiningOrbits.add(new DirectedEdge<ProjectiveUnitQuaternion>(source, target));
                }
            }
        }
        return edgesJoiningOrbits;
    }

    private List<ProjectiveUnitQuaternion> getOctahedralOrbit(ProjectiveUnitQuaternion q) {
        List<ProjectiveUnitQuaternion> octOrbit = new ArrayList<ProjectiveUnitQuaternion>();
        for (ProjectiveReducedQuaternion quat : q.getQuat().getOctahedralOrbit()) {
            octOrbit.add(new ProjectiveUnitQuaternion(quat));
        }
        return octOrbit;
    }
}