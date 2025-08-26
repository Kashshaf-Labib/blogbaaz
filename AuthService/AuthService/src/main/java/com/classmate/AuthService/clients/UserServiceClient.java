package com.classmate.AuthService.clients;

import com.classmate.AuthService.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/email/{email}")
    UserDto getUserByEmail(@PathVariable String email);

    @PostMapping("/api/users")
    UserDto createUser(@RequestBody UserDto userDto);
}
