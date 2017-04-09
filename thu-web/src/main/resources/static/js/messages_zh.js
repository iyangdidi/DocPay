(function( factory ) {
    if ( typeof define === "function" && define.amd ) {
        define( ["jquery", "../jquery.validate"], factory );
    } else {
        factory( jQuery );
    }
}(function( $ ) {
// 手机号码验证
    jQuery.validator.addMethod("isMobile", function(value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请输入正确的手机号");

    jQuery.validator.addMethod("isCaptcha", function(value, element) {
        var length = value.length;
        return this.optional(element) || (length == 6);
    }, "请输入正确验证码");

    jQuery.validator.addMethod("matchPassword", function(value, element) {
        var length = value.length;
        var pw = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
        return this.optional(element) || (length >= 6 && length <=20 && pw.test(value));
    }, "密码格式不正确");

	/*
	 * Translated default messages for the jQuery validation plugin.
	 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
	 */
    $.extend($.validator.messages, {
        required: "请填写此字段",
        remote: "手机号已存在",
        phone:"请输入正确的手机号",
        email: "请输入正确格式",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入有效的数字",
        digits: "只能输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "两次密码输入不一致",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多可以输入 {0} 个字符"),
        minlength: $.validator.format("最少要输入 {0} 个字符"),
        rangelength: $.validator.format("密码在 {0} 到 {1} 位之间"),
        range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值")
    });

}));