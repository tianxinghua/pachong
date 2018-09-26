package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class GiftcardItemOption {
    @SerializedName("giftcard_amount")
    @Expose
    private String giftcardAmount;
    @SerializedName("custom_giftcard_amount")
    @Expose
    private String customGiftcardAmount;
    @SerializedName("giftcard_sender_name")
    @Expose
    private String giftcardSenderName;
    @SerializedName("giftcard_recipient_name")
    @Expose
    private String giftcardRecipientName;
    @SerializedName("giftcard_sender_email")
    @Expose
    private String giftcardSenderEmail;
    @SerializedName("giftcard_recipient_email")
    @Expose
    private String giftcardRecipientEmail;
    @SerializedName("giftcard_message")
    @Expose
    private String giftcardMessage;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
}
