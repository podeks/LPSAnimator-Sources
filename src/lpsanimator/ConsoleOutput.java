package lpsanimator;

import api.IndexedNavigableRootedNeighborGraph;
import java.util.List;
import java.util.Map;
import lps.LPSConstructionUtility;
import quaternion.ProjectiveReducedQuaternion;
import quaterniongroup.ProjectiveUnitQuaternion;
import tools.ShellExpansionAnalyzer;

/**
 *
 * @author pdokos
 */
public class ConsoleOutput {
    
    private ConsoleOutput() {        
    }

    public static void print(IndexedNavigableRootedNeighborGraph<ProjectiveUnitQuaternion> lpsGraph, int p, short q) {

        ShellExpansionAnalyzer<ProjectiveUnitQuaternion> expAnalyzer = new ShellExpansionAnalyzer<ProjectiveUnitQuaternion>(lpsGraph);
        expAnalyzer.generateData();
        String bip;
        if (expAnalyzer.isBipartite()) {
            bip = "YES";
        } else {
            bip = "NO";
        }

        System.out.println();
        System.out.println("-------------" + "LPS(" + p + "," + q + ")" + "-------------");
        System.out.println("GENERATING SET REPRESENTATIVES: ");
        boolean eTransitive = outputGenSet(p, q);
        String edgeTrans;
        if (eTransitive) {
            edgeTrans = "YES";
        } else {
            edgeTrans = "NO";
        }
        System.out.println("       NUMVERTS: " + lpsGraph.getNumberOfVertices() + "    NUMEDGES: " + lpsGraph.getNumberOfEdges() / 2);
        System.out.println("       DIAMETER: " + lpsGraph.getMaxDistanceFromRoot());
        System.out.println("          GIRTH: " + expAnalyzer.getGirth());
        System.out.println("      BIPARTITE: " + bip);
        System.out.println("EDGE TRANSITIVE: " + edgeTrans);
        System.out.println();
        System.out.println("    SHELL SIZES: ");
        for (int i = 0; i <= lpsGraph.getMaxDistanceFromRoot(); i++) {
            if (i < 10) {
                System.out.print(" ");
            }
            System.out.println("             " + i + ": " + lpsGraph.getShell(i).size());
        }
        System.out.println("=====================================");
        System.out.println();
        System.out.println();
    }

    /**
     * Return true if the generating set consists of a single orbit.
     *
     * @param p
     * @param q
     * @return true if the generating set consists of a single orbit.
     */
    private static boolean outputGenSet(int p, short q) {
        List<Map<ProjectiveReducedQuaternion, Integer>> lpsQuaternionGeneratorOrbits = LPSConstructionUtility.getOrbitPartitionedLPSGenerators(p, q);
        boolean eTransitive = lpsQuaternionGeneratorOrbits.size() == 1;

        for (Map<ProjectiveReducedQuaternion, Integer> orbit : lpsQuaternionGeneratorOrbits) {

            int size = orbit.entrySet().size();
            Map.Entry<ProjectiveReducedQuaternion, Integer> fundOctRep = orbit.entrySet().iterator().next();
            ProjectiveReducedQuaternion gen = fundOctRep.getKey();
            Integer val = fundOctRep.getValue();
            for (int i = 1; i <= 3; i++) {
                if (gen.getEntry(i) < 10) {
                    System.out.print(" ");
                }
            }
            System.out.print("     " + gen.getEntry(0) + " " + gen.getEntry(1) + " " + gen.getEntry(2) + " " + gen.getEntry(3) + ":");
            System.out.print("    ORBIT SIZE: " + size);
            if (size < 10) {
                System.out.print(" ");
            }
            if (val>1) {
                System.out.println("\u001B[31m" + "     WEIGHT: " + val + "\u001B[0m");
            } else {
                System.out.println("     WEIGHT: " + val);
            }
        }

        System.out.println();
        return eTransitive;
    }
}