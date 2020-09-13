package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.SearchJobRepository;
import com.antzuhl.kibana.domain.SearchJob;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AntzUhl
 * @Date 21:00
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private SearchJobRepository searchJobRepository;

    @PostMapping("/upload")
    public Response upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Response.error("上传失败，请选择文件");
        }

        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".py")) {
            return Response.error("上传失败，请上传python文件");
        }
        String filePath = "D:/kibana-eye/upload/" + fileName;
        File dest = new File(filePath);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> fileMap = new HashMap<>();
        map.put("path", filePath);
        map.put("files", fileMap);
        try {
            file.transferTo(dest);
            SearchJob searchJob = new SearchJob();
            searchJob.setApplication(filePath);
            searchJob.setIndexName("Python Script");
            searchJob.setQuery(fileRead(dest));
            searchJob.setExecuteTime(Calendar.getInstance().getTime());
            searchJob.setDeleted(0);
            searchJob.setType("python");
            searchJob.setSendTo("ak@antzuhl.cn");
            searchJob.setSendCc("ak@antzuhl.cn");
            searchJobRepository.save(searchJob);
            return Response.success("上传成功", map);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return Response.error("上传失败！");
    }


    public String fileRead(File file) {
        FileReader reader = null;
        BufferedReader bReader = null;
        String s = "";
        String str = "";
        try {
            reader = new FileReader(file);
            bReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while ((s =bReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            bReader.close();
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bReader!=null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}
