package com.syt.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syt.yygh.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Wangdi
 */
@Mapper
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
