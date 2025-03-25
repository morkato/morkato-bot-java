package org.morkato.api.repository.family;

import org.morkato.api.dto.FamilyDTO;
import org.morkato.api.entity.family.FamilyId;
import org.morkato.api.repository.Repository;

public interface FamilyRepository extends Repository {
  FamilyDTO[] fetchAll(FamilyFetchQuery query);
  FamilyDTO fetch(FamilyId query);
  FamilyDTO create(FamilyCreationQuery query);
  FamilyDTO update(FamilyUpdateQuery query);
  void delete(FamilyId query);
}
