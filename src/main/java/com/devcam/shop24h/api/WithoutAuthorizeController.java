package com.devcam.shop24h.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devcam.shop24h.entity.User;
import com.devcam.shop24h.repository.UserRepository;
import com.devcam.shop24h.service.UserService;

/**
 * @author duongdx
 *
 */
@RestController
public class WithoutAuthorizeController {
	@Autowired
	private UserRepository userRepository;
	
    /**
     * Test trường hợp không check quyền Authorize lấy là danh sách user
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<Object>> getUsers() {
        return new ResponseEntity(userRepository.findAll(),HttpStatus.OK);
    }
    /**
     * Test trường hợp khôngcheck quyền Authorize
     * Tạo mới user
     * @param user
     * @return
     */
    @PostMapping("/users/createAdmin")
    public ResponseEntity<Object> create(@RequestBody User user) {
    	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    	return new ResponseEntity(userRepository.save(user),HttpStatus.CREATED);
    }
    
    @Autowired
    private UserService userService;
    /**
     * Test có kiểm tra quyền.
     * @param user
     * @return
     */
    @PostMapping("/users/create")
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public User register(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userService.createUser(user);
    }
}
