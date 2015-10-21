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
