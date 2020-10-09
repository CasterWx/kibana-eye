package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.dao.LoginInfoRepository;
import com.antzuhl.kibana.domain.LoginInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AntzUhl
 * @Date 19:30
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    LoginInfoRepository loginInfoRepository;

    @PostMapping("/all")
    public Response all() {
        List<LoginInfo> emps = loginInfoRepository.findAll();
        return Response.success("success", emps);
    }

    @RequestMapping(value = "/down", method = RequestMethod.POST)
    public void excelDownload(
            @RequestParam(name = "startTime", required = false) Integer startTime,
            @RequestParam(name = "endTime", required = false) Integer endTime,
            HttpServletResponse response) throws IOException {
        //表头数据
        String[] header = {"时间", "客户端", "电话号", "重试次数"};
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称为"学生表"
        HSSFSheet sheet = workbook.createSheet("登录统计表");
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(30);
        //创建标题的显示样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(0);

        //遍历添加表头
        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }
        //获取所有的employee
        List<LoginInfo> emps = loginInfoRepository.findAll();
        emps.stream().filter(item -> {
            String thisTime = item.getLogTime().split(" ")[0].replaceAll("-", "");
            Integer time = Integer.valueOf(thisTime);
            if (time >= startTime && time <= endTime) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        for(int i=0;i<emps.size();i++){
            //创建一行
            HSSFRow row1 = sheet.createRow(i+1);
            //第一列创建并赋值
            row1.createCell(0).setCellValue(new HSSFRichTextString(emps.get(i).getLogTime()));
            //第二列创建并赋值
            row1.createCell(1).setCellValue(new HSSFRichTextString(emps.get(i).getClientType()));
            //第三列创建并赋值
            row1.createCell(2).setCellValue(new HSSFRichTextString(emps.get(i).getMobile()));
            row1.createCell(3).setCellValue(new HSSFRichTextString(emps.get(i).getErrorNum().toString()));
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称，此例中名为student.xls
        response.setHeader("Content-disposition", "attachment;filename=loginFailInfo.xls");
        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }
}
