package com.hechi.niumall.controller.userAddrController;

import com.hechi.niumall.entity.UserAddr;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.UserAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
public class UserAddrController {
    @Autowired
    UserAddrService userAddrService;

    @PostMapping("/addUserAddr")
    public ResponseResult addUserAddr(@RequestBody @NotNull UserAddr userAd){
      return  userAddrService.createAddr(userAd);
    }

    @GetMapping("/getUserAddr")
    public ResponseResult getUserAddr(){
        return userAddrService.getAddrByUser();
    }

    @GetMapping("/deleteUserAddr")
    public ResponseResult deleteAddrById(Long id){

          return userAddrService.deleteAddrById(id);
    }

    @PostMapping("/updataUserAddr")
    public ResponseResult updataUserAddr(@RequestBody UserAddr userAddr){
        return userAddrService.updateAddr(userAddr);
    }
}
