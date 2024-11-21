package be.pxl.services.controller;

import be.pxl.services.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
}
