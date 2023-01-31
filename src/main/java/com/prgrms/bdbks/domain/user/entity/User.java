package com.prgrms.bdbks.domain.user.entity;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.user.role.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractTimeColumn {

	private static final String NUMBER_REGEX = "[0-9]+";
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_id", length = 20, nullable = false, unique = true)
	private String loginId;

	@Column(length = 60, nullable = false)
	private String password;

	@Column(length = 20, nullable = false)
	private String nickname;

	@Column(nullable = false)
	private LocalDate birthDate;

	@Column(length = 11, nullable = false, unique = true)
	private String phone;

	@Column(nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	protected User(Long id, String loginId, String password, String nickname, LocalDate birthDate, String phone,
		String email, Role role) {
		validateLoginId(loginId);
		validatePassword(password);
		validateNickname(nickname);
		validateBirthDate(birthDate);
		validatePhone(phone);
		validateEmail(email);
		validateAuthority(role);

		this.id = id;
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.role = role;
	}

	private void validateLoginId(String loginId) {
		checkArgument(StringUtils.hasText(loginId), "아이디를 입력해주세요.");
		checkArgument(6 <= loginId.length() && loginId.length() <= 20, "아이디의 길이를 확인해주세요.");
	}

	private void validatePassword(String password) {
		checkArgument(StringUtils.hasText(password), "비밀번호를 입력해주세요.");
		checkArgument(8 <= password.length() && password.length() <= 60, "비밀번호의 길이를 확인해주세요.");
	}

	private void validateNickname(String nickname) {
		checkArgument(StringUtils.hasText(nickname), "닉네임을 입력해주세요.");
		checkArgument(2 <= nickname.length() && nickname.length() <= 20, "닉네임의 길이를 확인해주세요.");
	}

	private void validateBirthDate(LocalDate birthDate) {
		checkNotNull(birthDate, "생일을 입력해주세요.");
		checkArgument(birthDate.isBefore(LocalDate.now()), "생년월일을 확인해주세요.");
	}

	private void validatePhone(String phone) {
		checkArgument(StringUtils.hasText(phone), "핸드폰 번호를 입력해주세요.");
		checkArgument(phone.length() == 11, "핸드폰 번호를 확인해주세요.");
		checkArgument(phone.matches(NUMBER_REGEX), "숫자를 입력해주세요.");
	}

	private void validateEmail(String email) {
		checkArgument(StringUtils.hasText(email), "이메일을 입력해주세요.");
		checkArgument(email.matches(EMAIL_REGEX));
	}

	private void validateAuthority(Role role) {
		checkNotNull(role, "권한을 입력해주세요.");
	}

	public void changePassword(String password) {
		validatePassword(password);
		this.password = password;
	}

	public void changeAuthority(Role role) {
		validateAuthority(role);
		this.role = role;
	}

}
