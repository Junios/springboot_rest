package net.junios.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import net.junios.api.exception.CUserNotFoundException;
import net.junios.api.model.User;
import net.junios.api.model.response.CommonResult;
import net.junios.api.model.response.ListResult;
import net.junios.api.model.response.SingleResult;
import net.junios.api.repository.UserJpaRepo;
import net.junios.api.service.ResponseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(userJpaRepo.findAll());
    }

//    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
//    @GetMapping(value = "/user/{msrl}")
//    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable long msrl) {
//        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
//        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
//    }

//    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
//    @GetMapping(value = "/user/{userId}")
//    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable long userId)throws Exception {
//        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
////        return responseService.getSingleResult(userJpaRepo.findById(userId).orElseThrow(Exception::new));
//        return responseService.getSingleResult(userJpaRepo.findById(userId).orElseThrow(CUserNotFoundException::new));
//    }

    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable long msrl, @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                                   @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
            @ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
            @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {
        userJpaRepo.deleteById(msrl);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }


    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(UserController.class);


    @ApiOperation(value="Test", notes = "이것은 테스트 입니다.")
    @GetMapping(value = "/test")
    @ResponseBody
    public String test(@RequestParam String lang) {
        //logger.debug((getMessage("test.code") + getMessage("userNotFound.msg")));
        //logger.debug("message {}", messageSource == null);
        System.out.println(getMessage("test.code")+getMessage("test.msg"));
        return "test";//(getMessage("test.code") + getMessage("userNotFound.msg"));
    }

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}