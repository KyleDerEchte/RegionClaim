package de.kyleonaut.regionclaim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    private long id;
    private Coordinate first;
    private Coordinate second;
    private List<RegionUser> regionUsers;
}
