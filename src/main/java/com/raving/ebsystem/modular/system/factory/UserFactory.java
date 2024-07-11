package com.raving.ebsystem.modular.system.factory;

import com.raving.ebsystem.modular.system.transfer.UserDto;
import com.raving.ebsystem.common.persistence.model.User;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 */
public class UserFactory {

    public static User createUser(UserDto userDto){
        if(userDto == null){
            return null;
        }else{
            User user = new User();
            BeanUtils.copyProperties(userDto,user);
            return user;
        }
    }
}
