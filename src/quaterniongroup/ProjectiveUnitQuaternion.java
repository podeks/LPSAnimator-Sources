package quaterniongroup;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lps.LPSConstructionUtility;
import quaternion.ProjectiveReducedQuaternion;
import api.Group;

/**
 *
 * @author pdokos
 */
public class ProjectiveUnitQuaternion implements Group<ProjectiveUnitQuaternion>{

    private ProjectiveReducedQuaternion quat;
    
    public ProjectiveUnitQuaternion(ProjectiveReducedQuaternion q) {
        quat=q;
    }
    
    public ProjectiveUnitQuaternion(int q) {
        this(new ProjectiveReducedQuaternion(1, 0, 0, 0, q));
    }
    
    public ProjectiveReducedQuaternion getQuat() {
        return quat;
    }
    
    @Override
    public boolean equals(Object b) {
        if (b instanceof ProjectiveUnitQuaternion) {
            return quat.equals(((ProjectiveUnitQuaternion) b).quat);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.quat != null ? this.quat.hashCode() : 0);
        return hash;
    }
    
    @Override
    public ProjectiveUnitQuaternion leftProductBy(ProjectiveUnitQuaternion h) {
        return new ProjectiveUnitQuaternion(quat.multiply(h.quat));
    }

    @Override
    public ProjectiveUnitQuaternion rightProductBy(ProjectiveUnitQuaternion h) {
        return new ProjectiveUnitQuaternion(h.quat.multiply(quat));
    }

    @Override
    public ProjectiveUnitQuaternion getInverse() {
        return new ProjectiveUnitQuaternion(quat.conjugate());
    }

    @Override
    public ProjectiveUnitQuaternion getIdentity() {
        return new ProjectiveUnitQuaternion(new ProjectiveReducedQuaternion(1, 0, 0, 0, quat.getModulus()));
    }

    @Override
    public boolean isOperationalWith(ProjectiveUnitQuaternion h) {
        return quat.getModulus()==h.quat.getModulus();
    }
    
    public static Set<ProjectiveUnitQuaternion> getLPSGenSet(int p, short q) {
        Map<ProjectiveReducedQuaternion, Integer> lpsQuaternionGenerators = LPSConstructionUtility.getLPSQuaternionGenerators(p, q);
        
        Set<ProjectiveUnitQuaternion> lpsGenSet = new HashSet<ProjectiveUnitQuaternion>();
        for (ProjectiveReducedQuaternion quatGen : lpsQuaternionGenerators.keySet()) {
            lpsGenSet.add(new ProjectiveUnitQuaternion(quatGen));
        }
        
        return lpsGenSet;
    }
    
    @Override
    public String toString() {
        return "(" + quat.getEntry(0) + ", " + quat.getEntry(1) + ", " + quat.getEntry(2) + ", " + quat.getEntry(3) + ")";
    }
    
}
