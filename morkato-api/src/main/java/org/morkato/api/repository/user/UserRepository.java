package org.morkato.api.repository.user;

import org.morkato.api.repository.Repository;
import org.morkato.api.entity.user.UserId;
import org.morkato.api.dto.UserDTO;

public interface UserRepository extends Repository {
  UserDTO[] fetchAll(UserFetchQuery query);
  UserDTO fetch(UserId query);
  UserDTO create(UserCreationQuery query);
  UserDTO update(UserUpdateQuery query);
  UserDTO delete(UserId query);
}
