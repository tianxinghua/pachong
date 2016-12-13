package com.shangpin.ephub.data.mysql.task.spuimport.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSpuImportTaskCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSpuImportTaskCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo=pageNo;
        this.startRow = (pageNo-1)*this.pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setStartRow(Integer startRow) {
        this.startRow=startRow;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize=pageSize;
        this.startRow = (pageNo-1)*this.pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setFields(String fields) {
        this.fields=fields;
    }

    public String getFields() {
        return fields;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSpuImportTaskIdIsNull() {
            addCriterion("spu_import_task_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdIsNotNull() {
            addCriterion("spu_import_task_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdEqualTo(Long value) {
            addCriterion("spu_import_task_id =", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdNotEqualTo(Long value) {
            addCriterion("spu_import_task_id <>", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdGreaterThan(Long value) {
            addCriterion("spu_import_task_id >", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spu_import_task_id >=", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdLessThan(Long value) {
            addCriterion("spu_import_task_id <", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("spu_import_task_id <=", value, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdIn(List<Long> values) {
            addCriterion("spu_import_task_id in", values, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdNotIn(List<Long> values) {
            addCriterion("spu_import_task_id not in", values, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdBetween(Long value1, Long value2) {
            addCriterion("spu_import_task_id between", value1, value2, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andSpuImportTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("spu_import_task_id not between", value1, value2, "spuImportTaskId");
            return (Criteria) this;
        }

        public Criteria andTaskNoIsNull() {
            addCriterion("task_no is null");
            return (Criteria) this;
        }

        public Criteria andTaskNoIsNotNull() {
            addCriterion("task_no is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNoEqualTo(String value) {
            addCriterion("task_no =", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoNotEqualTo(String value) {
            addCriterion("task_no <>", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoGreaterThan(String value) {
            addCriterion("task_no >", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoGreaterThanOrEqualTo(String value) {
            addCriterion("task_no >=", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoLessThan(String value) {
            addCriterion("task_no <", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoLessThanOrEqualTo(String value) {
            addCriterion("task_no <=", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoLike(String value) {
            addCriterion("task_no like", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoNotLike(String value) {
            addCriterion("task_no not like", value, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoIn(List<String> values) {
            addCriterion("task_no in", values, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoNotIn(List<String> values) {
            addCriterion("task_no not in", values, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoBetween(String value1, String value2) {
            addCriterion("task_no between", value1, value2, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskNoNotBetween(String value1, String value2) {
            addCriterion("task_no not between", value1, value2, "taskNo");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathIsNull() {
            addCriterion("task_ftp_file_path is null");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathIsNotNull() {
            addCriterion("task_ftp_file_path is not null");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathEqualTo(String value) {
            addCriterion("task_ftp_file_path =", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathNotEqualTo(String value) {
            addCriterion("task_ftp_file_path <>", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathGreaterThan(String value) {
            addCriterion("task_ftp_file_path >", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("task_ftp_file_path >=", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathLessThan(String value) {
            addCriterion("task_ftp_file_path <", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathLessThanOrEqualTo(String value) {
            addCriterion("task_ftp_file_path <=", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathLike(String value) {
            addCriterion("task_ftp_file_path like", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathNotLike(String value) {
            addCriterion("task_ftp_file_path not like", value, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathIn(List<String> values) {
            addCriterion("task_ftp_file_path in", values, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathNotIn(List<String> values) {
            addCriterion("task_ftp_file_path not in", values, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathBetween(String value1, String value2) {
            addCriterion("task_ftp_file_path between", value1, value2, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andTaskFtpFilePathNotBetween(String value1, String value2) {
            addCriterion("task_ftp_file_path not between", value1, value2, "taskFtpFilePath");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameIsNull() {
            addCriterion("local_file_name is null");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameIsNotNull() {
            addCriterion("local_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameEqualTo(String value) {
            addCriterion("local_file_name =", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameNotEqualTo(String value) {
            addCriterion("local_file_name <>", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameGreaterThan(String value) {
            addCriterion("local_file_name >", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("local_file_name >=", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameLessThan(String value) {
            addCriterion("local_file_name <", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameLessThanOrEqualTo(String value) {
            addCriterion("local_file_name <=", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameLike(String value) {
            addCriterion("local_file_name like", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameNotLike(String value) {
            addCriterion("local_file_name not like", value, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameIn(List<String> values) {
            addCriterion("local_file_name in", values, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameNotIn(List<String> values) {
            addCriterion("local_file_name not in", values, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameBetween(String value1, String value2) {
            addCriterion("local_file_name between", value1, value2, "localFileName");
            return (Criteria) this;
        }

        public Criteria andLocalFileNameNotBetween(String value1, String value2) {
            addCriterion("local_file_name not between", value1, value2, "localFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameIsNull() {
            addCriterion("sys_file_name is null");
            return (Criteria) this;
        }

        public Criteria andSysFileNameIsNotNull() {
            addCriterion("sys_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andSysFileNameEqualTo(String value) {
            addCriterion("sys_file_name =", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameNotEqualTo(String value) {
            addCriterion("sys_file_name <>", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameGreaterThan(String value) {
            addCriterion("sys_file_name >", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("sys_file_name >=", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameLessThan(String value) {
            addCriterion("sys_file_name <", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameLessThanOrEqualTo(String value) {
            addCriterion("sys_file_name <=", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameLike(String value) {
            addCriterion("sys_file_name like", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameNotLike(String value) {
            addCriterion("sys_file_name not like", value, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameIn(List<String> values) {
            addCriterion("sys_file_name in", values, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameNotIn(List<String> values) {
            addCriterion("sys_file_name not in", values, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameBetween(String value1, String value2) {
            addCriterion("sys_file_name between", value1, value2, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andSysFileNameNotBetween(String value1, String value2) {
            addCriterion("sys_file_name not between", value1, value2, "sysFileName");
            return (Criteria) this;
        }

        public Criteria andTaskStateIsNull() {
            addCriterion("task_state is null");
            return (Criteria) this;
        }

        public Criteria andTaskStateIsNotNull() {
            addCriterion("task_state is not null");
            return (Criteria) this;
        }

        public Criteria andTaskStateEqualTo(Byte value) {
            addCriterion("task_state =", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotEqualTo(Byte value) {
            addCriterion("task_state <>", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateGreaterThan(Byte value) {
            addCriterion("task_state >", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("task_state >=", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateLessThan(Byte value) {
            addCriterion("task_state <", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateLessThanOrEqualTo(Byte value) {
            addCriterion("task_state <=", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateIn(List<Byte> values) {
            addCriterion("task_state in", values, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotIn(List<Byte> values) {
            addCriterion("task_state not in", values, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateBetween(Byte value1, Byte value2) {
            addCriterion("task_state between", value1, value2, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotBetween(Byte value1, Byte value2) {
            addCriterion("task_state not between", value1, value2, "taskState");
            return (Criteria) this;
        }

        public Criteria andResultFilePathIsNull() {
            addCriterion("result_file_path is null");
            return (Criteria) this;
        }

        public Criteria andResultFilePathIsNotNull() {
            addCriterion("result_file_path is not null");
            return (Criteria) this;
        }

        public Criteria andResultFilePathEqualTo(String value) {
            addCriterion("result_file_path =", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathNotEqualTo(String value) {
            addCriterion("result_file_path <>", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathGreaterThan(String value) {
            addCriterion("result_file_path >", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("result_file_path >=", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathLessThan(String value) {
            addCriterion("result_file_path <", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathLessThanOrEqualTo(String value) {
            addCriterion("result_file_path <=", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathLike(String value) {
            addCriterion("result_file_path like", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathNotLike(String value) {
            addCriterion("result_file_path not like", value, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathIn(List<String> values) {
            addCriterion("result_file_path in", values, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathNotIn(List<String> values) {
            addCriterion("result_file_path not in", values, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathBetween(String value1, String value2) {
            addCriterion("result_file_path between", value1, value2, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andResultFilePathNotBetween(String value1, String value2) {
            addCriterion("result_file_path not between", value1, value2, "resultFilePath");
            return (Criteria) this;
        }

        public Criteria andProcessInfoIsNull() {
            addCriterion("process_info is null");
            return (Criteria) this;
        }

        public Criteria andProcessInfoIsNotNull() {
            addCriterion("process_info is not null");
            return (Criteria) this;
        }

        public Criteria andProcessInfoEqualTo(String value) {
            addCriterion("process_info =", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoNotEqualTo(String value) {
            addCriterion("process_info <>", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoGreaterThan(String value) {
            addCriterion("process_info >", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoGreaterThanOrEqualTo(String value) {
            addCriterion("process_info >=", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoLessThan(String value) {
            addCriterion("process_info <", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoLessThanOrEqualTo(String value) {
            addCriterion("process_info <=", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoLike(String value) {
            addCriterion("process_info like", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoNotLike(String value) {
            addCriterion("process_info not like", value, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoIn(List<String> values) {
            addCriterion("process_info in", values, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoNotIn(List<String> values) {
            addCriterion("process_info not in", values, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoBetween(String value1, String value2) {
            addCriterion("process_info between", value1, value2, "processInfo");
            return (Criteria) this;
        }

        public Criteria andProcessInfoNotBetween(String value1, String value2) {
            addCriterion("process_info not between", value1, value2, "processInfo");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(String value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(String value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(String value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(String value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLike(String value) {
            addCriterion("update_user like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotLike(String value) {
            addCriterion("update_user not like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<String> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<String> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(String value1, String value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(String value1, String value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andDataStateIsNull() {
            addCriterion("data_state is null");
            return (Criteria) this;
        }

        public Criteria andDataStateIsNotNull() {
            addCriterion("data_state is not null");
            return (Criteria) this;
        }

        public Criteria andDataStateEqualTo(Byte value) {
            addCriterion("data_state =", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotEqualTo(Byte value) {
            addCriterion("data_state <>", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateGreaterThan(Byte value) {
            addCriterion("data_state >", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("data_state >=", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateLessThan(Byte value) {
            addCriterion("data_state <", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateLessThanOrEqualTo(Byte value) {
            addCriterion("data_state <=", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateIn(List<Byte> values) {
            addCriterion("data_state in", values, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotIn(List<Byte> values) {
            addCriterion("data_state not in", values, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateBetween(Byte value1, Byte value2) {
            addCriterion("data_state between", value1, value2, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotBetween(Byte value1, Byte value2) {
            addCriterion("data_state not between", value1, value2, "dataState");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("memo is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("memo is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("memo =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("memo <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("memo >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("memo >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("memo <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("memo <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("memo like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("memo not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("memo in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("memo not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("memo between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("memo not between", value1, value2, "memo");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}