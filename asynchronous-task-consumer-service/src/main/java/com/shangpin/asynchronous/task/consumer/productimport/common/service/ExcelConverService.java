package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ExcelConverService {

    @Autowired
    FtpProperties ftpProperties;

    public String convertExcelMade(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);

        String[] headers = { "任务编号", "供货商产地", "尚品产地", "任务说明"};
        String[] columns = { "taskNo", "supplierVal", "hubVal", "task"};
        ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }

    public String convertExcel(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);

        String[] headers = { "任务编号", "货号", "任务状态", "任务说明","新货号" };
        String[] columns = { "taskNo", "spuModel", "taskState", "processInfo","spuNewModel"};
        ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }


    public String convertExcelBrand(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);

        String[] headers = { "任务编号", "供应商品牌", "尚品品牌", "任务说明" };
        String[] columns = { "taskNo", "supplierBrand", "hubBrandNo", "task"};
        ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }
    public String convertExcelCategory(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);

        String[] headers = { "任务编码", "供货商品类", "品类编码", "任务说明"};
        String[] columns = { "taskNo", "supplierCategory", "hubCategoryNo","task"};
        ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }
    public String convertExcelColor(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);

        String[] headers = {"任务编号","供应商颜色","sp颜色","任务状态" };
        String[] columns = {"colorDicItemId","colorItemName","hubcolor","task"};
        ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }

    //材质状态
    public String convertExcelMarterial(List<Map<String, String>> result, String taskNo) throws Exception {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String resultFileName = sim.format(new Date());
        File filePath = new File(ftpProperties.getLocalResultPath());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
        File file = new File(pathFile);
        FileOutputStream out = new FileOutputStream(file);
        String[] header = {"任务编码", "尚品材质名","供应商材质名","任务状态"};
        String[] column = {"taskNo", "hubMaterial","supplierMaterial","task"};
        ExportExcelUtils.exportExcel(resultFileName,header,column, result,out);
        // 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
        String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
        FTPClientUtil.closeFtp();
        if (file.exists()) {
            file.delete();
        }
        // 更新结果文件路径到表中

        return path + resultFileName + ".xls";
    }
}
