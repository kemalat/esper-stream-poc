package de.oriontec.esper.app.repository;

import de.oriontec.esper.app.domain.EngineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineUserRepository extends JpaRepository<EngineUser, Integer> {

  @Query("SELECT u FROM EngineUser u WHERE LOWER(u.username) = LOWER(:username)")
  EngineUser findByUsernameCaseInsensitive(@Param("username") String username);

  @Query
  EngineUser findByEmail(String email);

  @Query
  EngineUser findByEmailAndActivationKey(String email, String activationKey);

  @Query
  EngineUser findByEmailAndResetPasswordKey(String email, String resetPasswordKey);

}
