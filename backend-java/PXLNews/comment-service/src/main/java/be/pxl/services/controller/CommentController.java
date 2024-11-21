package be.pxl.services.controller;


import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
}
