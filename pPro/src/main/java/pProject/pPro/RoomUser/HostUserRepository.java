package pProject.pPro.RoomUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import pProject.pPro.entity.HostUserEntity;

public interface HostUserRepository extends JpaRepository<HostUserEntity, Long> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT h FROM HostUserEntity h JOIN FETCH h.room r "
			+ "WHERE r.roomId = :roomId AND h.user.userId = :userId")
	Optional<HostUserEntity> findLoginId(@Param("roomId") String roomId, @Param("userId") Long userId);

	@Query("SELECT h FROM HostUserEntity h JOIN FETCH h.room r WHERE h.user.userId = :userId and h.status='JOINED'")
	List<HostUserEntity> findRoomsByUser(@Param("userId") Long userId);

	@Query("""
			    SELECT h FROM HostUserEntity h JOIN FETCH h.room r
			    JOIN FETCH r.createUser c WHERE h.user.userId = :userId and h.status='JOINED'
			""")
	List<HostUserEntity> findRoomsByUserId(@Param("userId") Long userId);

	@Query("select count(h) from HostUserEntity h where h.user.userId=:userId and h.status ='JOINED'")
	Long hostCount(@Param("userId") Long userId);
}
