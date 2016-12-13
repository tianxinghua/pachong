package com.shangpin.ephub.data.mysql.task.spuimport.po;

import java.io.Serializable;
import java.util.Date;

public class HubSpuImportTask implements Serializable {
    /**
     * 主键
     */
    private Long spuImportTaskId;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 文件存储路径
     */
    private String taskFtpFilePath;

    /**
     * 原始文件名
     */
    private String localFileName;

    /**
     * 系统分配文件名
     */
    private String sysFileName;

    /**
     * 任务状态
     */
    private Byte taskState;

    /**
     * 处理结果文件路径
     */
    private String resultFilePath;

    /**
     * 处理信息
     */
    private String processInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 数据状态
     */
    private Byte dataState;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 备注
     */
    private String memo;

    private static final long serialVersionUID = 1L;

    public Long getSpuImportTaskId() {
        return spuImportTaskId;
    }

    public void setSpuImportTaskId(Long spuImportTaskId) {
        this.spuImportTaskId = spuImportTaskId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo == null ? null : taskNo.trim();
    }

    public String getTaskFtpFilePath() {
        return taskFtpFilePath;
    }

    public void setTaskFtpFilePath(String taskFtpFilePath) {
        this.taskFtpFilePath = taskFtpFilePath == null ? null : taskFtpFilePath.trim();
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName == null ? null : localFileName.trim();
    }

    public String getSysFileName() {
        return sysFileName;
    }

    public void setSysFileName(String sysFileName) {
        this.sysFileName = sysFileName == null ? null : sysFileName.trim();
    }

    public Byte getTaskState() {
        return taskState;
    }

    public void setTaskState(Byte taskState) {
        this.taskState = taskState;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath == null ? null : resultFilePath.trim();
    }

    public String getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(String processInfo) {
        this.processInfo = processInfo == null ? null : processInfo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuImportTaskId=").append(spuImportTaskId);
        sb.append(", taskNo=").append(taskNo);
        sb.append(", taskFtpFilePath=").append(taskFtpFilePath);
        sb.append(", localFileName=").append(localFileName);
        sb.append(", sysFileName=").append(sysFileName);
        sb.append(", taskState=").append(taskState);
        sb.append(", resultFilePath=").append(resultFilePath);
        sb.append(", processInfo=").append(processInfo);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", memo=").append(memo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HubSpuImportTask other = (HubSpuImportTask) that;
        return (this.getSpuImportTaskId() == null ? other.getSpuImportTaskId() == null : this.getSpuImportTaskId().equals(other.getSpuImportTaskId()))
            && (this.getTaskNo() == null ? other.getTaskNo() == null : this.getTaskNo().equals(other.getTaskNo()))
            && (this.getTaskFtpFilePath() == null ? other.getTaskFtpFilePath() == null : this.getTaskFtpFilePath().equals(other.getTaskFtpFilePath()))
            && (this.getLocalFileName() == null ? other.getLocalFileName() == null : this.getLocalFileName().equals(other.getLocalFileName()))
            && (this.getSysFileName() == null ? other.getSysFileName() == null : this.getSysFileName().equals(other.getSysFileName()))
            && (this.getTaskState() == null ? other.getTaskState() == null : this.getTaskState().equals(other.getTaskState()))
            && (this.getResultFilePath() == null ? other.getResultFilePath() == null : this.getResultFilePath().equals(other.getResultFilePath()))
            && (this.getProcessInfo() == null ? other.getProcessInfo() == null : this.getProcessInfo().equals(other.getProcessInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuImportTaskId() == null) ? 0 : getSpuImportTaskId().hashCode());
        result = prime * result + ((getTaskNo() == null) ? 0 : getTaskNo().hashCode());
        result = prime * result + ((getTaskFtpFilePath() == null) ? 0 : getTaskFtpFilePath().hashCode());
        result = prime * result + ((getLocalFileName() == null) ? 0 : getLocalFileName().hashCode());
        result = prime * result + ((getSysFileName() == null) ? 0 : getSysFileName().hashCode());
        result = prime * result + ((getTaskState() == null) ? 0 : getTaskState().hashCode());
        result = prime * result + ((getResultFilePath() == null) ? 0 : getResultFilePath().hashCode());
        result = prime * result + ((getProcessInfo() == null) ? 0 : getProcessInfo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        return result;
    }
}