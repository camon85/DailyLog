package com.camon.dailylog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jooyong on 2015-11-03.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "welcome home";
    }

}
