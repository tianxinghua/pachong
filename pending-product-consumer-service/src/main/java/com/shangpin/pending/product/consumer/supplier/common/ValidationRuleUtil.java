package com.shangpin.pending.product.consumer.supplier.common;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by loyalty on 16/12/15.
 * 规则验证工具类
 *
 *  Pattern Pattern.compile(String regex, int flag)

 Pattern.CANON_EQ     当且仅当两个字符的"正规分解(canonical decomposition)"都完全相同的情况下，才认定匹配。比如用了这个标志之后，表达式"a/u030A"会匹配"?"。默认情况下，不考虑"规 范相等性(canonical equivalence)"。
 Pattern.CASE_INSENSITIVE(?i)     默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。这个标志能让表达式忽略大小写进行匹配。要想对Unicode字符进行大小不明感的匹 配，只要将UNICODE_CASE与这个标志合起来就行了。
 Pattern.COMMENTS(?x)     在这种模式下，匹配时会忽略(正则表达式里的)空格字符(译者注：不是指表达式里的"//s"，而是指表达式里的空格，tab，回车之类)。注释从#开始，一直到这行结束。可以通过嵌入式的标志来启用Unix行模式。
 Pattern.DOTALL(?s)     在这种模式下，表达式'.'可以匹配任意字符，包括表示一行的结束符。默认情况下，表达式'.'不匹配行的结束符。
 Pattern.MULTILINE
 (?m)     在这种模式下，'^'和'$'分别匹配一行的开始和结束。此外，'^'仍然匹配字符串的开始，'$'也匹配字符串的结束。默认情况下，这两个表达式仅仅匹配字符串的开始和结束。
 Pattern.UNICODE_CASE
 (?u)     在这个模式下，如果你还启用了CASE_INSENSITIVE标志，那么它会对Unicode字符进行大小写不明感的匹配。默认情况下，大小写不敏感的匹配只适用于US-ASCII字符集。
 Pattern.UNIX_LINES(?d)     在这个模式下，只有'/n'才被认作一行的中止，并且与'.'，'^'，以及'$'进行匹配。

 */
@Component
public class ValidationRuleUtil {

    /**
     * 验证是否匹配
     * @param regex 验证规则（正则表达式）
     * @param spCategoryNo
     * @param spuModel
     * @return
     */
    public static boolean judageSpuMode(String regex,String spCategoryNo,String spuModel){
        //TODO 获取验证规则

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spuModel);
        return  matcher.matches();


    }
}
