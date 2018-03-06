package com.shangpin.pending.product.consumer.supplier.common;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.shangpin.pending.product.consumer.service.MaterialProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2018/3/2.
 *
 */
@Component
@Slf4j
public class TranslationUtil {


    @Autowired
    MaterialProperties commonProperties;



    public String translate(String text ){
        try {
            Translate translate = TranslateOptions.newBuilder().setApiKey(commonProperties.getGoogleKey()).build().getService();//TranslateOptions.getDefaultInstance().getService();


            // The text to translate
            Detection detect = translate.detect(text);
            // System.out.println("detect.getLanguage()="+detect.getLanguage());
            String sourceLanguage = detect.getLanguage();
            if("zh-CN".equals(sourceLanguage)){
                return text;
            }
            // Translates some text into Russian
            Translation translation =
                    translate.translate(
                            text,
                            Translate.TranslateOption.sourceLanguage(sourceLanguage),
                            Translate.TranslateOption.targetLanguage("zh-CN"));


//        System.out.printf("Text: %s%n", text);
//        System.out.printf("Translation: %s%n", translation.getTranslatedText());
            String translationTxt  =  translation.getTranslatedText();
            log.info("before=" + text  +" after=" +translationTxt);
            return translationTxt;
        } catch (Exception e) {
            log.error("翻译失败：" + e.getMessage(),e);
            return text;
        }
    }

    public String translateSourceLanguageDefaultEN(String text){
        try {
            Translate translate = TranslateOptions.newBuilder().setApiKey(commonProperties.getGoogleKey()).build().getService();//TranslateOptions.getDefaultInstance().getService();

            // Translates some text into Russian
            Translation translation =
                    translate.translate(
                            text,
                            Translate.TranslateOption.sourceLanguage("en"),
                            Translate.TranslateOption.targetLanguage("zh-CN"));


//        System.out.printf("Text: %s%n", text);
//        System.out.printf("Translation: %s%n", translation.getTranslatedText());
            String translationTxt  =  translation.getTranslatedText();
            log.info("before=" + text  +" after=" +translationTxt);
            return translationTxt;
        } catch (Exception e) {
            log.error("翻译失败：" + e.getMessage(),e);
            return text;
        }
    }
}
