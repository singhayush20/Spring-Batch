package com.ayushsingh.spring_boot_batch_demo.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class UserItemProcessor implements ItemProcessor<User,User>/*Specify input and output */  {

    @Override
    @Nullable
    public User process(@NonNull User inputUser) throws Exception {
        /* 
         * If we want to change something in the user,
         * it can be done here.
         */

        return inputUser;
    }
    
}
