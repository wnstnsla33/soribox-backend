package pProject.pPro.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
	    @Index(name = "idx_chat_user_id", columnList = "user_id"),
	    @Index(name = "idx_chat_room_id", columnList = "room_id")
	})
public class ChatEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long chatId;
	
	private String message;
	private LocalDateTime createTime;
	@ManyToOne
	@JoinColumn(name = "room_id")
	private RoomEntity room;
	@ManyToOne
	@JoinColumn(name= "user_id")
	private UserEntity user;
	
	public ChatEntity(String message,RoomEntity room,UserEntity user) {
		super();
		this.message = message;
		this.createTime = LocalDateTime.now();
		this.room = room;
		this.user=user;
	}
	
}
