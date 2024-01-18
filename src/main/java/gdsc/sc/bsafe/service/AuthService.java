package gdsc.sc.bsafe.service;

import gdsc.sc.bsafe.domain.AuthToken;
import gdsc.sc.bsafe.domain.User;
import gdsc.sc.bsafe.dto.request.LoginRequest;
import gdsc.sc.bsafe.dto.request.SignUpRequest;
import gdsc.sc.bsafe.dto.response.LoginResponse;
import gdsc.sc.bsafe.global.exception.CustomException;
import gdsc.sc.bsafe.global.exception.ErrorCode;
import gdsc.sc.bsafe.global.auth.AuthTokenGenerator;
import gdsc.sc.bsafe.global.auth.Password;
import gdsc.sc.bsafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gdsc.sc.bsafe.global.auth.Password.ENCODER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokenGenerator;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        userRepository.save(signUpRequest.toUser());
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findById(loginRequest.id())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        comparePassword(loginRequest.password(), user.getPassword());

        AuthToken generatedToken = authTokenGenerator.generate(user.getUserId(), user.getId());

        return new LoginResponse(
                user.getUserId(),
                generatedToken.getAccessToken(),
                generatedToken.getRefreshToken()
        );
    }

    private void comparePassword(String password, Password savedPassword) {
        if(!savedPassword.isSamePassword(password, ENCODER)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }
}