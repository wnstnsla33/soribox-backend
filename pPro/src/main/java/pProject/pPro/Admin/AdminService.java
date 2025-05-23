package pProject.pPro.Admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pProject.pPro.Admin.dto.AdminPagingDTO;
import pProject.pPro.Admin.dto.AdminUserDTO;
import pProject.pPro.Admin.dto.SearchDTO;
import pProject.pPro.Admin.dto.UserChatByAdmin;
import pProject.pPro.Admin.dto.UserDetailByAdmimDTO;
import pProject.pPro.RoomUser.HostUserRepository;
import pProject.pPro.User.UserRepository;
import pProject.pPro.chat.ChatRepository;
import pProject.pPro.entity.ChatEntity;
import pProject.pPro.entity.Grade;
import pProject.pPro.entity.HostUserEntity;
import pProject.pPro.entity.PostEntity;
import pProject.pPro.entity.RoomEntity;
import pProject.pPro.entity.UserEntity;
import pProject.pPro.global.ServiceUtils;
import pProject.pPro.post.PostRepository;
import pProject.pPro.post.DTO.PostListDTO;
import pProject.pPro.reply.ReplyRepository;
import pProject.pPro.room.RoomRepository;
import pProject.pPro.room.DTO.RoomDTO;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final ChatRepository chatRepository;
	private final ReplyRepository replyRepository;
	private final HostUserRepository hostUserRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ServiceUtils utils;

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	public AdminPagingDTO getUserList(SearchDTO dto) {
		Pageable pageable = PageRequest.of(dto.getPage(), 10, Sort.by("userCreateDate").descending());
		Page<UserEntity> pageResult = (dto.getName() != null && !dto.getName().isEmpty())
				? userRepository.findByUserNameContainingIgnoreCase(dto.getName(), pageable)
				: userRepository.findAll(pageable);
		List<AdminUserDTO> list =pageResult.getContent().stream().map(AdminUserDTO::new).toList();
		return new AdminPagingDTO(list,pageResult.getTotalPages());
	}

	public UserDetailByAdmimDTO getUserDetailInfo(Long userId) {
		UserEntity user = utils.findUserById(userId);
		Long replyCount = replyRepository.replyCount(userId);
		Long postCount = postRepository.postCount(userId);
		Long hostCount = hostUserRepository.hostCount(userId);
		UserDetailByAdmimDTO dto = new UserDetailByAdmimDTO(user);
		dto.setPostCount(postCount);
		dto.setReplyCount(replyCount);
		dto.setRoomCount(hostCount);
		return dto;
	}

	public List<PostListDTO> getPostListByAdmin(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPage(), 10, Sort.by("createDate").descending());
		Page<PostEntity> pageResult = postRepository.searchPostsByAdmin(
				searchDTO.getName() != null ? searchDTO.getName() : "", pageable);
		return pageResult.getContent().stream().map(post -> new PostListDTO(post, false, true)).toList();
	}

	public List<RoomDTO> getRoomListByAdmin(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPage(), 10, Sort.by("roomCreatDate").descending());
		Page<RoomEntity> pageResult = roomRepository.searchRooms(searchDTO.getName(),pageable);
		return pageResult.getContent().stream().map(room -> new RoomDTO(room, true)).toList();
	}

	public List<RoomDTO> getUserRoomsByAdmin(Long userId) {
		List<HostUserEntity> hostRooms = hostUserRepository.findRoomsByUserId(userId);
		return hostRooms.stream().map(hu -> new RoomDTO(hu, true)).toList();
	}

	public void deleteRoom(String roomId) {
		roomRepository.deleteById(roomId);
	}

	public void deletePostById(Long postId) {
		postRepository.deleteById(postId);
	}

	public void deleteRoomByAdmin(String roomId) {
		roomRepository.deleteById(roomId);
	}

	public List<UserChatByAdmin> getUserChatsByAdmin(Long userId, int page, String keyword) {
		Pageable pageable = PageRequest.of(page, 20, Sort.by("createTime").descending());
		Page<ChatEntity> chatPage = chatRepository.searchUserChatsWithRoomTitle(userId, keyword, pageable);
		return chatPage.getContent().stream().map(UserChatByAdmin::new).toList();
	}

}
