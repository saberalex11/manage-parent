layui.config({
  base : "js/"
}).use(['jquery','layer','carousel'], function () {
  var $ = layui.jquery,
      layer = layui.layer,
      carousel = layui.carousel;

//video背景
// $(window).resize(function(){
//   if($(".video-player").width() > $(window).width()){
//     $(".video-player").css({"height":$(window).height(),"width":"auto","left":-($(".video-player").width()-$(window).width())/2});
//   }else{
//     $(".video-player").css({"width":$(window).width(),"height":"auto","left":-($(".video-player").width()-$(window).width())/2});
//   }
// }).resize();

  /**背景图片轮播*/
  carousel.render({
    elem: '#login_carousel',
    width: '100%',
    height: '100%',
    interval:2000,
    arrow: 'none',
    anim: 'fade',
    indicator:'none'
  });

var vm = new Vue({
  el:'#dqapp',
  data:{
      userAccount: '',
    password: '',
    captcha: '',
    error: false,
    errorMsg: '',
    src: 'captcha.jpg'
  },
  beforeCreate: function(){
    if(self != top){
      top.location.href = self.location.href;
    }
  },
  methods: {
    refreshCode: function(){
      this.src = "captcha.jpg?t=" + $.now();
    },
    login: function () {
      var data = "userAccount="+vm.userAccount+"&password="+vm.password+"&captcha="+vm.captcha;
      var loginLoading = top.layer.msg('登录中...请稍候',{icon: 16,time:false,shade:0.8});
      $.ajax({
        type: "POST",
        url: "sys/login",
        data: data,
        dataType: "json",
        success: function(r){
          top.layer.close(loginLoading);
          if(r.code == 0){//登录成功
            localStorage.setItem("token", r.token);
            parent.location.href ='index.html';
          }else{
            layer.msg(r.msg);
            vm.refreshCode();
          }
        },
        error: function(){
          top.layer.close(loginLoading);
        }
      });
    }
  }
});

});//layui模块加载完毕