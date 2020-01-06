package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.RoleTypes;
import de.oth.tdanylenko.tdanbank.entity.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends CrudRepository <Roles, Long> {
    Roles findByName(RoleTypes roleName);
}
