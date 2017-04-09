~(function(window,$) {

    $.ajaxSettings.beforeSend= function(request) {

    };

    var token = '';
    var phone = '';
    var nickName = '';

    var initToken = {
        getToken : function () {
            token = $.cookie('userToken');
            phone = $.cookie('userPhone');
            if(window.location.href.indexOf("pay") != -1 && !token){
               // window.location.href = "/login";
            }
            if(!token){
                //$(".pay-btn").attr("href","/login");
            }else{
                $(".login-box").hide();
                $(".logged-in").show();
            }

        },
        checkToken : function (token,phone) {
            $.ajax({
                type : "post",
                url : "/user/checkToken?t=" + new Date().getTime(),
                dataType : "json",
                data : {
                    token : token,
                    userName : phone,
                }
            })
            .done(function ( data ) {
                switch(data.code) {
                    case "0":
                        break;
                    default:
                        $.removeCookie('userToken', { path: '/' });
                        $.removeCookie('userPhone', { path: '/' });
                        window.location.href = "/login";
                }
            })
            .fail(function (jqXHR, textStatus,errorThrown) {
                console.log(jqXHR);
            });
        },
        getUserInfo : function (token) {
            $.ajax({
                type : "post",
                url : "/user/get?t=" + new Date().getTime(),
                dataType : "json",
                data : {
                    token : token,
                }
            })
            .done(function ( data ) {
                if(data.code == '0'){
                    nickName = data.data.nickName;
                    $(".login-box .name a").text(nickName);
                    $.cookie('userPhone', data.data.phoneNumber, { expires: 7, path: '/' });
                    localStorage.setItem("userinfo", JSON.stringify(data.data));
                }
            })
            .fail(function (jqXHR, textStatus,errorThrown) {
                console.log(jqXHR);
            });
        }

    };

    initToken.getToken();
    if(token){
        initToken.checkToken(token,phone);
        initToken.getUserInfo(token);
    }



})(window,jQuery);