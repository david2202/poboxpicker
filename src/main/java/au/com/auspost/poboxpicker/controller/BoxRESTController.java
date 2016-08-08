package au.com.auspost.poboxpicker.controller;

import au.com.auspost.poboxpicker.domain.Box;
import au.com.auspost.poboxpicker.service.BoxSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoxRESTController {
    @Autowired
    private BoxSearchService boxSearchService;

    @RequestMapping("/rest/box")
    public List<Box> box(@RequestParam("searchTerm") String searchTerm) {
        return boxSearchService.search(searchTerm);
    }
}
