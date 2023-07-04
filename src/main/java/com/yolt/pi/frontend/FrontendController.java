package com.yolt.pi.frontend;

import com.yolt.pi.configuration.ApplicationScheduler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
class FrontendController {

    private final ContentService contentService;
    private final ApplicationScheduler applicationScheduler;

    @GetMapping({
            "/react/",
            "/react/{regex:sites|sitecards|site-description|jira|contacts}/**"
    })
    public String forwardToIndex() {
        return "forward:/react";
    }

    @GetMapping(value = "/icon/**", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getIcon(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String iconContentPath = servletPath.substring("/icon".length());
        return contentService.getIcon(iconContentPath);
    }

    @PostMapping(value = "/update-data")
    @ResponseStatus(value = HttpStatus.OK)
    public void forceUpdateAllData() {
        applicationScheduler.forceUpdateAll();
    }
}