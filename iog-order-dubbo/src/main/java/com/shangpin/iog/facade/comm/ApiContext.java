package com.shangpin.iog.facade.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * Api的上下文信息
 */
public class ApiContext {

    private static final ThreadLocal<ApiContext> LOCAL = new ThreadLocal<ApiContext>() {
        @Override
        protected ApiContext initialValue() {
            return new ApiContext();
        }
    };

    /**
     * get context.
     *
     * @return context
     */
    public static ApiContext getContext() {
        return LOCAL.get();
    }

    /**
     * remove context.
     *
     * @see com.alibaba.dubbo.rpc.filter.ContextFilter
     */
    public static void removeContext() {
        LOCAL.remove();
    }

    protected ApiContext() {
    }

    private final Map<String, String> attachments = new HashMap<String, String>();

    /**
     * get attachment.
     *
     * @param key
     * @return attachment
     */
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    /**
     * set attachment.
     *
     * @param key
     * @param value
     * @return context
     */
    public ApiContext setAttachment(String key, String value) {
        if (value == null) {
            attachments.remove(key);
        } else {
            attachments.put(key, value);
        }
        return this;
    }

    /**
     * remove attachment.
     *
     * @param key
     * @return context
     */
    public ApiContext removeAttachment(String key) {
        attachments.remove(key);
        return this;
    }

    /**
     * get attachments.
     *
     * @return attachments
     */
    public Map<String, String> getAttachments() {
        return attachments;
    }

    /**
     * set attachments
     *
     * @param attachment
     * @return context
     */
    public ApiContext setAttachments(Map<String, String> attachment) {
        this.attachments.clear();
        if (attachment != null && attachment.size() > 0) {
            this.attachments.putAll(attachment);
        }
        return this;
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

}
