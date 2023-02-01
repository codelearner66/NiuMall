package com.hechi.niumall.controller.EditorNotification;

import com.hechi.niumall.entity.EditotrNotification;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.EditorNotificationService;
import com.hechi.niumall.utils.TxyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EditorNotificationController {
    @Autowired
    EditorNotificationService editorNotificationService;
    @Autowired
    TxyunUtils txyunUtils;

    @RequestMapping("/addeditorNotification")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult addeditorNotification(@RequestBody @NotNull EditotrNotification editor) {
        return editorNotificationService.addeditorNotification(editor);
    }

    @RequestMapping("/removeeditorNotification")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult removeeditorNotification(@RequestBody @NotNull EditotrNotification editor) {
        return editorNotificationService.removeeditorNotification(editor);
    }
    @RequestMapping("/removeAllNotification")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult removeAllNotification(@RequestBody @NotNull List<EditotrNotification> editor) {
        EditotrNotification[] editotrNotifications = editor.toArray(new EditotrNotification[editor.size()]);
        return editorNotificationService.removeeditorNotification(editotrNotifications);
    }

    @RequestMapping("/modifyNotification")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult modifyNotification(@RequestBody @NotNull EditotrNotification editor) {
        return editorNotificationService.modifyNotification(editor);
    }
    @RequestMapping("/modifyAllNotification")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult modifyAllNotification(@RequestBody @NotNull List<EditotrNotification> editor) {
        EditotrNotification[] editotrNotifications = editor.toArray(new EditotrNotification[editor.size()]);
        return editorNotificationService.modifyNotification(editotrNotifications);
    }

    @RequestMapping("/getModifyNotification")
    ResponseResult getModifyNotification() {
        return editorNotificationService.getModifyNotification();
    }

    @RequestMapping("/addEditorImages")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult addEditorImages(@RequestParam("file") MultipartFile[] file) throws IOException {
        List<Object> list = new ArrayList<>();
        if (file != null && file.length > 0) {
            for (MultipartFile multipartFile : file) {
                ResponseResult result = editorNotificationService.addEditorImages(multipartFile);
                if (result != null && result.getCode() == 200) {
                    list.add(result.getData());
                }
            }
        }
        return ResponseResult.okResult(list);
    }

    @RequestMapping("/deleteEditorImages")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult deleteEditorImages(@RequestParam("fileName") @NotNull String fileName) {
        return editorNotificationService.deleteEditorImages(fileName);
    }
}
