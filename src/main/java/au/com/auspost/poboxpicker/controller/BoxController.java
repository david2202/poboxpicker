package au.com.auspost.poboxpicker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoxController {

    @RequestMapping(value = "/maintain", method = RequestMethod.GET)
    public ModelAndView maintain() {
        ModelAndView mav = new ModelAndView("maintain");
        return mav;
    }
}
