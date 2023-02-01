package com.prgrms.bdbks.domain.user.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.config.jwt.JwtConfigure;
import com.prgrms.bdbks.config.security.SecurityConfig;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.dto.TokenResponse;
import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserLoginRequest;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@EnableConfigurationProperties(JwtConfigure.class)
@Transactional
@Import({SecurityConfig.class})
@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class UserControllerIntegrationTest {

	private final MockMvc mockMvc;

	private final AuthController authController;

	private final UserService userService;

	private String token;

	@BeforeEach
	void setup() {
		UserCreateRequest userCreateRequest = UserObjectProvider.createBlackDogRequest();
		userService.register(userCreateRequest);

		UserLoginRequest userLoginRequest = UserObjectProvider.createBlackDogLoginRequest();
		TokenResponse tokenResponse = userService.login(userLoginRequest);

		token = "Bearer " + tokenResponse.getToken();
	}

	@DisplayName("me() - 인증 완료 후 개인정보 조회에 성공한다.")
	@Test
	void me_ValidParameters_Success() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("readUser() - 인증 완료 후 개인정보를 조회하는데 성공한다.")
	void readUser_success() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + UserObjectProvider.BLACK_DOG_LOGIN_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

}
