package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class ExtensionAttributes {
    @SerializedName("negotiable_quote_item")
    @Expose
    private NegotiableQuoteItem negotiableQuoteItem;
    @SerializedName("custom_options")
    @Expose
    private List<CustomOptions> customOptions;
    @SerializedName("file_info")
    @Expose
    private FileInfo fileInfo;
    @SerializedName("bundle_options")
    @Expose
    private BundleOptions bundleOptions;
    @SerializedName("configurable_item_options")
    @Expose
    private List<ConfigurableItemOptions> configurableItemOptions ;
    @SerializedName("downloadable_option")
    @Expose
    private DownloadableOption downloadableOption;
    @SerializedName("giftcard_item_option")
    @Expose
    private GiftcardItemOption giftcardItemOption;
    @SerializedName("gift_registry_id")
    @Expose
    private String gift_registry_id;
    @SerializedName("negotiable_quote_item_totals")
    @Expose
    private NegotiableQuoteItemTotals negotiableQuoteItemTotals;
    @SerializedName("tax_grandtotal_details")
    @Expose
    private List<TaxGrandtotalDetails> taxGrandtotalDetails;
    @SerializedName("gift_cards")
    @Expose
    private String giftCards;
    @SerializedName("gw_order_id")
    @Expose
    private String gwOrderId;
    @SerializedName("gw_item_ids")
    @Expose
    private List<String> gwItemIds ;
    @SerializedName("gw_allow_gift_receipt")
    @Expose
    private String gwAllowGiftReceipt;
    @SerializedName("gw_add_card")
    @Expose
    private String gwAddCard;
    @SerializedName("gw_price")
    @Expose
    private String gwPrice;
    @SerializedName("gw_base_price")
    @Expose
    private String gwBasePrice;
    @SerializedName("gw_items_price")
    @Expose
    private String gwItemsPrice;
    @SerializedName("gw_items_base_price")
    @Expose
    private String gwItemsBasePrice;
    @SerializedName("gw_card_price")
    @Expose
    private String gwCardPrice;
    @SerializedName("gw_card_base_price")
    @Expose
    private String gwCardBasePrice;
    @SerializedName("gw_base_tax_amount")
    @Expose
    private String gwBaseTaxAmount;
    @SerializedName("gw_tax_amount")
    @Expose
    private String gwTaxAmount;
    @SerializedName("gw_items_base_tax_amount")
    @Expose
    private String gwItemsBaseTaxAmount;
    @SerializedName("gw_items_tax_amount")
    @Expose
    private String gwItemsTaxAmount;
    @SerializedName("gw_card_base_tax_amount")
    @Expose
    private String gwCardBaseTaxAmount;
    @SerializedName("gw_card_tax_amount")
    @Expose
    private String gwCardTaxAmount;
    @SerializedName("gw_price_incl_tax")
    @Expose
    private String gwPriceInclTax;
    @SerializedName("gw_base_price_incl_tax")
    @Expose
    private String gwBasePriceInclTax;
    @SerializedName("gw_card_price_incl_tax")
    @Expose
    private String gwCardPriceInclTax;
    @SerializedName("gw_card_base_price_incl_tax")
    @Expose
    private String gwCardBasePriceInclTax;
    @SerializedName("gw_items_price_incl_tax")
    @Expose
    private String gwItemsPriceInclTax;

    @SerializedName("gw_items_base_price_incl_tax")
    @Expose
    private String gwItemsBasePriceInclTax;

    @SerializedName("coupon_label")
    @Expose
    private String couponLabel;
    @SerializedName("base_customer_balance_amount")
    @Expose
    private String baseCustomerBalanceAmount;
    @SerializedName("customer_balance_amount")
    @Expose
    private String customerBalanceAmount;
    @SerializedName("negotiable_quote_totals")
    @Expose
    private NegotiableQuoteTotals negotiableQuoteTotals;
    @SerializedName("reward_points_balance")
    @Expose
    private String rewardPointsBalance;
    @SerializedName("reward_currency_amount")
    @Expose
    private String rewardCurrencyAmount;
    @SerializedName("base_reward_currency_amount")
    @Expose
    private String baseRewardCurrencyAmount;

    @SerializedName("agreement_ids")
    @Expose
    private List<String> agreement_ids ;
}
